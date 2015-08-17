package com.saulmm.material.activities;


import com.saulmm.material.R;

public enum PlacesProviderEnum
{
    // The list of Places providers
    FREQUENT("FREQUENT", null, R.string.places_provider_frequent, R.drawable.ic_poi_tab_fav),
    POI_SERVICE("POI_SERVICE", PlacesAPIType.GRABTAXI, R.string.places_provider_poi_service, R.drawable.ic_poi_tab_myteksi),
    FOURSQUARE("FOURSQUARE", PlacesAPIType.FOURSQUARE, R.string.places_provider_foursquare, R.drawable.ic_poi_tab_foursquare),
    GOOGLE("GOOGLE", PlacesAPIType.GOOGLE, R.string.places_provider_google, R.drawable.ic_poi_tab_google),
    MAP("MAP", PlacesAPIType.GRABTAXI, R.string.places_provider_map, R.drawable.ic_poi_tab_map);

    private final String        mFriendlyName;
    private final PlacesAPIType mPlacesAPIType;
    private final int           mNameResId;
    private final int           mIconResId;

    PlacesProviderEnum(String friendlyName, PlacesAPIType placesAPIType, int nameResId, int iconResId)
    {
        this.mFriendlyName = friendlyName;
        this.mPlacesAPIType = placesAPIType;
        this.mNameResId = nameResId;
        this.mIconResId = iconResId;
    }

    public static PlacesProviderEnum getByName(final String placeName)
    {
        for (final PlacesProviderEnum value : PlacesProviderEnum.values())
        {
            if (value.getFriendlyName().equals(placeName))
            {
                return value;
            }
        }

        /** If you are going to fail, do it fast */
        throw new IllegalAccessError("There is no place with name " + placeName);
    }

    public final String getFriendlyName()
    {
        return this.mFriendlyName;
    }

    public final PlacesAPIType getPlacesAPIType()
    {
        return this.mPlacesAPIType;
    }

    public final int getIconResId()
    {
        return mIconResId;
    }

    /** Was used for previous version, in the new version, we use images on the provider tabs */
    @Deprecated
    @SuppressWarnings("unused")
    public int getNameResId()
    {
        return this.mNameResId;
    }

    public static PlacesProviderEnum[] getApplicablePlaceProviders()
    {
        return new PlacesProviderEnum[]
        { PlacesProviderEnum.FREQUENT, PlacesProviderEnum.POI_SERVICE, PlacesProviderEnum.FOURSQUARE,
                        PlacesProviderEnum.GOOGLE, PlacesProviderEnum.MAP };
    }
}