package com.fantasystock.fantasystock.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fantasystock.fantasystock.Helpers.CallBack;
import com.fantasystock.fantasystock.Helpers.DataCenter;
import com.fantasystock.fantasystock.Helpers.DataClient;
import com.fantasystock.fantasystock.Models.Stock;
import com.fantasystock.fantasystock.Models.User;
import com.fantasystock.fantasystock.R;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by wilsonsu on 3/22/16.
 */

public class TradeFragment extends DialogFragment {

    @Bind(R.id.btnTrade)
    Button btnTrade;
    @Bind(R.id.tvSymbol)
    TextView tvSymbol;
    @Bind(R.id.tvUnitPrice) TextView tvUnitPrice;
    @Bind(R.id.tvTotalCost) TextView tvTotalCost;
    @Bind(R.id.tvAvailableFund) TextView tvAvailableFund;
    @Bind(R.id.tvEstimatedCost) TextView tvEstimatedCost;
    @Bind(R.id.tvWarning) TextView tvWarning;
    @Bind(R.id.etShares) EditText etShares;
    @Bind(R.id.prLoadingSpinner) RelativeLayout prLoadingSpinner;


    private String symbol;
    private boolean isBuy;
    private DataCenter dataCenter;
    private DecimalFormat formatter;
    private Stock stock;

    public interface TradeFragmentListener {
        void onFinishTrading();
    }


    public static TradeFragment newInstance(String symbol, boolean isBuy) {
        TradeFragment frag = new TradeFragment();
        Bundle args = new Bundle();
        args.putString("symbol", symbol);
        args.putBoolean("isbuy", isBuy);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade, container);
        ButterKnife.bind(this, view);
        prLoadingSpinner.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().setLayout(400, 500);

        symbol = getArguments().getString("symbol");
        isBuy = getArguments().getBoolean("isbuy");
        tvSymbol.setText("Shares of " + symbol);

        tvEstimatedCost.setText("Estimated "+(isBuy?"Cost":"Gain"));

        btnTrade.setText(isBuy?"Buy":"Sell");

        dataCenter = DataCenter.getInstance();
        stock = User.currentUser.investingStocksMap.get(symbol);
        formatter = new DecimalFormat("$###,##0.00");
        DataClient.getInstance().getStockPrice(symbol, new CallBack() {
            @Override
            public void stockCallBack(Stock returnStock) {
                tvUnitPrice.setText(formatter.format(returnStock.current_price));
                if (stock == null) {
                    stock = returnStock;
                } else {
                    stock.current_price = returnStock.current_price;
                }
            }
        });
        etShares.setText("");
        etShares.requestFocus();
        // TODO: input method is not default out :(
    }


    @OnTextChanged(R.id.etShares)
    public void onSharesChange() {
        int numShares = 0;
        try {
            numShares = Integer.parseInt(etShares.getText().toString());
        } catch (NumberFormatException e) {
            numShares = 0;
        }

        if (isBuy) {
            tvAvailableFund.setText(formatter.format(User.currentUser.availableFund) + " available");
        } else {
            tvAvailableFund.setText(stock.share + " shares available");
        }

        tvTotalCost.setText(formatter.format(numShares * stock.current_price));

    }

    @OnClick(R.id.btnTrade)
    public void onTrade() {
        int numShares = 0;
        try {
            numShares = Integer.parseInt(etShares.getText().toString());
        } catch (NumberFormatException e) {
            onDismissTrading();
        }

        float cost = numShares * stock.current_price;
        String tradingMessage;
        if (isBuy) {
            if (cost > User.currentUser.availableFund) {
                tvWarning.setText("Not enough funds to buy.");
                return;
            }
            tradingMessage = "Bought " + numShares + " shares of " + symbol + " for " + formatter.format(cost);
        } else {
            if (stock.share < numShares) {
                tvWarning.setText("Not enough shares to sell.");
                return;
            }
            tradingMessage =  "Sold " + numShares + " shares of " + symbol + " for " + formatter.format(cost);
        }
        final String message = tradingMessage;
        if (!isBuy) numShares = -numShares;
        prLoadingSpinner.setVisibility(View.VISIBLE);
        DataCenter.getInstance().trade(stock.symbol, numShares, new CallBack() {
            @Override
            public void done() {
                prLoadingSpinner.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                onDismissTrading();
            }
        });
    }

    private void onDismissTrading() {
        TradeFragmentListener listener = (TradeFragmentListener) getTargetFragment();
        listener.onFinishTrading();
        dismiss();
    }
}