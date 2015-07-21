package com.alexxg.android_vap_demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.List;

/**
 * Implementation for Lesson Two: Existing Data? No Problem.
 */
public class DemoTwo extends HtmlFragment {

    /**
     * Unlike Lesson One, our CarModel class is based _entirely_ on an existing schema.
     *
     * In this case, every field in Oracle that's defined as a NUMBER type becomes a Number,
     * and each field defined as a VARCHAR2 becomes a String.
     *
     * When we load these models from Oracle, LoopBack uses these property setters and getters
     * to know what data we care about. If we left off `extras`, for example, LoopBack would
     * simply omit that field.
     */
    public static class CarModel extends Model {
    	private String vin;
    	private int year;
    	private String make;
    	private String model;
    	private String image;
    	private String carClass;
    	private String color;

    	public String getVin() {
			return vin;
		}
		public void setVin(String vin) {
			this.vin = vin;
		}
		public int getYear() {
			return year;
		}
		public void setYear(int year) {
			this.year = year;
		}
		public String getMake() {
			return make;
		}
		public void setMake(String make) {
			this.make = make;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public String getCarClass() {
			return carClass;
		}
		public void setCarClass(String carClass) {
			this.carClass = carClass;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
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
     * Our custom ModelRepository subclass. See Lesson One for more information.
     */
    public static class CarRepository extends ModelRepository<CarModel> {
        public CarRepository() {
            super("car", CarModel.class);
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
                list.setAdapter(new VideoListAdapter(getActivity(), models));
            }

            @Override
            public void onError(Throwable t) {
               // Log.e(getTag(), "Cannot save Note model.", t);
                showResult("Failed.");
                showResult(t.toString());
            }
        });
    }

    private void viewDevices() {
        // 1. Grab the shared RestAdapter instance.
        GuideApplication app = (GuideApplication)getActivity().getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();

        // 2. Instantiate our VideoRepository.
        DeviceRepository repository = adapter.createRepository(DeviceRepository.class);

        // 3. Rather than instantiate a model directly like we did in Lesson One, we'll query
        //    the server for all Cars, filling out our ListView with the results. In this case,
        //    the Repository is really the workhorse; the Model is just a simple container.

        repository.findAll(new ModelRepository.FindAllCallback<DemoTwo.Device>() {
            @Override
            public void onSuccess(List<Device> models) {
                list.setAdapter(new DeviceListAdapter(getActivity(), models));
            }

            @Override
            public void onError(Throwable t) {
                // Log.e(getTag(), "Cannot save Note model.", t);
                showResult("Failed.");
                showResult(t.toString());
            }
        });
    }


    private void showResult(String message) {
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

                Device model = (Device)list.getItemAtPosition(position);
                showResult(model.toString());
               // FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
               // fragmentTransaction.add(R.id.root_layout, item_display.newInstance(o.deviceID, o.uid, o.token, o.oldtoken, o.metadata, o.privatekey));
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
