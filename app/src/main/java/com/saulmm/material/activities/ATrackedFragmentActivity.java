package com.saulmm.material.activities;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;


import java.lang.ref.WeakReference;

/**
 * Base activity class with Analytics. All other activities must extend this class.
 */
public abstract class ATrackedFragmentActivity extends AppCompatActivity
{
    private FragmentManager mFragmentManager;

    private ActionBar mActionBar;



    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
    }


    @Override
    protected void onResume()
    {
        super.onResume();

    }

    @Override
    protected void onPause()
    {

        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        switch (item.getItemId())
        {
        // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public FragmentManager getSafeFragmentManager()
    {
        if (this.mFragmentManager == null)
        {
            this.mFragmentManager = this.getSupportFragmentManager();
        }
        return this.mFragmentManager;
    }

    public ActionBar getSafeActionBar()
    {
        if (this.mActionBar == null)
        {
            this.mActionBar = this.getActionBar();
        }

        return this.mActionBar;
    }

    public boolean isSafe()
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            if (this.isDestroyed())
            {
                return false;
            }
        }
        return !this.isFinishing();
    }


}
