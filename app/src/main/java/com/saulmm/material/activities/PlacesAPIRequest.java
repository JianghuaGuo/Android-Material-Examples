package com.saulmm.material.activities;


public class PlacesAPIRequest
{
    // Parameters
    public static final int PARAM_SEARCH_TYPE_ORIGIN      = 1;
    public static final int PARAM_SEARCH_TYPE_DESTINATION = 2;

    public static final int PARAM_SEARCH_MODE_SEARCH      = 1;
    public static final int PARAM_SEARCH_MODE_NEARBY      = 2;
    public static final int PARAM_SEARCH_MODE_REVERSE_GEO = 3;

    public static final int PARAM_SORT_BY_NONE            = 0;
    public static final int PARAM_SORT_BY_DISTANCE        = 1;
    public static final int PARAM_SORT_BY_BOTH            = 2;

    private String          keyword;
    private double          refLatitude;
    private double          refLongitude;
    private int             searchType;
    private int             searchMode;
    private int             resultLimit;
    private int             sortBy;
    private String          language;

    public PlacesAPIRequest()
    {
        super();
    }

    protected PlacesAPIRequest(final Builder builder)
    {
        this.keyword = builder.keyword;
        this.refLatitude = builder.refLatitude;
        this.refLongitude = builder.refLongitude;
        this.searchType = builder.searchType;
        this.searchMode = builder.searchMode;
        this.resultLimit = builder.resultLimit;
        this.sortBy = builder.sortBy;
        this.language = builder.language;
    }

    public static String toJsonString(final PlacesAPIRequest placesSearchRequest)
    {
//        if (placesSearchRequest == null)
//        {
//            return null;
//        }
//
//        return GsonUtils.getGson().toJson(placesSearchRequest, PlacesAPIRequest.class);
        return null;
    }

    public static PlacesAPIRequest fromJsonString(final String json)
    {
//        try
//        {
//            return GsonUtils.getGson().fromJson(json, PlacesAPIRequest.class);
//        }
//        catch (final JsonSyntaxException e)
//        {
//            return null;
//        }
        return null;
    }

    @Override
    public String toString()
    {
        return PlacesAPIRequest.toJsonString(this);
    }

    public static final class Builder
    {
        String       keyword;
        final double refLatitude;
        final double refLongitude;
        int          searchType;
        int          searchMode;
        int          resultLimit;
        int          sortBy;
        String       language;

        public Builder(final double refLatitude, final double refLongitude)
        {
            this.refLatitude = refLatitude;
            this.refLongitude = refLongitude;
        }

        public Builder keyword(final String keyword)
        {
            this.keyword = keyword;
            return this;
        }

        public Builder searchType(final int searchType)
        {
            this.searchType = searchType;
            return this;
        }

        public Builder searchMode(final int searchMode)
        {
            this.searchMode = searchMode;
            return this;
        }

        public Builder resultLimit(final int resultLimit)
        {
            this.resultLimit = resultLimit;
            return this;
        }

        public Builder sortBy(final int sortBy)
        {
            this.sortBy = sortBy;
            return this;
        }

        public Builder language(final String language)
        {
            this.language = language;
            return this;
        }

        public PlacesAPIRequest build()
        {
            return new PlacesAPIRequest(this);
        }
    }

    public String getKeyword()
    {
        return this.keyword;
    }

    public double getRefLatitude()
    {
        return this.refLatitude;
    }

    public double getRefLongitude()
    {
        return this.refLongitude;
    }

    public int getSearchType()
    {
        return this.searchType;
    }

    public int getSearchMode()
    {
        return this.searchMode;
    }

    public int getResultLimit()
    {
        return this.resultLimit;
    }

    public int getSortBy()
    {
        return this.sortBy;
    }

    public String getLanguage()
    {
        return this.language;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.keyword == null) ? 0 : this.keyword.hashCode());
        result = prime * result + ((this.language == null) ? 0 : this.language.hashCode());
        long temp;
        temp = Double.doubleToLongBits(this.refLatitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.refLongitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + this.resultLimit;
        result = prime * result + this.searchMode;
        result = prime * result + this.searchType;
        result = prime * result + this.sortBy;
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final PlacesAPIRequest other = (PlacesAPIRequest) obj;
        if (this.keyword == null)
        {
            if (other.keyword != null)
                return false;
        }
        else if (!this.keyword.equals(other.keyword))
            return false;
        if (this.language == null)
        {
            if (other.language != null)
                return false;
        }
        else if (!this.language.equals(other.language))
            return false;
        if (Double.doubleToLongBits(this.refLatitude) != Double.doubleToLongBits(other.refLatitude))
            return false;
        if (Double.doubleToLongBits(this.refLongitude) != Double.doubleToLongBits(other.refLongitude))
            return false;
        if (this.resultLimit != other.resultLimit)
            return false;
        if (this.searchMode != other.searchMode)
            return false;
        if (this.searchType != other.searchType)
            return false;
        if (this.sortBy != other.sortBy)
            return false;
        return true;
    }

}
