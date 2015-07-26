package com.alexxg.android_vap_demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link item_display#newInstance} factory method to
 * create an instance of this fragment.
 */
public class item_display extends HtmlFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SDID = "SECRET_DEVICE_ID";
    private static final String DID = "DEVICE_ID";
    private static final String TOKEN = "TOKEN";
    private static final String OLD_TOKEN = "OLD_TOKEN";
    private static final String MDATA = "METADATA";
    private static final String PK = "PRIVATE_KEY";

    // TODO: Rename and change types of parameters
    private String secret_device_id;
    private String device_id;
    private String token;
    private String oldtoken;
    private String metadata;
    private String privatekey;

   // private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param secretDeviceID Parameter 1.
     * @param deviceID Parameter 2.
     * @return A new instance of fragment item_display.
     */
    // TODO: Rename and change types and number of parameters
    public static item_display newInstance(String secretDeviceID, String deviceID, String ntoken, String noldtoken, String nmetadata, String nprivatekey) {
        item_display fragment = new item_display();
        Bundle args = new Bundle();
        args.putString(SDID, secretDeviceID);
        args.putString(DID, deviceID);
        args.putString(TOKEN, ntoken);
        args.putString(OLD_TOKEN, noldtoken);
        args.putString(MDATA, nmetadata);
        args.putString(PK, nprivatekey);
        fragment.setArguments(args);
        return fragment;
    }

    public item_display() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            secret_device_id = getArguments().getString(SDID);
            device_id = getArguments().getString(DID);
            token = getArguments().getString(TOKEN);
            oldtoken = getArguments().getString(OLD_TOKEN);
            metadata = getArguments().getString(MDATA);
            privatekey = getArguments().getString(PK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
                R.layout.fragment_item_display, container, false));



        TextView text = (TextView)getRootView().findViewById(R.id.vap_secret_device_id);
        text.setText(device_id);

        setHtmlText(R.id.vap_secret_id, R.string.android_device_id);
        text = (TextView)getRootView().findViewById(R.id.android_device_id_value);
        text.setText(secret_device_id);

        setHtmlText(R.id.vap_token, R.string.vap_token);
        text = (TextView)getRootView().findViewById(R.id.vap_token_value);
        text.setText(token);

        setHtmlText(R.id.vap_token, R.string.vap_old_token);
        text = (TextView)getRootView().findViewById(R.id.vap_old_token_value);
        text.setText(oldtoken);

        setHtmlText(R.id.vap_token, R.string.metadata);
        text = (TextView)getRootView().findViewById(R.id.metadata_value);
        text.setText(metadata);

        setHtmlText(R.id.vap_token, R.string.privatekey);
        text = (TextView)getRootView().findViewById(R.id.privatekey_value);
        text.setText(privatekey);

        return getRootView();
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/


    /*public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/


    /*public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activ
     * ities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(int position);
    } */

}
