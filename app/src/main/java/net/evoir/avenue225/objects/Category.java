package net.evoir.avenue225.objects;

import java.sql.SQLException;

import net.evoir.avenue225.db.Model;
import net.evoir.utils.Constants;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;


public class Category {

	private static Dao<Post, String> dao;
	private static Dao<Category, String> daoCat;

    @DatabaseField(id = true)
    private String slug;
    private int image;
    @DatabaseField
	private String title;
    @DatabaseField
	private int type;
    @DatabaseField
	private int number=0;
	private int STATUS_UNREAD =0;
	private int STATUS_LIKED =2;
	private Fragment method;




    public int getNumber() {
        return number;
	}
    public void setNumber(Context context,int customNumber) {
        try {

                dao = Model.getHelper(context).getDao(Post.class);
                QueryBuilder<Post, String> queryBuilder =
                        dao.queryBuilder();
                //get posts from database and group them by category
                queryBuilder.where().eq("categorySlug", slug);
                //queryBuilder.orderBy("pubDate", false);
                int postNumber;
                if(0==customNumber) {
                    postNumber = queryBuilder.query().size();

                }else{
                    postNumber=customNumber;
                }


            // we set an update builder
            daoCat = Model.getHelper(context).getDao(Category.class);
            UpdateBuilder<Category, String> updateBuilder = daoCat.updateBuilder();

            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("slug", slug);
            // update the value of the fields
            updateBuilder.updateColumnValue("number",postNumber);
            // update

            updateBuilder.update();
            this.number= postNumber;

        } catch (SQLException e) {
            Log.e("Category updated", e.getMessage());
        }
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
    public Category(int image,String title,int type,int number) {
        this.image = image;
        this.title= title;
        this.type= type;
        this.number =number;
    }
	// for SETTINGS purpose
	public Category(int image,String title,int type) {
		this.image = image;
		this.title= title;
		this.type= type;
    }

    // for DB purpose
    public Category(String title,int type,String slug) {
        this.title= title;
        this.type= type;
        this.slug = slug;
    }

}

