package net.evoir.utils;
import android.webkit.URLUtil;

public class UrlTool {


	public String getFileNameFromURL(String url) {
	    String fileNameWithExtension = null;
	    String fileNameWithoutExtension = null;
	    if (URLUtil.isValidUrl(url)) {
	        fileNameWithExtension = URLUtil.guessFileName(url, null, null);
	        if (fileNameWithExtension != null && !fileNameWithExtension.isEmpty()) {
	            String[] f = fileNameWithExtension.split(".");
	            if (f != null & f.length > 1) {
	                fileNameWithoutExtension = f[0];
	            }
	        }
	    }
	    return fileNameWithoutExtension;
	}

}
