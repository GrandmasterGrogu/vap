package com.alexxg.android_vap_demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.List;

/**
 * Implementation for Lesson Two: Existing Data? No Problem.
 */
public class DemoTwo extends HtmlFragment {

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
    private static final String  VID_CONFIRMED = "VIDEO_CONFIRMED";
    private static final String VID = "VIDEO_ID";
    private static final String VID_DEVICE = "VIDEO_DEVICE_ID";
    private static final String VID_MDATA = "VIDEO_METADATA";

    private boolean videoSelected = false;
    private boolean deviceSelected = false;


    public static class Video extends Model {

        private int videoID;
        private String metadata;
        private int deviceID;
        private int confirm;

        public void setVideoID(int videoID) {
            this.videoID = videoID;
        }

        public int getVideoID() {
            return videoID;
        }

        public void setMetadata(String metadata) {
            this.metadata = metadata;
        }

        public String getMetadata() {
            return metadata;
        }

        public void setDeviceID(int deviceID) {
            this.deviceID = deviceID;
        }

        public int getDeviceID() {
            return deviceID;
        }
        public int getConfirm() {
            return confirm;
        }
        public void setConfirm(int confirm) {
            this.confirm = confirm;
        }

/*
        public Boolean getConfirm() {
            if(confirm == 0)
                return false;
            else
                return true;
        }
*/
    }


    public static class Device extends Model {

        private String uid;
        private String token;
        private String oldtoken;
        private String publickey;
        private String metadata;
        private int deviceID;
        private int confirm;

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setOldtoken(String oldtoken) {
            this.oldtoken = oldtoken;
        }

        public String getOldtoken() {
            return oldtoken;
        }

        public void setPublickey(String publickey) {
            this.publickey = publickey;
        }

        public String getPublickey() {
            return publickey;
        }

        public void setMetadata(String metadata) {
            this.metadata = metadata;
        }

        public String getMetadata() {
            return metadata;
        }

        public void setDeviceID(int deviceID) {
            this.deviceID = deviceID;
        }

        public int getDeviceID() {
            return deviceID;
        }

        public int getConfirm() {
            return confirm;
        }
        public void setConfirm(int confirm) {
            this.confirm = confirm;
        }
/*
        public Boolean getConfirm() {
            if(confirm == "0")
                return false;
                else
                return true;
        }*/
    }

    /**
     * Our custom ModelRepository subclass. See Lesson One for more information.
     */
    public static class VideoRepository extends ModelRepository<Video> {
        public VideoRepository() {
            super("video", Video.class);
        }
    }
    /**
     * Our custom ModelRepository subclass. See Lesson One for more information.
     */
    public static class DeviceRepository extends ModelRepository<Device> {
        public DeviceRepository() {
            super("device", Device.class);
        }
    }


    /**
     * Loads all Car models from the server. To make full use of this, return to your (running)
     * Sample Application and restart it with the DB environment variable set to "oracle".
     * For example, on most *nix flavors (including Mac OS X), that looks like:
     *
     * 1. Stop the current server with Ctrl-C.
     * 2. DB=oracle slc run app
     *
     * What does this do, you ask? Without that environment variable, the Sample Application uses
     * simple, in-memory storage for all models. With the environment variable, it uses a custom-made
     * Oracle adapter with a demo Oracle database we host for this purpose. If you have existing
     * data, it's that easy to pull into LoopBack. No need to leave it behind.
     *
     * Advanced users: LoopBack supports multiple data sources simultaneously, albeit on a per-model
     * basis. In your next project, try connecting a schema-less model (e.g. our Note example)
     * to a Mongo data source, while connecting a legacy model (e.g. this Car example) to
     * an Oracle data source.
     */
    private void viewVideos() {
        // 1. Grab the shared RestAdapter instance.
        GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();

        // 2. Instantiate our VideoRepository.
        VideoRepository repository = adapter.createRepository(VideoRepository.class);

        // 3. Rather than instantiate a model directly like we did in Lesson One, we'll query
        //    the server for all Cars, filling out our ListView with the results. In this case,
        //    the Repository is really the workhorse; the Model is just a simple container.

        repository.findAll(new ModelRepository.FindAllCallback<DemoTwo.Video>() {
            @Override
            public void onSuccess(List<Video> models) {
                videoSelected = true;
                deviceSelected = false;
                list.setAdapter(new VideoListAdapter(getActivity(), models));
            }

            @Override
            public void onError(Throwable t) {
               // Log.e(getTag(), "Cannot save Note model.", t);
                showResult("Failed to load.");
                showResult(t.toString());
            }
        });
    }

