package hu.poketerkep.shared.model;

import hu.poketerkep.shared.geo.Coordinate;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomPokemonGenerator {

    private static Random random = new Random();

    public static Pokemon generateWithCoordinates(Coordinate coordinate) {
        Pokemon pokemon = new Pokemon();

        pokemon.setEncounterId(new BigInteger(120, random).toString(32));
        pokemon.setPokemonId(random.nextInt(720));

        pokemon.setCoordinate(coordinate);

        long disappearTime = Instant.now().plusMillis(random.nextInt(15 * 60 * 1000)).toEpochMilli();
        pokemon.setDisappearTime(disappearTime);

        return pokemon;
    }

    public static Pokemon generate() {
        double latitude = 47.0 + (double) random.nextInt(1000000) / 1000000;
        double longitude = 19.0 + (double) random.nextInt(1000000) / 1000000;

        return generateWithCoordinates(Coordinate.fromDegrees(latitude, longitude));
    }

    public static Collection<Pokemon> generateN(int n) {
        ArrayList<Pokemon> pokemons = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            pokemons.add(generate());
        }

        return pokemons;
    }
}
