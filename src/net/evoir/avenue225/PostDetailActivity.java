package net.evoir.avenue225;

import java.sql.SQLException;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import net.evoir.avenue225.DateFormat;

import org.jsoup.Jsoup;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class PostDetailActivity extends Activity {
	private Post post;
	private Context mContext = this;
	private Dao<Post, String> dao;
	private WebView webView;
	private int status;
	private int STATUS_UNREAD =0;
	private int STATUS_READ =1;
	private int STATUS_LIKED =2;
	
	private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.post_detail);

        if (this.getIntent().getExtras() != null) {
            String link = this.getIntent().getExtras().getString("link");
            
            Log.i("mytag", "on PostDetailActivity link is : " + link);

            
            try {
                dao = Model.getHelper(this).getDao(Post.class);

                post = dao.queryForId(link);
                status = post.getStatus();
                webView = (WebView) this.findViewById(R.id.postDescription_detail);
                DateFormat dateFormat= new DateFormat();
        		
                
                String content = new StringBuilder()
                .append("<h2 class='title' style='color:#FF8000'>" + post.getTitle() + "</h2>\r\n")
                .append("<span style='padding:3px 5px;background:#000;color:#FFF;font-size:0.8em;'>" +getResources().getString(R.string.app_name)+ " / "+dateFormat.Format(post.getPubDate()) + "</span>")
                .append(Jsoup.parse(post.getContent()).toString())
                .toString().replaceAll("<[a-zA-Z0-9]*></[a-zA-Z0-9]*>", ""); //remove tags and brackets
                webView.loadData(content.replaceAll("\\[.*]",""), "text/html; charset=UTF-8", null);
                
                 //fit image to screen
                webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                
                
                
                if (status==STATUS_UNREAD) {
                	post.setStatus(STATUS_READ,mContext);
                }
                
        } catch (SQLException e) {
            Log.e("mytag", "on PostDetailActivity error is: "+ e.getMessage());

        	}
        }
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.post_details, menu);
        
        this.menu = menu;
        if (status==STATUS_LIKED) {
        	//statusItem.setIcon(R.drawable.unliked);
        	menu.getItem(0).setIcon(R.drawable.liked);
        	
        }else{
            
        	menu.getItem(0).setIcon(R.drawable.unliked);
            
        }

        
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	if (item.getItemId() == R.id.action_list) {
    		//startActivity(new Intent(PostDetailActivity.this, MainActivity.class));
    		finish();
    	}
        if (item.getItemId() == R.id.action_share) {
        	Intent sendIntent = new Intent();
        	sendIntent.setAction(Intent.ACTION_SEND);
        	sendIntent.putExtra(Intent.EXTRA_TEXT, "Read: "+ post.getTitle() +" > " + post.getLink());
        	sendIntent.setType("text/plain");
        	startActivity(Intent.createChooser(sendIntent, "Share article"));
        }
        /*if (item.getItemId() == R.id.action_edit) {
        	Intent i = new Intent(PostDetailActivity.this, PostEditActivity.class);
        	i.putExtra("id", post.getId());
        	
        	startActivity(i);
        }*/
/*        if (item.getItemId() == R.id.action_delete) {
        	
        	
        	// Ask a validation
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        }*/
        
        if (item.getItemId() == R.id.action_status) {
        	if (status==STATUS_READ ||status==STATUS_UNREAD ) {
            	likePost();
            }
        	else if (status==STATUS_LIKED) {
            	unlikePost();
            }
        	
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void likePost() {
    	menu.getItem(0).setIcon(R.drawable.liked);
    	status=STATUS_LIKED;
    	post.setStatus(STATUS_LIKED, mContext);
    	Toast.makeText(mContext, "Post has been Favorized", Toast.LENGTH_LONG).show();

    }
   
    public void unlikePost() {
    	menu.getItem(0).setIcon(R.drawable.unliked);
    	status=STATUS_READ;
    	post.setStatus(STATUS_READ, mContext);
    	Toast.makeText(mContext, "Post has been Unfavorized", Toast.LENGTH_LONG).show();

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


}
