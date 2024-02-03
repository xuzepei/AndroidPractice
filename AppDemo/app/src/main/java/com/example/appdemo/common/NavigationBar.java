package com.example.appdemo.common;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.appdemo.R;

public class NavigationBar extends Toolbar {

    ClickCallback mCallback = null;

    public NavigationBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(ClickCallback callback) {
        // Customize your toolbar properties here

        mCallback = callback;

        // Add more customization as needed
        Button leftBtn = (Button)findViewById(R.id.left_button);
        if(leftBtn != null) {
            leftBtn.setOnClickListener(view -> {
                onClickLeftButton(view);
            });
        }

        Button rightBtn = (Button)findViewById(R.id.right_button);
        if(rightBtn != null) {
            rightBtn.setOnClickListener(view -> {
                onClickRightButton(view);
            });
        }
    }

    public void showButtonById(int id) {
        Button btn = (Button)findViewById(id);
        if(btn != null) {
            btn.setVisibility(View.VISIBLE);
        }
    }

    public void hideButtonById(int id) {
        Button btn = (Button)findViewById(id);
        if(btn != null) {
            btn.setVisibility(View.GONE);
        }
    }

    public void onClickLeftButton(View view) {
        if(mCallback != null) {
            mCallback.onClickLeftButton(view);
        }
    }

    public void onClickRightButton(View view) {
        if(mCallback != null) {
            mCallback.onClickRightButton(view);
        }
    }

    public void setTitle(String title, @ColorInt int color) {
        TextView tv = (TextView)findViewById(R.id.title);
        if(tv != null) {
            tv.setText(title);
            tv.setTextColor(color);
        }
    }

    public void setLeftButtonDrawable(@NonNull Context context, @DrawableRes int drawableId, @ColorInt int color) {
        Button leftBtn = (Button)findViewById(R.id.left_button);
        if(leftBtn != null) {
            // Get the drawable from the Button
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            leftBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
    }

    public void setRightButtonDrawable(@NonNull Context context, @DrawableRes int drawableId, @ColorInt int color) {
        Button rightBtn = (Button)findViewById(R.id.right_button);
        if(rightBtn != null) {
            // Get the drawable from the Button
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            rightBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
    }

    public void setLeftButtonTintColor(@NonNull Context context, @ColorRes int id) {
        Button backBtn = (Button)findViewById(R.id.left_button);
        if(backBtn != null) {
            // Get the drawable from the Button
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.back_arrow);

            // Set the tint color using DrawableCompat
            int tintColor = ContextCompat.getColor(context, id);
            drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);

            // Set the modified drawable back to the Button
            backBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
    }
}
