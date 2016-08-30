package hu.poketerkep.shared.datasource;

import hu.poketerkep.shared.model.Pokemon;
import hu.poketerkep.shared.model.RandomPokemonGenerator;
import hu.poketerkep.shared.validator.PokemonValidationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collection;
import java.util.HashSet;

/**
 * Tests the Pokemon data source with a running redis instance
 */
public class PokemonDataSourceTest {

    private JedisPool jedisPool;
    private PokemonDataSource pokemonDataSource;

    @Before
    public void setUp() throws Exception {
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");

        Jedis jedis = jedisPool.getResource();

        Assert.assertNotNull("Cannot connect to jedis", jedis);
        pokemonDataSource = new PokemonDataSource(jedis);
    }

    private void clear() {
        //Delete old pokemons key
        jedisPool.getResource().del(PokemonDataSource.POKEMONS);
    }

    @After
    public void tearDown() throws Exception {
        jedisPool.destroy();
    }

    @Test
    public void addPokemon() throws Exception {
        // Clear redis
        clear();

        Pokemon input = RandomPokemonGenerator.generate();
        pokemonDataSource.addPokemon(input);

        HashSet<Pokemon> output = pokemonDataSource.getAll();

        Assert.assertEquals(1, output.size());
        Pokemon actual = output.iterator().next();
        Assert.assertEquals(input, actual);
    }

    @Test
    public void removePokemon() throws Exception {
        // Clear redis
        clear();

        Pokemon input = RandomPokemonGenerator.generate();
        pokemonDataSource.addPokemon(input);
        pokemonDataSource.removePokemon(input);

        Assert.assertEquals(0, pokemonDataSource.getAll().size());
    }

    @Test
    public void getAll() throws Exception {
        // Clear redis
        clear();

        // Add 10 pokemons
        Collection<Pokemon> input = RandomPokemonGenerator.generateN(10);

        input.forEach(pokemon -> {
            try {
                pokemonDataSource.addPokemon(pokemon);
            } catch (PokemonValidationException e) {
                e.printStackTrace();
            }
        });

        HashSet<Pokemon> output = pokemonDataSource.getAll();

        Assert.assertEquals("The input and output size should match", input.size(), output.size());

        for (Pokemon inputObj : input) {
            boolean found = false;
            for (Pokemon outputObj : output) {
                if (inputObj.equals(outputObj)) {
                    found = true;
                }
            }

            Assert.assertTrue("An added pokemon is not found", found);
        }

    }

    @Test
    public void getWithinRadius() throws Exception {

    }

}