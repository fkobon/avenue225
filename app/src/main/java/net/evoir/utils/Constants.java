package net.evoir.utils;

public class Constants {

	public static final String TAG = "mytag";
	public static final String JSON_LINK = "http://www.avenue225.com/json";

	//JSON Node names
	public static final String TAG_LINK = "link";
	public static final String TAG_TITLE = "title";
	public static final String TAG_IMG = "image";
	public static final String TAG_DESC = "description";
	public static final String TAG_PUBDATE = "pubDate";
	public static final String TAG_CONTENT = "content";
	public static final String TAG_CATEGORY = "category";
	public static final String TAG_CATEGORY_SLUG = "categorySlug";
	public static final int STATUS_UNREAD =0;

    public static final int MINUTE_VALUE = 10;
	public static String lastPubDate;
	public static void setLastPubDate(String newLastPubDate) {
		// TODO Auto-generated method stub
		lastPubDate = newLastPubDate;
		
	}


}
