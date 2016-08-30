package hu.poketerkep.shared.datasource;

import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.Pokemon;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collection;
import java.util.HashSet;

import static hu.poketerkep.shared.model.RandomPokemonGenerator.*;

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

        pokemonDataSource = new PokemonDataSource(jedisPool);
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

        Pokemon input = generate();
        pokemonDataSource.add(input);

        HashSet<Pokemon> output = pokemonDataSource.getAll();

        Assert.assertEquals(1, output.size());
        Pokemon actual = output.iterator().next();
        Assert.assertEquals(input, actual);
    }

    @Test
    public void removePokemon() throws Exception {
        // Clear redis
        clear();

        Pokemon input = generate();
        pokemonDataSource.add(input);
        pokemonDataSource.remove(input);

        Assert.assertEquals(0, pokemonDataSource.getAll().size());
    }

    @Test
    public void addAllAndGetAll() throws Exception {
        // Clear redis
        clear();

        // Add 10 pokemons
        Collection<Pokemon> input = generateN(10);
        pokemonDataSource.addAll(input);

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
        // Clear redis
        clear();

        Coordinate origo = Coordinate.fromDegrees(47.43, 19.02);

        Collection<Pokemon> inRadius = new HashSet<>();

        inRadius.add(generateWithCoordinates(origo.getNew(0.02, 30)));
        inRadius.add(generateWithCoordinates(origo.getNew(0.03, 60)));
        inRadius.add(generateWithCoordinates(origo.getNew(0.04, 90)));
        inRadius.add(generateWithCoordinates(origo.getNew(0.05, 120)));
        inRadius.add(generateWithCoordinates(origo.getNew(0.06, 240)));

        pokemonDataSource.addAll(inRadius);

        Collection<Pokemon> outOfRadius = new HashSet<>();

        outOfRadius.add(generateWithCoordinates(origo.getNew(0.12, 30)));
        outOfRadius.add(generateWithCoordinates(origo.getNew(0.13, 60)));
        outOfRadius.add(generateWithCoordinates(origo.getNew(0.14, 90)));
        outOfRadius.add(generateWithCoordinates(origo.getNew(0.15, 120)));
        outOfRadius.add(generateWithCoordinates(origo.getNew(0.16, 240)));

        pokemonDataSource.addAll(outOfRadius);

        HashSet<Pokemon> withinRadius = pokemonDataSource.getWithinRadius(origo, 0.07);

        Assert.assertEquals("Sizes does not match", inRadius.size(), withinRadius.size());

        for (Pokemon obj1 : withinRadius) {

            boolean found = false;
            for (Pokemon obj2 : inRadius) {
                if (obj1.equals(obj2)) {
                    found = true;
                }
            }

            Assert.assertTrue("A pokemon is not found in the radius", found);
        }

    }

}