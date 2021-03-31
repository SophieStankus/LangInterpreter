import java.util.LinkedList;

public class IdentifierParse extends StatementParse {

    public IdentifierParse(String name, int index) {
        super(name, index);
        this.children = new LinkedList<Parse>();
    }

    // Like int parse
    public String toString() {
        return "" + this.name;
    }
}
