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
import android.widget.TextView;
import android.widget.Toast;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;

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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
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
	private static String SAMPLE_DIGITAL_SIGNATURE_PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKkbSUT9/Q2uBfGRau6/XJyZhcF5abo7\n" +
			"b37I5hr3EmwGykdzyk8GSyJK3TOrjyl0sdJsGbFmgQaRyV+DLE7750ECAwEAAQ==";
	private static String SAMPLE_VIDEO_FILE_HASH = "6c367a3596eb03bde47f88e29d5557d1c7c12f5b";
	private final static int NO_OPTIONS=0;
	private static final int REQUEST_VIDEO_CAPTURE = 3;
	private static final int REQUEST_VIDEO_FILE = 2;
	private static final int REQUEST_JSON_FILE = 1;
	private static final int PURPOSE_REGISTER = 0;
	private static final int PURPOSE_AUTHENTICATE = 0;


	public DemoOne() {

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

				contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/greet", "POST"),
						getClassName() + ".greet");

				return contract;
			}

		public void greet(String filehash,int deviceIdentifier, String token, String metadata, int purpose, final Adapter.JsonObjectCallback callback) {
			Map<String, Object> params = new HashMap<String, Object>();

			params.put("filehash", filehash); // Instead of uploading the video file, the server can receive a hash to check against.
			params.put("deviceID", deviceIdentifier);
			params.put("token", token);
			params.put("metadata", metadata);
			params.put("purpose", purpose);
			invokeStaticMethod("greet", params, new Adapter.JsonObjectCallback() {
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
	 * Our custom DeviceRepository subclass.
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

		public void greet(String deviceIdentifier, String digitalSignaturePublicKey, String metadata, int purpose, final Adapter.JsonObjectCallback callback) {
			Map<String, Object> params = new HashMap<String, Object>();

			params.put("deviceIdentifier", deviceIdentifier);
			params.put("digitalSignaturePublicKey", deviceIdentifier);
			params.put("metadata", metadata);
			params.put("purpose", purpose);
			invokeStaticMethod("greet", params, new Adapter.JsonObjectCallback() {
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
	private void sendRequest(final String jsonMetadata) {

		GuideApplication app = (GuideApplication)getActivity().getApplication();
		final RestAdapter adapter = app.getLoopBackAdapter();
showResult(jsonMetadata);
// Retrieve secret device ID and secret token
		// Or retrieve hardware identifier if those are missing.
		// Check some value to see if it is registered.
		// deviceModelInstance.greet(REGISTER_VALUE)...
		// If it isn't, register device.
if(getSecretDeviceId()==0 || getSecretDeviceToken().equals(UNKNOWN)) {
	// 2. Instantiate our DeviceRepository.
	final DeviceRepository repository = adapter.createRepository(DeviceRepository.class);

	setSemiSecretDigitalSignaturePublicKey(SAMPLE_DIGITAL_SIGNATURE_PUBLIC_KEY);
	repository.greet(gethwID(), SAMPLE_DIGITAL_SIGNATURE_PUBLIC_KEY, buildDeviceMetadata().toString(), PURPOSE_REGISTER,
			new Adapter.JsonObjectCallback() {
				@Override
				public void onError(Throwable t) {
					showResult(t.toString());
				}

				@Override
				public void onSuccess(JSONObject response) {
					showResult(getResources().getString(R.string.registered_device));
					if (response.has("deviceID") && response.has("publickey") && response.has("token")) {



						try {
							showResult(response.getString("publickey"));
							setSecretEncryptionPublicKey(response.getString("publickey"));

						} catch (JSONException e) {
							e.printStackTrace();
						}
						try {
							int newSecretDeviceID = response.getInt("deviceID");
							showResult(Integer.toString(newSecretDeviceID));
							setSecretDeviceId(newSecretDeviceID);


						} catch (JSONException e) {
							e.printStackTrace();
						}
						try {
							String theNewToken = response.getString("token");
							showResult(theNewToken);
							setSecretDeviceToken(theNewToken);
									} catch (JSONException e) {
							e.printStackTrace();
						}

						// This next part is the Device Registration confirmation step.
						try {
							repository.greet(gethwID(), response.getString("token"), buildDeviceMetadata().toString(), response.getInt("deviceID"),
                                    new Adapter.JsonObjectCallback() {
                                        @Override
                                        public void onError(Throwable t) {
                                        showResult(t.toString());
                                        }

                                        @Override
                                        public void onSuccess(JSONObject response) {
											try {
												String theNewToken = response.getString("token");
												showResult(theNewToken);
												setSecretDeviceToken(theNewToken);
												showResult(getResources().getString(R.string.device_registration_confirmed));
												sendVideoAuthenticationProtocolRequest(jsonMetadata, SAMPLE_VIDEO_FILE_HASH, adapter);
											} catch (JSONException e) {
												e.printStackTrace();
											}
                                        }
                                    });
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} // end if the response gave back
					else {
						showResult(getResources().getString(R.string.error_connectivity));
					}
				} // end OnSuccess for Device

			});
} // End if Secret Device ID and Token is unknown.
		else{
	sendVideoAuthenticationProtocolRequest(jsonMetadata, SAMPLE_VIDEO_FILE_HASH, adapter);
}


	//	deviceModelInstance.greet(CONFIRM_REGISTER_VALUE)...
		// Then follow same group of functions, if had been already registered.
		//
		// If it is already registered, send device request with metadata including video hash.
	//	videoModelInstance.greet(RECORD_VALUE)...

	//	videoModelInstance.greet(CONFIRM_RECORD_VALUE)...
		// Check token against secret value and replace with new one. Send response back if successful or not.
/*  sendVideoAuthenticationProtocolRequest
* This request to record a video as authenticated in the manufacturer's database.
*  A confirmation response and request completes the transaction.
 */
	}

	private void sendVideoAuthenticationProtocolRequest(String jsonMetadata,String videoHash, RestAdapter adapter) {
		final String metadataHash = computeSHAHash(jsonMetadata);


		//GuideApplication app = (GuideApplication)getActivity().getApplication();
		//RestAdapter adapter = app.getLoopBackAdapter();
		final VideoRepository repository = adapter.createRepository(VideoRepository.class);
		showResult(getResources().getString(R.string.requested_authentication));
		repository.greet(videoHash, getSecretDeviceId(), getSecretDeviceToken(), jsonMetadata, PURPOSE_AUTHENTICATE,
				new Adapter.JsonObjectCallback() {
					@Override
					public void onError(Throwable t) {

						showResult(getResources().getString(R.string.error_authentication_failed));
						showResult(t.getMessage());

						t.printStackTrace();
					}

					@Override
					public void onSuccess(JSONObject response) {
						showResult(getResources().getString(R.string.received_response_confirm_hash));
						if (response.has("filehash")&& response.has("metadatahash") && response.has("token") && response.has("oldtoken") && response.has("purpose") ) {
							String responseFilehash = "";
							String responseMetadatahash = "";
							String theNewToken = "";
							String oldToken = "";
							int videoID = 0;

							try {
								responseFilehash =  response.getString("filehash");
								showResult(getResources().getString(R.string.receive_file_hash));
								showResult(responseFilehash);


							} catch (JSONException e) {
								e.printStackTrace();
							}
							try {
								showResult(getResources().getString(R.string.receive_metadata_hash));
								responseMetadatahash = response.getString("metadatahash");
								showResult(responseMetadatahash);


							} catch (JSONException e) {
								e.printStackTrace();
							}
							try {
								showResult(getResources().getString(R.string.receive_videoID));
								videoID = response.getInt("purpose");
								showResult(Integer.toString(videoID));
							} catch (JSONException e) {
								e.printStackTrace();
							}
							try {
								showResult(getResources().getString(R.string.receive_token));
								 theNewToken = response.getString("token");
								showResult(theNewToken);

							} catch (JSONException e) {
								e.printStackTrace();
							}

							try {
								showResult(getResources().getString(R.string.receive_old_token));
								oldToken = response.getString("oldtoken");
								showResult(oldToken);

							} catch (JSONException e) {
								e.printStackTrace();
							}
							// Check to see if the metadata on the device matches the request sent to the cloud.
							// A hardware chip would have a protected metadata storage
							 	if(metadataHash.compareTo(responseMetadatahash) == 0) {
								// Compare the tokens and see if they match.
								if(getSecretDeviceToken().compareTo(oldToken) == 0) {
									setSecretDeviceToken(theNewToken);
showResult(getResources().getString(R.string.sending_video_confirm));
									// This next part is the Video Registration confirmation step.

										repository.greet(responseFilehash, getSecretDeviceId(), theNewToken, null, videoID,
												new Adapter.JsonObjectCallback() {
													@Override
													public void onError(Throwable t) {
														showResult(t.toString());
													}

													@Override
													public void onSuccess(JSONObject response) {
														try {
															String theNewToken = response.getString("token");
															showResult(theNewToken);
															setSecretDeviceToken(theNewToken);
															showResult(getResources().getString(R.string.video_authenticated_and_confirmed));

														} catch (JSONException e) {
															e.printStackTrace();
														}
													}
												});

								}
								else{
									showResult(getResources().getString(R.string.error_authentication_failed));
									showResult(getResources().getString(R.string.error_token_no_match));
								}
							}
							else{
								showResult(getResources().getString(R.string.error_authentication_failed));
								showResult(getResources().getString(R.string.error_video_metadata_no_match));
									showResult(metadataHash);
									showResult(responseMetadatahash);
							}
						} // end if the response gave back
						else {
							showResult(getResources().getString(R.string.error_connectivity));
						}
					} // end OnSuccess for Video

				});
	}

	/* buildDeviceMetadata
    * Creates a set of metadata by reading it from the Device.
    * @return A JSONObject
     */
	private JSONObject buildDeviceMetadata(){
		JSONObject deviceMetadata = new JSONObject();
		try{
		String _OSVERSION = System.getProperty("os.version");
		String _RELEASE = android.os.Build.VERSION.RELEASE;
		String _DEVICE = android.os.Build.DEVICE;
		String _MODEL = android.os.Build.MODEL;
		String _PRODUCT = android.os.Build.PRODUCT;
		String _BRAND = android.os.Build.BRAND;
		String _DISPLAY = android.os.Build.DISPLAY;
		String _CPU_ABI = android.os.Build.CPU_ABI;
		String _CPU_ABI2 = android.os.Build.CPU_ABI2;
		String _UNKNOWN = android.os.Build.UNKNOWN;
		String _HARDWARE = android.os.Build.HARDWARE;
		String _ID = android.os.Build.ID;
		String _MANUFACTURER = android.os.Build.MANUFACTURER;
		String _SERIAL = android.os.Build.SERIAL;
		String _USER = android.os.Build.USER;
		String _HOST = android.os.Build.HOST;


			deviceMetadata.put("OSVERSION",_OSVERSION);
			deviceMetadata.put("RELEASE",_RELEASE);
			deviceMetadata.put("DEVICE",_DEVICE);
			deviceMetadata.put("MODEL",_MODEL);
			deviceMetadata.put("PRODUCT",_PRODUCT);
			deviceMetadata.put("BRAND",_BRAND);
			deviceMetadata.put("DISPLAY",_DISPLAY);
			deviceMetadata.put("CPU_ABI",_CPU_ABI);
			deviceMetadata.put("CPU_ABI2",_CPU_ABI2);
			deviceMetadata.put("UNKNOWN",_UNKNOWN);
			deviceMetadata.put("HARDWARE",_HARDWARE);
			deviceMetadata.put("ID",_ID);
			deviceMetadata.put("MANUFACTURER",_MANUFACTURER);
			deviceMetadata.put("SERIAL",_SERIAL);
			deviceMetadata.put("USER",_USER);
			deviceMetadata.put("HOST",_HOST);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return deviceMetadata;
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
		startActivityForResult(pickMedia, REQUEST_VIDEO_FILE);
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
			startActivityForResult(takeVideoIntent,REQUEST_VIDEO_CAPTURE  );
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
try {

	Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
	pickMedia.setType("application/octet-stream|application/json|*/*"); // This only works on some
	//	pickMedia.setType("*/*");
	//	pickMedia.addCategory(Intent.CATEGORY_OPENABLE);
	startActivityForResult(pickMedia, REQUEST_JSON_FILE);
}
		catch (Exception e)
		{
			e.printStackTrace();

			showResult("Failed to load J3M file. Will try with sample JSON");
			sendRequest("{JSON METADATA SAMPLE: 1}");
}
	}



/* onActivityResult
* This says what to do when another Activity returns a result
*/
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_VIDEO_FILE) {
			if (resultCode == Activity.RESULT_OK) {
				// Retrieve Video file URI
				Uri selectedVideoLocation = data.getData();

				sendRequest(generateMetadata(selectedVideoLocation).toString());
				// Do something with the data...
				videoUploaded = true;
			}
		}

		if (requestCode == REQUEST_JSON_FILE) {
			if (resultCode == Activity.RESULT_OK) {
				// Retrieve JSON URI
				Uri selectedJSONLocation = data.getData();
				String JsonMetadata = readText(selectedJSONLocation);
				// Do something with the data...
				metadataUploaded = true;
				sendRequest(JsonMetadata);
			}
		}

		if (requestCode == REQUEST_VIDEO_CAPTURE) {
			if (resultCode == Activity.RESULT_OK) {
				// Retrieve Video content URI
				Uri recordedVideoLocation = data.getData();
showResult(recordedVideoLocation.toString());
				sendRequest(generateMetadata(recordedVideoLocation).toString());

			}

		}
		}
/* generateMetadata
* Receives a file: or content: URI of a video and generates video metadata for it.
 * @return A JSONObject
 */
	private JSONObject generateMetadata(Uri recordedVideoLocation) {
		JSONObject videoMetadata = new JSONObject();

		try {
			String theFileHash = getVideoFileHash(recordedVideoLocation);
			String digitalSignature = Base64.encodeToString(getDigitalSignature(theFileHash, VAPprivateKey), Base64.DEFAULT);

			videoMetadata.put("fileHash", theFileHash);
			videoMetadata.put("digitalSignature", digitalSignature);
			videoMetadata.put("filePath", recordedVideoLocation.toString());
			showResult(theFileHash);
				showResult(digitalSignature);
			showResult(recordedVideoLocation.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return videoMetadata;
	}


	/* getVideoFileHash
    * returns empty string or the filehash generated
     */
	private String getVideoFileHash(Uri video) {
	String filehash = "";
//File videoFile = new File(video.getPath());
		FileInputStream inputStream = null;
		if(video.getScheme().equalsIgnoreCase("file")) {
			try {
				inputStream = new FileInputStream(new File(video.getPath()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else if (video.getScheme().equalsIgnoreCase("content")){
			try {
				inputStream = (FileInputStream) getActivity().getApplicationContext().getContentResolver().openInputStream(video);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		MessageDigest digester = null;
		try {
			digester = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] bytes = new byte[8192];
		//inputStream.read
		int byteCount;
		try {
			if (inputStream != null) {
				while ((byteCount = inputStream.read(bytes)) > 0) {
                    digester.update(bytes, 0, byteCount);
                }
			}
			else{
				showResult("Failed filehash generation.");
				return "test";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] digest = digester.digest();
filehash = Base64.encodeToString(digest, Base64.DEFAULT);
		return filehash;
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


// From http://karanbalkar.com/2013/05/tutorial-28-implement-sha1-and-md5-hashing-in-android/
	private static String convertToHex(byte[] data) throws java.io.IOException
	{

		StringBuilder buf = new StringBuilder();
		for (byte b : data) {
			int halfbyte = (b >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
				halfbyte = b & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
/*
		StringBuffer sb = new StringBuffer();
		String hex=null;

		hex=Base64.encodeToString(data, 0, data.length, NO_OPTIONS);

		sb.append(hex);

		return sb.toString();*/
	}

// From http://karanbalkar.com/2013/05/tutorial-28-implement-sha1-and-md5-hashing-in-android/
	public String computeSHAHash(String text)
	{
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			md.update(text.getBytes("utf-8"), 0, text.length());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] sha1hash = md.digest();
		try {
			return convertToHex(sha1hash);
		} catch (IOException e) {
			e.printStackTrace();
			return "Android device error: Failed to convert hash to hex.";
		}
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
