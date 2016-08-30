package hu.poketerkep.shared.geo;

import org.junit.Assert;
import org.junit.Test;

public class CoordinateTest {

    @Test
    public void fromDegrees() throws Exception {
        Coordinate coordinate = Coordinate.fromDegrees(15, 20);

        Assert.assertEquals(15, coordinate.getLatitude(), 0.1);
        Assert.assertEquals(20, coordinate.getLongitude(), 0.1);
    }


    @Test
    public void getNew() throws Exception {
        Coordinate expected = Coordinate.fromDegrees(15, 20);

        Coordinate a = expected.getNew(1.234, 90);
        Coordinate actual = expected.getNew(1.234, 270);

        //Latitude
        Assert.assertEquals(expected.getLatitude(), actual.getLatitude(), 0.1);

        //Longitude
        Assert.assertEquals(expected.getLongitude(), actual.getLongitude(), 0.1);
    }

    @Test
    public void getDistance() throws Exception {
        Coordinate a = Coordinate.fromDegrees(47.0, 19.0);
        Coordinate b = Coordinate.fromDegrees(47.0001, 19.0001);

        double expected = .013;

        Assert.assertEquals(expected, a.getDistance(b), .001);
        Assert.assertEquals(expected, b.getDistance(a), .001);
    }

    @Test
    public void testToString() throws Exception {
        Coordinate a = Coordinate.fromDegrees(47.0, 19.0);
        Assert.assertEquals("[47.0,19.0]", a.toString());
    }

}