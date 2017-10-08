package com.sami.rippel.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sami.rippel.allah.R;
import com.sami.rippel.labs.basmala.ActivityBasmalaScreen;
import com.sami.rippel.model.listner.AdsListner;
import com.sami.rippel.ui.activity.ViewPagerWallpaperActivity;
import com.sami.rippel.labs.stickers.StickersLabActivity;

public class LabFragment extends Fragment {

    private AppBarLayout mAppBarLayout;
    private AdsListner mListener = null;
    private TextView mButtonTakeImage;
    private TextView mButtonTakeImageCam;
    private TextView mButtonTakeImageActivityStikers;
    private TextView mButtonOpenBasmala;

    public static LabFragment newInstance() {
        return new LabFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lab, container, false);
        mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        mButtonTakeImage = (TextView) rootView.findViewById(R.id.buttonTakeImageActivity);
        mButtonTakeImageCam = (TextView) rootView.findViewById(R.id.buttonTakeImageCam);
        mButtonOpenBasmala = (TextView) rootView.findViewById(R.id.buttonOpenBasmala);

        mButtonTakeImageActivityStikers = (TextView) rootView.findViewById(R.id.buttonTakeImageActivityStikers);
        mButtonTakeImageCam.setOnClickListener(view -> {
            if (mListener != null)
            {
                mListener.onOpenScreenTracker("FrameActivity");
                mListener.onOpenCameraChooser();
            }
        });
        mButtonTakeImage.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onOpenScreenTracker("FrameActivity");
                mListener.onOpenGalleryChooser();
            }
        });
        mButtonTakeImageActivityStikers.setOnClickListener(view -> {
            ViewPagerWallpaperActivity.isAdsShow = true;
            if (mListener != null)
            {
                mListener.onOpenScreenTracker("StikerActivity");
            }
            Intent intent = new Intent(
                    getActivity(),
                    StickersLabActivity.class);
            startActivity(intent);
        });

        mButtonOpenBasmala.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onOpenScreenTracker("OpenBasmala");
                Intent intent = new Intent(
                        getActivity(),
                        ActivityBasmalaScreen.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void setListener(AdsListner l) {
        mListener = l;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
