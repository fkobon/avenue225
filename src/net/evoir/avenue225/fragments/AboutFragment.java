package net.evoir.avenue225.fragments;
import org.jsoup.Jsoup;

import net.evoir.avenue225.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
public class AboutFragment extends Fragment {
    String copyrightText;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
    	
        View view = inflater.inflate(R.layout.menu_detail_fragment, container, false);
        //String menu = getArguments().getString("Menu");
        WebView copyrightView= (WebView) view.findViewById(R.id.copyrightText);
        
        copyrightText ="<style> a {text-decoration:none;color:#FF8000;}" +
        		"</style>" +
        		"<section style='text-align:center;'>Cette application a été developpée pour <a href=\"http://avenue225.com\">Avenue225.com</a>," +
        		" le premier site d'information de proximité de Côte d'Ivoire.<br>" +
    			"<hr style='height:0.5px;'><br><small>Copyright <a href=\"http://e-voir.net\">E-voir - Internet marketing & medias </a></small></section>" ;
        
        String content = new StringBuilder()
        .append(copyrightText).toString();
        copyrightView.loadData(content.replaceAll("\\[.*]",""), "text/html; charset=UTF-8", null);
        
        //setting webview text size
        /*WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultFontSize(16);*/
        
        //fit image to screen
        copyrightView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        
        return view;
    }
}