    private void viewDevices() {
        // 1. Grab the shared RestAdapter instance.
        GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();

        // 2. Instantiate our DeviceRepository.
        DeviceRepository repository = adapter.createRepository(DeviceRepository.class);

        // 3. Rather than instantiate a model directly like we did in Lesson One, we'll query
        //    the server for all Cars, filling out our ListView with the results. In this case,
        //    the Repository is really the workhorse; the Model is just a simple container.

        repository.findAll(new ModelRepository.FindAllCallback<DemoTwo.Device>() {
            @Override
            public void onSuccess(List<Device> models) {
                videoSelected = false;
                deviceSelected = true;
                list.setAdapter(new DeviceListAdapter(getActivity(), models));
            }

            @Override
            public void onError(Throwable t) {
                // Log.e(getTag(), "Cannot save Note model.", t);
                showResult("Failed to load.");
                showResult(t.toString());
            }
        });
    }


    protected void showResult(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Basic ListAdapter implementation using our custom Model type.
     */
    private static class VideoListAdapter extends ArrayAdapter<Video> {
        public VideoListAdapter(Context context, List<Video> list) {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, null);
            }

            Video model = getItem(position);
            if (model == null) return convertView;

            TextView textView = (TextView)convertView.findViewById(
                    android.R.id.text1);
            Object [] arguments = {String.valueOf(model.getVideoID()),String.valueOf(model.getDeviceID())};
            textView.setText(MessageFormat.format("Video {0} from Device {1}", arguments));

            return convertView;
        }
    }

    /**
     * Basic ListAdapter implementation using our custom Model type.
     */
    private static class DeviceListAdapter extends ArrayAdapter<Device> {
        public DeviceListAdapter(Context context, List<Device> list) {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, null);
            }

            Device model = getItem(position);
            if (model == null) return convertView;

            TextView textView = (TextView)convertView.findViewById(
                    android.R.id.text1);
            Object [] arguments = {String.valueOf(model.getDeviceID())};
            textView.setText(MessageFormat.format("Device {0}", arguments));

            return convertView;
        }
    }


    //
    // GUI glue
    //
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
                R.layout.fragment_demo_two, container, false));

        list = (ListView)getRootView().findViewById(R.id.list);


        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
        if(deviceSelected){
                        Device model = (Device)list.getItemAtPosition(position);
                        Boolean confirmNum = false;
                        if(model.getConfirm()==1)
                            confirmNum = true;
                        Intent intent = new Intent(getActivity(),ItemDetails.class);
                        intent.putExtra(DEVICE_OR_VIDEO,1);
                        intent.putExtra(SDID,model.getDeviceID());
                        intent.putExtra(DID,model.getUid());
                        intent.putExtra(TOKEN,model.getToken());
                        intent.putExtra(OLD_TOKEN,model.getOldtoken());
                        intent.putExtra(MDATA,model.getMetadata());
                        intent.putExtra(PK,model.getPublickey());
                        intent.putExtra(CONFIRMED,confirmNum);

                        startActivity(intent);
        }
                else if(videoSelected){
            // Digital Signature Test Code
            /*byte[] testSig = getDigitalSignature("TEST", VAPprivateKey);
            boolean verified = verfiySignature(testSig, "TEST",VAPpublicKey);
            boolean verified = verfiySignature(     Base64.decode(Base64.encodeToString(testSig, Base64.DEFAULT), Base64.DEFAULT), "TEST",VAPpublicKey);

            showResult(testSig.toString());
            showResult(Base64.encodeToString(testSig, Base64.DEFAULT));
            showResult(Base64.encodeToString(VAPpublicKey.getEncoded(), Base64.DEFAULT));
            showResult(String.valueOf(verified));*/

                    Video model = (Video)list.getItemAtPosition(position);
            processMetadataSignature(model.getMetadata());
                    Boolean confirmNum = false;
                                if(model.getConfirm()==1)
                                    confirmNum = true;
                    Intent intent = new Intent(getActivity(),ItemDetails.class);
                    intent.putExtra(DEVICE_OR_VIDEO,2);
                    intent.putExtra(VID,model.getVideoID());
                    intent.putExtra(VID_DEVICE,model.getDeviceID());
                    intent.putExtra(VID_MDATA,model.getMetadata());
                    intent.putExtra(VID_CONFIRMED,confirmNum);

            startActivity(intent);
                }

                // Tried writing code to start as fragment. did not work...
           /*    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
               // fragmentTransaction.add(R.id.verticallayout, item_display.newInstance(Integer.toString(model.getDeviceID()), model.getUid(), model.getToken(), model.getOldToken(), model.getMetadata(), model.getPublicKey()));
                fragmentTransaction.replace(R.id.topframe, item_display.newInstance(Integer.toString(model.getDeviceID()), model.getUid(), model.getToken(), model.getOldToken(), model.getMetadata(), model.getPublicKey()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
    /* write you handling code like...
    String st = "sdcard/";
    File f = new File(st+o.toString());
    // do whatever u want to do with 'f' File object
    */
            }
        });
        setHtmlText(R.id.content, R.string.lessonTwo_content);

        installButtonClickHandlerViewVideos();
        installButtonClickHandlerViewDevices();
        return getRootView();
    }

    private void processMetadataSignature(String metadata) {
        // Digital Signature Test Code
            /*byte[] testSig = getDigitalSignature("TEST", VAPprivateKey);
            boolean verified = verfiySignature(testSig, "TEST",VAPpublicKey);
            boolean verified = verfiySignature(     Base64.decode(Base64.encodeToString(testSig, Base64.DEFAULT), Base64.DEFAULT), "TEST",VAPpublicKey);

            showResult(testSig.toString());
            showResult(Base64.encodeToString(testSig, Base64.DEFAULT));
            showResult(Base64.encodeToString(VAPpublicKey.getEncoded(), Base64.DEFAULT));
            showResult(String.valueOf(verified));*/
        boolean verified = false;
        Boolean hashAvailable = false;
        Boolean signatureAvailable = false;
        String videoHash = "";
        String videoDigitalSignature = "";
        JSONObject mdata = null;
        try {
            mdata = new JSONObject(metadata);
            showResult(mdata.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            showResult("The metadata did not successfully convert to a JSONObject.");
        }

        try {
            videoHash = mdata.getString("fileHash");
            hashAvailable = true;
        } catch (JSONException e) {
            e.printStackTrace();
            hashAvailable = false;
            showResult("The metadata did not successfully extract, with regards to the file hash.");
        }
        catch (Exception e){
            e.printStackTrace();
            hashAvailable = false;
            showResult("The metadata did not successfully extract, with regards to the file hash.");
        }

        try {
            videoDigitalSignature = mdata.getString("digitalSignature");
            signatureAvailable = true ;
        } catch (JSONException e) {
            e.printStackTrace();
            signatureAvailable = false;
            showResult("The metadata did not successfully extract, with regards to the digital signature.");
        }
        catch (Exception e){
            e.printStackTrace();
            signatureAvailable = false;
            showResult("The metadata did not successfully extract, with regards to the digital signature.");
        }
try {
    if (hashAvailable && signatureAvailable) {
        verified = verfiySignature(Base64.decode(videoDigitalSignature, Base64.DEFAULT), videoHash, VAPpublicKey);
    }
}
catch (Exception e){
    e.printStackTrace();
    showResult("The verification failed due to a highly false or malformed digital signature or file hash received.");
}
        showResult("Was the digital signature able to verify?");
        showResult(String.valueOf(verified));
    }

    private void installButtonClickHandlerViewVideos() {
        final Button button = (Button) getRootView().findViewById(R.id.viewVideos);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewVideos();
            }
        });
    }

    private void installButtonClickHandlerViewDevices() {
        final Button button = (Button) getRootView().findViewById(R.id.viewDevices);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewDevices();
            }
        });
    }
}
