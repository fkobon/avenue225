package net.evoir.avenue225;

import java.util.List;

import net.evoir.avenue225.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter {

	private final List<Post> items;
	private final Context context;

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
			convertView = View.inflate(context, R.layout.rss_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.postTitle_item);
			holder.link = (TextView) convertView.findViewById(R.id.postDescription_item);
			holder.pubDate = (TextView) convertView.findViewById(R.id.postDueDate_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(items.get(position).getTitle());
		holder.link.setText(items.get(position).getLink());
		DateFormat dateFormat= new DateFormat();
		holder.pubDate.setText(dateFormat.Format(items.get(position).getPubDate()));
		return convertView;
	}

	static class ViewHolder {
		TextView title,link,pubDate;
	}
}
