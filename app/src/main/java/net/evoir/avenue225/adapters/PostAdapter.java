package net.evoir.avenue225.adapters;

import java.net.URLEncoder;
import java.util.List;
import com.androidquery.AQuery;
import net.evoir.avenue225.R;
import net.evoir.avenue225.objects.Post;
import net.evoir.utils.AppController;
import net.evoir.utils.DateFormat;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter {

	private final List<Post> items;
	private Context context;
	private ViewHolder holder;
	public PostAdapter(Context context, List<Post> items) {
		this.items = items;
		this.context = context;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.listview_item, null);


			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.postTitle_item);
			holder.description = (TextView) convertView.findViewById(R.id.postDescription_item);
			holder.pubDate = (TextView) convertView.findViewById(R.id.postDueDate_item);
			holder.image = (ImageView) convertView.findViewById(R.id.image_item);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.progress);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// make sure image is not empty
		String itemImage =items.get(position).getImage();
		//Log.v(Constants.TAG,"imageItem "+position+ " is"+itemImage);
    	//if getImage is not null
		
		//hide imageView by default and only show if image is loaded
    	if (itemImage!= null && !itemImage.isEmpty()) {
            //holder.image.setVisibility(View.GONE);

            //load image
            AQuery aq = new AQuery(convertView);
            aq.id(holder.image).progress(holder.progress).image(itemImage, true, true,200,R.drawable.imageview_back);



        }
		
		//Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(holder.image);

		holder.title.setText(items.get(position).getTitle());
		holder.description.setText(items.get(position).getDescription()+"...");
		//holder.description.setText(items.get(position).getDescription());
		DateFormat dateFormat= new DateFormat();
		holder.pubDate.setText(dateFormat.Format(items.get(position).getPubDate()));
		

		return convertView;
		}

	static class ViewHolder {
		TextView title,description,pubDate;
		ImageView image;
        ProgressBar progress;


	}
	public String UrlEncode (String url) {
		String decodeValue = URLEncoder.encode(url);
		return decodeValue;
	}


}
