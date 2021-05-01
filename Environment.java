import java.util.ArrayList;

public class Environment {
    Environment parent;
    ArrayList<Variable> variables = new ArrayList<>();

    public class Variable {
        String name;
        Value value;

        public Variable(String str, Value val) {
            this.name = str;
            this.value = val;
        }

        @Override 
        public String toString() {
            return "Name: " + this.name + " Val: " + this.value;
        }
    }

    public Environment() {
        this.parent = null;
    }
    
    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void setParent(Environment parent) {
        this.parent = parent;
    }

    public void addVariable(String str, Value value) {
        Variable newVar = new Variable(str, value);
        variables.add(newVar);
    }
    
    public Boolean contains(String str) {
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).name.equals(str)) {
                return true;
            } 
        }
        return false;
    }

    public void setVariable(String str, Value val) {
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).name.equals(str)) {
                variables.get(i).value = val;
                return;
            } 
        }
    }

    public Value getValue(String str) {
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).name.equals(str)) {
                return variables.get(i).value;
            } 
        }
        return new Value();
    }

    public Environment getParent() {
        return this.parent;
    }
}
