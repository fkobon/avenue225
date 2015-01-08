package net.evoir.avenue225.adapters;

import java.net.URLEncoder;
import java.util.List;

import com.squareup.picasso.Picasso;

import net.evoir.avenue225.R;
import net.evoir.avenue225.objects.Post;
import net.evoir.utils.Constants;
import net.evoir.utils.DateFormat;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter {

	private final List<Post> items;
	private Context context;

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
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.listview_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.postTitle_item);
			holder.description = (TextView) convertView.findViewById(R.id.postDescription_item);
			holder.pubDate = (TextView) convertView.findViewById(R.id.postDueDate_item);
			holder.image = (ImageView) convertView.findViewById(R.id.image_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// make sure image is not empty
		String itemImage =items.get(position).getImage();
		Log.v(Constants.TAG,"imageItem "+position+ " is"+itemImage);
		if (itemImage != null && !itemImage.isEmpty()){
			Picasso.with(context).load(itemImage).placeholder(android.R.drawable.ic_menu_camera).error(android.R.drawable.ic_menu_camera).resize(300,200).into(holder.image);	
		}
		//Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(holder.image);

		holder.title.setText(items.get(position).getTitle());
		//holder.description.setText(items.get(position).getDescription());
		DateFormat dateFormat= new DateFormat();
		holder.pubDate.setText(dateFormat.Format(items.get(position).getPubDate()));
		

		return convertView;
		}

	static class ViewHolder {
		TextView title,description,pubDate;
		ImageView image;
	}
	public String UrlEncode (String url) {
		String decodeValue = URLEncoder.encode(url);
		return decodeValue;
	}


}
