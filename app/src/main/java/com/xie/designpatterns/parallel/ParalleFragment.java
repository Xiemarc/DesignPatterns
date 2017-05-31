package com.xie.designpatterns.parallel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by marc on 2017/4/20.
 */

public class ParalleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        int layoutId = arguments.getInt("layoutId");
        int pageIndex = arguments.getInt("pageIndex");
        View view = inflater.inflate(layoutId, null);
        view.setTag(pageIndex);
        return view;
    }
}
