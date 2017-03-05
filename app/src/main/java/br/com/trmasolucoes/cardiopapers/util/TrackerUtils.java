package br.com.trmasolucoes.cardiopapers.util;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import br.com.trmasolucoes.cardiopapers.AnalyticsApplication;

/**
 * Created by tairo on 05/03/17.
 */

public class TrackerUtils {

    public static void send(Activity context, String page) {
        // Get tracker.
        Tracker t = ((AnalyticsApplication) context.getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
        // Set screen name.
        t.setScreenName(page);
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
