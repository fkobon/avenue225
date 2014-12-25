package net.evoir.avenue225;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import net.evoir.avenue225.adapters.CategoryAdapter;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.fragments.AboutFragment;
import net.evoir.avenue225.fragments.CategoryFragment;
import net.evoir.avenue225.fragments.FavorisFragment;
import net.evoir.avenue225.fragments.SearchFragment;
import net.evoir.avenue225.fragments.SendFragment;
import net.evoir.avenue225.fragments.SyncFragment;
import net.evoir.avenue225.objects.Category;
import net.evoir.avenue225.objects.Post;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends Activity {
       private DrawerLayout mDrawerLayout;
       private ActionBarDrawerToggle mDrawerToggle;
       private ListView mDrawerList;
       private static Dao<Post, String> dao;
       private ArrayList<Category> categories = new ArrayList<Category>();
       private ArrayList<Category> finalCategories = new ArrayList<Category>();
       private List<Post> listPosts;
       private Context mContext;
       private CategoryAdapter adapter;
       private int savedPosition=0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.category_list_layout);
        //menu = new String[]{"Home","Android","Windows","Linux","Raspberry Pi","WordPress","Videos","Contact Us"};
        mContext = this;  
    	
        
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         
          // Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

          mDrawerList = (ListView) findViewById(R.id.left_drawer);
          
          finalCategories = buildCategories();
          
          
          adapter = new CategoryAdapter(mContext, finalCategories);	
          mDrawerList.setAdapter(adapter);
          adapter.notifyDataSetChanged();
          
          getActionBar().setDisplayHomeAsUpEnabled(true);
          getActionBar().setHomeButtonEnabled(true);
          getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_orange));
   
          mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                  R.drawable.ic_drawer, //nav menu toggle icon
                  R.string.app_name, // nav drawer open - description for accessibility
                  R.string.app_name // nav drawer close - description for accessibility
          ){
              public void onDrawerClosed(View view) {

              }
   
              public void onDrawerOpened(View drawerView) {
                  // calling onPrepareOptionsMenu() to hide action bar icons
                  //invalidateOptionsMenu();
                                    //recreating the adapter if we toggle the Drawer
                  
            	  /*Log.v("mytag", "Drawer was opened");
                  onDrawerClickListener();*/
 
              }
              
               
          };
          // set onClickListener on Drawer
          onDrawerClickListener();
          
              // on first time display view for first nav item
          if (savedInstanceState == null) {
              displayHomeView();
          }
          

  	}
  @Override
  protected void onPause()
  {		 
	  super.onPause();
	  savedPosition = mDrawerList.getFirstVisiblePosition();
     // store index using shared preferences   
  }
  
  @Override
  public void onResume() {
  super.onResume();
  // get index from shared preferences

  if(mDrawerList != null){
      if(mDrawerList.getCount() > savedPosition) {
          adapter.clear();
          adapter = new CategoryAdapter(mContext, buildCategories());	
          //adapter.notifyDataSetChanged();
          mDrawerList.setAdapter(adapter);
          adapter.notifyDataSetChanged();
          mDrawerList.setSelectionFromTop(savedPosition, 0);
      }
      else{
          mDrawerList.setSelectionFromTop(0,0);}
  	}
  }
  private ArrayList<Category> buildCategories() {
	// TODO Auto-generated method stub
		try {
			dao = Model.getHelper(mContext).getDao(Post.class);
	    	QueryBuilder<Post, String> queryBuilder =
	    	dao.queryBuilder();
			//get posts from database and group them by category
			listPosts = queryBuilder.groupBy("category").query();
			//inflate the category arrayList<String> with every post.getCategory 
			//Log.v("mytag","there are "+listPosts.size()+" categories");
			
			// convert List<String> to String[]
        	
			categories.clear();
			categories.add(new Category("Menu",0));
	        
			

	        for(int i = 0; i < listPosts.size(); i++){
	        	
				//Log.v("mytag","category "+ i+" is "+listPosts.get(i).getCategory());
				Category categoryItem = new Category(R.drawable.logo,listPosts.get(i).getCategory());
	            categoryItem.setSlug(listPosts.get(i).getCategorySlug());
				categories.add(categoryItem);
	            

	        }
	        Fragment aboutFragment = new AboutFragment();
	        Fragment favorisFragment = new FavorisFragment();
	        Fragment sendFragment = new SendFragment();
	        Fragment syncFragment = new SyncFragment();
	        categories.add(new Category("Options",0));
	        categories.add(new Category(android.R.drawable.ic_menu_help,"About",1,aboutFragment));
	        categories.add(new Category(android.R.drawable.ic_menu_send,"Send feedback",1,sendFragment));
			categories.add(new Category(R.drawable.liked,"Favoris",2,favorisFragment));
			categories.add(new Category(android.R.drawable.ic_popup_sync,"Synchroniser",1,syncFragment));
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categories;	

	
}





private void onDrawerClickListener() {

    mDrawerList.setOnItemClickListener(new OnItemClickListener(){
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Log.v("mytag", "selectItem() was called");

    	selectItem(position);        	
    }
  });
}

