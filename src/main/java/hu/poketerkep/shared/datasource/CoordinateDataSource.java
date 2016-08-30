package hu.poketerkep.shared.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.helpers.CoordinateAware;
import hu.poketerkep.shared.validator.ValidationException;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashSet;

/**
 * This DataSource's objects have coordinates
 */
abstract class CoordinateDataSource<T extends CoordinateAware> {
    private final String KEY_NAME;
    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;
    private final Class<T> type;

    CoordinateDataSource(Class<T> type, String key_name, JedisPool jedisPool) {
        this.KEY_NAME = key_name;
        this.jedisPool = jedisPool;
        this.type = type;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Get all pokemons
     *
     * @return all pokemons
     */
    public HashSet<T> getAll() {
        HashSet<T> result = new HashSet<>();

        Jedis jedis = jedisPool.getResource();

        // http://stackoverflow.com/questions/11504154/get-all-members-in-sorted-set
        jedis.zrange(KEY_NAME, 0, -1).stream()
                .map(this::convertFromJSON)
                .forEach(result::add);

        jedis.close();

        return result;
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
        HashSet<T> result = new HashSet<>();

        Jedis jedis = jedisPool.getResource();

        // http://stackoverflow.com/questions/11504154/get-all-members-in-sorted-set
        jedis.georadius(KEY_NAME, coordinate.getLongitude(), coordinate.getLatitude(), radius, GeoUnit.KM).stream()
                .map(GeoRadiusResponse::getMemberByString)
                .map(this::convertFromJSON)
                .forEach(result::add);

        jedis.close();

        return result;
    }


    /**
     * Add a T to the database
     *
     * @param obj the T object
     */
    public void add(T obj) throws ValidationException {
        String json = convertToJSON(obj);
        Jedis jedis = jedisPool.getResource();
        jedis.geoadd(KEY_NAME, obj.getLongitude(), obj.getLatitude(), json);
        jedis.close();
    }

    /**
     * Remove a T from the database
     *
     * @param obj T object
     */
    public void remove(T obj) {
        String json = convertToJSON(obj);
        Jedis jedis = jedisPool.getResource();
        jedis.zrem(KEY_NAME, json);
        jedis.close();
    }

    /**
     * Read a T from JSON
     *
     * @param json the JSON string
     * @return a T
     */
    private T convertFromJSON(String json) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
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
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
