package hu.poketerkep.shared.geo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Coordinate implements Serializable {
    private static final double R = 6378.1;
    private final double latitude;
    private final double longitude;

    @JsonCreator
    private Coordinate(@JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Coordinate fromDegrees(double latitudeDegrees, double longitudeDegrees) {
        return new Coordinate(latitudeDegrees, longitudeDegrees);
    }

    private static Coordinate fromRadians(double latitudeRadians, double longitudeRadians) {
        return new Coordinate(Math.toDegrees(latitudeRadians), Math.toDegrees(longitudeRadians));
    }

    /**
     * Get a new Coordinate which in a distance and bearing
     *
     * @param distance      distance
     * @param bearingDegree bearing
     * @return a new coordinate
     */
    public Coordinate getNew(double distance, double bearingDegree) {
        final double bearing = Math.toRadians(bearingDegree);

        double newLatitude = Math.asin(
                Math.sin(this.getLatitudeRadians()) * Math.cos(distance / R) +
                        Math.cos(this.getLatitudeRadians()) * Math.sin(distance / R) * Math.cos(bearing));

        double newLongitude = this.getLongitudeRadians() + Math.atan2(
                Math.sin(bearing) * Math.sin(distance / R) * Math.cos(this.getLatitudeRadians()),
                Math.cos(distance / R) - Math.sin(this.getLatitudeRadians()) * Math.sin(newLatitude));

        return Coordinate.fromRadians(newLatitude, newLongitude);
    }

    /**
     * Get the Distance between two coordinates
     *
     * @param coordinate other coordinate
     * @return distance in km
     */
    public double getDistance(Coordinate coordinate) {
        return Haversine.calc(this.latitude, this.longitude, coordinate.latitude, coordinate.longitude);
    }


    @JsonIgnore
    private double getLatitudeRadians() {
        return Math.toRadians(latitude);
    }

    @JsonIgnore
    private double getLongitudeRadians() {
        return Math.toRadians(longitude);
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }


    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0########", DecimalFormatSymbols.getInstance());
        return "[" + decimalFormat.format(latitude) + "," + decimalFormat.format(longitude) + "]";
    }
}
