package net.evoir.avenue225.fragments;


import net.evoir.avenue225.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
	   
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    
	   super.onCreate(savedInstanceState);
	   addPreferencesFromResource(R.xml.settings_fragment);
 }

}