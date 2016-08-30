package hu.poketerkep.shared.model;


import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.helpers.CoordinateAware;
import hu.poketerkep.shared.model.helpers.LastUsed;

public class LocationConfig implements LastUsed, CoordinateAware {
    private String locationId;
    private Coordinate coordinate;
    private int steps;
    private long lastUsed;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Override
    public long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    @Override
    public String toString() {
        return "LocationConfig{" +
                "locationId='" + locationId + '\'' +
                ", latitude=" + coordinate.getLatitude() +
                ", longitude=" + coordinate.getLongitude() +
                ", steps=" + steps +
                ", lastUsed=" + lastUsed +
                '}';
    }

}
