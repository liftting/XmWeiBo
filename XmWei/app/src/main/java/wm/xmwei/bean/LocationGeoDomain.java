package wm.xmwei.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class LocationGeoDomain implements Parcelable {

    private String type;
    private double[] coordinates = {0.0, 0.0};

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public double getLat() {
        return coordinates[0];
    }

    public double getLon() {
        return coordinates[1];
    }

    public void setLatitude(double lat) {
        coordinates[0] = lat;
    }

    public void setLongitude(double lon) {
        coordinates[1] = lon;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeDoubleArray(coordinates);
    }

    public static final Parcelable.Creator<LocationGeoDomain> CREATOR =
            new Parcelable.Creator<LocationGeoDomain>() {
                public LocationGeoDomain createFromParcel(Parcel in) {
                    LocationGeoDomain geoBean = new LocationGeoDomain();
                    geoBean.type = in.readString();
                    geoBean.coordinates = new double[2];
                    in.readDoubleArray(geoBean.coordinates);
                    return geoBean;
                }

                public LocationGeoDomain[] newArray(int size) {
                    return new LocationGeoDomain[size];
                }
            };

}
