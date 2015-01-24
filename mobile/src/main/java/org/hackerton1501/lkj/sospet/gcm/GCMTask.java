package org.hackerton1501.lkj.sospet.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.hackerton1501.lkj.sospet.constants.AppConstant;

import java.io.IOException;

public class GCMTask extends AsyncTask<Void, Void, String> {
	private Context _context;
	private GoogleCloudMessaging _gcm;
	
	private String _gcm_device_token;
	private CompleteStoreRegistrationId _deviceTokenListener;
	
	public GCMTask(Context context, GoogleCloudMessaging gcm, CompleteStoreRegistrationId deviceTokenListener) {
		_context = context;
		_gcm = gcm;
		_deviceTokenListener = deviceTokenListener;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			_gcm_device_token = _gcm.register(AppConstant.SENDER_ID);
			GCMUtil.storeRegistrationId(_context, _gcm_device_token);
			
			//send gcm token to i2talk server by snshelper
			GCMUtil.sendRegistrationIdToBackend();

			Log.d("GCMTask", "deviceToken===>>" + _gcm_device_token);
			if (_deviceTokenListener != null) { 
			    _deviceTokenListener.onComplete(_gcm_device_token);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return _gcm_device_token;
	}
	
	public static interface CompleteStoreRegistrationId {
	    public abstract void onComplete(String $gcmDeviceToken);
	}

}
