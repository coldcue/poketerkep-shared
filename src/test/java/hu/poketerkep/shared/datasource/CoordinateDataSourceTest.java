package hu.poketerkep.shared.datasource;


import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.Pokemon;
import hu.poketerkep.shared.model.helpers.CoordinateAware;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CoordinateDataSourceTest {


    @Test(expected = InvocationTargetException.class)
    public void convertFromJSONShouldThrowException() throws Exception {
        Class<CoordinateDataSource> clazz = CoordinateDataSource.class;
        Method method = clazz.getDeclaredMethod("convertFromJSON", String.class);
        method.setAccessible(true);
        method.invoke(new EmptyCoordinateDataSource(), "{\"asd\":true}");
    }

    @Test(expected = InvocationTargetException.class)
    public void convertToJSONShouldThrowException() throws Exception {
        Class<CoordinateDataSource> clazz = CoordinateDataSource.class;

        Method method = clazz.getDeclaredMethod("convertToJSON", CoordinateAware.class);
        method.setAccessible(true);

        //Try to convert an other CoordinateAware class
        Object invoke = method.invoke(new EmptyCoordinateDataSource(), new EmptyCoordinateAware());
        System.out.println(invoke);
    }


    private class EmptyCoordinateDataSource extends CoordinateDataSource<Pokemon> {
        EmptyCoordinateDataSource() {
            super(Pokemon.class, "asd", null);
        }
    }

    private class EmptyCoordinateAware implements CoordinateAware {

        @Override
        public Coordinate getCoordinate() {
            return null;
        }

        @Override
        public void setCoordinate(Coordinate coordinate) {

        }
    }

}