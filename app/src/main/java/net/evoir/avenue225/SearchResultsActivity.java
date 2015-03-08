package net.evoir.avenue225;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import net.evoir.avenue225.adapters.PostAdapter;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class SearchResultsActivity extends Activity {

	   private ListView listView;
       private static Dao<Post, String> dao;
       private List<Post> postList;
       private Post post;
       private Context mContext;
       private PostAdapter adapter;
       private String query;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_list_layout);
		mContext = this;  
		
		//run the sear query and update categories
		  if (this.getIntent().getExtras() != null) {
			  
	          //use the query to search your data somehow
	          query = this.getIntent().getExtras().getString("query");
	          showList(mContext);
	          
	      }

  	}



	public void showList(Context context) {
		
		try {
			dao = Model.getHelper(mContext).getDao(Post.class);
	    	QueryBuilder<Post, String> queryBuilder =
	    	dao.queryBuilder();
			//get posts from database and group them by category
	    	//queryBuilder.where().eq("categorySlug", query);
	    	
	    	//custom orm lite query for searching through titles
	    	queryBuilder.where().like("title", "%"+query+"%");
	    	queryBuilder.orderBy("pubDate", false);
			postList = queryBuilder.query();
			if (postList.size() >0) {
				Log.v("mytag",postList.size()+" posts found");
				adapter = new PostAdapter(context, postList);

				
				listView = (ListView) findViewById(R.id.postsList);
				listView.setAdapter(adapter);
				
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
				Toast.makeText(context, "An error occured while downloading information. Check your network connection",
						Toast.LENGTH_LONG).show();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("mytag"," Dao error" + e.getMessage());		
		}
		
		
	};
}