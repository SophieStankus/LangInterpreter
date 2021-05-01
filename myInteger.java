public class myInteger extends Value {
    int theInt;

    public myInteger(int newInt) {
        theInt = newInt;
    }

    @Override
    public String toString() {
        return Integer.toString(theInt);
    }
}
