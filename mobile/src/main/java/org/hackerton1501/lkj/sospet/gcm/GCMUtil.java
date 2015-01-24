package org.hackerton1501.lkj.sospet.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.hackerton1501.lkj.sospet.constants.AppConstant;
import org.hackerton1501.lkj.sospet.http.HttpRequestTaskextends;

public class GCMUtil {
	public static final String TAG = "GCMUtil";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String PROPERTY_APP_VERSION = "appVersion";

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	public static boolean checkPlayServices(Activity activity) {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getBaseContext());
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				activity.finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there
	 * is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	public static String getRegistrationId(Context context) {
		final SharedPreferences prefs = getPreferences(context);
		String registrationId = prefs.getString(AppConstant.PREF_GCM_TOKEN, "");
		if ("".equals(registrationId)) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	   /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    public static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AppConstant.PREF_GCM_TOKEN, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private static SharedPreferences getPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(context.getClass().getSimpleName(), Context.MODE_PRIVATE);
	}
	

	public static void unregister(Context context) {

	}

	/**
	 * Clear internal resources.
	 * 
	 * <p>
	 * This method should be called by the main activity's {@code onDestroy()}
	 * method.
	 */
	public static synchronized void onDestroy(Context context) {
			Log.v(TAG, "Unregistering receiver");
//			context.unregisterReceiver(sRetryReceiver);
	}
	
	/**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    public static void sendRegistrationIdToBackend() {
      // Your implementation here.
    }
    
    /**
     * GCM TOKEN 서버저장 
     * @Method Name  : requestToSaveGCMToken
     * @작성일   : 2014. 2. 18. 
     * @작성자   : shlee
     * @변경이력  :
     */
//    public static void requestToDelGCMToken(I2ConnectApplication aUILApplication)
//    {
//    	String kUrl = i2UrlHelper.GCM.getSaveGcmTokenUrl(null);
//        String kOauthToken = aUILApplication.getPreferenceString(StringConstant.PREF_OAUTH_TOKEN);
//        String kClientId = NetworkConstant.OAUTH_CLIENT_ID;
//        String kGCMToken = "";
//        String kUseYn = "Y";
//        ArrayList<BasicNameValuePair> kParamList = i2UrlHelper.GCM.getSaveGcmTokenParams(kOauthToken, kClientId, kGCMToken, kUseYn);
//        Log.e("requestToSaveGCMToken", kParamList.toString());
//        new kr.co.i2max.i2framework.net.SNSNetHelper(null, aUILApplication)
//        .requestServerPostList(kUrl, i2UrlHelper.getTokenHeader(kOauthToken), kParamList);
//
//    }

    //
    public static void sendGCMPush(String msg) {
//        String jsonMsg = "{ \"data\": {\"message\":\"hello SOSpet1\", \"badge\": 11}, \"registration_ids\":[ \"APA91bEhJqTFLTs-s0yuhldlA7NPpZQ-qgI9rhJxn0qOvhFUXE7ZtenJj1ANsh5ro_CbzfVvaKnFpKDgLF5NIVAE5YhNK0BmTM3oUbatgUZBxJz5SUZKpg1zDP-L54P65yPLw1LFQvY3mPRGO8gX9FK6mtvCsufKtFZNO12NaHEAUQVnqCpeIC0\"]}";
        String jsonMsg = "{ \"data\": {\""+msg+"\":\"hello SOSpet1\", \"badge\": 11}, \"registration_ids\":[ \"APA91bEhJqTFLTs-s0yuhldlA7NPpZQ-qgI9rhJxn0qOvhFUXE7ZtenJj1ANsh5ro_CbzfVvaKnFpKDgLF5NIVAE5YhNK0BmTM3oUbatgUZBxJz5SUZKpg1zDP-L54P65yPLw1LFQvY3mPRGO8gX9FK6mtvCsufKtFZNO12NaHEAUQVnqCpeIC0\"]}";
        new HttpRequestTaskextends().execute(AppConstant.GCM_SERVER_URL, AppConstant.SEND_API_KEY, jsonMsg);
    }
}
