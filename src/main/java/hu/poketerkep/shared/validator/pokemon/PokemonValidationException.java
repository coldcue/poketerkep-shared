package hu.poketerkep.shared.validator.pokemon;

import hu.poketerkep.shared.validator.ValidationException;

public class PokemonValidationException extends ValidationException {
    public PokemonValidationException(String message) {
        super(message);
    }
}
