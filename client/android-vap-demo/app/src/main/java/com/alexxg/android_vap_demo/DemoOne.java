package com.alexxg.android_vap_demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

	public static class NoteModel extends Model {
		private String user;
		private String comment;
		private Boolean reviewed;

		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		public Boolean getReviewed() {
			return reviewed;
		}
		public void setReviewed(Boolean reviewed) {
			this.reviewed = reviewed;
		}

	}



	/**
	 * The ModelRepository provides an interface to the Model's "type" on the server. For instance,
	 * we'll (SPOILER!) see in Lessons Two how the ModelRepository is used for queries;
	 * in Lesson Three we'll use it for custom, collection-level behaviour: those locations within
	 * the collection closest to the given coordinates.
	 *
	 * This subclass, however, provides an additional benefit: it acts as glue within the LoopBack
	 * interface between a RestAdapter representing the _server_ and a named collection or
	 * type of model within it. In this case, that type of model is named "note", and it contains
	 * NoteModel instances.
	 *
	 * Note: in a regular application, this class would be defined as top-level (non-static)
	 * class in a file of its own. We are keeping it as a static nested class only to make
	 * it easier to follow this guide.
	 */
	public static class NoteRepository extends ModelRepository<NoteModel> {
		public NoteRepository() {
			super("note", "notes", NoteModel.class);
		}
	}

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



	/**
	 * Saves the desired Note model to the server with all values pulled from the UI.
	 */
	private void sendRequest() {
		String userMsg = "Request Sent.";
	   // If the video is selected
		if(!videoUploaded) {
			userMsg = "Select a video first.";
		}
		// and the metadata is selected
		else if(!metadataUploaded) {
			userMsg = "Select a metadata file first.";
		}

	    showResult(userMsg);
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
		startActivityForResult(pickMedia,777);
	}

/* userPickMetadata
* This is the action of the Select JSON Metadata button
* It allows the user to pick a metadata JSON file using the intent ACTION_GET_CONTENT.
* On a successful selection, The videoUploaded variable is set to true.
*/
	private void userPickMetadata(	)
	{//showResult("Pick a Metadata File");

		Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
		pickMedia.setType("application/octet-stream");
		startActivityForResult(pickMedia,888);
		}



	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 777) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedVideoLocation = data.getData();
                  showResult(selectedVideoLocation.toString());
				// Do something with the data...
				videoUploaded = true;
			}
		}

		if (requestCode == 888) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedJSONLocation = data.getData();
				showResult(selectedJSONLocation.toString());
				// Do something with the data...
				metadataUploaded = true;
			}
		}
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

		installSendRequestButtonClickHandler();
		installUserPickVideoButtonClickHandler();
		installUserPickMetadataButtonClickHandler();
        return getRootView();
	}

	private void installSendRequestButtonClickHandler() {
		final Button button = (Button) getRootView().findViewById(R.id.viewVideos);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendRequest();
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
