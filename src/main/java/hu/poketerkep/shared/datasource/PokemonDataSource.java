package hu.poketerkep.shared.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.Pokemon;
import hu.poketerkep.shared.validator.PokemonValidationException;
import hu.poketerkep.shared.validator.PokemonValidator;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;

@SuppressWarnings("WeakerAccess")
public class PokemonDataSource {
    static final String POKEMONS = "pokemons";
    private final Jedis jedis;
    private final ObjectMapper objectMapper;

    public PokemonDataSource(Jedis jedis) {
        this.jedis = jedis;
        objectMapper = new ObjectMapper();
    }

    /**
     * Get all pokemons
     *
     * @return all pokemons
     */
    public HashSet<Pokemon> getAll() {
        HashSet<Pokemon> result = new HashSet<>();

        // http://stackoverflow.com/questions/11504154/get-all-members-in-sorted-set
        jedis.zrange(POKEMONS, 0, -1).stream()
                .map(this::convertFromJSON)
                .forEach(result::add);

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
    public HashSet<Pokemon> getWithinRadius(Coordinate coordinate, double radius) {
        HashSet<Pokemon> result = new HashSet<>();

        // http://stackoverflow.com/questions/11504154/get-all-members-in-sorted-set
        jedis.georadius(POKEMONS, coordinate.getLongitude(), coordinate.getLatitude(), radius, GeoUnit.KM).stream()
                .map(GeoRadiusResponse::getMemberByString)
                .map(this::convertFromJSON)
                .forEach(result::add);

        return result;
    }


    /**
     * Add a pokemon to the database
     *
     * @param pokemon the pokemon
     */
    public void addPokemon(Pokemon pokemon) throws PokemonValidationException {
        //Validate
        PokemonValidator.validatePokemon(pokemon);

        String json = convertToJSON(pokemon);
        jedis.geoadd(POKEMONS, pokemon.getLongitude(), pokemon.getLatitude(), json);
    }

    /**
     * Remove a pokemon from the database
     *
     * @param pokemon the pokemon
     */
    public void removePokemon(Pokemon pokemon) {
        String json = convertToJSON(pokemon);
        jedis.zrem(POKEMONS, json);
    }


    /**
     * Read a pokemon from JSON
     *
     * @param json the JSON string
     * @return a Pokemon
     */
    private Pokemon convertFromJSON(String json) {
        try {
            return objectMapper.readValue(json, Pokemon.class);
        } catch (IOException e) {
            // There should be no problem with this
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a pokemon to JSON
     *
     * @param pokemon the pokemon
     * @return json string
     */
    private String convertToJSON(Pokemon pokemon) {
        try {
            return objectMapper.writeValueAsString(pokemon);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
