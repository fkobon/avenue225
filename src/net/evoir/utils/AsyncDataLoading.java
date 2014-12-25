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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncDataLoading  extends AsyncTask<Void, Void, Void> {

	private static final String JSON_LINK = "http://www.avenue225.com/json";
	private JSONArray posts = null;

	//JSON Node names
	private static final String TAG_LINK = "link";
	private static final String TAG_TITLE = "title";
	private static final String TAG_IMG = "image";
	private static final String TAG_DESC = "description";
	private static final String TAG_PUBDATE = "pubDate";
	private static final String TAG_CONTENT = "content";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_CATEGORY_SLUG = "categorySlug";
	private Context mContext;
	 
    public AsyncDataLoading(Context mContext) {
		// TODO Auto-generated constructor stub
    	mContext = this.mContext;
	}

	protected void onPreExecute(Context context) {
        super.onPreExecute();
        // before making http calls


    }

    @Override
	protected Void doInBackground(Void... arg0) {
    	
		Log.d(Constants.TAG, "Service started");
		//List<Post> rssItems = null;
		JsonDownload(JSON_LINK);


        return null;
    }

    @Override
	protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
	
    public void JsonDownload(String jsonLink) {
		try {
	        Log.v("mytag", "entered Json download");

		    // Getting Array of Contacts
    		JsonParser parser = new JsonParser();

			//JSONObject json = parser.getJSONFromUrl(jsonLink);
			//posts = json.getJSONArray(TAG_POSTS);
			posts = parser.getJSONFromUrl(jsonLink);
			List<Post> postList = new ArrayList<Post>();

			if (posts!=null) {
			Log.v("mytag","posts contains "+posts.length()+" items");						

		    // looping through All Contacts
		    for(int i = 0; i < posts.length(); i++){
		        JSONObject c = posts.getJSONObject(i);
	
		        // Storing each json item in variable
		        String title = c.getString(TAG_TITLE);
		        String link = c.getString(TAG_LINK);
		        String description = c.getString(TAG_DESC);
		        String content = c.getString(TAG_CONTENT);
		        String pubDate = c.getString(TAG_PUBDATE);
		        String image = c.getString(TAG_IMG);
		        String category = c.getString(TAG_CATEGORY);
		        String categorySlug = c.getString(TAG_CATEGORY_SLUG);
				Log.v("mytag","in JsonParser title "+i+" is " +title);						

	
				if (title != null && link != null && description != null && content != null && pubDate != null && image != null && category != null && categorySlug != null) {
					/*DateFormat formatter = new DateFormat();
					pubDate = formatter.Format(pubDate);*/
					Post item = new Post(link, title,description,content,pubDate,image,category,categorySlug);
					Log.v("mytag","in JsonParser date "+i+" is " +category);						

					postList.add(item);
	                try {
						Model.getHelper(mContext).getDao(Post.class).createOrUpdate(item);
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
		    }
			}else {
				Log.v("mytag","on JsonDownload posts contains 0 items");						

			}
		} catch (JSONException e) {
			Log.v("mytag",e.getMessage());						

		    e.printStackTrace();
		}
	
	
	}
}