package hu.poketerkep.shared.validator.pokemon;

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

        // Check max 15 min
        long nowPlus15Min = Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli();

        if (pokemon.getDisappearTime() > nowPlus15Min) {
            throw new PokemonValidationException("disappearTime.wrong");
        }


        // -- spawnpointId
        if (pokemon.getSpawnpointId() == null || pokemon.getSpawnpointId().length() == 0) {
            throw new PokemonValidationException("spawnpointId.empty");
        }
    }
}
