package com.alexxg.android_vap_demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StepsFragment extends HtmlFragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
        		R.layout.fragment_steps, container, false));

        setHtmlText(R.id.steps_content, R.string.steps_content);
        return getRootView();
	}
}
