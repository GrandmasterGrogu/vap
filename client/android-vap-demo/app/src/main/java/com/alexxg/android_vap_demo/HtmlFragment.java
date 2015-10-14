package com.alexxg.android_vap_demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.security.KeyPairGeneratorSpec;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.x500.X500Principal;

import static android.os.Debug.startMethodTracing;
import static android.os.Debug.stopMethodTracing;

public class HtmlFragment extends Fragment {
	public static final String VAP_FILE = "VideoAuthenticationProtcol_Simulation_File";
	public static final String VAP_KEYS = "VideoAuthenticationProtcol_KeyPair";
	public static final String SECRET_DEVICE_ID = "SDI";
	public static final String SECRET_TOKEN = "ST";
	public static final String SEMI_SECRET_DIGITAL_SIGNATURE_PUBLIC_KEY = "SSDSPK";
	public static final String SECRET_ENCRYPTION_PUBLIC_KEY = "SEPK";
	public static final String UNKNOWN = "Unknown";
	public static final String ERROR_TAG = "ERROR";
	public static final String NORMAL_TAG = "NORMAL";
	protected static RSAPrivateKey VAPprivateKey;
	protected static RSAPublicKey VAPpublicKey;

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


	// The device would digitally sign each communication in the full protocol.
	// This digital signing function would be available to anyone including the user.
	// A protected key pair can be used in devices with technologies like TPM.
// Found these sample useful generation functions to use here http://stackoverflow.com/questions/30929103/digital-signature-in-java-android-rsa-keys
	// Also, this was a useful reference http://crypto.stackexchange.com/questions/5646/what-are-the-differences-between-a-digital-signature-a-mac-and-a-hash
/*	public static KeyPair createKeyPair() {
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
*/

/*
 * Generated a signed String
 * @param text : string to sign
 * @param strPrivateKey : private key (String format)
 */
public byte[] getDigitalSignature(String text, PrivateKey pk)  {
	//startMethodTracing("DigitalSigning"); // Debug and Performance Measuring
    try {
//showResult(text);
		//showResult(strPrivateKey);
        // Get private key from String
       // PrivateKey pk = loadPrivateKey(strPrivateKey);

        // text to bytes
        byte[] data = text.getBytes("UTF8");

        // signature
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(pk);
        sig.update(data);
        byte[] signatureBytes = sig.sign();
//stopMethodTracing(); // Debug and Performance Measuring
		return signatureBytes;
       // return javax.xml.bind.DatatypeConverter.printBase64Binary(signatureBytes);

    }catch(Exception e){
        return null;
    }

}
/* generateKeyPair
* Generates a RSA Keypair and stores in KeyStore.
* It retrieves the KeyStore entry and stores in the protected RSA key pair variables.
 */
	private void generateKeyPair(){
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("AndroidKeyStore");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		try {
			keyStore.load(null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}

		//String alias = "my_key"; // replace as required or get it as a function argument

		int nBefore = 0; // debugging variable to help convince yourself this works
		try {
			nBefore = keyStore.size();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

// Create the keys if necessary
		try {
			if (!keyStore.containsAlias(VAP_KEYS)) {

                Calendar notBefore = Calendar.getInstance();
                Calendar notAfter = Calendar.getInstance();
                notAfter.add(Calendar.YEAR, 1);
                KeyPairGeneratorSpec spec;
				spec = new KeyPairGeneratorSpec.Builder(getActivity())
                        .setAlias(VAP_KEYS)
                        .setSubject(new X500Principal("CN=test"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(notBefore.getTime())
                        .setEndDate(notAfter.getTime())
                        .build();
				KeyPairGenerator generator = null;
                try {
                    generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }

                try {
                    generator.initialize(spec);
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }

                KeyPair keyPair = generator.generateKeyPair();
            }
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		int nAfter = 0;
		try {
			nAfter = keyStore.size();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		Log.v(ERROR_TAG, "Before = " + nBefore + " After = " + nAfter);

// Retrieve the keys
		KeyStore.PrivateKeyEntry privateKeyEntry = null;
		try {
			privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(VAP_KEYS, null);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnrecoverableEntryException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		VAPprivateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
		VAPpublicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

		Log.v(NORMAL_TAG, "private key = " + VAPprivateKey.toString());
		Log.v(NORMAL_TAG, "public key = " + VAPpublicKey.toString());

	}
/*
	private void storeKeyPair(){
		KeyStore ks = null;
		try {
			ks = KeyStore.getInstance("AndroidKeyStore");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		try {
			ks.load(null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		KeyStore.Entry entry = null;
		try {
			entry = ks.getEntry(VAP_KEYS, null);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnrecoverableEntryException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
			Log.w(ERROR_TAG, "Not an instance of a PrivateKeyEntry");

		}
	}
*/

private PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
    byte[] clear = Base64.decode(key64, Base64.DEFAULT);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PrivateKey priv = fact.generatePrivate(keySpec);
    Arrays.fill(clear, (byte) 0);
    return priv;
}

/*
 * Verify signature of a string
 * @param signature : signature
 * @param origina: original string to verify
 * @param publicKey: user public key
 */
public static boolean verfiySignature(byte[] signatureBytes, String original, PublicKey pk){
	startMethodTracing("VerifySignature"); // Debug and Performance Measuring
    try{

        // Get private key from String
      //  PublicKey pk = loadPublicKey(publicKey);

        // text to bytes
        byte[] originalBytes = original.getBytes("UTF8");

        //signature to bytes
        //byte[] signatureBytes = signature.getBytes("UTF8");
       // byte[] signatureBytes = Base64.decode(signature, Base64.DEFAULT);
        // javax.xml.bind.DatatypeConverter.parseBase64Binary(signature);

        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(pk);
        sig.update(originalBytes);
stopMethodTracing(); // Debug and Performance Measuring
        return sig.verify(signatureBytes);

    }catch(Exception e) {
		e.printStackTrace();
        Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        log.log(Level.SEVERE,"Error for signature:" + e.getMessage());
        return false;
    }

}


/*
 * Generate a PublicKey object from a string
 * @ key64 : public key in string format (BASE 64)
 */
private static PublicKey loadPublicKey(String key64) throws GeneralSecurityException {
    byte[] data = Base64.decode(key64, Base64.DEFAULT);
    //javax.xml.bind.DatatypeConverter.parseBase64Binary(key64);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    return fact.generatePublic(spec);
}


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
	protected void showShortResult(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
		generateKeyPair();
		setSecretDeviceId(id);
		setSecretDeviceToken(token);
		setSemiSecretDigitalSignaturePublicKey(Base64.encodeToString(VAPpublicKey.getEncoded(), Base64.DEFAULT));
		setSecretEncryptionPublicKey(e_public_key);
	}
}
