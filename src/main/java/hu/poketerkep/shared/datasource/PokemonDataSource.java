package hu.poketerkep.shared.datasource;

import hu.poketerkep.shared.model.Pokemon;
import hu.poketerkep.shared.validator.ValidationException;
import hu.poketerkep.shared.validator.pokemon.PokemonValidator;
import redis.clients.jedis.JedisPool;

import java.util.Collection;

@SuppressWarnings("WeakerAccess")
public class PokemonDataSource extends CoordinateDataSource<Pokemon> {
    static final String POKEMONS = "pokemons";

    public PokemonDataSource(JedisPool jedisPool) {
        super(Pokemon.class, POKEMONS, jedisPool);
    }

    @Override
    public void add(Pokemon pokemon) throws ValidationException {
        //Validate
        PokemonValidator.validatePokemon(pokemon);

        super.add(pokemon);
    }

    @Override
    public void addAll(Collection<Pokemon> pokemons) throws ValidationException {
        //Validate all pokemons
        for (Pokemon pokemon : pokemons) {
            PokemonValidator.validatePokemon(pokemon);
        }

        super.addAll(pokemons);
    }
}
