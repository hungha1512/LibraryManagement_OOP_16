package com.hunghq.librarymanagement.Enum;

public enum State {
    BORROWED("Borrowed"),
    RETURNED("Returned"),
    OVERDUE("Overdue");

    private String state;

    State(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public static State fromValue(String value) {
        for (State s : State.values()) {
            if (s.state.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown state: " + value);
    }
}
