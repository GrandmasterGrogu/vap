package com.alexxg.android_vap_demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IntroductionFragment extends HtmlFragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView((ViewGroup) inflater.inflate(
        		R.layout.fragment_introduction, container, false));

        setHtmlText(R.id.introduction_content, R.string.introduction_content);
        return getRootView();
	}
}
