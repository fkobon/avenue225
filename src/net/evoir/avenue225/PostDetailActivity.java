package net.evoir.avenue225;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.evoir.avenue225.adapters.ViewPagerAdapter;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class PostDetailActivity extends FragmentActivity {
	private Post post;
	private Context mContext = this;
	private Dao<Post, String> dao;
	private int STATUS_READ =1;
	private int STATUS_LIKED =2;
	
	private FragmentPagerAdapter adapterViewPager;
	private ViewPager vpPager;
	private int tabPosition;
	private ProgressDialog dialog;
	private List<Post> postList;
	private ArrayList<String> stringList;
	//private List<Post> postList;

	
	private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
    	stringList =  getIntent().getStringArrayListExtra("posts");
        tabPosition = getIntent().getIntExtra("tabPosition",0);
        dialog = new ProgressDialog(mContext); 
        loadListAsync loadListAsync= new loadListAsync();
        loadListAsync.execute();
        
        vpPager.setOnPageChangeListener(new OnPageChangeListener() {
        public void onPageScrollStateChanged(int state) {

            // Check if this is the page you want.
        	//Log.v("mytag", "onPageScrollStateChanged called");


        
        }
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        	//Log.v("mytag", "onPageScrolled called");

        }

        public void onPageSelected(int position) {
        	String currentPostLink= postList.get(position).getLink();
        	// we need to querry the dabase to get the current post's real data
        	try {
        		dao = Model.getHelper(mContext).getDao(Post.class);
        		
                post = dao.queryForId(currentPostLink);

                
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		//Log.v("mytag", "post title of page "+position+" is "+postList.get(position).getTitle());
            

    }      
    });
    
        

}

    
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.post_details, menu);
        
        this.menu = menu;
        showHeart(menu);

        
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	if (item.getItemId() == R.id.action_list) {
    		finish();
    	}
        if (item.getItemId() == R.id.action_share) {
        	Intent sendIntent = new Intent();
        	sendIntent.setAction(Intent.ACTION_SEND);
        	sendIntent.putExtra(Intent.EXTRA_TEXT, "Lis vite cet article!"+ post.getTitle() +" > " + post.getLink()+" - Avenue225");
        	sendIntent.setType("text/plain");
        	startActivity(Intent.createChooser(sendIntent, "Partagez l'article"));
        }
        
        if (item.getItemId() == R.id.action_status) {
        	if (post.getStatus()==STATUS_LIKED) {
            	Log.v("mytag", "post.getStatus() is "+post.getStatus());

            	unlikePost();
            }
        	else {
            	likePost();
            }
        	
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void likePost() {
    	menu.getItem(0).setIcon(R.drawable.liked);
    	//status=STATUS_LIKED;
/*    	Log.v("mytag", "status was "+post.getStatus()+" now status is "+STATUS_LIKED);
*/    	post.setStatus(STATUS_LIKED, mContext);
    	Toast.makeText(mContext, "Post has been Liked", Toast.LENGTH_LONG).show();

    }
   
    public void unlikePost() {
    	menu.getItem(0).setIcon(R.drawable.unliked);

    	//status=STATUS_READ;
    	post.setStatus(STATUS_READ, mContext);
    	Toast.makeText(mContext, "Post has been Unliked", Toast.LENGTH_LONG).show();

    }
    	// handle answers
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //Yes button clicked
            	//delete using dao
            	// we set an update builder
            	try {
					dao = Model.getHelper(mContext).getDao(Post.class);
	            	DeleteBuilder<Post, String> deleteBuilder = dao.deleteBuilder();
	            	deleteBuilder.where().eq("link", post.getLink());
	            	deleteBuilder.delete();
	            	
	            	// prepare the new intent
	            	Intent i = new Intent(PostDetailActivity.this, MainActivity.class);
	            	Toast.makeText(mContext, "Post has been deleted", Toast.LENGTH_LONG).show();
	            	startActivity(i);
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

    private void showHeart(Menu menu) {
        if (post.getStatus()==STATUS_LIKED) {
        	//statusItem.setIcon(R.drawable.unliked);
        	menu.getItem(0).setIcon(R.drawable.liked);
        	
        }else{
            
        	menu.getItem(0).setIcon(R.drawable.unliked);
            
        }

    }

    /*
     * ASYNC JOB
     * 
     * */
    private class loadListAsync extends AsyncTask <Void, Void, Void> {

     
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading, please wait.");
            //dialog.setProgressStyle(dialog.STYLE_SPINNER);
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
        	loadList(mContext);
            return null;

        }
        
        public void loadList(Context context){
            Log.v("mytag","On postDetailActivity, stringList contains "+stringList.size()+" items");
            postList = new ArrayList<Post>();
            for (int i = 0; i < stringList.size(); i++) {
            	
            	try {
            		dao = Model.getHelper(mContext).getDao(Post.class);
            		
                    post = dao.queryForId(stringList.get(i).toString());
                    //Log.v("mytag","On postDetailActivity, stringList #"+i+" is "+post.getLink());
            		
            		
            		
                    postList.add(post);
                    
                    
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}

    		}

        }
        public void showList(Context context){
            // load the postList into the Viewpager adapter
            adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager(), postList);
        	vpPager.setAdapter(adapterViewPager);
        	adapterViewPager.notifyDataSetChanged();
            vpPager.setCurrentItem(tabPosition);	
            
        }
         
    }
   
}
