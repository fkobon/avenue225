package net.evoir.avenue225.fragments;

import java.sql.SQLException;
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
public class SearchFragment extends Fragment {
	
	private ListView listView;
	private static Dao<Post, String> dao;
	private Post post;
	private List<Post> postList;
	private Context mContext;
	private String query;
	
	
    TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.home_activity, container, false);
        
		return view;
    }
	

    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        
        if (this.getArguments() != null) {
			  
			  //use the query to search your data somehow
			
			query = getArguments().getString("query");   
			//show ListView 
			mContext = getActivity();
			showList(mContext);
        }
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
	    	//custom orm lite query for searching through titles
	    	queryBuilder.where().like("title", "%"+query+"%");
	    	queryBuilder.orderBy("pubDate", false);
			postList = queryBuilder.query();
			if (postList.size() >0) {
				PostAdapter adapter = new PostAdapter(getActivity(), postList);

				
				listView = (ListView) getActivity().findViewById(R.id.postsList);
				//listView.setSelector(R.color.yellowLight);
				listView.setAdapter(adapter);
				

				
				// highliting issue
				
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		                @Override
		                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		                    post = postList.get(position);
		                    Intent intent = new Intent(mContext, PostDetailActivity.class);
		                    intent.putExtra("link", post.getLink());
		                    startActivity(intent);
		                }

		            });	
			} else {
				Toast.makeText(context, "0 Results found. Try again and be less restrictive",
						Toast.LENGTH_LONG).show();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("mytag"," Dao error" + e.getMessage());		
		}
		
		
	};

}