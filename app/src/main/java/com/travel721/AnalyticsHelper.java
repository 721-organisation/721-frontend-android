package com.travel721;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * This class is restrictively designed for refined reporting of events to Firebase.
 *
 * @author Bhav
 * @see ReleaseAnalyticsEvent Events reportable in release mode only
 * @see DebugAnalyticsEvent Events reportable in either debug or release mode
 */
public class AnalyticsHelper {
    // Don't show the debug warning more than once
    private static boolean warn_user_analytics_disabled = false;
    /*
     These constants provide a 'translation' between in-app events and meaningful analytics
    */
    // Release Constants
    public static final String USER_SWIPED_RIGHT = "User_Liked_Event";
    public static final String USER_SWIPED_LEFT = "User_Disliked_Event";
    public static final String USER_SWIPED_DOWN = "User_Viewed_More_Event_Info";
    public static final String SETTINGS_OPENED = "User_Opened_App_Settings";
    public static final String USER_OPENED_LIST_EVENTS_ACTIVITY = "User_Saw_Their_Liked_Events";
    public static final String USER_CLICKS_EVENT_IN_LIKED_EVENT_LIST = "User_Reviewed_Liked_Event_Info";
    public static final String USER_CONVERSION_EVENT_AFF_LINK_CLICK = "User_Affiliate_Link_Click";
    // Debug Constants
    public static final String DEBUG_USED_FUSED_LOCATION_PROVIDER = "FusedLocationProvider_Was_Used";
    public static final String DEBUG_USED_NATIVE_LOCATION_MANAGER = "NativeLocationManager_Was_Used";
    public static final String TEST_RELEASE_ANALYTICS_EVENT = "PleaseIgnoreThisStatistic";
    public static final String USER_POSITIVE_FEEDBACK = "FeedbackCardPositiveFeedback";
    public static final String USER_NEGATIVE_FEEDBACK = "FeedbackCardNegativeFeedback";

    /**
     * This is the custom Analytics logger for 721.
     * Analytics are NOT reported to Firebase in debug mode.
     *
     * @param context The Activity that an event is being reported in.
     * @param event   Must be one of the @ReleaseAnalyticsEvent constants
     * @param bundle  (optional) extra info to bundle in
     * @see ReleaseAnalyticsEvent for permissible events
     */
    public static void logEvent(Context context, @ReleaseAnalyticsEvent String event, @Nullable Bundle bundle) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        // Do not report RELEASE information in debug build
        if (BuildConfig.ENABLE_FIREBASE_ANALYTICS) {
            mFirebaseAnalytics.logEvent(event, bundle);
        } else if (!warn_user_analytics_disabled) {
            warn_user_analytics_disabled = true;
            Toast.makeText(context, "Analytics: Disabled in this build mode. Do not distribute this version of the app.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * WARNING: Do not use this to log events that concern only genuine users.
     * This method is designed especially for debugging events that could genuinely crop up
     * in either internal or external testing. As such, these are always reported to Firebase.
     *
     * @param context The Activity that an event is being reported in.
     * @param event   Must be one of the @DebugAnalyticsEvent constants
     * @param bundle  (optional) extra info to bundle in
     * @see DebugAnalyticsEvent for permissible events
     */
    public static void debugLogEvent(Context context, @DebugAnalyticsEvent String event, @Nullable Bundle bundle) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.logEvent(event, bundle);
    }
}
