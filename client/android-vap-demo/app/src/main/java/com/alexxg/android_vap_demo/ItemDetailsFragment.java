package com.alexxg.android_vap_demo;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class ItemDetailsFragment extends HtmlFragment {

    private static final String LOG_TAG = "LOG";

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
            installExportJSONButtonClickHandler(theActivity);
        }

        return getRootView();
    }



/* installExportJSONButtonClickHandler
A function to install the button
*/
    private void installExportJSONButtonClickHandler(final ItemDetails theActivity) {
        final Button button = (Button) getRootView().findViewById(R.id.exportJSONbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showResult("This far");
               // exportJSON(theActivity);
            }
        });
    }

/* exportJSON
A function to trigger export of JSON
*/
    private void exportJSON(ItemDetails theActivity) {
        String filename = "Video"+theActivity.getVideoID()+"-"+theActivity.getVideoDeviceID()+ "-VAP.json";
        String string = buildJSON(theActivity);
        if(isExternalStorageWritable()){
            File path = getDownloadsDir();
            File file = new File(path, filename);
             boolean jsonFail = false;
            try {
                FileOutputStream stream = new FileOutputStream(file, true);
                stream.write(string.getBytes());
                stream.close();
                Log.i("saveData", "Data Saved");
            } catch (IOException e) {
                Log.e("SAVE DATA", "Could not write file " + e.getMessage());
                showResult("Failed to write data to JSON");
                jsonFail =true;
            }
            if(!jsonFail) {
                Intent intent =
                        new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
                theActivity.sendBroadcast(intent);

                showResult("The JSON file has been exported to the Documents directory.");
            }
        }
            else{
            FileOutputStream outputStream;

            try {
                outputStream = getActivity().getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(string.getBytes());
                outputStream.close();
                showResult("The JSON file has been exported to the main directory.");
            } catch (Exception e) {
                e.printStackTrace();
                showResult("Error writing file. Export failed.");
            }
        }
    }

    private String buildJSON(ItemDetails theActivity) {
        JSONObject theJSON = new JSONObject();
        JSONObject metadata = null;
        try {
            metadata = new JSONObject(theActivity.getVideoMetadata());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String digitalSig = "None";
        String fileHash = "None";
        String creationDate = "None";
        if(metadata != null){
            try {

            creationDate = metadata.getString("creationDate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
            try {

                fileHash = metadata.getString("fileHash");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                digitalSig = metadata.getString("digitalSignature");
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
        try {
        theJSON.put("digitalSignature",digitalSig);
        theJSON.put("deviceID",theActivity.getVideoDeviceID());
        theJSON.put("fileHash",fileHash);
        theJSON.put("creationDate", creationDate);
            theJSON.put("cloudAddress", "vap.alexxg.com");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return theJSON.toString();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getDownloadsDir() {
        // Get the directory for the user's public pictures directory.
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        if (!path.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return path;
    }

}
