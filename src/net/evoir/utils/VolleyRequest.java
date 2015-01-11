package net.evoir.utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.evoir.avenue225.MainActivity;
import net.evoir.avenue225.R;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class VolleyRequest{
	private Context mContext;
	private Dao<Post, String> dao;
	private SharedPreferences prefs;
		public VolleyRequest(Context context) {
			
			this.mContext = context;
			prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		}
		
		// do the parsing job and populate the database
		public void fetch() {
			
			Boolean activate_update = prefs.getBoolean("activate_update", false);
			if(activate_update){
				//check for new posts and start notification
	        	getNewPosts(Constants.lastPubDate,true);
			}
			
			Constants.setLastPubDate(lastPubDate());
			//Log.v(Constants.TAG,"start making JsonRequest on JsonService.makeJsonArrayRequest()"); 
		    JsonArrayRequest req = new JsonArrayRequest(Constants.JSON_LINK,
		            new Response.Listener<JSONArray>() {
		                @Override
		                public void onResponse(JSONArray posts) {
		                	//Log.v(Constants.TAG, "start downloading Json feed");
		                    try {
		                        // Parsing json array posts
		                        // loop through each json object
	
		                        if(posts!=null){
		                        	//Log.v(Constants.TAG, "posts contains "+posts.length()+" items");						
	
		                    		List<Post> postList = new ArrayList<Post>();
		                            for (int i = 0; i < posts.length(); i++) {
		                            	 
		                                JSONObject c = (JSONObject) posts
		                                        .get(i);
		 
	
								
										// Storing each json item in variable
										String title = c.getString(Constants.TAG_TITLE);
										String link = c.getString(Constants.TAG_LINK);
										String description = c.getString(Constants.TAG_DESC);
										String content = c.getString(Constants.TAG_CONTENT);
										String pubDate = c.getString(Constants.TAG_PUBDATE);				
										String image = c.getString(Constants.TAG_IMG);
										
										String category = c.getString(Constants.TAG_CATEGORY);
										String categorySlug = c.getString(Constants.TAG_CATEGORY_SLUG);
										//Log.v("mytag","in makeJsonArrayRequest() title "+i+" is " +title);						
								
								
										if (title != null && link != null && description != null && content != null && pubDate != null && image != null && category != null && categorySlug != null) {
											/*DateFormat formatter = new DateFormat();
											pubDate = formatter.Format(pubDate);*/
											Post item = new Post(link, title,description,content,pubDate,image,category,categorySlug);
											//Log.v("mytag","in JsonParser image "+i+" is " +image);						
								
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
									

		                    	}
	
	
	
		                    } catch (JSONException e) {
		                        e.printStackTrace();
		                        /*Toast.makeText(getApplicationContext(),
		                                "Error: " + e.getMessage(),
		                                Toast.LENGTH_LONG).show();*/
		                        Log.v(Constants.TAG, "error while downloading:"+e.getMessage());
		                    }
	
		                }
		            }, new Response.ErrorListener() {
		                @Override
		                public void onErrorResponse(VolleyError error) {
		                    VolleyLog.d(Constants.TAG, "Error: " + error.getMessage());
		                    //Toast.makeText(getApplication(),error.getMessage(), Toast.LENGTH_SHORT).show();
		                }
		            });
	
			
		    AppController.getInstance().addToRequestQueue(req);
		    
		    
		}
		
		
		public int getNewPosts(String lastPubDate, Boolean notify){
			int postNumber=0;
			//Log.v(Constants.TAG,"on getNewPosts, lastPubDate = "+lastPubDate);

			try {
				dao = Model.getHelper(mContext).getDao(Post.class);
				QueryBuilder<Post, String> queryBuilder =
						dao.queryBuilder();
				//get posts from database where status is -1 (brand new)
				queryBuilder.orderBy("pubDate", false);
				List<Post> listPosts = new ArrayList<Post>();
				List<Post> tempList=queryBuilder.query();
					
					// loop through database and search for brand new posts
				if(tempList.size()>0 && lastPubDate!=null) {
					for(int i=0;i<tempList.size();i++) {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
						try {
							Date LastPubDate =format.parse(lastPubDate);
							Date PostPubDate =format.parse(tempList.get(i).getPubDate());

							if (LastPubDate.compareTo(PostPubDate) < 0) {
								//the post is a brand new one
								listPosts.add(tempList.get(i));
								
							}
							
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					// if list of brand new post is not null then postNumber will count those posts
					if (listPosts!=null) {
						
						postNumber = listPosts.size();
						Log.v(Constants.TAG,"Notification sent!"+listPosts.size()+" new posts");
						if(notify){
							sendNotification(listPosts);
							//Log.v(Constants.TAG,"on getNewPosts postNumber ="+postNumber);
						}
			


					}
				}


				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return postNumber;
		}
		
		//notification
	   public void sendNotification(List<Post> postList) {
		   
		   Boolean activate_notification =prefs.getBoolean("activate_notifications", false);
		   if(activate_notification){
			   if(postList.size()>0){
		    		String sentence=null;
		    		if(postList.size()>1){
		        		sentence ="Nouveaux articles!";

		    		}else{
		    			sentence ="Nouvel article!";
		    		}
		        	NotificationCompat.Builder mBuilder =
		        		    new NotificationCompat.Builder(mContext)
		        		    .setSmallIcon(R.drawable.logo)
		        		    .setContentTitle(sentence)
		        		    .setContentText(postList.size()+" "+sentence);
		        	int mNotificationId = 001;
		        	// Gets an instance of the NotificationManager service
		        	NotificationManager mNotifyMgr = 
		        	        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		        	
		        	//prepare the pending Intent
		 		   Intent resultIntent = new Intent(mContext, MainActivity.class);
				   // Because clicking the notification opens a new ("special") activity, there's
				   // no need to create an artificial back stack.
				   PendingIntent resultPendingIntent =
				       PendingIntent.getActivity(
				       mContext,
				       0,
				       resultIntent,
				       PendingIntent.FLAG_UPDATE_CURRENT
				   );
				   	mBuilder.setAutoCancel(true);
				   	mBuilder.setLights(Color.MAGENTA, 1000, 1000);
				   	
				   	//get data from SharedPreferences
				   	
			        boolean vibrate = prefs.getBoolean("activate_vibrate", false);
			        if(vibrate){
		                long[] pattern = {500,500,500,500,500,500,500,500,500};
		                mBuilder.setVibrate(pattern);
			        }
			        
			        String ringtone = prefs.getString("ringtone", "default ringtone");
			        if (ringtone!="default ringtone"){
				        Uri ringToneUri = Uri.parse(ringtone);
				        mBuilder.setSound(ringToneUri);
			        }

		        	mBuilder.setContentIntent(resultPendingIntent);
		        	// Builds the notification and issues it.
		        	mNotifyMgr.notify(mNotificationId, mBuilder.build());
		    	}
		   }
	    	

	    }
	 
		@SuppressWarnings("deprecation")
		public String lastPubDate() {
			String pubDate="1970-00-00 00:00:00";
		
			try {
				dao = Model.getHelper(mContext).getDao(Post.class);
				QueryBuilder<Post, String> queryBuilder =
			    	dao.queryBuilder();
					//get posts from database where status is -1 (brand new)
					queryBuilder.orderBy("pubDate", false);
					queryBuilder.limit(1);
					List<Post> listPosts =queryBuilder.query();
					if (listPosts.size()>0){
						pubDate = listPosts.get(0).getPubDate().toString();
					}
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}

			return pubDate;
					
			
		}
}

