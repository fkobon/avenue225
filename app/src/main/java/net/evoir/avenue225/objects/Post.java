package net.evoir.avenue225.objects;

import java.sql.SQLException;

import net.evoir.avenue225.db.Model;


import android.content.Context;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;

public class Post {


	private static Dao<Post, String> dao;
	@DatabaseField
	private String image;
	
	@DatabaseField(id = true)  
	private String link;
	@DatabaseField
	private String title;
	@DatabaseField
	private String description;
	@DatabaseField
    private String content;

	@DatabaseField
	private String pubDate;
	
	@DatabaseField
	private String category;
	
	@DatabaseField
    private String categorySlug;
	
	@DatabaseField
    private int status =0;
    
    
    







	public Post() {
        
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }


    public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}       
	
	public String getCategorySlug() {
		return categorySlug;
	}

	public void setCategorySlug(String categorySlug) {
		this.categorySlug = categorySlug;
	}
	
    public Post(String link, String title, String description,String content,String pubDate, String image, String category,String categorySlug) {
    	this.link = link;
        this.title = title;
        this.description = description;
        this.content = content;
        this.pubDate = pubDate;
        this.image= image;
        this.category= category;
        this.categorySlug= categorySlug;
    }    
    
	public int getStatus() {
		return status;
	}

	public void setStatus(int status,Context context) {
		//this.status = status;
		   try {
           	// we set an update builder
           	dao = Model.getHelper(context).getDao(Post.class);
           	UpdateBuilder<Post, String> updateBuilder = dao.updateBuilder();
           	
           	// set the criteria like you would a QueryBuilder
           	updateBuilder.where().eq("link", this.link);
           	// update the value of the fields
           	updateBuilder.updateColumnValue("status",status );
        	// update
           	updateBuilder.update();
           	this.status= status;

           } catch (SQLException e) {
               Log.e("Task EditActivity", e.getMessage());
           }
	}

}
