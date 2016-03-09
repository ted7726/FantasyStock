package com.fantasystock.fantasystock;

import android.text.format.DateUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wilsonsu on 3/6/16.
 */
public class Utils {
    public static Date timeStampConverter(int timestamp) {
        return new Date((long) timestamp*1000);
    }

    public static String converTimetoRelativeTime(Date time) {
        String relativeDate = DateUtils.getRelativeTimeSpanString(time.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_TIME).toString();
        relativeDate.replaceFirst("hour", "h");
        relativeDate.replaceFirst("minute", "min");
        return relativeDate;
    }
    public static Calendar convertDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String numberConverter(int n) {
        String text =  (n > 1000 ? n / 1000 + "k" : n + "");
        return text;
    }

    public static void repeatAnimationGenerator(final View view, final CallBack callBack) {
        final AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        final AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(500);
        animation2.setDuration(500);
        animation2.setStartOffset(5000);

        //animation1 AnimationListener
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation2 when animation1 ends (continue)
                view.startAnimation(animation2);
            }
        });

        //animation2 AnimationListener
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                callBack.task();
                view.startAnimation(animation1);
            }
        });
        view.startAnimation(animation1);
    }

    public static void fadeIneAnimation(final View view) {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        view.startAnimation(animation);
    }
}
