package net.evoir.avenue225.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import net.evoir.avenue225.PostDetailActivity;
import net.evoir.avenue225.R;
import net.evoir.avenue225.adapters.PostAdapter;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import net.evoir.utils.Constants;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class CategoryFragment extends Fragment {
	
	private ListView listView;
	private static Dao<Post, String> dao;
	private List<Post> postList;
	private Context mContext;
	private String category;
	private String categorySlug;
	private int categoryNumber;
	private ProgressDialog dialog;
	private Boolean hasArguments;
	private PostAdapter adapter;
	private View view;
	private int positionInListView;
	private DialogInterface.OnClickListener dialogClickListener;
    TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        view = inflater.inflate(R.layout.main_activity, container, false);
        mContext = getActivity();
        dialog = new ProgressDialog(mContext); 
		return view;
    }
	

    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);
        
        
		        //dialog = new ProgressDialog(mContext); 
		       
		        //dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		        
		        loadListAsync loadFragments = new loadListAsync();
			    
			    if (savedInstanceState == null) {
			    	
			        if (getArguments() != null) {
			        	category = getArguments().getString("category");   
				        categoryNumber = getArguments().getInt("categoryNumber");   	        	  
				        categorySlug = getArguments().getString("categorySlug");   
				        //show ListView 
				        
					     // change app menu title
				        setTitle(category);
				    	  /*if(categoryNumber<=0) {
				    		  setTitle(category);
				    		  
				    	  }else {
				    		  setTitle(category+" ("+categoryNumber+")");
				    	  }*/
				    	hasArguments = true;  
			    	    loadFragments.execute();	  
				        
			
			        }else {
			        	hasArguments = false;
			        	loadFragments.execute();
			        }
			    }

    }
	/**
	 * Once the {@link RssService} finishes its task, the result is sent to this
	 * ResultReceiver.
	 */

    public void showList(Context context) {
		
    	postList =loadList(context);
		if (postList.size() >0) {
			adapter = new PostAdapter(getActivity(), postList);

			
			listView = (ListView) getActivity().findViewById(R.id.postsList);
			//listView.setSelector(R.color.yellowLight);
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			
			/*for (int i = 0; i < postList.size(); i++) {
				Log.v("mytag"," title is " +postList.get(i).getTitle());
				Log.v("mytag"," image url is " +postList.get(i).getImage());
				//Log.v("mytag","in PostActivity content is " +postList.get(i).getContent());						
			}*/
			
			// highliting issue
			
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	                @Override
	                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	                    //Intent intent = new Intent(mContext, PostDetailActivity.class);
	                    //intent.putExtra("link", post.getLink());
	                    
	                    
	                    ArrayList<String> postString = new ArrayList<String>();
	                    
	                    //adding postsList to newly created ArrayList : parcelablePosts
	                    
	                    for(int i = 0; i < postList.size(); i++){
	        	        	
	        				//Log.v("mytag","category "+ i+" is "+listPosts.get(i).getCategory());
	                		//Log.v("mytag","On CategoryFragment, stringList #"+i+" is "+postList.get(i).getLink());

	        				String postLink = postList.get(i).getLink();
	        				postString.add(postLink);

	                    }    
	                    //Log.v(Constants.TAG,"On CategoryFragment, stringList contains "+postString.size()+" items");
	                    
	                    //post = postList.get(position);
	                    
	                    
	                    Intent intent = new Intent(getActivity(), PostDetailActivity.class);
	                    intent.putExtra("tabPosition", position);
	                    intent.putStringArrayListExtra("posts", postString);
	                    startActivity(intent);
	                
	            }

	            });	
			
            /* set long click on ListView item             */
			
			listView.setLongClickable(true);
    	    listView.setOnItemLongClickListener(new OnItemLongClickListener() {
    	        @Override
    	        public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
    	        	
    	        	/*Toast bread = Toast.makeText(mContext, "Post "+id+" link is "+postList.get(pos).getLink(), Toast.LENGTH_LONG);
    	        	bread.show();*/
    	        	deletePost((int)id);
    	        	
    	        	return true;                
    				}
    	    });
		} else {
			Toast.makeText(context, "0 posts found. Try with the search bar above or check your network connection",
					Toast.LENGTH_LONG).show();
		}
    }
	@SuppressWarnings("deprecation")
	public List<Post> loadList(Context context) {
		List<Post> dbPostlist = null;

		try {
			dao = Model.getHelper(mContext).getDao(Post.class);
			//new list for storing result from database as "dbPostlist"
	    	if (hasArguments) {
	    		Log.v("mytag","start querying the Database on CategoryFragment.loadlist()");
	    		QueryBuilder<Post, String> queryBuilder =
	    		    	dao.queryBuilder();
	    		//get posts from database and group them by category
		    	queryBuilder.where().eq("categorySlug", categorySlug);
		    	queryBuilder.orderBy("pubDate", false);
		    	dbPostlist = queryBuilder.query();
	    	}else {
	    		QueryBuilder<Post, String> queryBuilder =
	    		    	dao.queryBuilder();
	    		//get posts from database and group them by category
		    	queryBuilder.orderBy("pubDate", false);
		    	queryBuilder.limit(50);
		    	dbPostlist = queryBuilder.query();
	    	}
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("mytag"," Dao error" + e.getMessage());		
		}
		
		return dbPostlist;	
	};
	
    /*
     * ASYNC JOB
     * 
     * */
    private class loadListAsync extends AsyncTask <Void, Void, Void> {

     
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading, please wait.");
            dialog.setProgressStyle(dialog.STYLE_SPINNER);
            dialog.show();
        }
         
        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            	showList(mContext);
                
            }
        }
         
        @Override
        protected Void doInBackground(Void... params) {
 /*       try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        	//loadList(mContext);
            return null;

        }
         
    }
    public void deletePost(int position){
    	
    	// handle answers
        dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
            	// we set an update builder
            	try {
					dao = Model.getHelper(mContext).getDao(Post.class);
	            	DeleteBuilder<Post, String> deleteBuilder = dao.deleteBuilder();
	            	deleteBuilder.where().eq("link", postList.get(positionInListView).getLink());
	            	deleteBuilder.delete();
	            	
	            	//update the ListView
					postList.remove(positionInListView);
                  	adapter.notifyDataSetChanged();
	            	Toast.makeText(mContext, "Post has been deleted", Toast.LENGTH_LONG).show();
	            	
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
				}

            	
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
            	Toast.makeText(mContext, "Deletion canceled", Toast.LENGTH_LONG).show();
                
                break;
            }
        }
    };
    	positionInListView = position;
    	// Ask a validation
    	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    	// set title
		builder.setTitle("Delete Post");
        builder.setMessage("Do you really want to delete this post?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
        
        


    }
	  public void setTitle(CharSequence title) {
		  CharSequence mTitle = title;
	      getActivity().getActionBar().setTitle(mTitle);
	  }

}