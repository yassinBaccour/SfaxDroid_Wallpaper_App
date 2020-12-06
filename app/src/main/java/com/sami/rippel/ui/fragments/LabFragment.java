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
        TextView stickers = view.findViewById(R.id.buttonTakeImageActivityStikers);
        stickers.setOnClickListener(views -> {
            HomeActivity.isAdsShow = true;
            if (mListener != null) {
                mListener.onOpenScreenTracker("StikerActivity");
            }
            Intent intent = new Intent(
                    getActivity(),
                    StickersLabActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lab, container, false);
    }

    public void setListener(AdsListener listener) {
        mListener = listener;
    }

}
