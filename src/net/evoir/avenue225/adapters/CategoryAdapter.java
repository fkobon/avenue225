package net.evoir.avenue225.adapters;

import java.util.ArrayList;
import java.util.List;

import net.evoir.avenue225.R;
import net.evoir.avenue225.adapters.PostAdapter.ViewHolder;
import net.evoir.avenue225.objects.Category;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryAdapter extends ArrayAdapter<Category> {

	private ArrayList<Category> menu;
	private Context context;
	private static final int TYPE_CATEGORY = -1;
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_CUSTOM = 1;
	private static final int TYPE_FAVORIZED = 2;
	private static final int TYPE_SETTINGS = 3;

	private LayoutInflater vi;
	private int number;
	
	public CategoryAdapter(Context context, ArrayList<Category> menuItems) {
		super(context,0, menuItems);
		this.menu = menuItems;
		this.context = context;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		
/*		menuItems.add(new Category(0,"Avenue225"));
		notifyDataSetChanged();*/

	}

	@Override
	public int getCount() {
		return menu.size();
	}

	@Override
	public Category getItem(int position) {
		return menu.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public int getItemViewType(int position) {
		return menu.get(position).getType();
	}
    @Override
    public int getViewTypeCount() {
        return 4;  // sectionheader and regular item
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		//if type is Favoris
		if (menu.get(position).getType()==TYPE_FAVORIZED) {
			   number = menu.get(position).getLikedNumber(context);

		}else {
			   number = menu.get(position).getNumber(context);

		}
	   //if type is HEADER (Menu, Options etc)
       if (getItemViewType(position)==TYPE_HEADER){
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.header_view,null);
				holder.title = (TextView) convertView.findViewById(R.id.textHeader);
				holder.title.setText(menu.get(position).getTitle());
				convertView.setTag(holder);
				//convertView.setClickable(false);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.title.setText(menu.get(position).getTitle());

			}

       }
		//if type is Settings
       if (menu.get(position).getType()==TYPE_SETTINGS){
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.category_list_item, null);
				holder.title = (TextView) convertView.findViewById(R.id.drawerTitle_item);
				holder.title.setText(menu.get(position).getTitle());
				holder.image = (ImageView) convertView.findViewById(R.id.drawerImage_item);
				holder.image.setImageResource(menu.get(position).getImage());
				convertView.setTag(holder);
				//convertView.setClickable(false);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.title.setText(menu.get(position).getTitle());

			}

       }
/*       if (type==TYPE_CUSTOM){
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.category_list_item, null);
                holder.title = (TextView) convertView.findViewById(R.id.drawerTitle_item);
    			holder.image = (ImageView) convertView.findViewById(R.id.drawerImage_item);
				holder.image.setImageResource(menu.get(position).getImage());
				holder.title.setText(menu.get(position).getTitle());
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.title.setText(menu.get(position).getTitle());

			}

       }*/
       else{
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.category_list_item, null);
                holder.title = (TextView) convertView.findViewById(R.id.drawerTitle_item);
                holder.number = (TextView) convertView.findViewById(R.id.drawerNumber);
    			holder.image = (ImageView) convertView.findViewById(R.id.drawerImage_item);
				holder.image.setImageResource(menu.get(position).getImage());
				holder.title.setText(menu.get(position).getTitle());
				
				if(number>0){
					holder.number.setVisibility(View.VISIBLE);
					holder.number.setText(""+number);
					notifyDataSetChanged();
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				//holder.title.setText(menu.get(position).getTitle());

			}

       }

		return convertView;
		}

	static class ViewHolder {
		TextView title,number;
		ImageView image;
	}

	/* override native methods of BaseAdapter "areAllItemsEnabled"
	and "isEnabled" in order to set Header unclickable*/
	@Override
	public boolean areAllItemsEnabled(){
		return false;
	}
	
	@Override 
	public boolean isEnabled(int position) {
		return (TYPE_HEADER==menu.get(position).getType())?false:true;
	}

}
