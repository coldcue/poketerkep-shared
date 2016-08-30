package hu.poketerkep.shared.model.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.poketerkep.shared.geo.Coordinate;

/**
 * Has a Coordinate
 */
public interface CoordinateAware {
    /**
     * Get the coordinate of the object
     *
     * @return coordinate
     */
    Coordinate getCoordinate();

    /**
     * Set the coordinate of the object
     *
     * @param coordinate the coordinate
     */
    void setCoordinate(Coordinate coordinate);

    @JsonIgnore
    default double getLongitude() {
        return getCoordinate().getLongitude();
    }

    @JsonIgnore
    default double getLatitude() {
        return getCoordinate().getLatitude();
    }
}
