package pathfinding;

public enum Floors {
    LL2("Lower Level 2", "L2", 0), LL1("Lower Level 1", "L1", 1),
    GROUND("Ground Floor", "G", 2), FIRST("First Floor", "1", 3),
    SECOND("Second Floor", "2", 4), THIRD("Third Floor", "3", 5),
    FOURTH("Fourth Floor", "4", 6);

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

    public int getTabIndex() {
        return Floors.values().length - getIndex() - 1;
    }

    public static Floors getByID(String ID) {
        if(ID.equals(FIRST.ID)) {
            return FIRST;
        }
        if(ID.equals(SECOND.ID)) {
            return SECOND;
        }
        if(ID.equals(THIRD.ID)) {
            return THIRD;
        }
        if(ID.equals(GROUND.ID)) {
            return GROUND;
        }
        if(ID.equals(LL1.ID)) {
            return LL1;
        }
        if(ID.equals(FOURTH.ID)) {
            return FOURTH;
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
        if(ID.equals(FOURTH.name)) {
            return FOURTH;
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
            }case (6): {
                return FOURTH;
            }default:
                return SECOND;
        }
    }
}