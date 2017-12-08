package com.sami.rippel.labs.stickers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.kobakei.ratethisapp.RateThisApp;
import com.myandroid.views.MultiTouchListener;
import com.myandroid.views.myView;
import com.sami.rippel.allah.R;
import com.sami.rippel.base.BaseActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.listner.StickersListner;
import com.sami.rippel.ui.activity.GalleryWallpaperActivity;
import com.sami.rippel.ui.adapter.StickersFragmentPagerAdapter;
import com.sami.rippel.views.GlideApp;

import net.hockeyapp.android.CrashManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("NewApi")
public class StickersLabActivity extends BaseActivity implements StickersListner {
    private static final int PICTURE_TAKEN_FROM_GALLERY = 1;
    private static final int SHARE_REQUEST_CODE = 23123;
    private static final int PICK_USER_PROFILE_IMAGE = 1000;
    private static long back_pressed;
    @BindView(R.id.progressBar)
    public ProgressBar mProgressLoader;
    @BindView(R.id.ImageRotate)
    public ImageView mRotateBtn;
    @BindView(R.id.ImageDelete)
    public ImageView mDeleteBtn;
    @BindView(R.id.imageview_from_gallery)
    public ImageView mImageViewGalleryImage;
    @BindView(R.id.ImagePalette)
    public ImageView mImagePalette;
    @BindView(R.id.rootLayout)
    public CoordinatorLayout mRootLayout;
    @BindView(R.id.capture_id_rl)
    public RelativeLayout mRelativeLayoutMain;
    @BindView(R.id.bottomsheetLayout)
    public BottomSheetLayout mBottomSheet;
    @BindView(R.id.viewpager)
    public ViewPager mViewPager;
    @BindView(R.id.fab)
    public FloatingActionButton mfab;
    @BindView(R.id.surfaceView)
    public SurfaceView mSurfaceView;
    @BindView(R.id.imageViewAddHelp)
    public ImageView mImageViewAddHelp;
    @BindView(R.id.linearButtionEdition)
    public LinearLayout mLinearButtionEdition;
    @BindView(R.id.imageview_frame)
    public ImageView mImageview_frame;
    public int mTxtSize = 20;
    public String mFabDefault = Constants.KEY_DEFAULT;
    public String imagePath = "";
    private Activity mActivity;
    private Matrix mMatrix = new Matrix();
    private Bitmap mBitmapDrawing;
    private Bitmap mBitmapMain = null;
    private boolean mIsDelete = false;
    private int mRotateAngle = 0;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleGestureDetector;
    private ArrayList<myView> mAllImageAtScreenList = new ArrayList<>();
    private ArrayList<ImageView> mArrayListImageeView = new ArrayList<>();
    private ArrayList<TextView> mArrayListTextView = new ArrayList<>();
    private int mColor = -4522170;
    private boolean mChangeColorMode = false;
    private AddTextFragment mFragmentText;
    private HelpFragment helpFragment;
    private int mCurrentPos = 0;
    private int mCurrentFont = 1;
    private int mPaddingLeftRight = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mActivity = this;
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        SurfaceHolder mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mImagePalette.setVisibility(View.GONE);
        mFragmentText = new AddTextFragment();
        mFragmentText.setListener(this);

