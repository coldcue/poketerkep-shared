package hu.poketerkep.shared.model;

import hu.poketerkep.shared.geo.Coordinate;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomPokemonGenerator {
    public static Pokemon generate() {
        Random random = new Random();
        Pokemon pokemon = new Pokemon();

        pokemon.setEncounterId(new BigInteger(30, random).toString(32));
        pokemon.setPokemonId(random.nextInt(720));

        double latitude = 47.0 + (double) random.nextInt(9999) / 10000;
        double longitude = 19.0 + (double) random.nextInt(9999) / 10000;

        pokemon.setCoordinate(Coordinate.fromDegrees(latitude, longitude));

        long disappearTime = Instant.now().plusMillis(random.nextInt(15 * 60 * 1000)).toEpochMilli();
        pokemon.setDisappearTime(disappearTime);

        pokemon.setSpawnpointId("notgenerated");

        return pokemon;
    }

    public static Collection<Pokemon> generateN(int n) {
        ArrayList<Pokemon> pokemons = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            pokemons.add(generate());
        }

        return pokemons;
    }
}
