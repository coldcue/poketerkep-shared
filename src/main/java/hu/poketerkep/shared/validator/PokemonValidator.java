package hu.poketerkep.shared.validator;

import hu.poketerkep.shared.model.Pokemon;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Pokemon validator
 */
public class PokemonValidator {

    public static void validatePokemon(Pokemon pokemon) throws PokemonValidationException {
        // -- encounterId
        if (pokemon.getEncounterId() == null || pokemon.getEncounterId().length() == 0) {
            throw new PokemonValidationException("encounterId.empty");
        }

        // -- disappearTime
        if (pokemon.getDisappearTime() == null) {
            throw new PokemonValidationException("disappearTime.null");
        } else {
            // Check max 15 min
            long nowPlus15Min = Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli();

            if (pokemon.getDisappearTime() > nowPlus15Min) {
                throw new PokemonValidationException("disappearTime.wrong");
            }
        }

        // -- latitude
        if (pokemon.getLatitude() == null) {
            throw new PokemonValidationException("latitude.null");
        }

        // -- longitude
        if (pokemon.getLongitude() == null) {
            throw new PokemonValidationException("longitude.null");
        }

        // -- pokemonId
        if (pokemon.getPokemonId() == null) {
            throw new PokemonValidationException("pokemonId.null");
        }


        // -- spawnpointId
        if (pokemon.getSpawnpointId() == null || pokemon.getSpawnpointId().length() == 0) {
            throw new PokemonValidationException("spawnpointId.empty");
        }
    }
}
