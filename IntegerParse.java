public class IntegerParse extends Parse {

    // the value of the parse
    private int value;

    public IntegerParse(int value, int index) {
        super("int", index);
        this.value = value;
    }

    public String toString() {
        return "" + this.value;
    }

    public int getValue() {
        return this.value;
    }
}
