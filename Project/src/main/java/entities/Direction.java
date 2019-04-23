package entities;

public class Direction {
    String direction;
    String floor;

    public Direction(String direction, String floor) {
        this.direction = direction;
        this.floor = floor;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
