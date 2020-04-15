package com.sami.rippel.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sami.rippel.allah.R;
import com.sami.rippel.labs.basmala.ActivityBasmalaScreen;
import com.sami.rippel.labs.stickers.StickersLabActivity;
import com.sami.rippel.model.listner.AdsListener;
import com.sami.rippel.ui.activity.HomeActivity;

public class LabFragment extends Fragment {

    private AdsListener mListener = null;

    public static LabFragment newInstance() {
        return new LabFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lab, container, false);
        TextView mButtonTakeImage = rootView.findViewById(R.id.buttonTakeImageActivity);
        TextView mButtonTakeImageCam = rootView.findViewById(R.id.buttonTakeImageCam);
        TextView mButtonOpenBasmala = rootView.findViewById(R.id.buttonOpenBasmala);
        TextView mButtonTakeImageActivityStikers = rootView.findViewById(R.id.buttonTakeImageActivityStikers);

        mButtonTakeImageCam.setOnClickListener(view -> {
            if (mListener != null) {
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
            HomeActivity.isAdsShow = true;
            if (mListener != null) {
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

    public void setListener(AdsListener l) {
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
