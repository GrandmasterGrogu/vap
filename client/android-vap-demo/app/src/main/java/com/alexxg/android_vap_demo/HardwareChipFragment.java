package com.alexxg.android_vap_demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HardwareChipFragment extends HtmlFragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
        		R.layout.fragment_hardware_chip, container, false));

        setHtmlText(R.id.finale_content, R.string.hardware_title);


        return getRootView();
	}
}
