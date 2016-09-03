package hu.poketerkep.shared.geo;

public enum Direction {
    NORTH(0), EAST(90), SOUTH(180), WEST(270);

    private final double angle;

    Direction(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

}
