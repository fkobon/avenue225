package net.evoir.parsers;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import net.evoir.avenue225.R;
import net.evoir.avenue225.adapters.PostAdapter;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class PostFragment extends Fragment implements OnItemClickListener {

	private ListView listView;
	private View view;
	//private ImageView logo;
	private Context mContext;
	private static Dao<Post, Integer> dao;
	private List<Post> postList;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.post_detail, container, false);
			listView = (ListView) view.findViewById(R.id.postsList);
			listView.setOnItemClickListener(this);
			

				showList(mContext);
		} else {
			// If we are returning from a configuration change:
			// "view" is still attached to the previous view hierarchy
			// so we need to remove it and re-attach it to the current one
			ViewGroup parent = (ViewGroup) view.getParent();
			parent.removeView(view);
		}
		return view;
	}

/*	private void startService() {
		Intent intent = new Intent(getActivity(), RssService.class);
		intent.putExtra(RssService.RECEIVER, resultReceiver);
		getActivity().startService(intent);
	}*/

	/**
	 * Once the {@link RssService} finishes its task, the result is sent to this
	 * ResultReceiver.
	 */
/*	private final ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
		@SuppressWarnings("unchecked")
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			progressBar.setVisibility(View.GONE);
			logo.setVisibility(View.GONE);
			versionText.setVisibility(View.GONE);
			//List<Post> items = (List<Post>) resultData.getSerializable(RssService.ITEMS);
			
			showList(mContext);
			
		}
	};*/

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		PostAdapter adapter = (PostAdapter) parent.getAdapter();
		Post item = (Post) adapter.getItem(position);
		Uri uri = Uri.parse(item.getLink());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		//Intent intent = new Intent(mContext,PostDetailActivity.class);
		intent.putExtra("id", uri);
		startActivity(intent);
	}

	public void showList(Context context) {
		try {
			dao = Model.getHelper(context).getDao(Post.class);
			postList = dao.queryForAll();
			if (postList != null) {
				PostAdapter adapter = new PostAdapter(getActivity(), postList);
				listView.setAdapter(adapter);
				for (int i = 0; i < postList.size(); i++) {
					Log.v("mytag"," title is " +postList.get(i).getTitle());
					Log.v("mytag"," image url is " +postList.get(i).getImage());
					Log.v("mytag"," link is " +postList.get(i).getLink());						
				}
				
			} else {
				Toast.makeText(getActivity(), "An error occured while downloading the rss feed. Check your network connection",
						Toast.LENGTH_LONG).show();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("mytag"," Dao error" + e.getMessage());		
		}
		
	};
}

