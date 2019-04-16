package pathfinding;

public enum Floors {
    LL2("Lower Level 2", "L2", 0), LL1("Lower Level 1", "L1", 1),
    GROUND("Ground Floor", "G", 2), FIRST("First Floor", "1", 3),
    SECOND("Second Floor", "2", 4), THIRD("Third Floor", "3", 5);

    private final String name;
    private final String ID;
    private final int index;

    Floors(String name, String ID, int index) {
        this.name = name;
        this.ID = ID;
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public String getID() {
        return this.ID;
    }

    public int getIndex() {
        return this.index;
    }

    public static Floors getByID(String ID) {
        if(ID.equals("1")) {
            return FIRST;
        }
        if(ID.equals("2")) {
            return SECOND;
        }
        if(ID.equals("3")) {
            return THIRD;
        }
        if(ID.equals("G")) {
            return GROUND;
        }
        if(ID.equals("L1")) {
            return LL1;
        }
        return LL2;
    }

    public static Floors getByName(String ID) {
        if(ID.equals(FIRST.name)) {
            return FIRST;
        }
        if(ID.equals(SECOND.name)) {
            return SECOND;
        }
        if(ID.equals(THIRD.name)) {
            return THIRD;
        }
        if(ID.equals(GROUND.name)) {
            return GROUND;
        }
        if(ID.equals(LL1.name)) {
            return LL1;
        }
        return LL2;
    }

    public static Floors getByIndex(int index) {
        switch (index) {
            case (0): {
                return LL2;
            } case (1): {
                return LL1;
            } case (2): {
                return GROUND;
            } case (3): {
                return FIRST;
            } case (4): {
                return SECOND;
            }case (5): {
                return THIRD;
            } default:
                return SECOND;
        }
    }
}