package project;

class Entry implements Comparable<Entry> {
    private String name;
    private String difficulty;
    private int value;

    public Entry(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Entry(String name, int value, String difficulty) {
        this.name = name;
        this.value = value;
        this.difficulty = difficulty;
    }


    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Entry other) {
        // Compare based on the value in ascending order
        return Integer.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return name + " " + value + " " + difficulty;
    }
}
