package net.evoir.avenue225.objects;

import java.sql.SQLException;

import net.evoir.avenue225.db.Model;

import android.app.Fragment;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;


public class Category {
	private static Dao<Post, String> dao;

	private String slug;
	private int image;
	private String title;
	private int type;
	private int number;
	private int STATUS_UNREAD =0;
	private int STATUS_LIKED =2;
	private Fragment method;

	
	public int getNumber(Context context) {
		try {
			dao = Model.getHelper(context).getDao(Post.class);
	    	QueryBuilder<Post, String> queryBuilder =
	    	    	dao.queryBuilder();
	    			//get posts from database and group them by category
			    		queryBuilder.where().eq("categorySlug", this.slug).and().eq("status", STATUS_UNREAD);
			    		queryBuilder.orderBy("pubDate", false);
			    		number = queryBuilder.query().size();

	    			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return number;
		
	}
	public int getLikedNumber(Context context) {
		try {
			dao = Model.getHelper(context).getDao(Post.class);
	    	QueryBuilder<Post, String> queryBuilder =
	    	    	dao.queryBuilder();
	    			//get posts from database and group them by category
			    		queryBuilder.where().eq("status", STATUS_LIKED);
			    		queryBuilder.orderBy("pubDate", false);
			    		number = queryBuilder.query().size();

	    			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return number;
		
	}
	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Fragment getMethod() {
		return method;
	}

	public void setMethod(Fragment method) {
		this.method = method;
	}

	public void setImage(int image) {
		this.image = image;
	}


	
	
	public int getImage() {
		return image;
	}
	
	public void setImg(int img) {
		this.image = img;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Category() {
		
	}
	
	//for TYPE_CUSTOM
	public Category(int image,String title,int type,Fragment method) {
		this.image = image;
		this.title= title;
		this.type= type;
		this.method= method;
    }

	//for TYPE_HEADER
	public Category(String title, int type) {
		// TODO Auto-generated constructor stub
		this.title= title;
		this.type= type;
	}

	// for TYPE_DEFAULT
	public Category(int image,String title) {
		this.image = image;
		this.title= title;
		this.type= 3;
    }


}