private void displayHomeView() {
	// TODO Auto-generated method stub
	  Log.v("mytag", "displayHomeView() called");  
      //home fragment
	  
      Fragment home = new CategoryFragment();
      FragmentManager fragmentManager = getFragmentManager();      
      fragmentManager.beginTransaction().replace(R.id.content_frame, home).commit();
      

}

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.options_menu, menu);
      
      
      // Associate searchable configuration with the SearchView
      SearchManager searchManager =
             (SearchManager) getSystemService(Context.SEARCH_SERVICE);
      SearchView searchView =
              (SearchView) menu.findItem(R.id.action_search).getActionView();
      //searchView.setSubmitButtonEnabled(false);
      searchView.setSubmitButtonEnabled(false);
      searchView.setQueryHint("Texte ...");
      searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

      /*searchView.setSearchableInfo(
              searchManager.getSearchableInfo(getComponentName()));*/

      SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String s) {
        	 // return false;
        	  
        	  // runSearhResultsActivity 
        	  
/*        	  Intent intent = new Intent(mContext, SearchResultsActivity.class);
        	  intent.putExtra("query", s);
              startActivity(intent);*/
        	  
              Bundle args = new Bundle();
              args.putString("query", s);
              Fragment search = new SearchFragment();
              search.setArguments(args);
              // change app menu title
              if(s!="") {
                  
                  FragmentManager fragmentManager = getFragmentManager();
                  fragmentManager.beginTransaction()
            			      	 .replace(R.id.content_frame, search)
            			      	 .commit();
                  
              }

              return true;
          }

          @Override
          public boolean onQueryTextChange(String s) {
/*              ListView listView = (ListView) findViewById(R.id.main_listView);
              listView.setTextFilterEnabled(true);

              if (s.isEmpty()) {
                  listView.clearTextFilter();
              } else {
                  customAdapter.getFilter().filter(s);
              }

              return true;*/
        	  return false;
          }
      };

      searchView.setOnQueryTextListener(queryTextListener);
      return super.onCreateOptionsMenu(menu);
 
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      // toggle nav drawer on selecting action bar app icon/title
	      if (mDrawerToggle.onOptionsItemSelected(item)) {
	    	// set onClickListener on Drawer
              onDrawerClickListener();
	      }
		return true;
 
      }
  




  /**
   * When using the ActionBarDrawerToggle, you must call it during
   * onPostCreate() and onConfigurationChanged()...
   */

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      // Sync the toggle state after onRestoreInstanceState has occurred.
      mDrawerToggle.syncState();

  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  // Pass any configuration change to the drawer toggls
	  mDrawerToggle.onConfigurationChanged(newConfig);
	  //adapter.notifyDataSetChanged();
	  
	  
  }


public void selectItem(int position)
  {
      mDrawerLayout.closeDrawers();
      Bundle args = new Bundle();
      args.putString("category", categories.get(position).getTitle());
      args.putString("categorySlug", categories.get(position).getSlug());
      args.putInt("categoryNumber", categories.get(position).getNumber(mContext));
      
      Fragment detail = new Fragment();

      if(categories.get(position).getType()==1){
    	  detail = (Fragment) categories.get(position).getMethod();
    	  
    	  /*
    	   * TODO
    	   * refine the logic and clear unused lines
    	   * */
    	  if(categories.get(position).getNumber(mContext)<=0) {
    		  setTitle(categories.get(position).getTitle());
    		  
    	  }else {
    		  setTitle(categories.get(position).getTitle()+" ("+categories.get(position).getNumber(mContext)+")");
    	  }
    	  
      }
      else if(categories.get(position).getType()==2){
	    	  detail = (Fragment) categories.get(position).getMethod();
	
	          // change app menu title
	          if(categories.get(position).getLikedNumber(mContext)<=0) {
	        	  setTitle(categories.get(position).getTitle());
	              
	          }else {
	        	  setTitle(categories.get(position).getTitle()+" ("+categories.get(position).getLikedNumber(mContext)+")");
	          }
	          
    	  }
      
      else {
    		  detail = new CategoryFragment();
    	  }
      detail.setArguments(args);

      FragmentManager fragmentManager = getFragmentManager();
      fragmentManager.beginTransaction()
			      	 .replace(R.id.content_frame, detail)
			      	 .commit();
     
  }
 

  @Override
  public void setTitle(CharSequence title) {
	  CharSequence mTitle = title;
      getActionBar().setTitle(mTitle);
  }
}