package com.example.locoassignment.utils;

import android.animation.Animator;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewAnimationUtils;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    /**
     * Calculates the dimensions required for cropped video view.
     */
    public static int[] getVideoDimensions(MediaPlayer player, int width, int height) {
        int layoutWidth = width;
        int layoutHeight = height;

        float mediaWidth = player.getVideoWidth();
        float mediaHeight = player.getVideoHeight();

        float ratioWidth = layoutWidth / mediaWidth;
        float ratioHeight = layoutHeight / mediaHeight;
        float aspectRatio = mediaWidth / mediaHeight;

        if (ratioWidth > ratioHeight) {
            layoutWidth = (int) (layoutHeight * aspectRatio);
        } else {
            layoutHeight = (int) (layoutWidth / aspectRatio);
        }

        return new int[]{layoutWidth, layoutHeight};

    }

    /***
     * Generate Circular Ripple Animation
     * @param view on which we have to apply the animation
     */

    public static void setRippleAnimation(View view) {
        int x = view.getWidth() / 2;
        int y = view.getHeight() / 2;
        int finalRadius = (int) Math.hypot(view.getWidth(), view.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(view, x, y, 0, finalRadius);
        animation.setDuration(500);
        animation.start();
    }

    /***
     * Random Number generator between 300 to 500
     * @param context
     * @return
     */
    public static int getRandomHeightForCard(Context context) {
        int cardHeight = ThreadLocalRandom.current().nextInt(300, 500);
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (cardHeight * scale + 0.5f);
    }
}
