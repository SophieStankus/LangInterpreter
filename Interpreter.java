import java.io.IOException;
public class Interpreter {
    String output = "";
    Environment currentEnv = new Environment();

    String execute(Parse node) {
        try {
            exec(node);
        } catch (ArithmeticException e) {
            this.output += e.getMessage();
        } catch (AssertionError e) {
            this.output += e.getMessage();
        }
        return this.output;
    }

    // General execute 
    String exec(Parse node) {
        output = "";
        if (node.name.equals("print")) {
            exec_print(node);
        } else if (node.name.equals("sequence")) {
            exec_sequence(node);
        } else if (node.name.equals("declare")) {
            exec_declare(node);
        } else if (node.name.equals("assign")) {
            exec_assign(node);
        } else {
            eval(node);
        }
        return this.output;
    }

    // General eval 
    Integer eval(Parse node) {
        // Check size
        if (node.name.equals("+")) {
            //System.out.println(eval_plus(node));
            return eval_plus(node);
        } else if (node.name.equals("-")) {
            return eval_sub(node);
        } else if (node.name.equals("*")) {
            return eval_mul(node);
        } else if (node.name.equals("/")) {
            return eval_div(node);
        } else if (node.name.equals("lookup")) {
            return eval_lookup(node);
        } else {
            return eval_int(node);
        }
    }

    // print
    void exec_print(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child = origNode.children.get(0);
        //System.out.println(origNode.children.get(0));
        //System.out.println(output + eval(child));
        output += eval(child);
        output += "\n";
    }

    // sequence 
    void exec_sequence(Parse node) {
        StatementParse origNode = (StatementParse)node;
        for (int i = 0; i < origNode.children.size(); i++) {
            output += exec(origNode.children.get(i));
        }
    }

    // declare 
    void exec_declare(Parse node) {
        StatementParse parse = (StatementParse)node;
        Parse variable = parse.children.get(0);
        if (currentEnv.contains(variable.getName())) {
            throw new AssertionError("runtime error: variable already defined");
        }
        // add new environment variable
        int newValue = eval(parse.children.get(1));
        currentEnv.addVariable(variable.getName(), newValue);
    }

    // assign
    void exec_assign(Parse node) {
        Environment currentEnviro = eval_varloc(node);
        StatementParse parse = (StatementParse)node;
        String originalLoc = parse.children.get(0).getName();
        int newValue = eval(parse.children.get(1));
        currentEnviro.setVariable(originalLoc, newValue);
    }

    // Varloc
    Environment eval_varloc(Parse node) {
        Environment currentEnviro = currentEnv;
        StatementParse parse = (StatementParse)node;
        Parse variable = parse.children.get(0);
        while (!currentEnviro.contains(variable.getName())) {
            currentEnviro = currentEnviro.parent;
            if (currentEnviro == null) {
                throw new AssertionError("runtime error: undefined variable");
            }
        }
        return currentEnviro;
    }

    // lookup
    Integer eval_lookup(Parse node) {
        Environment currentEnviro = currentEnv;
        StatementParse origNode = (StatementParse)node;
        Parse variable = origNode.children.get(0);
        while (!currentEnviro.contains(variable.getName())) {
            currentEnviro = currentEnviro.parent;
            if (currentEnviro == null) {
                throw new AssertionError("runtime error: undefined variable");
            }
        }
        return currentEnviro.getValue(variable.getName());
    }

    // int eval functions 
    Integer eval_plus(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        //System.out.println(child1 +  " + " +  child2  + " = " + (eval(child1) + eval(child2)));
        return eval(child1) + eval(child2);
    }

    Integer eval_sub(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        //System.out.println(child1 +  " - " +  child2  + " = " + (eval(child1) - eval(child2)));
        return eval(child1) - eval(child2);
    }

    Integer eval_mul(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        //System.out.println(child1 +  " * " +  child2  + " = " + (eval(child1) * eval(child2)));
        return eval(child1) * eval(child2);
    }

    Integer eval_div(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        int int1 = eval(child1);
        int int2 = eval(child2);
        if (int2 == 0) {
            throw new ArithmeticException("runtime error: divide by zero");
        }
        return int1/int2;
        //System.out.println(child1 +  " / " +  child2  + " = " + (eval(child1) / eval(child2)));
    }

    Integer eval_int(Parse node) {
        IntegerParse origNode = (IntegerParse)node;
        int value = origNode.getValue();
        return (Integer)value;
    }

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        
    }
}
