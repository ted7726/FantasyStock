package com.fantasystock.fantasystock.Models;

import java.util.ArrayList;

/**
 * Created by chengfu_lin on 3/5/16.
 */

public class User {
    public int id;
    public String created_time;
    public float cash;
    public ArrayList<Stock> shared_stock;
    public ArrayList<Transaction> history;

}