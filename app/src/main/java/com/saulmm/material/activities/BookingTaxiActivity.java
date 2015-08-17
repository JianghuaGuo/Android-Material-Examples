package com.saulmm.material.activities;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpStatus;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.saulmm.material.R;

public class BookingTaxiActivity extends ATrackedFragmentActivity
{

    private static final String TAG                       = BookingTaxiActivity.class.getSimpleName();

    private static final int     DEFAULT_PAX_RADIUS        = 50;
    private static final int     RES_CODE_TAXI_INFO        = 101;

    private static final float   DEFAULT_ZOOM              = 14f;


    private TextView mFrom;
    private TextView mTo;
    private TextView mFromLabel;
    private TextView mToLabel;

    public static void start(Activity activity)
    {
        activity.startActivity(getStartIntent(activity));
    }

    public static Intent getStartIntent(Context context)
    {
        return new Intent(context, BookingTaxiActivity.class);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            this.setTheme(R.style.BookingTaxiTheme);
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        } else {
            this.setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_booking_taxi);

        this.mFrom = (TextView) findViewById(R.id.booking_pick_up_text);
        this.mFromLabel = (TextView) findViewById(R.id.booking_pick_up_label);
        this.mTo = (TextView) findViewById(R.id.booking_drop_off_text);
        this.mToLabel = (TextView) findViewById(R.id.booking_drop_off_label);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {
//            Slide slideExitTransition = new Slide(Gravity.TOP);
//            getWindow().setExitTransition(slideExitTransition);
//        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && this.isSafe())
        {
//            int widthFrom = this.mFromLabel.getWidth();
//            int widthTo = this.mToLabel.getWidth();
//            if (widthFrom > widthTo)
//            {
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.mToLabel.getLayoutParams();
//                lp.width = widthFrom;
//            }
//            else
//            {
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.mFromLabel.getLayoutParams();
//                lp.width = widthTo;
//            }
//            this.mTo.setVisibility(View.VISIBLE);
//            this.mFrom.setVisibility(View.VISIBLE);

        }
    }

    /**
     * Callback method to process the response from poi search
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
    }

    /**
     * Views have this function set in android:onClick in the layout xml
     */
    public void onClick(final View view)
    {
        if (!this.isSafe())
        {
            return;
        }

        final int id = view.getId();
        if (id == R.id.booking_pick_up_layout)
        {
            this.searchForPickUp();
        }
        else if (id == R.id.booking_drop_off_layout)
        {
            this.searchForDropOff();
        }
    }

    private void searchForPickUp()
    {
        final Intent intentFrom = new Intent(this, TabbedTextSearchActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(BookingTaxiActivity.this, Pair.create(findViewById(R.id.booking_pick_up_layout), getString(R.string.booking_shared_elements_transition_name)));
            startActivityForResult(intentFrom, 1, options.toBundle());
        } else
        {
            BookingTaxiActivity.this.startActivityForResult(intentFrom, 1);
        }

    }

    private void searchForDropOff()
    {
        final Intent intentTo = new Intent(this, TabbedTextSearchActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(BookingTaxiActivity.this, Pair.create(findViewById(R.id.booking_drop_off_layout), getString(R.string.booking_shared_elements_transition_name)));
            startActivityForResult(intentTo, 2, options.toBundle());
        } else
        {
            BookingTaxiActivity.this.startActivityForResult(intentTo, 2);
        }
    }


}
