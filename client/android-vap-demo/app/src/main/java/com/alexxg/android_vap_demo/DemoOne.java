package com.alexxg.android_vap_demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * Implementation for Lesson One: One Model, Hold the Schema
 */
public class DemoOne extends HtmlFragment {
	/**
	 * This custom subclass of Model is the closest thing to a "schema" the Note model has.
	 *
	 * When we save an instance of NoteModel, LoopBack uses the property getters and setters
	 * of the subclass to customize the request it makes to the server. The server handles
	 * this freeform request appropriately, saving our freeform model to the database just
	 * as we expect.
	 *
	 * Note: in a regular application, this class would be defined as top-level (non-static)
	 * class in a file of its own. We are keeping it as a static nested class only to make
	 * it easier to follow this guide.
	 */
private static boolean videoUploaded = false;
	private static boolean metadataUploaded = false;
	static final int REQUEST_VIDEO_CAPTURE = 1;
	static final int REQUEST_VIDEO_FILE = 888;
	static final int REQUEST_JSON_FILE = 777;

	public static class Video extends Model {

		private int videoID;
		private String metadata;
		private int deviceID;

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

	}


	public static class Device extends Model {

		private String uid;
		private String metadata;
		private int deviceID;

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getUid() {
			return uid;
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

	}

	/**
	 * Our custom ModelRepository subclass. See Lesson One for more information.
	 */
	public static class VideoRepository extends ModelRepository<Video> {
		public VideoRepository() {
			super("video", Video.class);
		}
			public RestContract createContract() {
				RestContract contract = super.createContract();

				contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/greet", "POST"),
						getClassName() + ".greet");

				return contract;
			}

			public void greet(String metadata, int purpose, final VoidCallback callback) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("purpose", purpose);
				params.put("metadata", metadata);
				invokeStaticMethod("greet", params, new Adapter.Callback() {

					@Override
					public void onError(Throwable t) {
						callback.onError(t);
					}

					@Override
					public void onSuccess(String response) {
						callback.onSuccess();
					}
				});
			}

	}
	/**
	 * Our custom ModelRepository subclass. See Lesson One for more information.
	 */
	public static class DeviceRepository extends ModelRepository<Device> {
		public DeviceRepository() {
			super("device", Device.class);
		}

		public RestContract createContract() {
			RestContract contract = super.createContract();

			contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/greet", "POST"),
					getClassName() + ".greet");

			return contract;
		}

		public void greet(String deviceIdentifier, String purpose, final VoidCallback callback) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("purpose", purpose);
			params.put("deviceIdentifier", deviceIdentifier);
			invokeStaticMethod("greet", params, new Adapter.Callback() {

				@Override
				public void onError(Throwable t) {
					callback.onError(t);
				}

				@Override
				public void onSuccess(String response) {
					callback.onSuccess();
				}
			});
		}
	}

	// The device would digitally sign each communication in the full protocol.
	// This digital signing function would be available to anyone including the user.
	// A protected key pair can be used in devices with technologies like TPM.
// Found these sample useful generation functions to use here http://stackoverflow.com/questions/30929103/digital-signature-in-java-android-rsa-keys
public static KeyPair createKeyPair() {
	KeyPair keyPair = null;

	try {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		keygen.initialize(2048);
		keyPair = keygen.generateKeyPair();
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		return null;
	}
	return keyPair;
}

	public static String getPrivateKeyBase64Str(KeyPair keyPair){
		if (keyPair == null) return null;
		return getBase64StrFromByte(keyPair.getPrivate().getEncoded());
	}

	public static String getPublicKeyBase64Str(KeyPair keyPair){
		if (keyPair == null) return null;
		return getBase64StrFromByte(keyPair.getPublic().getEncoded());
	}

	public static String getBase64StrFromByte(byte[] key){
		if (key == null || key.length == 0) return null;
		return new String(Base64.encode(key, Base64.DEFAULT));
	}

	/**
	 * Saves the desired Note model to the server with all values pulled from the UI.
	 */
	private void sendRequest(int choice) {
		String userMsg = "Request Sent.";
	   // If the video is selected
		switch(choice){
			case 1:
				showResult("Testing");
				break;
			case 2:
				showResult(getResources().getString(R.string.error_in_development));
				break;
			case 3:
				showResult(getResources().getString(R.string.error_in_development));
				break;
		}
		if(!videoUploaded) {
			userMsg = "Select a video first.";
		}
		// and the metadata is selected
		else if(!metadataUploaded) {
			userMsg = "Select a metadata file first.";
		}

	    showResult(userMsg);
		// Retrieve secret device ID and secret token
		//getSecretDeviceId();
		//getSecretDeviceToken();
		// Or retrieve hardware identifier if those are missing.
		//gethwID();
		// Check some value to see if it is registered.
        // deviceModelInstance.greet(REGISTER_VALUE)...
		// If it isn't, register device.
	//	deviceModelInstance.greet(CONFIRM_REGISTER_VALUE)...
		// Then follow same group of functions, if had been already registered.
		//
		// If it is already registered, send device request with metadata including video hash.
	//	videoModelInstance.greet(RECORD_VALUE)...

	//	videoModelInstance.greet(CONFIRM_RECORD_VALUE)...
		// Check token against secret value and replace with new one. Send response back if successful or not.
	       /*// 1. Grab the shared RestAdapter instance.
		GuideApplication app = (GuideApplication)getActivity().getApplication();
		RestAdapter adapter = app.getLoopBackAdapter();

	    // 2. Instantiate our NoteRepository. For the intrepid, notice that we could create this
	    //    once (say, in onCreateView) and use the same instance for every request.
		//    Additionally, the shared adapter is associated with the prototype, so we'd only
		//    have to do step 1 in onCreateView also. This more verbose version is presented
		//    as an example; making it more efficient is left as a rewarding exercise for the reader.
		NoteRepository repository = adapter.createRepository(NoteRepository.class);

	    // 3. From that prototype, create a new NoteModel. We pass in an empty dictionary to defer
		//    setting any values.
		NoteModel model = repository.createObject(ImmutableMap.of("user", "Pencil"));

	    // 4. Pull model values from the UI.
		model.setUser(getUser());
		model.setComment(getComment());
		model.setReviewed(isReviewed());

		// 5. Save!
		model.save(new Model.Callback() {

			@Override
			public void onSuccess() {
				showResult("Saved!");
			}

			@Override
			public void onError(Throwable t) {
				Log.e(getTag(), "Cannot save Note model.", t);
				showResult("Failed.");
			}
		});*/
	}
