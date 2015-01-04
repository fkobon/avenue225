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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class JsonService extends IntentService {
	private static final String JSON_LINK = "http://www.avenue225.com/json";

	//JSON Node names
	private static final String TAG_LINK = "link";
	private static final String TAG_TITLE = "title";
	private static final String TAG_IMG = "image";
	private static final String TAG_DESC = "description";
	private static final String TAG_PUBDATE = "pubDate";
	private static final String TAG_CONTENT = "content";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_CATEGORY_SLUG = "categorySlug";
	private static String TAG ="mytag";
	
		
	public JsonService() {
		super("JsonService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(Constants.TAG, "Service Intent started");
		makeJsonArrayRequest();
	}
	
    /**
     * Async Task to make http call
     */

	private void makeJsonArrayRequest() {
		 
		Log.v(Constants.TAG,"start making JsonRequest on JsonService.makeJsonArrayRequest()"); 
        JsonArrayRequest req = new JsonArrayRequest(JSON_LINK,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray posts) {
                    	Log.v(Constants.TAG, "start downloading Json feed");
                        try {
                            // Parsing json array posts
                            // loop through each json object

                            if(posts!=null){
                            	Log.v(Constants.TAG, "posts contains "+posts.length()+" items");						

                        		List<Post> postList = new ArrayList<Post>();
                                for (int i = 0; i < posts.length(); i++) {
                                	 
                                    JSONObject c = (JSONObject) posts
                                            .get(i);
     

    						
    								// Storing each json item in variable
    								String title = c.getString(TAG_TITLE);
    								String link = c.getString(TAG_LINK);
    								String description = c.getString(TAG_DESC);
    								String content = c.getString(TAG_CONTENT);
    								String pubDate = c.getString(TAG_PUBDATE);				
    								String image = c.getString(TAG_IMG);
    								
    								String category = c.getString(TAG_CATEGORY);
    								String categorySlug = c.getString(TAG_CATEGORY_SLUG);
    								Log.v("mytag","in makeJsonArrayRequest() title "+i+" is " +title);						
    						
    						
    								if (title != null && link != null && description != null && content != null && pubDate != null && image != null && category != null && categorySlug != null) {
    									/*DateFormat formatter = new DateFormat();
    									pubDate = formatter.Format(pubDate);*/
    									Post item = new Post(link, title,description,content,pubDate,image,category,categorySlug);
    									//Log.v("mytag","in JsonParser image "+i+" is " +image);						
    						
    									postList.add(item);
    									try {
    										Model.getHelper(getApplication()).getDao(Post.class).createOrUpdate(item);
    									} catch (SQLException er) {
    										// TODO Auto-generated catch block
    										er.printStackTrace();
    										Log.v("mytag"," Dao error" + er.getMessage());		
    						
    									}
    						
    									title = null;
    									link = null;
    									description = null;
    									image = null;
    									pubDate = null;
    									content = null;
    									image = null;
    									category = null;
    									categorySlug = null;
    								}     
    							
								Log.v(Constants.TAG, "finish downloading Json feed");		
                                }

                        	}

 
 
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.v(Constants.TAG, "erron while downloading:"+e.getMessage());
                        }
 
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplication(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
 
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }}
