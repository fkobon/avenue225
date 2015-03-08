package net.evoir.avenue225.adapters;

import java.util.List;

import net.evoir.avenue225.fragments.DetailFragment;
import net.evoir.avenue225.objects.Post;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ViewPagerAdapter extends FragmentPagerAdapter {

	/**
	 * @param args
	 */

		// TODO Auto-generated method stub
		private List<Post> postList;
		private int NUM_ITEMS;

        public ViewPagerAdapter(FragmentManager fragmentManager,List<Post> postList) {
            super(fragmentManager);
        	this.postList= postList; 
        	NUM_ITEMS = postList.size();

        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
        	
            return DetailFragment.init(postList.get(position).getLink());
            
        }       
}