/* userPickVideo
* This is the action of the Select a Video button
* It allows the user to pick a video using the intent ACTION_GET_CONTENT.
* On a successful selection, The videoUploaded variable is set to true.
 */
	private void userPickVideo(){
		// showResult("Pick a Video");
		Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
		pickMedia.setType("video/*");
		startActivityForResult(pickMedia,REQUEST_JSON_FILE);
	}

/* userPickMetadata
* This is the action of the Select JSON Metadata button
* It allows the user to pick a metadata JSON file using the intent ACTION_GET_CONTENT.
* On a successful selection, The metadataUploaded variable is set to true.
*/
	private void userRecordVideo(	)
	{//showResult("Pick a Metadata File");
// This code basically says to Android "Hey, I want a video!"
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		// This if is asking if the activity and package is allowed to do it.
		if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
			}
		else{
			showResult(getResources().getString(R.string.error_package_error));
		}
		}

	/* userRecordVideo
* This is the action of the Record Video button
* It allows the user to record a video and automatically do metadata generation and a VAP request.
*/
	private void userPickMetadata(	)
	{//showResult("Pick a Metadata File");

		Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
		pickMedia.setType("application/octet-stream|application/json");
	//	pickMedia.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(pickMedia,REQUEST_VIDEO_FILE);
	}




	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_JSON_FILE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedVideoLocation = data.getData();
                  showResult(selectedVideoLocation.toString());
				// Do something with the data...
				videoUploaded = true;
			}
		}

		if (requestCode == REQUEST_VIDEO_FILE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedJSONLocation = data.getData();
				// showResult(selectedJSONLocation.toString());
				String JsonMetadata = readText(selectedJSONLocation);
				showResult(JsonMetadata);
				//
				// Do something with the data...
				metadataUploaded = true;
			}
		}

		if (requestCode == REQUEST_VIDEO_CAPTURE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri recordedVideoLocation = data.getData();
				showResult(recordedVideoLocation.toString());

				//
				// Do something with the data...

			}
		}
	}


// Reads text file from Android Uri that can convert to string
	// Adapted from http://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android
	private String readText(Uri filepath) {
		String ret = "";

		try {
			FileInputStream inputStream = new FileInputStream(new File(filepath.getPath()));

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e("login activity", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("login activity", "Can not read file: " + e.toString());
		}
		return ret;
	}

	void showResult(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}

	//
	// GUI glue
	//

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
        		R.layout.fragment_demo_one, container, false));

		setHtmlText(R.id.content, R.string.lessonOne_content);

		installRecordVideoButtonClickHandler();
		installUserPickVideoButtonClickHandler();
		installUserPickMetadataButtonClickHandler();
        return getRootView();
	}

	private void installRecordVideoButtonClickHandler() {
		final Button button = (Button) getRootView().findViewById(R.id.userRecordVideo);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				userRecordVideo();
			}
		});
	}

	private void installUserPickVideoButtonClickHandler() {
		final Button button = (Button) getRootView().findViewById(R.id.userPickVideo);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				userPickVideo();
			}
		});
	}

	private void installUserPickMetadataButtonClickHandler() {
		final Button button = (Button) getRootView().findViewById(R.id.userPickMetadata);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				userPickMetadata();
			}
		});
	}

	//
	// Properties for accessing form values
	//
/*
	private String getUser() {
		final EditText widget = (EditText) getRootView().findViewById(R.id.editUser);
		return widget.getText().toString();
	}

	private String getComment() {
		final EditText widget = (EditText) getRootView().findViewById(R.id.editCaliber);
		return widget.getText().toString();
	}

	private Boolean isReviewed() {
		final CheckBox widget = (CheckBox) getRootView().findViewById(R.id.editArmorPiercing);
		return widget.isChecked();
	}*/
}
