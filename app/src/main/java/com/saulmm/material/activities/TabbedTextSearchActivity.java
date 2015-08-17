package com.saulmm.material.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import com.saulmm.material.R;

public class TabbedTextSearchActivity extends ATrackedFragmentActivity implements ViewPager.OnPageChangeListener,
                TextWatcher
{

    public static final String EXTRA_SEARCH_TYPE          = "addressType";
    public static final String EXTRA_REFERENCE_POINT      = "referencePoint";

    // respond with selected poi details
    public static final String EXTRA_LOCATION_JSON        = "poiAsJson";

    private static final int            DEFAULT_SEARCH_LIMIT       = 10;
    private static final int            MIN_CHARS_BEFORE_SEARCH    = 3;

    private static final String STATE_LAST_REFERENCE_POINT = "mLastReferencePoint";
    private static final String STATE_LAST_SEARCH_TYPE     = "mLastSearchType";
    private static final String STATE_LAST_SEARCH_MODE     = "mLastSearchMode";
    private static final String STATE_LAST_LIMIT           = "mLastLimit";

    // Auto-search
    private static final long           START_SEARCH_DELAY         = 750;                  // Milliseconds
    private static final int            MESSAGE_SEARCH_CHANGE      = 99;

//    private PlacesProvidersPagerAdapter mPlacesProvidersPagerAdapter;
    private EditText                    mSearchField;
    private View                        mClearButton;
    private View                        mFakeActionBar;
    private TabbedSearchViewPager       mViewPager;

    private Handler                     mSearchDelayHandler;

    private PlacesProviderEnum[]        mPlacesProviders;

    // Places Search Properties
    private int                         mLastSearchType;
    private int                         mLastSearchMode;

    private int                         mLastLimit;

    // Favorite List
    private List<String> mFavoritesUID;


    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setTheme(R.style.TabbedSearchTheme);
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        } else {
            this.setTheme(R.style.AppTheme);
        }

        // to hide the menu bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        overridePendingTransition(R.anim.addr_search_fade_in, R.anim.addr_search_fade_out);

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.text_search_view_pager);
//        ViewCompat.setTransitionName(findViewById(R.id.poi_ab_search), VIEW_NAME_ADDRESS_SELECT);
        // Load list of places provider and their names
        this.mPlacesProviders = PlacesProviderEnum.getApplicablePlaceProviders();

        if (savedInstanceState == null)
        {
            // First load settings
            this.mLastSearchType = PlacesAPIRequest.PARAM_SEARCH_TYPE_ORIGIN;
            this.mLastLimit = TabbedTextSearchActivity.DEFAULT_SEARCH_LIMIT;

            // Init search properties from intent
            final Bundle extras = this.getIntent().getExtras();
            if ((extras != null) && extras.containsKey(TabbedTextSearchActivity.EXTRA_SEARCH_TYPE))
            {
                this.mLastSearchType = extras.getInt(TabbedTextSearchActivity.EXTRA_SEARCH_TYPE);
            }

            // Search mode depends on whether we have a keyword or not at this point
            this.mLastSearchMode = PlacesAPIRequest.PARAM_SEARCH_MODE_NEARBY;
        }
        else
        {
            // Restore from bundle
            this.mLastSearchType = savedInstanceState.getInt(TabbedTextSearchActivity.STATE_LAST_SEARCH_TYPE);
            this.mLastSearchMode = savedInstanceState.getInt(TabbedTextSearchActivity.STATE_LAST_SEARCH_MODE);
            this.mLastLimit = savedInstanceState.getInt(TabbedTextSearchActivity.STATE_LAST_LIMIT);
        }

        // Update Favorite List
        updateFavoritesList();

        setupViewPager();
        // Set up the handler to handle search events
        if (this.mSearchDelayHandler == null)
        {
            this.mSearchDelayHandler = new MyHandler(this);
        }
        this.setupSearchBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Fade fadeEnterTransition = new Fade();
            fadeEnterTransition.setDuration(3000);
            fadeEnterTransition.setMode(Fade.IN);
            fadeEnterTransition.addTarget(R.id.poi_pager);
            fadeEnterTransition.addListener(new Transition.TransitionListener()
            {
                @Override
                public void onTransitionStart(Transition transition)
                {
                    Log.e("gjh","onTransitionStart");
                    Log.e("gjh","viewpager:" + mViewPager.getVisibility());
                    Log.e("gjh","poi_action_bar:" + findViewById(R.id.poi_action_bar).getVisibility());
                }

                @Override
                public void onTransitionEnd(Transition transition)
                {
                    Log.e("gjh","onTransitionEnd");
                    Log.e("gjh","viewpager visibility:" + mViewPager.getVisibility());
                    Log.e("gjh","poi_action_bar visibility:" + findViewById(R.id.poi_action_bar).getVisibility());
                }

                @Override
                public void onTransitionCancel(Transition transition)
                {
                    Log.e("gjh","onTransitionCancel");
                }

                @Override
                public void onTransitionPause(Transition transition)
                {
                    Log.e("gjh", "onTransitionPause");
                }

                @Override
                public void onTransitionResume(Transition transition)
                {
                    Log.e("gjh", "onTransitionResume");
                }
            });
            getWindow().setEnterTransition(fadeEnterTransition);
        }
    }

    private void setupViewPager()
    {
        // ViewPager and its adapters use support library fragments, so use getSupportFragmentManager.
//        mPlacesProvidersPagerAdapter = new PlacesProvidersPagerAdapter(this.getSupportFragmentManager(),
//                        mPlacesProviders, mLastSearchType, getReferencePoint());
        mViewPager = (TabbedSearchViewPager) this.findViewById(R.id.poi_pager);
//        mViewPager.setAdapter(this.mPlacesProvidersPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        // Set ViewPager to page at index 1 (the default remote search provider)
        if (mPlacesProviders.length > 1)
        {
            mViewPager.setCurrentItem(1);
        }
    }

    private void unsetViewPager()
    {
        mViewPager.setAdapter(null);
        mViewPager = null;
    }
    @Override
    public void onSaveInstanceState(final Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(TabbedTextSearchActivity.STATE_LAST_SEARCH_TYPE, this.mLastSearchType);
        savedInstanceState.putInt(TabbedTextSearchActivity.STATE_LAST_SEARCH_MODE, this.mLastSearchMode);
        savedInstanceState.putInt(TabbedTextSearchActivity.STATE_LAST_LIMIT, this.mLastLimit);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

//        this.getContentResolver().registerContentObserver(PointOfInterestContentProvider.getContentURI(), true,
//                        this.poiObserver);

        //this.triggerSearch();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        this.getContentResolver().unregisterContentObserver(this.poiObserver);
    }

    private void setupSearchBar()
    {
        mFakeActionBar = this.findViewById(R.id.poi_action_bar);
        mSearchField = (EditText) this.findViewById(R.id.poi_ab_search);
        mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                {
                    TabbedTextSearchActivity.this.onClickSearch();

                    return true;
                }
                return false;
            }
        });
        mSearchField.setCompoundDrawablesWithIntrinsicBounds(
                        mLastSearchType == PlacesAPIRequest.PARAM_SEARCH_TYPE_ORIGIN ? R.drawable.ic_poi_search_pickup
                                        : R.drawable.ic_poi_search_dropoff, 0, 0, 0);
        mSearchField.addTextChangedListener(this);

        // Add button clear for edittext
        mClearButton = this.findViewById(R.id.poi_ab_clear);
        mClearButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(final View v)
            {
                TabbedTextSearchActivity.this.mSearchField.getEditableText().clear();
            }
        });
    }

    private void unsetSearchBar()
    {
        mSearchField.setOnEditorActionListener(null);
        mSearchField.removeTextChangedListener(this);
        mClearButton.setOnClickListener(null);
        mSearchField = null;
        mClearButton = null;
    }

    public void onClickSearch()
    {
        mLastSearchMode = PlacesAPIRequest.PARAM_SEARCH_MODE_SEARCH;

        // Get current position
        final int currPosition = mViewPager.getCurrentItem();
//        mPlacesProvidersPagerAdapter.doSearch(this.getPlacesAPIRequest(), mFavoritesUID, currPosition);
    }


    @Override
    public void onPageScrollStateChanged(final int state)
    {
        if (state == ViewPager.SCROLL_STATE_IDLE)
        {
            final int position = mViewPager.getCurrentItem();

            // Enable/Disable Bezel (Edge) swiping
//            mViewPager.setSwipeDisabled(!mPlacesProvidersPagerAdapter.getBezelSwipeEnabled(position));

            // Dismiss the keyboard and hide the actionbar
            if (mPlacesProviders[position] == PlacesProviderEnum.MAP)
            {
//                KeyboardUtils.hideKeyboard(this);
            }
            this.mFakeActionBar.setVisibility(mPlacesProviders[position] == PlacesProviderEnum.MAP ? View.GONE
                            : View.VISIBLE);
        }
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels)
    {
        // Do nothing
    }

    /*
     * Start search after a delay to avoid spamming
     */
    @Override
    public void onPageSelected(final int position)
    {
        this.triggerSearch();
    }

    private void triggerSearch()
    {
        if (mSearchDelayHandler != null)
        {
            mSearchDelayHandler.removeMessages(TabbedTextSearchActivity.MESSAGE_SEARCH_CHANGE);
            final Message message = Message.obtain(mSearchDelayHandler, TabbedTextSearchActivity.MESSAGE_SEARCH_CHANGE);
            mSearchDelayHandler.sendMessageDelayed(message, TabbedTextSearchActivity.START_SEARCH_DELAY);
        }
    }

    private void updateFavoritesList()
    {
        TabbedTextSearchActivity.this.mFavoritesUID = new ArrayList<String>();

//        final List<PointOfInterest> favoriteList = PointOfInterestDAO.getInstance().loadFavorites();
//        for (final PointOfInterest favorite : favoriteList)
//        {
//            final String uid = favorite.getUid();
//            if (!TextUtils.isEmpty(uid))
//            {
//                TabbedTextSearchActivity.this.mFavoritesUID.add(uid);
//            }
//        }
    }

    @Override
    protected void onDestroy()
    {
        if (mSearchDelayHandler != null)
        {
            mSearchDelayHandler.removeMessages(TabbedTextSearchActivity.MESSAGE_SEARCH_CHANGE);
            mSearchDelayHandler = null;
        }
        unsetSearchBar();
//        unsetBottomTabStrip();
        unsetViewPager();

        super.onDestroy();
    }

    /**
     * @param view
     */
    public void onClickGoBack(final View view)
    {
        this.setResult(Activity.RESULT_CANCELED, new Intent());
        this.finish();
    }

    public String getKeyword()
    {
        return mSearchField.getText().toString();
    }

    @Override
    public void afterTextChanged(final Editable s)
    {
        // Don't care
    }

    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after)
    {
        // Don't care
    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count)
    {
        // Pass delayed message to handler to handle change
        this.triggerSearch();
        this.mClearButton.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
    }

    @SuppressLint("NewApi")
    private final ContentObserver poiObserver = new ContentObserver(new Handler(Looper.getMainLooper()))
                                              {
                                                  @Override
                                                  public void onChange(final boolean selfChange, final Uri uri)
                                                  {
                                                      super.onChange(selfChange, uri);
                                                  }

                                                  @Override
                                                  public void onChange(final boolean selfChange)
                                                  {
                                                      TabbedTextSearchActivity.this.updateFavoritesList();
                                                  }
                                              };

    /**********************
     *     MyHandler
     **********************/
    private static final class MyHandler extends Handler
    {
        private final WeakReference<TabbedTextSearchActivity> mActivityRef;

        protected MyHandler(TabbedTextSearchActivity activity)
        {
            this.mActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message)
        {
            if (message.what == TabbedTextSearchActivity.MESSAGE_SEARCH_CHANGE)
            {
                final TabbedTextSearchActivity activity = mActivityRef.get();
                if (activity == null)
                {
                    return;
                }

                final String searchString = activity.mSearchField.getText().toString();

                if (searchString != null)
                {
                    if ((searchString.length() >= TabbedTextSearchActivity.MIN_CHARS_BEFORE_SEARCH)
                                    || searchString.length() == 0)
                    {
                        // Send search
                        activity.onClickSearch();
                    }
                }
            }
        }
    }
}
