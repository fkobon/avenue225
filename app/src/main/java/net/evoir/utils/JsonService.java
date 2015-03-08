package net.evoir.utils;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class JsonService extends IntentService {
	
		
	public JsonService() {
		super("JsonService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//Log.v(Constants.TAG, "Service Intent started");
		VolleyRequest jsonVolleyRequest = new VolleyRequest(getApplicationContext());
		jsonVolleyRequest.fetch();
	}
	
    /**
     * Async Task to make http call
     */

}
