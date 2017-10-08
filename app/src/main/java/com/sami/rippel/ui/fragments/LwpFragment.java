package com.sami.rippel.ui.fragments;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sami.rippel.model.ViewModel;
import com.sami.rippel.ui.activity.AboutActivty;
import com.sami.rippel.ui.activity.GalleryWallpaperActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.allah.R;
import com.sami.rippel.ui.activity.ViewPagerWallpaperActivity;
import com.sami.rippel.model.listner.AdsListner;
import com.sami.rippel.livewallpapers.lwpskyview.RajawaliExampleWallpaper;
import com.sami.rippel.livewallpapers.lwptimer.WallpaperSchedulerActivity;

import java.io.IOException;

public class LwpFragment extends Fragment {

    private AppBarLayout mAppBarLayout;
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
    private AdsListner mListener = null;

    public static LwpFragment newInstance() {
        return new LwpFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mButtonRippleLWP.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onTrackAction("LwpFragment", "OpenLiveWallpapers");
            }
            Intent intent = new Intent(
                    getActivity(),
                    GalleryWallpaperActivity.class);
            intent.putExtra("LwpName", "RippleLwp");
            startActivity(intent);
        });

        mButtonSkybox.setOnClickListener(v -> {
            clearCurrentWallpaper();
            try {
                if (mListener != null) {
                    mListener.onTrackAction("LwpFragment", "OpenSkyBox");
                }
                ViewPagerWallpaperActivity.isAdsShow = true;

                Intent intent = new Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(getActivity(),
                                RajawaliExampleWallpaper.class));
                 /*
                Intent intent = new Intent(
                        getActivity(),
                        GalleryWallpaperActivity.class);
                intent.putExtra("LWP", "Skybox");
                 */
                startActivity(intent);

            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                    startActivity(intent);
                } catch (Exception exception) {
                }
            }
        });

        mButtonDoua.setOnClickListener(view12 -> {
            if (mListener != null) {
                mListener.onTrackAction("LwpFragment", "DouaLWP");
            }
            ViewPagerWallpaperActivity.isAdsShow = true;
            Intent intent = new Intent(
                    getActivity(),
                    GalleryWallpaperActivity.class);
            intent.putExtra("LwpName", "DouaLWP");
            startActivity(intent);
        });

        mButtonTimer.setOnClickListener(view13 -> {
            if (mListener != null) {
                mListener.onTrackAction("LwpFragment", "TimerLWP");
            }
            ViewPagerWallpaperActivity.isAdsShow = true;
            Intent intent = new Intent(
                    getActivity(),
                    WallpaperSchedulerActivity.class);
            startActivity(intent);
        });

        if (ViewModel.Current.device.getScreenWidthPixels() < 1000
                && ViewModel.Current.device.getScreenHeightPixels() < 1700) {
            mCardViewSkyBox.setVisibility(View.GONE);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCardViewTimer.setVisibility(View.VISIBLE);
        } else
            mCardViewTimer.setVisibility(View.GONE);

        mBtotherapp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(Constants.SFAXDROID_LINK));
            startActivity(intent);
        });

        mBtnRating.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri
                    .parse("market://details?id=com.sami.rippel.allah"));
            startActivity(intent);
        });

        mTextViewAbout.setOnClickListener(v -> {
            Intent intent = new Intent(
                    getActivity(),
                    AboutActivty.class);
            startActivity(intent);
        });

        mButtonNameofallah2D.setOnClickListener(view1 -> {
            if (mListener != null) {
                mListener.onTrackAction("LwpFragment", "NameOfAllah2D");
            }
            ViewPagerWallpaperActivity.isAdsShow = true;
            Intent intent = new Intent(
                    getActivity(),
                    GalleryWallpaperActivity.class);
            intent.putExtra("LwpName", "NameOfAllah2DLWP");
            startActivity(intent);
        });

        resizeTitle();
    }

    public void changeButtonSate(boolean mState)
    {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lwp, container, false);
        mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        mButtonRippleLWP = (TextView) rootView.findViewById(R.id.button1);
        mButtonSkybox = (TextView) rootView.findViewById(R.id.buttonSkybox);
        mButtonDoua = (TextView) rootView.findViewById(R.id.buttonDoua);
        mButtonTimer = (TextView) rootView.findViewById(R.id.buttonTimer);
        mBtotherapp = (Button) rootView.findViewById(R.id.btotherapp);
        mBtnRating = (Button) rootView.findViewById(R.id.btnRating);
        mButtonNameofallah2D = (TextView) rootView.findViewById(R.id.buttonNameofallah2D);
        mCardViewSkyBox = (CardView) rootView.findViewById(R.id.cardViewSkyBox);
        mCardViewTimer = (CardView) rootView.findViewById(R.id.cardViewTimer);
        mTextViewAbout = (TextView) rootView.findViewById(R.id.textViewAbout);
        //LWP TITLE
        mTxttitleTimer = (TextView) rootView.findViewById(R.id.txttitleTimer);
        mTxttitledoua = (TextView) rootView.findViewById(R.id.txttitledoua);
        mTxttilekybox = (TextView) rootView.findViewById(R.id.txttilekybox);
        mTxttitleripple = (TextView) rootView.findViewById(R.id.txttitleripple);
        mTxttitleNameofallah2D = (TextView) rootView.findViewById(R.id.txttitleNameofallah2D);
        //LWP DESC
        mTxtdesctimer = (TextView) rootView.findViewById(R.id.txtdesctimer);
        mTxtdescdoua = (TextView) rootView.findViewById(R.id.txtdescdoua);
        mTxtdescskybox = (TextView) rootView.findViewById(R.id.txtdescskybox);
        mTxtdescripple = (TextView) rootView.findViewById(R.id.txtdescripple);
        mTxtdescNameofallah2D    = (TextView) rootView.findViewById(R.id.txtdescNameofallah2D);
        return rootView;
    }

    public void resizeTitle() {
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
        } catch (IOException e) {
        }
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
