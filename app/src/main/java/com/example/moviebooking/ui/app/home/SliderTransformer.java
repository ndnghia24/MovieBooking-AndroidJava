package com.example.moviebooking.ui.app.home;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

public class SliderTransformer implements ViewPager2.PageTransformer {

    private static final float DEFAULT_TRANSLATION_X = 0f;
    private static final float DEFAULT_TRANSLATION_FACTOR = 1.2f;
    private static final float SCALE_FACTOR = 0.14f;
    private static final float DEFAULT_SCALE = 1f;
    private static final float ALPHA_FACTOR = 0.3f;
    private static final float DEFAULT_ALPHA = 1f;

    private ViewPager2 viewPager2;

    public SliderTransformer(ViewPager2 viewPager2) {
        this.viewPager2 = viewPager2;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        ViewCompat.setElevation(page, -Math.abs(position));

        float scaleFactor = -SCALE_FACTOR * position + DEFAULT_SCALE;
        float alphaFactor = -ALPHA_FACTOR * position + DEFAULT_ALPHA;

        if (position <= 0f) {
            page.setTranslationX(DEFAULT_TRANSLATION_X);
            page.setScaleX(DEFAULT_SCALE);
            page.setScaleY(DEFAULT_SCALE);
            page.setAlpha(DEFAULT_ALPHA + position);
        } else if (position <= viewPager2.getOffscreenPageLimit() - 1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setTranslationX(-(page.getWidth() / DEFAULT_TRANSLATION_FACTOR) * position);
            page.setAlpha(alphaFactor);
        } else {
            page.setTranslationX(DEFAULT_TRANSLATION_X);
            page.setScaleX(DEFAULT_SCALE);
            page.setScaleY(DEFAULT_SCALE);
            page.setAlpha(DEFAULT_ALPHA);
        }
    }
}