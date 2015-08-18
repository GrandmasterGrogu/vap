package com.alexxg.android_vap_demo;

import android.content.ClipData;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class ItemDetailsFragment extends HtmlFragment {

    public ItemDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ItemDetails theActivity = (ItemDetails)getActivity();
int deviceOrVideo = theActivity.getDeviceOrVideo();
        setRootView((ViewGroup) inflater.inflate(
                R.layout.fragment_item_details, container, false));
        if( deviceOrVideo== 1)
        {
            TextView text = (TextView)getRootView().findViewById(R.id.entity_name);
            text.setText(getString(R.string.device_no_html));

            text = (TextView)getRootView().findViewById(R.id.id_number);
            text.setText(Integer.toString(theActivity.getSecretDeviceID()));

            text = (TextView)getRootView().findViewById(R.id.first_label);
            text.setText(getString(R.string.android_device_id_no_html));

            text = (TextView)getRootView().findViewById(R.id.first_value);
            text.setText(theActivity.getDeviceID());

            text = (TextView)getRootView().findViewById(R.id.confirm_label);
            text.setText(getString(R.string.device_was_confirmed));

            text = (TextView)getRootView().findViewById(R.id.confirm_value);
            if(theActivity.getConfirmedDevice())
                text.setText(getString(R.string.yes));
            else
                text.setText(getString(R.string.no));

            text = (TextView)getRootView().findViewById(R.id.metadata);
            text.setText(getString(R.string.metadata_no_html));

            text = (TextView)getRootView().findViewById(R.id.metadata_value);
            text.setText(theActivity.getDeviceMetadata());

            text = (TextView)getRootView().findViewById(R.id.vap_token);
            text.setText(getString(R.string.token_no_html));

            text = (TextView)getRootView().findViewById(R.id.vap_token_value);
            text.setText(theActivity.getToken());

            text = (TextView)getRootView().findViewById(R.id.vap_old_token);
            text.setText(getString(R.string.oldtoken_no_html));

            text = (TextView)getRootView().findViewById(R.id.vap_old_token_value);
            text.setText(theActivity.getOldToken());

            text = (TextView)getRootView().findViewById(R.id.public_key);
            text.setText(getString(R.string.public_key));

            text = (TextView)getRootView().findViewById(R.id.public_key_value);
            text.setText(theActivity.getPublicKey());

        }
        else if ( deviceOrVideo== 2)
        {

            TextView text = (TextView)getRootView().findViewById(R.id.entity_name);
            text.setText(getString(R.string.video));

            text = (TextView)getRootView().findViewById(R.id.id_number);
            text.setText(Integer.toString(theActivity.getVideoID()));

            text = (TextView)getRootView().findViewById(R.id.first_label);
            text.setText(getString(R.string.device));

            text = (TextView)getRootView().findViewById(R.id.first_value);
            text.setText(Integer.toString(theActivity.getVideoDeviceID()));

            text = (TextView)getRootView().findViewById(R.id.confirm_label);
            text.setText(getString(R.string.video_was_confirmed));

            text = (TextView)getRootView().findViewById(R.id.confirm_value);
            if(theActivity.getConfirmedVideo())
                text.setText(getString(R.string.yes));
            else
                text.setText(getString(R.string.no));

            text = (TextView)getRootView().findViewById(R.id.metadata);
            text.setText(getString(R.string.metadata_no_html));

            text = (TextView)getRootView().findViewById(R.id.metadata_value);
            text.setText(theActivity.getVideoMetadata());
        }

        return getRootView();
    }
}
