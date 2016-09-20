package hu.poketerkep.shared.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.helpers.CoordinateAware;
import hu.poketerkep.shared.validator.ValidationException;
import redis.clients.jedis.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This DataSource's objects have coordinates
 */
abstract class CoordinateDataSource<T extends CoordinateAware> {
    private final String KEY_NAME;
    private final JedisPool jedisPool;
    private final Class<T> type;

    CoordinateDataSource(Class<T> type, String key_name, JedisPool jedisPool) {
        this.KEY_NAME = key_name;
        this.jedisPool = jedisPool;
        this.type = type;
    }

    /**
     * Get all pokemons
     *
     * @return all pokemons
     */
    public HashSet<T> getAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            HashSet<T> result = new HashSet<>();

            // http://stackoverflow.com/questions/11504154/get-all-members-in-sorted-set
            jedis.zrange(KEY_NAME, 0, -1).stream()
                    .map(this::convertFromJSON)
                    .forEach(result::add);

            return result;
        }
    }

    /**
     * Get pokemons in given location and radius
     * http://redis.io/commands/georadius
     *
     * @param coordinate center location
     * @param radius     maximum distance from center in km
     * @return the pokemons in the given radius
     */
    public HashSet<T> getWithinRadius(Coordinate coordinate, double radius) {
        try (Jedis jedis = jedisPool.getResource()) {
            HashSet<T> result = new HashSet<>();

            jedis.georadius(KEY_NAME, coordinate.getLongitude(), coordinate.getLatitude(), radius, GeoUnit.KM).stream()
                    .map(GeoRadiusResponse::getMemberByString)
                    .map(this::convertFromJSON)
                    .forEach(result::add);
            return result;
        }
    }


    /**
     * Add a T to the database
     *
     * @param obj the T object
     */
    public void add(T obj) throws ValidationException {
        String json = convertToJSON(obj);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.geoadd(KEY_NAME, obj.getLongitude(), obj.getLatitude(), json);
        }
    }

    /**
     * Add all T to the database
     *
     * @param objs T objects
     * @throws ValidationException
     */
    public void addAll(Collection<T> objs) throws ValidationException {
        HashMap<String, GeoCoordinate> memberCoordinateMap = new HashMap<>();

        for (T obj : objs) {
            memberCoordinateMap.put(convertToJSON(obj), new GeoCoordinate(obj.getLongitude(), obj.getLatitude()));
        }

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.geoadd(KEY_NAME, memberCoordinateMap);
        }
    }

    /**
     * Remove a T from the database
     *
     * @param obj T object
     */
    public void remove(T obj) {
        String json = convertToJSON(obj);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.zrem(KEY_NAME, json);
        }
    }

    /**
     * Read a T from JSON
     *
     * @param json the JSON string
     * @return a T
     */
    private T convertFromJSON(String json) {
        try {
            return new ObjectMapper().readValue(json, type);
        } catch (Exception e) {
            // There should be no problem with this
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a T to JSON
     *
     * @param obj the T object
     * @return json string
     */
    private String convertToJSON(T obj) {
        try {
            if (!type.equals(obj.getClass())) {
                throw new RuntimeException("cannot convert different types");
            }
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
