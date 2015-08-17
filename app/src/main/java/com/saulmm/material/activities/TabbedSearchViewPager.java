package com.saulmm.material.activities;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.MapView;

public class TabbedSearchViewPager extends ViewPager
{
    private boolean swipeDisabled = false;

    public TabbedSearchViewPager(final Context context)
    {
        super(context);
    }

    public TabbedSearchViewPager(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(final View v, final boolean checkV, final int dx, final int x, final int y)
    {
        if (v instanceof MapView)
        {
            this.setSwipeDisabled(true);
            return true;
        }

        this.setSwipeDisabled(false);

        return super.canScroll(v, checkV, dx, x, y);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
        final int LEFT_OR_RIGHT_EDGE = MotionEvent.EDGE_LEFT | MotionEvent.EDGE_RIGHT;

        if (this.isSwipeDisabled() && (event.getEdgeFlags() & LEFT_OR_RIGHT_EDGE) != 0)
        {
            return false;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event)
    {
        final int LEFT_OR_RIGHT_EDGE = MotionEvent.EDGE_LEFT | MotionEvent.EDGE_RIGHT;

        if (this.isSwipeDisabled() && (event.getEdgeFlags() & LEFT_OR_RIGHT_EDGE) != 0)
        {
            return false;
        }

        return super.onInterceptTouchEvent(event);
    }

    public boolean isSwipeDisabled()
    {
        return this.swipeDisabled;
    }

    public void setSwipeDisabled(final boolean swipeDisabled)
    {
        this.swipeDisabled = swipeDisabled;
    }
}