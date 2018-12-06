package com.example.locoassignment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.locoassignment.utils.Utils;
import com.example.locoassignment.views.CustomVideoView;
import com.example.locoassignment.views.TransparentCardView;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, TransparentCardView.OnLayoutListener, MediaPlayer.OnCompletionListener {

    private final String TAG = getClass().getSimpleName();
    int length = 0;
    private CustomVideoView customVideoView;
    private TransparentCardView transparentCardView;
    private MediaPlayer mediaPlayer;

    private int fullWidth;
    private int fullHeight;
    private Handler handler = new Handler();
    private boolean isCircle;
    private int cropCenterX;
    private int cropCenterY;
    private int cropRadius;
    private int croppedWidth;
    private int croppedHeight;
    private RelativeLayout relativeLayout;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isCircle = !isCircle;
            transparentCardView.setVisibility(isCircle ? View.VISIBLE : View.INVISIBLE);

            Utils.setRippleAnimation(relativeLayout);
            setVideoView();
            transparentCardView.setCardHeight(Utils.getRandomHeightForCard(MainActivity.this));

            handler.postDelayed(runnable, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initParameters();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (handler != null)
            handler.postDelayed(runnable, 5000);
        if (mediaPlayer != null) {
            if (length > 0)
                mediaPlayer.seekTo(length);
            mediaPlayer.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            length = mediaPlayer.getCurrentPosition();
        }
    }

    /***
     * Initialise the views which is used.
     */
    private void initView() {
        customVideoView = findViewById(R.id.video_view);
        transparentCardView = findViewById(R.id.transferent_view);
        relativeLayout = findViewById(R.id.activity_main_relative_layout);
        transparentCardView.setOnLayoutListener(this);
    }

    /**
     * Parameters Initialisation for further use.
     */
    private void initParameters() {
        handler = new Handler();
        transparentCardView.setVisibility(View.INVISIBLE);
        transparentCardView.setOnLayoutListener(this);
        SurfaceHolder holder = customVideoView.getHolder();
        holder.addCallback(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.play);
        mediaPlayer.setOnCompletionListener(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dimenFull[] = Utils.getVideoDimensions(mediaPlayer, displayMetrics.widthPixels, displayMetrics.heightPixels);
        fullWidth = dimenFull[0];
        fullHeight = dimenFull[1];

        setVideoView();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * Video dimensions changed for specific view.
     */

    private void setVideoView() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) customVideoView.getLayoutParams();

        if (isCircle) {
            layoutParams.setMargins(0, transparentCardView.getVideoMargin(), 0, 0);
            layoutParams.width = croppedWidth;
            layoutParams.height = croppedHeight;
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else {
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.width = fullWidth;
            layoutParams.height = fullHeight;
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        }

        customVideoView.cropCircle(cropCenterX, cropCenterY, cropRadius);
        customVideoView.setCircular(isCircle);
        customVideoView.setLayoutParams(layoutParams);
    }


    @Override
    public void onLayoutCreated() {
        int dimen[] = Utils.getVideoDimensions(mediaPlayer, transparentCardView.getVideoWidth(), transparentCardView.getVideoWidth());
        croppedWidth = dimen[0];
        croppedHeight = dimen[1];
        cropRadius = croppedWidth / 2;
        cropCenterX = cropRadius;
        cropCenterY = cropRadius;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        handler.removeCallbacksAndMessages(null);
    }
}
