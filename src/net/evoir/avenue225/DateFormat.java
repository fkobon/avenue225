package net.evoir.avenue225;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormat {

	public String Format(String date) {
		 //String mytime="Jan 17, 2012";
	    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
	    Date myDate = null;
	    try {
	        myDate = dateFormat.parse(date);

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd");
	    String finalDate = timeFormat.format(myDate);

	    return finalDate;
	}

}

