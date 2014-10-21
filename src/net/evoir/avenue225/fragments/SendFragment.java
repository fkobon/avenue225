package net.evoir.avenue225.fragments;
import net.evoir.avenue225.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
public class SendFragment extends Fragment {
    TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
    	//View view = inflater.inflate(R.layout.menu_detail_fragment, container, false);
        View view = null;
        //String menu = getArguments().getString("Menu");
        //text= (TextView) view.findViewById(R.id.detail);
        //text.setText(menu);
        //getActivity().finish();
        
        
        SendFeedback();
        displayAboutView(); //go back to home view

        return view;
    }

    private void SendFeedback() {
    	// TODO Auto-generated method stub
    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto","samuelguebo@gmail.com", null));
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Avenue225 Moile App");
    startActivity(Intent.createChooser(emailIntent, "Send Feedback..."));

    }
    private void displayAboutView() {
    	// TODO Auto-generated method stub
    	    
          //home fragment
    		
          Fragment home = new AboutFragment();
          FragmentManager fragmentManager = getFragmentManager();
          fragmentManager.beginTransaction().replace(R.id.content_frame, home).commit();

    }
}