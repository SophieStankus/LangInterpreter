import java.util.ArrayList;

public class Environment {
    Environment parent;
    ArrayList<Variable> variables = new ArrayList<>();

    public class Variable {
        String name;
        int value;

        public Variable(String str, int val) {
            this.name = str;
            this.value = val;
        }
    }

    public Environment() {
        this.parent = null;
    }

    public void setParent(Environment parent) {
        this.parent = parent;
    }

    public void addVariable(String str, int value) {
        Variable newVar = new Variable(str, value);
        variables.add(newVar);
    }
    
    public Boolean contains(String str) {
        for (int i = 0; i < variables.size(); i++) {
            //System.out.println(variables.get(i).name);
            if (variables.get(i).name.equals(str)) {
                return true;
            } 
        }
        return false;
    }

    public void setVariable(String str, int val) {
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).name.equals(str)) {
                variables.get(i).value = val;
                return;
            } 
        }
    }

    public int getValue(String str) {
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).name.equals(str)) {
                return variables.get(i).value;
            } 
        }
        return -1000000000;
    }
}
