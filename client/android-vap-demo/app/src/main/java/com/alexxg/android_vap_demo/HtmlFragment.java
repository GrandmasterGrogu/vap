package com.alexxg.android_vap_demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class HtmlFragment extends Fragment {
	public static final String VAP_FILE = "VideoAuthenticationProtcol_Simulation_File";
	public static final String SECRET_DEVICE_ID = "SDI";
	public static final String SECRET_TOKEN = "ST";
	public static final String SEMI_SECRET_DIGITAL_SIGNATURE_PUBLIC_KEY = "SSDSPK";
	public static final String SECRET_ENCRYPTION_PUBLIC_KEY = "SEPK";
	public static final String UNKNOWN = "Unknown";
	private String hwID;
	protected Context context;
	private ViewGroup rootView;

	protected ViewGroup getRootView() { return rootView; }
	protected void setRootView(ViewGroup value) { rootView = value; }

	public HtmlFragment() {
		super();
	}

	/* Secret Device ID
This is the ID value the is used to communication to the manufacturer's cloud.
It is created at registration. On the device, it would be stored on a hardware chip
that can only receive encrypted communication from the cloud.
 */
	protected Integer getSecretDeviceId(){
		SharedPreferences settings = context.getSharedPreferences(VAP_FILE, 0);
		int id = settings.getInt(SECRET_DEVICE_ID, 0);

		return id;}

	/* Secret Device Token
This is the token value the is used to communication to the manufacturer's cloud.
The token value is created by the last communication and protects against replay attacks.
*/
	protected String getSecretDeviceToken(){
		SharedPreferences settings = context.getSharedPreferences(VAP_FILE, 0);
		String token = settings.getString(SECRET_TOKEN, UNKNOWN);
		return token;}

	/* Semi-secret Digital Signature Public Key
This is the public key that is used for digital signatures outgoing from the device.
In the hardware chip, it would be usable by the user, but not viewable.
*/
	protected String getSemiSecretDigitalSignaturePublicKey(){
		SharedPreferences settings = context.getSharedPreferences(VAP_FILE, 0);
		String ds_public_key = settings.getString(SEMI_SECRET_DIGITAL_SIGNATURE_PUBLIC_KEY, UNKNOWN);

		return ds_public_key;
	}
	/* Secret Encryption Public Key
This is the public key that is used for encryption to communicate with the
manufacturer database. It is not used in this simulation, but instead SSL can be used.
However, currently, there is not a way to use the self-signed certificates available with Android and the demo's free Amazon EC2 account.
https://github.com/alexxgathp/vap/issues/1
*/
	protected String getSecretEncryptionPublicKey(){
		SharedPreferences settings = context.getSharedPreferences(VAP_FILE, 0);
		String e_public_key = settings.getString(SECRET_ENCRYPTION_PUBLIC_KEY, UNKNOWN);
		return e_public_key;}
	protected String gethwID(){ return hwID;}

	protected void setSecretDeviceId(Integer newValue){
		SharedPreferences settings = context.getSharedPreferences(VAP_FILE, 0);
		settings.edit().putInt(SECRET_DEVICE_ID, newValue).apply();

	}
	protected void setSecretDeviceToken(String newValue){
		SharedPreferences settings = context.getSharedPreferences(VAP_FILE, 0);
		settings.edit().putString(SECRET_TOKEN, newValue).apply();

	}
	protected void showResult(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}

	protected void setSemiSecretDigitalSignaturePublicKey(String newValue){
		SharedPreferences settings = context.getSharedPreferences(VAP_FILE, 0);
		settings.edit().putString(SEMI_SECRET_DIGITAL_SIGNATURE_PUBLIC_KEY, newValue).apply();
	}

	protected void setSecretEncryptionPublicKey(String newValue){
		SharedPreferences settings = context.getSharedPreferences(VAP_FILE, 0);
		settings.edit().putString(SECRET_ENCRYPTION_PUBLIC_KEY, newValue).apply();
	}

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
		setDeviceId(Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID));
		SharedPreferences settings = context.getSharedPreferences(VAP_FILE, 0);
		int id = settings.getInt(SECRET_DEVICE_ID, 0);
		String token = settings.getString(SECRET_TOKEN, UNKNOWN);
		String ds_public_key = settings.getString(SEMI_SECRET_DIGITAL_SIGNATURE_PUBLIC_KEY, UNKNOWN);
		String e_public_key = settings.getString(SECRET_ENCRYPTION_PUBLIC_KEY, UNKNOWN);

		setSecretDeviceId(id);
		setSecretDeviceToken(token);
		setSemiSecretDigitalSignaturePublicKey(ds_public_key);
		setSecretEncryptionPublicKey(e_public_key);
	}
}
