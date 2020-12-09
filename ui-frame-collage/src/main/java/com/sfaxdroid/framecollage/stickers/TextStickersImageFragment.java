package com.sfaxdroid.framecollage.stickers;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.listner.StickersListener;

public class TextStickersImageFragment extends Fragment {

    private StickersListener stickersListner;
    private Button mButtonAddText;
    private Button mButtonAddFilter;
    private Button mButtonResetAll;
    private Button mButtonSize;
    private Button mButtonBackground;
    private Button mButtonMorePadding;
    private Button mButtonLessPadding;

    public static TextStickersImageFragment newInstance() {
        return new TextStickersImageFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    public void setListener(StickersListener stickersListner) {
        this.stickersListner = stickersListner;
    }
}