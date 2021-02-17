import java.util.LinkedList;

public class StatementParse extends Parse {

    // Define member variables
    public LinkedList<Parse> children;

    public StatementParse(String name, int index) {
        super(name, index);
        this.children = new LinkedList<Parse>();
    }

    public LinkedList<Parse> getChildren() {
        return this.children;
    }

    public String toString() {
        String result = "";
        result += "(";
        result += this.name;
        for (Parse child : this.children) {
            result += " " + child.toString();
        }
        result += ")";
        return result;
    }
}
