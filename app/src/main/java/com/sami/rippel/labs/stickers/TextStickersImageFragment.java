package com.sami.rippel.labs.stickers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.listner.StickersListner;

public class TextStickersImageFragment extends Fragment {

    StickersListner stickersListner;
    Button mButtonAddText;
    Button mButtonAddFilter;
    Button mButtonResetAll;
    Button mButtonSize;
    Button mButtonBackground;
    Button mButtonMorePadding;
    Button mButtonLessPadding;

    public static TextStickersImageFragment newInstance() {
        return new TextStickersImageFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mButtonAddText.setOnClickListener(x -> {
            if (stickersListner != null) stickersListner.addTextFromFragment();
        });
        mButtonAddFilter.setOnClickListener(x -> {
            if (stickersListner != null) stickersListner.changeTextFont();
        });
        mButtonResetAll.setOnClickListener(x -> {
            if (stickersListner != null) stickersListner.resetAllView();
        });
        mButtonSize.setOnClickListener(x -> {
            if (stickersListner != null) stickersListner.changeTextSize();
        });
        mButtonBackground.setOnClickListener(x -> {
            if (stickersListner != null) stickersListner.changeTextBackground();
        });
        mButtonMorePadding.setOnClickListener(x -> {
            if (stickersListner != null) stickersListner.morePadding();
        });
        mButtonLessPadding.setOnClickListener(x -> {
            if (stickersListner != null) stickersListner.lestPadding();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_text_stickers, container, false);
        mButtonAddText = (Button) rootView.findViewById(R.id.buttonAddText);
        mButtonAddFilter = (Button) rootView.findViewById(R.id.buttonAddFilter);
        mButtonResetAll = (Button) rootView.findViewById(R.id.buttonResetAll);
        mButtonSize = (Button) rootView.findViewById(R.id.buttonSize);
        mButtonBackground = (Button) rootView.findViewById(R.id.buttonBackground);
        mButtonMorePadding = (Button) rootView.findViewById(R.id.buttonMorePadding);
        mButtonLessPadding = (Button) rootView.findViewById(R.id.buttonLessPadding);
        return rootView;
    }

    public void setListener(StickersListner stickersListner) {
        this.stickersListner = stickersListner;
    }
}
