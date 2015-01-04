package net.evoir.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.evoir.avenue225.Constants;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.util.Log;

public class JsonService extends IntentService {
	private Context mContext;
	
	public JsonService() {
		super("JsonService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(Constants.TAG, "Service started");
		new AsyncDataLoading(mContext).execute();
	}
	
    /**
     * Async Task to make http call
     */

}
