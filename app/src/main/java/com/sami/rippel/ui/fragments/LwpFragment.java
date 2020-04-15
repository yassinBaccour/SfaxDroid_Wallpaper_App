package com.sami.rippel.ui.fragments;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sami.rippel.allah.R;
import com.sami.rippel.base.SimpleFragment;
import com.sami.rippel.livewallpapers.lwpskyview.SkyNameOfAllahLiveWallpaper;
import com.sami.rippel.livewallpapers.lwptimer.WallpaperSchedulerActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.listner.AdsListener;
import com.sami.rippel.ui.activity.AboutActivity;
import com.sami.rippel.ui.activity.GalleryActivity;
import com.sami.rippel.ui.activity.HomeActivity;

import java.io.IOException;

public class LwpFragment extends SimpleFragment {

    private TextView mButtonSkybox;
    private TextView mButtonRippleLWP;
    private TextView mButtonDoua;
    private TextView mButtonTimer;
    private TextView mTextViewAbout;
    private TextView mTxttitleTimer;
    private TextView mTxttitledoua;
    private TextView mTxttilekybox;
    private TextView mTxttitleripple;
    private TextView mTxtdesctimer;
    private TextView mTxtdescdoua;
    private TextView mTxtdescskybox;
    private TextView mTxtdescripple;
    private TextView mTxttitleNameofallah2D;
    private TextView mTxtdescNameofallah2D;
    private TextView mButtonNameofallah2D;
    private Button mBtnRating;
    private Button mBtotherapp;
    private CardView mCardViewSkyBox;
    private CardView mCardViewTimer;
    private AdsListener mListener = null;

    public static LwpFragment newInstance() {
        return new LwpFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mButtonRippleLWP.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onTrackAction("LwpFragment", "OpenLiveWallpapers");
            }
            Intent intent = new Intent(
                    getActivity(),
                    GalleryActivity.class);
            intent.putExtra("LwpName", "RippleLwp");
            startActivity(intent);
        });

        mButtonSkybox.setOnClickListener(v -> {
            clearCurrentWallpaper();
            try {
                if (mListener != null) {
                    mListener.onTrackAction("LwpFragment", "OpenSkyBox");
                }

                Intent intent = new Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(getActivity(),
                                SkyNameOfAllahLiveWallpaper.class));
                startActivity(intent);

            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                    startActivity(intent);
                } catch (Exception ignored) {
                }
            }
        });

        mButtonDoua.setOnClickListener(view12 -> {
            if (mListener != null) {
                mListener.onTrackAction("LwpFragment", "DouaLWP");
            }
            HomeActivity.isAdsShow = true;
            Intent intent = new Intent(
                    getActivity(),
                    GalleryActivity.class);
            intent.putExtra("LwpName", "DouaLWP");
            startActivity(intent);
        });

        mButtonTimer.setOnClickListener(view13 -> {
            if (mListener != null) {
                mListener.onTrackAction("LwpFragment", "TimerLWP");
            }
            HomeActivity.isAdsShow = true;
            openWallpaperSchedulerActivity();
        });

        if (ViewModel.Current.device.getScreenWidthPixels() < 710
                && ViewModel.Current.device.getScreenHeightPixels() < 1200) {
            mCardViewSkyBox.setVisibility(View.GONE);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCardViewTimer.setVisibility(View.VISIBLE);
        } else {
            mCardViewTimer.setVisibility(View.GONE);
        }

        mBtotherapp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(Constants.SFAXDROID_LINK));
            startActivity(intent);
        });

        mBtnRating.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri
                    .parse(Constants.APP_PACKAGE));
            startActivity(intent);
        });

        mTextViewAbout.setOnClickListener(v -> {
            Intent intent = new Intent(
                    getActivity(),
                    AboutActivity.class);
            startActivity(intent);
        });

        mButtonNameofallah2D.setOnClickListener(view1 -> {
            if (mListener != null) {
                mListener.onTrackAction("LwpFragment", "NameOfAllah2D");
            }
            HomeActivity.isAdsShow = true;
            Intent intent = new Intent(
                    getActivity(),
                    GalleryActivity.class);
            intent.putExtra("LwpName", "NameOfAllah2DLWP");
            startActivity(intent);
        });

        resizeTitleForSmallDevice();
    }

    private void openWallpaperSchedulerActivity() {
        Intent intent = new Intent(
                getActivity(),
                WallpaperSchedulerActivity.class);
        startActivity(intent);
    }

    public void changeButtonSate(boolean mState) {
        mButtonRippleLWP.setEnabled(mState);
        mButtonSkybox.setEnabled(mState);
        mButtonDoua.setEnabled(mState);
        mButtonTimer.setEnabled(mState);
        mBtotherapp.setEnabled(mState);
        mBtnRating.setEnabled(mState);
        mTextViewAbout.setEnabled(mState);
        mButtonNameofallah2D.setEnabled(mState);
    }

    @Override
    public int getFragmentId() {
        return R.layout.fragment_lwp;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mButtonRippleLWP = rootView.findViewById(R.id.button1);
        mButtonSkybox = rootView.findViewById(R.id.buttonSkybox);
        mButtonDoua = rootView.findViewById(R.id.buttonDoua);
        mButtonTimer = rootView.findViewById(R.id.buttonTimer);
        mBtotherapp = rootView.findViewById(R.id.btotherapp);
        mBtnRating = rootView.findViewById(R.id.btnRating);
        mButtonNameofallah2D = rootView.findViewById(R.id.buttonNameofallah2D);
        mCardViewSkyBox = rootView.findViewById(R.id.cardViewSkyBox);
        mCardViewTimer = rootView.findViewById(R.id.cardViewTimer);
        mTextViewAbout = rootView.findViewById(R.id.textViewAbout);
        //LWP TITLE
        mTxttitleTimer = rootView.findViewById(R.id.txttitleTimer);
        mTxttitledoua = rootView.findViewById(R.id.txttitledoua);
        mTxttilekybox = rootView.findViewById(R.id.txttilekybox);
        mTxttitleripple = rootView.findViewById(R.id.txttitleripple);
        mTxttitleNameofallah2D = rootView.findViewById(R.id.txttitleNameofallah2D);
        //LWP DESC
        mTxtdesctimer = rootView.findViewById(R.id.txtdesctimer);
        mTxtdescdoua = rootView.findViewById(R.id.txtdescdoua);
        mTxtdescskybox = rootView.findViewById(R.id.txtdescskybox);
        mTxtdescripple = rootView.findViewById(R.id.txtdescripple);
        mTxtdescNameofallah2D = rootView.findViewById(R.id.txtdescNameofallah2D);
        return rootView;
    }

    public void resizeTitleForSmallDevice() {
        if (ViewModel.Current.device.getScreenWidthPixels() < 500
                && ViewModel.Current.device.getScreenHeightPixels() < 820) {
            //TITLE LWP
            mTxttitleripple.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            mTxttilekybox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            mTxttitledoua.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            mTxttitleTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            mTxttitleNameofallah2D.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            //DESC LWP
            mTxtdesctimer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            mTxtdescdoua.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            mTxtdescskybox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            mTxtdescripple.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            mTxtdescNameofallah2D.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
    }

    public void clearCurrentWallpaper() {
        WallpaperManager myWallpaperManager = WallpaperManager
                .getInstance(getActivity());
        try {
            myWallpaperManager.clear();
        } catch (IOException ignored) {
        }
    }

    public void setListener(AdsListener adsListener) {
        mListener = adsListener;
    }
}
