package hu.poketerkep.shared.api;

import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.Pokemon;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface ClientAPIEndpoint {
    ResponseEntity<Void> addPokemons(Pokemon[] pokemons);

    ResponseEntity<Collection<Coordinate>> nextScanLocations(int limit);
}
