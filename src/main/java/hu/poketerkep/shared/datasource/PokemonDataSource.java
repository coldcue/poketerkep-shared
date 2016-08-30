package hu.poketerkep.shared.datasource;

import hu.poketerkep.shared.model.Pokemon;
import hu.poketerkep.shared.validator.pokemon.PokemonValidator;
import hu.poketerkep.shared.validator.ValidationException;
import redis.clients.jedis.JedisPool;

@SuppressWarnings("WeakerAccess")
public class PokemonDataSource extends CoordinateDataSource<Pokemon> {
    static final String POKEMONS = "pokemons";

    public PokemonDataSource(JedisPool jedisPool) {
        super(Pokemon.class, POKEMONS, jedisPool);
    }

    @Override
    public void add(Pokemon obj) throws ValidationException {
        //Validate
        PokemonValidator.validatePokemon(obj);

        super.add(obj);
    }
}
