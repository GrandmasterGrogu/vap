package com.alexxg.android_vap_demo;


import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;



public class ItemDetails extends FragmentActivity {
    // Device String identifiers
    private static final String SDID = "SECRET_DEVICE_ID";
    private static final String DID = "DEVICE_ID";
    private static final String TOKEN = "TOKEN";
    private static final String OLD_TOKEN = "OLD_TOKEN";
    private static final String MDATA = "METADATA";
    private static final String PK = "PUBLIC_KEY";
    private static final String  CONFIRMED = "CONFIRMED";
    private static final String  DEVICE_OR_VIDEO = "DEVICE_OR_VIDEO";
    // Video String identifiers
    private static final String VID_CONFIRMED = "VIDEO_CONFIRMED";
    private static final String VID = "VIDEO_ID";
    private static final String VID_MDATA = "VIDEO_METADATA";
    private static final String VID_DEVICE = "VIDEO_DEVICE_ID";
    private static final String VID_PUBLIC_KEY = "VIDEO_PUBLIC_KEY";

    private int deviceOrVideo;
    private int secretDeviceID;
    private String deviceID;
    private String tokenValue;
    private String oldTokenValue;
    private String metadata;
    private String publicKey;
    private Boolean confirmedDevice;

    private int videoID;
    private int videoDeviceID;
    private String videoMetadata;
    private String videoPublicKey;
    private Boolean confirmedVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
Boolean extra = true;
Bundle getThem = savedInstanceState;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            getThem = extras;
            if(extras == null) {
                deviceOrVideo = 0;
            } else {
                deviceOrVideo= extras.getInt(DEVICE_OR_VIDEO);

            }
        } else {
            deviceOrVideo= savedInstanceState.getInt(DEVICE_OR_VIDEO);
            extra = false;
        }

        if(deviceOrVideo == 1){
           initializeDevice(getThem, extra);
        }
        else if(deviceOrVideo == 2){
            initializeVideo(getThem, extra);
        }
        if (savedInstanceState == null) {
            Fragment newFragment = new ItemDetailsFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.item_fragment, newFragment).commit();
        }
    }

    private void initializeVideo(Bundle getThem, Boolean extra) {
        if(getThem != null){
            if(extra){
                videoID = getThem.getInt(VID);
                videoDeviceID = getThem.getInt(VID_DEVICE);
                videoMetadata = getThem.getString(VID_MDATA);
                videoPublicKey = getThem.getString(VID_PUBLIC_KEY);
                confirmedVideo = getThem.getBoolean(VID_CONFIRMED);
            }else{
                videoID = getThem.getInt(VID);
                videoDeviceID = getThem.getInt(VID_DEVICE);
                videoMetadata = (String) getThem.getSerializable(VID_MDATA);
                videoPublicKey = (String) getThem.getSerializable(VID_PUBLIC_KEY);
                confirmedVideo = (Boolean) getThem.getBoolean(VID_CONFIRMED);
            }
        }
    }

    private void initializeDevice(Bundle getThem, Boolean extra){
        if(getThem != null){
            if(extra){
                secretDeviceID = getThem.getInt(SDID);
                deviceID = getThem.getString(DID);
                tokenValue = getThem.getString(TOKEN);
                oldTokenValue = getThem.getString(OLD_TOKEN);
                metadata = getThem.getString(MDATA);
                publicKey = getThem.getString(PK);
                confirmedDevice = getThem.getBoolean(CONFIRMED);
            }else{
                secretDeviceID = getThem.getInt(SDID);
                deviceID = (String) getThem.getSerializable(DID);
                tokenValue = (String) getThem.getSerializable(TOKEN);
                oldTokenValue = (String) getThem.getSerializable(OLD_TOKEN);
                metadata = (String) getThem.getSerializable(MDATA);
                publicKey = (String) getThem.getSerializable(PK);
                confirmedDevice = getThem.getBoolean(CONFIRMED);
            }
        }
    }

    public int getDeviceOrVideo(){return deviceOrVideo;}

    public int getSecretDeviceID(){return secretDeviceID;}
    public String getDeviceID(){return deviceID;}
    public String getToken(){return tokenValue;}
    public String getOldToken(){return oldTokenValue;}
    public String getDeviceMetadata(){return metadata;}
    public String getPublicKey(){return publicKey;}
    public Boolean getConfirmedDevice(){return confirmedDevice;}

    public int getVideoID(){return videoID;}
    public int getVideoDeviceID(){return videoDeviceID;}
    public String getVideoMetadata(){return videoMetadata;}
    public String getVideoPublicKey(){return videoPublicKey;}
    public Boolean getConfirmedVideo(){return confirmedVideo;}

}
