package com.alexxg.android_vap_demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;

public class HtmlFragment extends Fragment {
	public static final String VAP_FILE = "VideoAuthenticationProtcol_Simulation_File";
	public static final String SECRET_DEVICE_ID = "SDI";
	public static final String SECRET_TOKEN = "ST";
	public static final String UNKNOWN = "Unknown";
	/* Secret Device ID
    This is the ID value the is used to communication to the manufacturer's cloud.
    It is created at registration. On the device, it would be stored on a hardware chip
    that can only receive encrypted communication from the cloud.
     */
	private String secretDeviceId;
	/* Secret Device Token
This is the token value the is used to communication to the manufacturer's cloud.
The token value is created by the last communication and protects against replay attacks.
 */
	private String secretDeviceToken;
	private String hwID;
	protected Context context;
	private ViewGroup rootView;

	protected ViewGroup getRootView() { return rootView; }
	protected void setRootView(ViewGroup value) { rootView = value; }

	public HtmlFragment() {
		super();
	}

	protected String getSecretDeviceId(){ return secretDeviceId;}
	protected String getSecretDeviceToken(){ return secretDeviceToken;}
	protected String gethwID(){ return hwID;}

	protected void setSecretDeviceId(String newValue){ secretDeviceId = newValue;}
	protected void setSecretDeviceToken(String newValue){ secretDeviceToken = newValue;}
	private void setDeviceId(String newValue){hwID = newValue;}

	protected void setHtmlText(int textViewId, int stringResourceId) {
	    TextView text = (TextView)getRootView().findViewById(textViewId);
	    String htmlContent = getString(stringResourceId);
		text.setText(Html.fromHtml(htmlContent));
		text.setMovementMethod(LinkMovementMethod.getInstance());

	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Get Device Hardware ID
       /* TelephonyManager mTelephonyMgr;
        mTelephonyMgr = Context.getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyMgr.getDeviceId();*/

		// Restore app preferences / data / settings
		context = getActivity().getApplicationContext();
		setDeviceId(Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID));
		SharedPreferences settings = context.getSharedPreferences(VAP_FILE, 0);
		String id = settings.getString(SECRET_DEVICE_ID, UNKNOWN);
		String token = settings.getString(SECRET_TOKEN, UNKNOWN);
		setSecretDeviceId(id);
		setSecretDeviceToken(token);
	}
}
