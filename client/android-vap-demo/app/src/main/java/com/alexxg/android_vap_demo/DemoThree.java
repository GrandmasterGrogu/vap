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

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * Implementation for Lesson One: One Model, Hold the Schema
 */
public class DemoThree extends HtmlFragment {
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
	private static String SAMPLE_DIGITAL_SIGNATURE_PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKkbSUT9/Q2uBfGRau6/XJyZhcF5abo7\n" +
			"b37I5hr3EmwGykdzyk8GSyJK3TOrjyl0sdJsGbFmgQaRyV+DLE7750ECAwEAAQ==";
	private static String SAMPLE_VIDEO_FILE_HASH = "6c367a3596eb03bde47f88e29d5557d1c7c12f5b";
	private final static int NO_OPTIONS=0;
	private static final int REQUEST_VIDEO_CAPTURE = 3;
	private static final int REQUEST_VAP_FILE = 7;
	private static final int REQUEST_JSON_FILE = 1;
	private static final int PURPOSE_REGISTER = 0;
	private static final int PURPOSE_AUTHENTICATE = 0;


	public DemoThree() {

	}

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

		public Boolean getConfirm() {
			if(confirm == 0)
				return false;
			else
				return true;
		}

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

		public void setOldToken(String oldtoken) {
			this.oldtoken = oldtoken;
		}

		public String getOldToken() {
			return oldtoken;
		}

		public void setPublicKey(String publickey) {
			this.publickey = publickey;
		}

		public String getPublicKey() {
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

		public Boolean getConfirm() {
			if(confirm == 0)
				return false;
			else
				return true;
		}
	}

	/**
	 * Our custom VideoRepository subclass..
	 */
	public static class VideoRepository extends ModelRepository<Video> {
		public VideoRepository() {
			super("video", Video.class);
		}
			public RestContract createContract() {
				RestContract contract = super.createContract();

				contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/verify", "POST"),
						getClassName() + ".verify");

				return contract;
			}

		public void verify(String device, String video,
						   String metadata, final Adapter.JsonObjectCallback callback) {
			Map<String, Object> params = new HashMap<String, Object>();

			params.put("device", device); // Instead of uploading the video file, the server can receive a hash to check against.
			params.put("video", video);
			params.put("metadata", metadata);

			invokeStaticMethod("verify", params, new Adapter.JsonObjectCallback() {
				@Override
				public void onError(Throwable t) {
					callback.onError(t);
				}

				@Override
				public void onSuccess(JSONObject response) {
					callback.onSuccess(response);
				}
			});
		}


	}


	/**
	 * Sends a device registration request before the VAP request, if necessary.
	 */
	private void sendRequest(final String fileHash, final String publicKey, final String signature) {

		GuideApplication app = (GuideApplication) getActivity().getApplication();
		final RestAdapter adapter = app.getLoopBackAdapter();
		showResult("Sending Device Identifier: Public Key");
		showResult(publicKey);
		showResult("Sending Video Identifier: File Hash");
		showResult(fileHash);
		showResult("Sending Proof: Digital Signature");
		showResult(signature);

// Send the three necessary identifiers to find out

		// 2. Instantiate our DeviceRepository.
		final VideoRepository repository = adapter.createRepository(VideoRepository.class);


		repository.verify(publicKey, fileHash, signature,
				new Adapter.JsonObjectCallback() {
					@Override
					public void onError(Throwable t) {
						showResult(t.toString());
					}

					@Override
					public void onSuccess(JSONObject response) {
						if (response.has("valid") && response.has("metadata") && response.has("error")) {

							try {
								showResult("Is this a VAP authenticated video?");
								showResult(String.valueOf(response.getBoolean("valid")));
							} catch (JSONException e) {
								e.printStackTrace();
							}
							try {
								showResult("Metdata that was returned: ");
								showResult(response.getString("metadata"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
							try {
								JSONObject error = response.getJSONObject("error");
								if (error.getString("msg") != null) {
									showResult(error.getString("msg"));
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} // end if the response gave back
						else {
							showResult(getResources().getString(R.string.error_connectivity));
						}
					} // end OnSuccess for Device

				});

	}


/* userPickVideo
* This is the action of the Select a Video button
* It allows the user to pick a video using the intent ACTION_GET_CONTENT.
* On a successful selection, The videoUploaded variable is set to true.
 */
	private void userPickVAP(){
		// showResult("Pick a Video");
		Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
		pickMedia.setType("text/*");
		startActivityForResult(pickMedia, REQUEST_VAP_FILE);
	}



/* onActivityResult
* This says what to do when another Activity returns a result
*/
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


		if (requestCode == REQUEST_VAP_FILE) {
			if (resultCode == Activity.RESULT_OK) {
				// Retrieve JSON URI
				Uri selectedJSONLocation = data.getData();
				// Read data from file as string
				String JsonMetadata = readText(selectedJSONLocation);
				// Convert the string to JSON
				JSONObject vapMetadata = null;
				String fileHash = "";
				String publicKey = "";
				String signature = "";
				try {
					vapMetadata = new JSONObject(JsonMetadata);
					fileHash = vapMetadata.getString("filehash");
					publicKey = vapMetadata.getString("publickey");
					signature = vapMetadata.getString("digitalSignature");
				} catch (JSONException e) {
					e.printStackTrace();
				vapMetadata = null;
				}
// Extract the necessary three String variables
				if(vapMetadata!=null || !fileHash.isEmpty() || !publicKey.isEmpty() || !signature.isEmpty())
				sendRequest(fileHash, publicKey, signature);
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
                  bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();

				ret = stringBuilder.toString();

			}
		} catch (FileNotFoundException e) {
			Log.e("reading text", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("reading text", "Can not read file: " + e.toString());
		}
		return ret;
	}



	//
	// GUI glue
	//

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
				R.layout.fragment_demo_three, container, false));

		setHtmlText(R.id.content, R.string.demoThree_content);


		installUserPickVAPButtonClickHandler();

        return getRootView();
	}



	private void installUserPickVAPButtonClickHandler() {
		final Button button = (Button) getRootView().findViewById(R.id.userPickVap);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				userPickVAP();
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
