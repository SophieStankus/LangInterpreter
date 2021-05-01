public class Parse {

    // name of the parse node
    public String name;
    // the number of characters into the string we've parsed
    public int index;

    public Parse(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public boolean equals(Parse other) {
        return (this.name.equals(other.name)) && (this.index == other.index);
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setName(String str) {
        this.name = str;
    }

}
