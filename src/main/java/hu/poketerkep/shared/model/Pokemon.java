package hu.poketerkep.shared.model;

import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.helpers.CoordinateAware;

import java.io.Serializable;

@SuppressWarnings("WeakerAccess")
public class Pokemon implements CoordinateAware, Serializable {
    private String encounterId;
    private long disappearTime;
    private int pokemonId;
    private Coordinate coordinate;

    public Pokemon() {
    }

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
    }

    public long getDisappearTime() {
        return disappearTime;
    }

    public void setDisappearTime(long disappearTime) {
        this.disappearTime = disappearTime;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public boolean equals(Object obj) {
        return Pokemon.class.equals(obj.getClass()) && encounterId.equals(((Pokemon) obj).encounterId);
    }


}