        if (ViewModel.Current.dataUtils.GetSetting("clicked", false)) {
            mImageViewAddHelp.setVisibility(View.GONE);
        }
        mDeleteBtn.setVisibility(View.GONE);
        setupViewPager();
        mfab.setOnClickListener(v -> {
            if (!mIsDelete) {
                if (mFabDefault.equals(Constants.KEY_DEFAULT)) {
                    if (mImageViewGalleryImage.getVisibility() == View.VISIBLE) {
                        showMenuSheet(MenuSheetView.MenuType.LIST);
                    } else {
                        showMenuSheetSelectPicture(MenuSheetView.MenuType.LIST);
                        mImageViewAddHelp.setVisibility(View.GONE);
                        ViewModel.Current.dataUtils.SetSetting("clicked", true);
                    }
                } else if (mFabDefault.equals(Constants.KEY_TEXT)) {
                    addTextToView(mFragmentText.getStringFromEditText());
                    hideTextFragment();
                } else if (mFabDefault.equals(Constants.KEY_HELP)) {
                    mfab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
                    mfab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add_white_18dp));
                    hideTextFragment();
                }
            } else {
                ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.disableDeleteMode));
            }
        });

        mRotateBtn.setVisibility(View.GONE);
        mRotateBtn.setOnClickListener(view -> {
            mRotateAngle = mRotateAngle + 90;
            mImageViewGalleryImage.setImageBitmap(
                    ViewModel.Current.fileUtils.rotateBitmap(((BitmapDrawable) mImageViewGalleryImage.getDrawable()).getBitmap(), mRotateAngle));
        });

        mDeleteBtn.setOnClickListener(view -> {

            mIsDelete = !mIsDelete;
            enableModeMultitouchOrDelete(mIsDelete);
            setImageDeleteBackground();
            if (((mArrayListTextView.size() == 0 && mCurrentPos == 3) ||
                    mAllImageAtScreenList.size() == 0 && mCurrentPos < 3) && mIsDelete) {
                ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.nothingAtScreen));
            }
        });

        mImagePalette.setOnClickListener(view -> {
            if (!mIsDelete) {
                chooseColor();
            } else {
                ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.disableDeleteMode));
            }
        });
        initRatingApp();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_islamic_stickers;
    }

    @Override
    protected void initEventAndData() {

    }

    public void startCameraActivity() throws IOException {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = ViewModel.Current.fileUtils.createImageFile();
            imagePath = photoFile.getAbsolutePath();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkURI = FileProvider.getUriForFile(
                        this,
                        getApplicationContext()
                                .getPackageName() + ".provider", photoFile);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, apkURI);
            } else {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            }
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(cameraIntent, PICK_USER_PROFILE_IMAGE);
        }
    }

    public void chooseBackgroundColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle(getString(R.string.choosecolor))
                .initialColor(Color.BLUE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                })
                .setPositiveButton(getString(R.string.btnok), (dialog, selectedColor, allColors) -> {
                    if (mCurrentPos == 3 && mArrayListTextView.size() > 0) {
                        mArrayListTextView.get(mArrayListTextView.size() - 1).setBackgroundColor(selectedColor);
                    } else {
                        ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.nothingAtScreen));
                    }
                })
                .setNegativeButton(getString(R.string.btncancel), (dialog, which) -> {
                })
                .build()
                .show();
    }

    public void chooseColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle(getString(R.string.choosecolor))
                .initialColor(Color.BLUE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                })
                .setPositiveButton(getString(R.string.btnok), (dialog, selectedColor, allColors) -> {
                    mColor = selectedColor;
                    if (mCurrentPos == 1) {
                        setColorToDrawablePalette(selectedColor);
                    } else if (mCurrentPos == 3 && mArrayListTextView.size() > 0) {
                        mArrayListTextView.get(mArrayListTextView.size() - 1).setTextColor(mColor);
                    } else {
                        ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.nothingAtScreen));
                    }
                })
                .setNegativeButton(getString(R.string.btncancel), (dialog, which) -> {
                })
                .build()
                .show();
    }

    private void setColorToDrawablePalette(int selectedColor) {
        mImagePalette.setImageDrawable(
                ViewModel.Current.fileUtils
                        .covertBitmapToDrawable(StickersLabActivity.this,
                                ViewModel.Current.fileUtils.
                                        changeImageColor(ViewModel.Current.fileUtils.
                                                        convertDrawableToBitmap(getResources()
                                                                .getDrawable(R.mipmap.ic_palette))
                                                , selectedColor)));
    }


    public void resizeTextureAsync(Bitmap mBitmap, String resizeType) {
        if (!mBitmap.isRecycled()) {
            addSubscribe(
                    Flowable.fromCallable(() -> getResizedBitmap(mBitmap, resizeType))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                mImageViewGalleryImage.setVisibility(View.VISIBLE);
                                mImageViewGalleryImage.setImageBitmap(bitmap);
                                mImageViewGalleryImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                mRotateBtn.setVisibility(View.VISIBLE);
                                changeProgressBarVisibility(false);
                            }));
        }
    }


    public void downloadAndPutTextureAtScreen(String mUrl, String resizeType) {
        changeProgressBarVisibility(true);
        if (ViewModel.Current.device.getScreenHeightPixels() < 600)
            mUrl = mUrl.replace("islamicimages", "islamicimagesmini");

        GlideApp.with(mActivity).asBitmap().load(mUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                resizeTextureAsync(resource, resizeType);
            }
        });
    }

    public void changeColorAndSetImage(Bitmap bitmap) {
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.fileUtils.changeImageColor(bitmap, mColor))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap1 -> {
                    myView myViewTemp = new myView(getApplicationContext());
                    myViewTemp.setImageBitmap(bitmap1);
                    myViewTemp.setOnTouchListener(new MultiTouchListener());
                    addView(myViewTemp);
                    mIsDelete = false;
                    enableModeMultitouchOrDelete(mIsDelete);
                    setImageDeleteBackground();
                    if (mCurrentPos == 0) {
                        createAndOpenHelpFragment("HELP1X");
                    } else if (mCurrentPos == 1)
                        createAndOpenHelpFragment("HELP2X");
                    if (mDeleteBtn.getVisibility() == View.GONE)
                        mDeleteBtn.setVisibility(View.VISIBLE);
                    changeProgressBarVisibility(false);
                })
        );
    }

    public void putImageAtScreen(Bitmap bitmap) {
        myView myViewTemp = new myView(getApplicationContext());
        myViewTemp.setImageBitmap(bitmap);
        myViewTemp.setOnTouchListener(new MultiTouchListener());
        addView(myViewTemp);
        mIsDelete = false;
        enableModeMultitouchOrDelete(mIsDelete);
        setImageDeleteBackground();
        if (mCurrentPos == 0) {
            createAndOpenHelpFragment("HELP1X");
        } else if (mCurrentPos == 1)
            createAndOpenHelpFragment("HELP2X");
        if (mDeleteBtn.getVisibility() == View.GONE)
            mDeleteBtn.setVisibility(View.VISIBLE);
        changeProgressBarVisibility(false);
    }


    @Override
    public void downloadAndPutImageAtScreen(String mUrl) {
        if (!mIsDelete) {
            changeProgressBarVisibility(true);
            GlideApp.with(mActivity).asBitmap().load(mUrl)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource,
                                                    Transition<? super Bitmap> glideAnimation) {

                            if (mChangeColorMode) {
                                changeColorAndSetImage(resource);
                            } else {
                                putImageAtScreen(resource);
                            }
                        }
                    });
        } else {
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.disableDeleteMode));
        }
    }

    @Override
    public void downloadAndPutFrameAtScreen(String mUrl, String resizeType) {
        mUrl = mUrl.replace("prev_", "");
        if (!mIsDelete) {
            changeProgressBarVisibility(true);
            GlideApp.with(mActivity).asBitmap().load(mUrl)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource,
                                                    Transition<? super Bitmap> glideAnimation) {
                            resizeBitmapAsync(resource, resizeType);
                        }
                    });
        } else {
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.disableDeleteMode));
        }
    }

    public void resizeBitmapAsync(Bitmap mBitmap, String resizeType) {
        if (!mBitmap.isRecycled()) {
            addSubscribe(
                    Flowable.fromCallable(() -> getResizedBitmap(mBitmap, resizeType))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                mImageview_frame.setImageBitmap(bitmap);
                                mImageview_frame.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                mImageview_frame.setVisibility(View.VISIBLE);
                                changeProgressBarVisibility(false);
                            }));
        }
    }


    public Bitmap getResizedBitmap(Bitmap bm, String resizeType) {

        int newWidth = mSurfaceView.getHeight();
        int newHeight = mSurfaceView.getHeight();

        if (resizeType.equals(Constants.SQUARE_TYPE)) {
            newWidth = mSurfaceView.getHeight();
            newHeight = mSurfaceView.getHeight();
        } else if (resizeType.equals(Constants.LANDSCAPE_TYPE)) {
            newWidth = mSurfaceView.getWidth();
            newHeight = mSurfaceView.getHeight();
        }

        if (!bm.isRecycled()) {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);
            bm.recycle();
            return resizedBitmap;
        } else
            return null;
    }

    private void createAndOpenHelpFragment(String helpKey) {
        if (ViewModel.Current.dataUtils.GetSetting(helpKey, true)) {
            ViewModel.Current.dataUtils.SetSetting(helpKey, false);
            helpFragment = new HelpFragment();
            helpFragment.setListener(StickersLabActivity.this);
            helpFragment.setMhelpType(helpKey);
            addHelpFragment();
        }
    }

    @Override
    public void addTextFromFragment() {
        if (!mIsDelete) {
            if (!mFragmentText.ismState()) {
                mFabDefault = Constants.KEY_TEXT;
                enableDisableView(false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mFragmentText)
                        .addToBackStack("tag").commit();
            }
        } else {
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.disableDeleteMode));
        }
    }

    public void addHelpFragment() {
        enableDisableView(false);
        mFabDefault = Constants.KEY_HELP;
        mfab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        mfab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_exit));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, helpFragment)
                .addToBackStack("tag").commit();
    }

    @Override
    public void addTextToView(String mTxt) {
        if (!mTxt.isEmpty()) {
            TextView mTextView = new TextView(this);
            mTextView.setOnTouchListener(new MultiTouchListener());
            mTextView.setText(mTxt);
            mTextView.setTextColor(mColor);
            mTextView.setTextSize(20);
            mTextView.setPadding(mPaddingLeftRight, 3, mPaddingLeftRight, 3);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            mRelativeLayoutMain.addView(mTextView, params);
            mArrayListTextView.add(mTextView);
            if (mDeleteBtn.getVisibility() == View.GONE)
                mDeleteBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void changeTextFont() {
        if (mFragmentText != null && mFragmentText.ismState())
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.addtochangefont));
        else {
            if (mArrayListTextView.size() > 0) {
                if (mCurrentFont == 47)
                    mCurrentFont = 1;
                mArrayListTextView.get(mArrayListTextView.size() - 1).setTypeface(getTypeFace(mCurrentFont), getTypeFaceStyle());
                mCurrentFont++;
            } else {
                ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.noText));
            }
        }
    }

    public int getTypeFaceStyle() {
        String type = ViewModel.Current.dataUtils.GetSetting("TypeFace", "NORMAL");
        if (type.equals("NORMAL"))
            return Typeface.NORMAL;
        if (type.equals("BOLD"))
            return Typeface.BOLD;
        if (type.equals("BOLD_ITALIC"))
            return Typeface.BOLD_ITALIC;
        if (type.equals("ITALIC"))
            return Typeface.ITALIC;
        else
            return Typeface.NORMAL;
    }

    @Override
    public void resetAllView() {
        if (mArrayListTextView.size() > 0) {
            for (TextView editText :
                    mArrayListTextView) {
                mArrayListTextView.get(mArrayListTextView.indexOf(editText)).setVisibility(View.GONE);
                mArrayListTextView.remove(editText);
            }
        }
    }

    @Override
    public void changeTextSize() {
        if (mFragmentText != null && mFragmentText.ismState())
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.addtochangesize));
        else {
            if (mArrayListTextView.size() > 0) {
                mTxtSize = mTxtSize + 5;
                if (mTxtSize == 100)
                    mTxtSize = 20;
                mArrayListTextView.get(mArrayListTextView.size() - 1).setTextSize(mTxtSize);
            } else {
                ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.noText));
            }
        }
    }

    @Override
    public void changeTextBackground() {
        if (mFragmentText != null && mFragmentText.ismState())
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.addtochangebackground));
        else
            chooseBackgroundColor();
    }

    @Override
    public void morePadding() {
        if (mFragmentText != null && mFragmentText.ismState())
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.addtochangebackground));
        else {
            if (mCurrentPos == 3 && mArrayListTextView.size() > 0) {
                mPaddingLeftRight = mPaddingLeftRight + 10;
                mArrayListTextView.get(mArrayListTextView.size() - 1).setPadding(mPaddingLeftRight, 3, mPaddingLeftRight, 3);
            } else {
                ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.nothingAtScreen));
            }
        }
    }

    @Override
    public void lestPadding() {
        if (mFragmentText != null && mFragmentText.ismState())
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.addtochangebackground));
        else {
            if (mCurrentPos == 3 && mArrayListTextView.size() > 0) {
                mPaddingLeftRight = mPaddingLeftRight - 10;
                mArrayListTextView.get(mArrayListTextView.size() - 1).setPadding(mPaddingLeftRight, 3, mPaddingLeftRight, 3);
            } else {
                ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.nothingAtScreen));
            }
        }
    }

    @Override
    public void RemoveFrame() {
        mImageview_frame.setVisibility(View.GONE);
        changeProgressBarVisibility(false);
    }

    public void changeProgressBarVisibility(Boolean isVisible) {
        if (mProgressLoader != null) {
            if (isVisible)
                mProgressLoader.setVisibility(View.VISIBLE);
            else
                mProgressLoader.setVisibility(View.GONE);
        }
    }

    private void setupViewPager() {
        StickersFragmentPagerAdapter mAdapterViewPager =
                new StickersFragmentPagerAdapter(getSupportFragmentManager(), this, this);
        mViewPager.setAdapter(mAdapterViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPos = position;
                if (position == 1 || position == 3) {
                    mImagePalette.setVisibility(View.VISIBLE);
                    mChangeColorMode = true;
                } else {
                    mImagePalette.setVisibility(View.GONE);
                    mChangeColorMode = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setImageDeleteBackground() {
        if (mIsDelete)
            mDeleteBtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_delete_stickers_on));
        else
            mDeleteBtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_delete_stickers_off));
    }

    public void enableDisableView(boolean mState) {
        mViewPager.setEnabled(mState);
        mDeleteBtn.setEnabled(mState);
        mRotateBtn.setEnabled(mState);
        mImagePalette.setEnabled(mState);

        if (mState) {
            mLinearButtionEdition.setVisibility(View.VISIBLE);
            mSurfaceView.setVisibility(View.VISIBLE);
        } else {
            mLinearButtionEdition.setVisibility(View.GONE);
            mSurfaceView.setVisibility(View.GONE);
        }

        for (myView view : mAllImageAtScreenList) {
            if (mState)
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.GONE);
        }

        for (TextView txt : mArrayListTextView) {
            if (mState)
                txt.setVisibility(View.VISIBLE);
            else
                txt.setVisibility(View.GONE);
        }

    }

    public void recycleBitmap() {
        if (mBitmapDrawing != null)
            mBitmapDrawing.recycle();
        if (mBitmapMain != null)
            mBitmapMain.recycle();
    }

    private void showMenuSheet(MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView = new MenuSheetView(this, menuType, "", getMenuSheetListener());
        menuSheetView.inflateMenu(R.menu.menu_stickers);
        mBottomSheet.showWithSheetView(menuSheetView);
    }

    private void showMenuSheetSelectPicture(MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView = new MenuSheetView(this, menuType, "", getMenuOpencameraListener());
        menuSheetView.inflateMenu(R.menu.menu_open);
        mBottomSheet.showWithSheetView(menuSheetView);
    }

    private void dismissMenuSheet() {
        if (mBottomSheet.isSheetShowing()) {
            mBottomSheet.dismissSheet();
        }
    }

    public void openGalleryIntent() {
        Intent gallerypickerIntent1 = new Intent(Intent.ACTION_PICK);
        gallerypickerIntent1.setType("image/*");
        startActivityForResult(gallerypickerIntent1,
                PICTURE_TAKEN_FROM_GALLERY);
    }

    public void removeCurrentPicture() {
        mImageViewGalleryImage.setVisibility(View.GONE);
        mRotateBtn.setVisibility(View.GONE);
    }

    public MenuSheetView.OnMenuItemClickListener getMenuSheetListener() {
        return item -> {
            switch (item.getItemId()) {
                case R.id.image_gallery:
                    recycleBitmap();
                    removeCurrentPicture();
                    break;
                case R.id.imageview_save:
                    saveImage();
                    break;
                case R.id.buttonSareFb:
                    saveImageAndShare();
                    break;
            }
            dismissMenuSheet();
            return true;
        };
    }

    public MenuSheetView.OnMenuItemClickListener getMenuOpencameraListener() {
        return item -> {
            switch (item.getItemId()) {
                case R.id.open_camera:
                    try {
                        startCameraActivity();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.open_gallery:
                    openGalleryIntent();
                    break;
                case R.id.open_web:
                    Intent intent = new Intent(StickersLabActivity.this, GalleryWallpaperActivity.class);
                    intent.putExtra(Constants.KEY_LWP_NAME, Constants.KEY_TEXTURE);
                    startActivityForResult(intent, 456);
                    break;
            }
            dismissMenuSheet();
            return true;
        };
    }

    public void saveImage() {
        mBitmapDrawing = Bitmap.createBitmap(mRelativeLayoutMain.getWidth(),
                mRelativeLayoutMain.getHeight(), Config.ARGB_8888);
        mBitmapDrawing = ThumbnailUtils.extractThumbnail(mBitmapDrawing,
                mBitmapDrawing.getWidth(), mBitmapDrawing.getHeight());
        Canvas b = new Canvas(mBitmapDrawing);
        mRelativeLayoutMain.draw(b);
        if (mImageview_frame.getVisibility() == View.GONE) {
            int x = (mRelativeLayoutMain.getWidth() - mImageViewGalleryImage
                    .getWidth()) / 2;
            int y = (mRelativeLayoutMain.getHeight() - mImageViewGalleryImage
                    .getHeight()) / 2;
            mBitmapMain = Bitmap.createBitmap(mBitmapDrawing, x, y,
                    mImageViewGalleryImage.getWidth(),
                    mImageViewGalleryImage.getHeight());
        } else {
            int x = (mRelativeLayoutMain.getWidth() - mImageview_frame
                    .getWidth()) / 2;
            int y = (mRelativeLayoutMain.getHeight() - mImageview_frame
                    .getHeight()) / 2;

            mBitmapMain = Bitmap.createBitmap(mBitmapDrawing, x, y,
                    mImageview_frame.getWidth(),
                    mImageview_frame.getHeight());
        }
        new SaveFile(mBitmapMain)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void saveImageAndShare() {
        changeProgressBarVisibility(true);
        addSubscribe(Flowable.fromCallable(() -> saveCapturedImage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    ShareImage(bitmap);
                    hideProgressLoader();
                }));
    }


    public Bitmap saveCapturedImage() {
        mBitmapDrawing = Bitmap.createBitmap(mRelativeLayoutMain.getWidth(),
                mRelativeLayoutMain.getHeight(), Config.ARGB_8888);

        mBitmapDrawing = ThumbnailUtils.extractThumbnail(mBitmapDrawing,
                mBitmapDrawing.getWidth(), mBitmapDrawing.getHeight());

        Canvas b = new Canvas(mBitmapDrawing);
        mRelativeLayoutMain.draw(b);

        if (mImageview_frame.getVisibility() == View.GONE) {

            int x = (mRelativeLayoutMain.getWidth() - mImageViewGalleryImage
                    .getWidth()) / 2;
            int y = (mRelativeLayoutMain.getHeight() - mImageViewGalleryImage
                    .getHeight()) / 2;

            return ViewModel.Current.fileUtils.rotateBitmap(Bitmap.createBitmap(mBitmapDrawing, x, y,
                    mImageViewGalleryImage.getWidth(),
                    mImageViewGalleryImage.getHeight()), -mRotateAngle);

        } else {
            int x = (mRelativeLayoutMain.getWidth() - mImageview_frame
                    .getWidth()) / 2;
            int y = (mRelativeLayoutMain.getHeight() - mImageview_frame
                    .getHeight()) / 2;

            return ViewModel.Current.fileUtils.rotateBitmap(Bitmap.createBitmap(mBitmapDrawing, x, y,
                    mImageview_frame.getWidth(),
                    mImageview_frame.getHeight()), 0);
        }
    }

    public void ShareImage(Bitmap mBitmap) {
        String mPathOfBmp = Media.insertImage(
                getContentResolver(), mBitmap, "title", null);
        if (mPathOfBmp != null && !mPathOfBmp.isEmpty()) {
            Uri bmpUri = Uri.parse(mPathOfBmp);
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("image/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            startActivityForResult(Intent.createChooser(sharingIntent,
                    getString(R.string.ShareUsing)), SHARE_REQUEST_CODE);
        }
    }

    public void rateApplication() {
        if (ViewModel.Current.dataUtils.GetSetting("rating", "non").equals("non")) {
            RateThisApp.showRateDialog(this);
        }
    }

    public void initRatingApp() {
        RateThisApp.Config config = new RateThisApp.Config(3, 5);
        config.setTitle(R.string.txtrate2);
        config.setMessage(R.string.txtrate1);
        config.setYesButtonText(R.string.txtrate5);
        config.setNoButtonText(R.string.txtrate4);
        config.setCancelButtonText(R.string.txtrate3);
        RateThisApp.setCallback(new RateThisApp.Callback() {
            @Override
            public void onYesClicked() {
                RateThisApp.stopRateDialog(StickersLabActivity.this);
                ViewModel.Current.dataUtils.SetSetting("rating",
                        "yes");
            }

            @Override
            public void onNoClicked() {
                ViewModel.Current.dataUtils.SetSetting("rating",
                        "non");
            }

            @Override
            public void onCancelClicked() {
            }
        });

        RateThisApp.init(config);
    }

    public void loadBitmapAsync(Intent data) {
        changeProgressBarVisibility(true);
        addSubscribe(
                Flowable.just(data)
                        .flatMap(myData -> Flowable.fromCallable(() -> handleResultFromChooser(myData)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bitmap -> {
                            if (bitmap != null) {
                                mImageViewGalleryImage.setVisibility(View.VISIBLE);
                                mImageViewGalleryImage.setImageBitmap(bitmap);
                                mImageViewGalleryImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                mRotateBtn.setVisibility(View.VISIBLE);
                                hideProgressLoader();
                            }
                        }));
    }

    private Bitmap handleResultFromChooser(Intent data) {
        Bitmap takenPictureData = null;
        Uri selectedImage = data.getData();
        if (selectedImage != null) {
            try {
                takenPictureData = Media.getBitmap(getContentResolver(),
                        selectedImage);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            ByteArrayOutputStream mByteArray = new ByteArrayOutputStream();
            assert takenPictureData != null;
            takenPictureData.compress(Bitmap.CompressFormat.PNG, 100, mByteArray);
        }
        return takenPictureData;
    }

    @Override
    public void onBackPressed() {
        recycleBitmap();
        if (mFragmentText != null && mFragmentText.ismState()) {
            mFragmentText.setmState(false);
            enableDisableView(true);
            mFabDefault = Constants.KEY_DEFAULT;
        }

        if (helpFragment != null && helpFragment.ismState()) {
            helpFragment.setmState(false);
            mFabDefault = Constants.KEY_DEFAULT;
            enableDisableView(true);
            helpFragment = null;
        }

        mfab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICTURE_TAKEN_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    loadBitmapAsync(data);
                }
                break;
            case SHARE_REQUEST_CODE:
                hideProgressLoader();
                this.finish();
                break;
            case PICK_USER_PROFILE_IMAGE:
                if (resultCode == RESULT_OK) {
                    mImageViewGalleryImage.setVisibility(View.VISIBLE);
                    mImageViewGalleryImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    reduceImageSizeAsync();
                }
            case 456:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    String mUrl = extras.getString("urltoopen");
                    String mType = extras.getString("type");
                    mRotateAngle = 0;
                    downloadAndPutTextureAtScreen(mUrl, mType);
                }
        }
    }

    void reduceImageSizeAsync() {
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.setReducedImageSize(imagePath))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    mImageViewGalleryImage.setImageBitmap(bitmap);
                    mRotateBtn.setVisibility(View.VISIBLE);
                })
        );
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mScaleGestureDetector.onTouchEvent(ev);
        return true;
    }

    @Override
    protected void onResume() {
        checkForCrashes();
        super.onResume();
    }

    @Override
    public void hideProgressLoader() {
        if (mProgressLoader != null)
            changeProgressBarVisibility(false);
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    public void hideTextFragment() {
        this.onBackPressed();
    }

    public void addView(myView myView) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mAllImageAtScreenList.add(myView);
        mRelativeLayoutMain.addView(myView, params);
    }

    public void addOnclickToDeleteImageView() {
        if (mCurrentPos == 3) {
            for (TextView view : mArrayListTextView) {
                view.setOnTouchListener(null);
                view.setOnClickListener(mView -> {
                    mView.setVisibility(View.GONE);
                    mRelativeLayoutMain.removeView(view);
                    mArrayListTextView.remove(view);
                });
            }
        } else {
            for (myView view : mAllImageAtScreenList) {
                view.setOnTouchListener(null);
                view.setOnClickListener(mView -> {
                    mView.setVisibility(View.GONE);
                    mRelativeLayoutMain.removeView(view);
                    mAllImageAtScreenList.remove(view);
                });
            }
        }
        //hideDeleteButton();
    }


    void hideDeleteButton() {
        if (mArrayListTextView.size() == 0 && mAllImageAtScreenList.size() == 0) {
            mIsDelete = !mIsDelete;
            enableModeMultitouchOrDelete(mIsDelete);
            setImageDeleteBackground();
            mDeleteBtn.setVisibility(View.GONE);
        }
    }

    public void addMultitouch() {
        {
            for (myView view : mAllImageAtScreenList) {
                view.setOnTouchListener(new MultiTouchListener());
            }

            for (TextView txt : mArrayListTextView) {
                txt.setOnTouchListener(new MultiTouchListener());
            }
            //hideDeleteButton();
        }
    }

    public void enableModeMultitouchOrDelete(boolean mState) {
        if (mState) {
            addOnclickToDeleteImageView();
        } else {
            addMultitouch();
        }
    }

    public Typeface getTypeFace(int mTypefacenum) {
        try {
            return Typeface.createFromAsset(getAssets(), "arabicfont" + mTypefacenum + ".ttf");
        } catch (Exception e) {
            return Typeface.createFromAsset(getAssets(), "arabicfont" + mTypefacenum + ".otf");
        } catch (ExceptionInInitializerError e) {
            return Typeface.DEFAULT;
        }
    }

    @Override
    protected void initInject() {

    }

    @Override
    public void showSnackMsg(String msg) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showADS() {

    }

    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScale *= detector.getScaleFactor();
            mScale = Math.max(0.1f, Math.min(mScale, 5.0f));
            mMatrix.setScale(mScale, mScale);
            for (int i = 0; i < mArrayListImageeView.size(); i++) {
                mArrayListImageeView.get(i).setImageMatrix(mMatrix);
            }
            return true;
        }
    }

    private final class SaveFile extends AsyncTask<String, String, String> {
        private Bitmap mBmp;

        private SaveFile(Bitmap mBmp) {
            this.mBmp = mBmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... mUrl) {
            Calendar cal = Calendar.getInstance();
            ViewModel.Current.fileUtils.saveBitmapToStorage(mBitmapMain, "islamicApp + "
                    + cal.getTimeInMillis() + "jpg", 1);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.issaved));
        }
    }
}
