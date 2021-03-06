package net.evoir.avenue225.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import net.evoir.avenue225.PostDetailActivity;
import net.evoir.avenue225.R;
import net.evoir.avenue225.adapters.PostAdapter;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class FavorisFragment extends Fragment {
	
	private ListView listView;
	private static Dao<Post, String> dao;
	private Post post;
	private List<Post> postList;
	private Context mContext;

	
	
    TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.main_activity, container, false);
        
		return view;
    }
	

    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
 
        //show ListView 
        mContext = getActivity();
        showList(mContext);

    }
	/**
	 * Once the {@link RssService} finishes its task, the result is sent to this
	 * ResultReceiver.
	 */


	public void showList(Context context) {
		
		try {
			dao = Model.getHelper(mContext).getDao(Post.class);
	    	QueryBuilder<Post, String> queryBuilder =
	    	dao.queryBuilder();
			//get posts from database and group them by category
/*	    	queryBuilder.where().eq("status", 1);
*/	    	queryBuilder.where().eq("status", 2);
	    	queryBuilder.orderBy("pubDate", false);
			postList = queryBuilder.query();
			//Log.v("mytag",postList.size()+" posts found");

			if (postList.size() >0) {
				PostAdapter adapter = new PostAdapter(getActivity(), postList);

				
				listView = (ListView) getActivity().findViewById(R.id.postsList);
				//listView.setSelector(R.color.yellowLight);
				listView.setAdapter(adapter);
				
				/*for (int i = 0; i < postList.size(); i++) {
					Log.v("mytag"," title is " +postList.get(i).getTitle());
					Log.v("mytag"," image url is " +postList.get(i).getImage());
					//Log.v("mytag","in PostActivity content is " +postList.get(i).getContent());						
				}*/
				
				// highliting issue
				
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		                @Override
		                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		                    post = postList.get(position);

		                    ArrayList<String> postString = new ArrayList<String>();
		                    
		                    //adding postsList to newly created ArrayList : parcelablePosts
		                    
		                    for(int i = 0; i < postList.size(); i++){
		        	        	
		        				//Log.v("mytag","category "+ i+" is "+listPosts.get(i).getCategory());
		                		//Log.v("mytag","On CategoryFragment, stringList #"+i+" is "+postList.get(i).getLink());

		        				String postLink = postList.get(i).getLink();
		        				postString.add(postLink);

		                    }    
		                    //Log.v("mytag","On CategoryFragment, stringList contains "+postString.size()+" items");
		                    
		                    //post = postList.get(position);
		                    
		                    
		                    Intent intent = new Intent(getActivity(), PostDetailActivity.class);
		                    intent.putExtra("tabPosition", position);
		                    intent.putStringArrayListExtra("posts", postString);
		                    startActivity(intent);
		                }

		            });	
			} else {
				Toast.makeText(context, "No post has been liked yet. ",
						Toast.LENGTH_LONG).show();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("mytag"," Dao error" + e.getMessage());		
		}
		
		
	};

}