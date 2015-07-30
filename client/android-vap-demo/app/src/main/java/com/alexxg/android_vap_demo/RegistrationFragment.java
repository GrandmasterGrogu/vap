package com.alexxg.android_vap_demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RegistrationFragment extends HtmlFragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
        		R.layout.fragment_registration, container, false));

        setHtmlText(R.id.registration_content, R.string.registration_content);

        setHtmlText(R.id.android_device_id, R.string.android_device_id);
        TextView text = (TextView)getRootView().findViewById(R.id.android_device_id_value);
        text.setText(gethwID());

        setHtmlText(R.id.vap_secret_id, R.string.vap_secret_id);
        text = (TextView)getRootView().findViewById(R.id.vap_secret_id_value);
        text.setText(getSecretDeviceId().toString());

        setHtmlText(R.id.vap_token, R.string.vap_token);
        text = (TextView)getRootView().findViewById(R.id.vap_token_value);
        text.setText(getSecretDeviceToken());
        installRefreshButtonClickHandler();
        installHardwareResetButtonClickHandler();

        return getRootView();
	}
    private void installRefreshButtonClickHandler(){
        final ImageButton button = (ImageButton) getRootView().findViewById(R.id.refreshButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView text = (TextView) getRootView().findViewById(R.id.vap_secret_id_value);
                text.setText(getSecretDeviceId().toString());
                text = (TextView) getRootView().findViewById(R.id.vap_token_value);
                text.setText(getSecretDeviceToken());
                showResult(getResources().getString(R.string.refreshed));
            }
        });
            }

            private void installHardwareResetButtonClickHandler() {
                final Button button = (Button) getRootView().findViewById(R.id.hwResetButton);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setSecretDeviceToken(UNKNOWN);
                        setSecretDeviceId(0);
                        setSemiSecretDigitalSignaturePublicKey(UNKNOWN);
                        setSecretEncryptionPublicKey(UNKNOWN);
                        showResult(getResources().getString(R.string.wipeVAP));
                        TextView text = (TextView) getRootView().findViewById(R.id.vap_secret_id_value);
                        text.setText(getSecretDeviceId().toString());

                        text = (TextView) getRootView().findViewById(R.id.vap_token_value);
                        text.setText(getSecretDeviceToken());
                    }
                });
            }
        }
