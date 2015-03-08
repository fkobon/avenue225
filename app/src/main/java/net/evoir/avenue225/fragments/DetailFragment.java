package net.evoir.avenue225.fragments;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.androidquery.AQuery;
import com.j256.ormlite.dao.Dao;

import net.evoir.avenue225.R;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import net.evoir.utils.DateFormat;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.TextView;
@SuppressWarnings("deprecation")
public class DetailFragment extends Fragment {
    TextView text;
	private Post post;
	private Context mContext;
	private Dao<Post, String> dao;
	private WebView webView;
	private String link;
    private View view;
    private String content;
    private ImageView detailImage;

    // newInstance constructor for creating fragment with arguments
  public static DetailFragment init(String link) {

	  	DetailFragment f = new DetailFragment();
        Bundle b = new Bundle();
        b.putString("link", link);

        f.setArguments(b);

        return f;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        view = inflater.inflate(R.layout.detail_single_fragment, container, false);
        //String menu = getArguments().getString("Menu");
        //text= (TextView) view.findViewById(R.id.detail);
        //text.setText(menu);
        

        
        mContext = getActivity();
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        	
            link = getArguments().getString("link");
            
            //Log.i("mytag", "on DetailFragment link is : " + link);

            showContent(mContext);
        }
        return view;
    }


		  
        private void showContent(Context mContext) {
        	
        	// load content

			// TODO Auto-generated method stub
            try {
                dao = Model.getHelper(mContext).getDao(Post.class);

                post = dao.queryForId(link);
                post.getStatus();

                
            } catch (SQLException e) {
            Log.e("mytag", "on PostDetailActivity error is: "+ e.getMessage());

        	}
            
        	detailImage = (ImageView) view.findViewById(R.id.detailImage);
        	detailImage.setVisibility(View.GONE);
        	
        	//if getImage is not null
        	if (post.getImage()!= null && !post.getImage().isEmpty()) {

                AQuery aq = new AQuery(mContext);
                aq.id(detailImage).image(post.getImage(), true, true, 320, 200, null, AQuery.FADE_IN_NETWORK, aq.GONE);

            }
    			
    			//.into(detailImage);
    			
    			//detailImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.drop_shadow));

			// TODO Auto-generated method stub
        	webView = (WebView) view.findViewById(R.id.postDescription_detail);
            DateFormat dateFormat= new DateFormat();
    		
            
            Document doc = Jsoup.parse(post.getContent().toString());
            Elements imgs = doc.getElementsByTag("img");
            if(imgs.size()>0){
                doc.select("img").first().remove();
            	
            }
            
            content = new StringBuilder()
            .append("<h2 class='title' style='color:#FF8000'>" + post.getTitle() + "</h2>\r\n")
            .append("<span style='padding:3px 5px;background:#000;color:#FFF;font-size:0.8em;'>" +getResources().getString(R.string.app_name)+ " / "+dateFormat.Format(post.getPubDate()) + "</span>")
            .append(doc.toString().replaceAll("<[a-zA-Z0-9]*></[a-zA-Z0-9]*>", ""))
            .append("<br><br><a href="+post.getLink().toString()+
            		" style='padding:8px 12px;background:#cecece;" +
            		"color:#000;font-size:0.9em;text-align:center;" +
            		"text-transform:uppercase;text-decoration:none;" +
            		"margin:0 auto;display:block;'>" +
            		"Aller sur le site</a>")
            .toString(); //remove tags and brackets
            webView.loadData(content.replaceAll("\\[.*]",""), "text/html; charset=UTF-8", null);
            
             //fit image to screen
            webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
            
            
/*            if (status==STATUS_UNREAD) {
            	post.setStatus(STATUS_READ,mContext);
               // Log.v("mytag", status+" was unread, now status is "+status);

            }*/
		}
    

}