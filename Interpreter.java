import java.util.HashSet;

public class Interpreter {
    String output;
    Environment currentEnv;
    Boolean returns = false;
    int depth = 0;
    Value currReturn = new myInteger(0);

    String execute(Parse node) {
        currentEnv = new Environment();
        this.output = "";
        this.depth = 0;
        this.currReturn = new myInteger(0);
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
        if (node.name.equals("print")) {
            exec_print(node);
        } else if (node.name.equals("sequence")) {
            exec_sequence(node);
        } else if (node.name.equals("declare")) {
            exec_declare(node);
        } else if (node.name.equals("assign")) {
            exec_assign(node);
        } else if (node.name.equals("while")) {
            exec_while(node);
        } else if (node.name.equals("ifelse")) {
            exec_ifelse(node);
        } else if (node.name.equals("if")) {
            exec_if(node);
        } else if (node.name.equals("return")) {
            exec_return(node);
        } else {
            eval(node);
        }
        return this.output;
    }

    // General eval 
    Value eval(Parse node) {
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
        } else if (node.name.equals("!")) {
            return eval_not(node);
        } else if (node.name.equals("&&")) {
            return eval_and(node);
        } else if (node.name.equals("||")) {
            return eval_or(node);
        } else if (node.name.equals("==")) {
            return eval_equals(node);
        } else if (node.name.equals("!=")){
            return eval_not_equal(node);
        } else if (node.name.equals("<=")) {
            return eval_less_equal(node);
        } else if (node.name.equals(">=")) {
            return eval_greater_equal(node);
        } else if (node.name.equals("<")) {
            return eval_less(node);
        } else if (node.name.equals(">")) {
            return eval_greater(node);
        } else if (node.name.equals("call")) {
            return eval_call(node);
        } else if ((node.name.equals("function"))) {
            return eval_function(node);
        } else {
            return eval_int(node);
        }
    }

    // print
    void exec_print(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child = origNode.children.get(0);
        Value child2 = eval(child);
        //System.out.println(origNode.children.get(0));
        //System.out.println(output + eval(child));
        this.output += child2;
        this.output += "\n";
    }

    // sequence 
    void exec_sequence(Parse node) {
        StatementParse origNode = (StatementParse)node;
        for (int i = 0; i < origNode.children.size(); i++) {
            if (returns == true) {
                return;
            }
            exec(origNode.children.get(i));
        }
    }

    // return 
    void exec_return(Parse node) {
        // check for return outside function
        if (depth <= 0) {
            throw new AssertionError("runtime error: returning outside function");
        }
        StatementParse origNode = (StatementParse)node;
        Value child = (Value)eval(origNode.children.get(0));
        currReturn = child;
        returns = true;
    }

    // declare 
    void exec_declare(Parse node) {
        StatementParse parse = (StatementParse)node;
        Parse variable = parse.children.get(0);
        Value temp = eval(parse.children.get(1));
        if (currentEnv.contains(variable.getName())) {
            throw new AssertionError("runtime error: variable already defined");
        }
        // add new environment variable
        currentEnv.addVariable(variable.getName(), temp);
    }

    // assign
    void exec_assign(Parse node) {
        StatementParse parse = (StatementParse)node;
        StatementParse originalLoc = (StatementParse)parse.children.get(0);
        String newVar = originalLoc.children.get(0).getName();
        Value temp = eval(parse.children.get(1));
        myInteger temp1 = (myInteger)temp;
        Environment currentEnviro = eval_varloc(node);
        currentEnviro.setVariable(newVar, temp1);
    }

    // while
    void exec_while(Parse node) {
        StatementParse origParse = (StatementParse)node;
        Parse condition = origParse.children.get(0);
        Parse steps = origParse.children.get(1);
        Value temp = eval(condition);
        if (temp instanceof Closure) {
        } else {
            myInteger newTemp = (myInteger)temp;
            while (newTemp.theInt != 0 && !returns)  {
                Environment enviro = new Environment(currentEnv);
                this.currentEnv = enviro;
                exec(steps);
                newTemp = (myInteger)eval(condition);
                this.currentEnv = enviro.getParent();
            }
        }
    }

    // if 
    void exec_if(Parse node) {
        StatementParse origParse = (StatementParse)node;
        Parse condition = origParse.children.get(0);
        Parse steps = origParse.children.get(1);
        Environment enviro = new Environment(currentEnv);
        this.currentEnv = enviro;
        Value temp = eval(condition);
        if (temp instanceof Closure) {
            this.exec(steps);
        } else if (temp instanceof myInteger) {
            myInteger convert = (myInteger)temp;
            if (convert.theInt != 0) {
                this.exec(steps);
            }
        } 
        this.currentEnv = enviro.parent;
    }
    
    // if else
    void exec_ifelse(Parse node) {
        StatementParse origParse = (StatementParse)node;
        Parse condition = origParse.children.get(0);
        Parse steps = origParse.children.get(1);
        Parse otherSteps = origParse.children.get(2);
        Environment enviro = new Environment(currentEnv);
        this.currentEnv = enviro;
        Value temp = eval(condition);
        if (temp instanceof Closure) {
            this.exec(steps);
        } else if (temp instanceof myInteger) {
            myInteger convert = (myInteger)temp;
            if (convert.theInt != 0) {
                this.exec(steps);
            } else {
                this.exec(otherSteps);
            }
        } 
        this.currentEnv = enviro.parent;
    }

    // function
    Closure eval_function(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        StatementParse parameters = (StatementParse)child1;
        Parse child2 = origNode.children.get(1);
        StatementParse body = (StatementParse)child2;
        // check duplicate parameters
        HashSet<String> uniqueParameters = new HashSet<String>();
        for (int i = 0; i < parameters.children.size(); i++) {
            uniqueParameters.add(parameters.children.get(i).getName());
        }
        if (parameters.children.size() != uniqueParameters.size()) {
            throw new AssertionError("runtime error: duplicate parameter");
        }
        return new Closure(parameters, body, currentEnv);
    }

    // function call
    Value eval_call(Parse node) {
        StatementParse node2 = (StatementParse)node;
        Value valueInstance = eval(node2.children.get(0));
        if (!(valueInstance instanceof Closure)) {
            throw new AssertionError("runtime error: calling a non-function");
        }
        Closure parse = (Closure)valueInstance;
        StatementParse arguments = (StatementParse)node2.children.get(1);
        if (arguments.children.size() != parse.parameters.children.size()) {
            throw new AssertionError("runtime error: argument mismatch");
        }
        // add parameter / arg vars
        Environment newEnv = new Environment();
        newEnv.setParent(parse.currentEnv);
        Environment originalEnv = currentEnv;
        for (int i = 0; i < parse.parameters.children.size(); i++) {
            newEnv.addVariable(parse.parameters.children.get(i).getName(), eval(arguments.children.get(i)));
        }
        // get correct env
        this.currentEnv = newEnv;
        depth += 1;
        // adjust returns for execution
        Boolean origReturns = returns;
        returns = false;
        exec(parse.body);
        Value ret = currReturn;
        // reset member vars
        depth -= 1;
        currReturn = new myInteger(0);
        returns = origReturns;
        currentEnv = originalEnv;
        return ret;
    }

    // Varloc
    Environment eval_varloc(Parse node) {
        Environment currentEnviro = currentEnv;
        StatementParse parse = (StatementParse)node;
        StatementParse variable = (StatementParse)parse.children.get(0);
        String newVar = variable.children.get(0).getName();
        while (!currentEnviro.contains(newVar)) {
            currentEnviro = currentEnviro.parent;
            if (currentEnviro == null) {
                throw new AssertionError("runtime error: undefined variable");
            }
        }
        return currentEnviro;
    }

    // lookup
    Value eval_lookup(Parse node) {
        Environment currentEnviro = currentEnv;
        StatementParse origNode = (StatementParse)node;
        Parse variable = origNode.children.get(0);
        //System.out.println(variable.getName());
        while (!currentEnviro.contains(variable.getName())) {
            //System.out.println(currentEnviro.variables + "\n\n");
            currentEnviro = currentEnviro.parent;
            if (currentEnviro == null) {
                throw new AssertionError("runtime error: undefined variable");
            }
        }
        return currentEnviro.getValue(variable.getName());    
    }

    // eval control flow
    myInteger eval_not(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Value int1 = eval(child1);
        Boolean first = false;
        // check first truthiness
        if (int1 instanceof Closure) {
            first = true;
        } else if (int1 instanceof myInteger) {
            myInteger convert = (myInteger)int1;
            if (convert.theInt != 0) {
                first = true;
            }
        }
        if (first) {
            return new myInteger(0);
        } else {
            return new myInteger(1);
        }
    }

    myInteger eval_and(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value int1 = eval(child1);
        Value int2 = eval(child2);
        Boolean first = false;
        Boolean second = false;
        // check first truthiness
        if (int1 instanceof Closure) {
            first = true;
        } else if (int1 instanceof myInteger) {
            myInteger convert = (myInteger)int1;
            if (convert.theInt != 0) {
                first = true;
            }
        }
        // check second truthiness
        if (int2 instanceof Closure) {
            second = true;
        } else if (int2 instanceof myInteger) {
            myInteger convert = (myInteger)int2;
            if (convert.theInt != 0) {
                second = true;
            }
        }
        if (first && second) {
            return new myInteger(1);
        } else {
            return new myInteger(0);
        }
    }

    myInteger eval_or(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value int1 = eval(child1);
        Value int2 = eval(child2);
        Boolean first = false;
        Boolean second = false;
        // check first truthiness
        if (int1 instanceof Closure) {
            first = true;
        } else if (int1 instanceof myInteger) {
            myInteger convert = (myInteger)int1;
            if (convert.theInt != 0) {
                first = true;
            }
        }
        // check second truthiness
        if (int2 instanceof Closure) {
            second = true;
        } else if (int2 instanceof myInteger) {
            myInteger convert = (myInteger)int2;
            if (convert.theInt != 0) {
                second = true;
            }
        }
        if (first || second) {
            return new myInteger(1);
        } else {
            return new myInteger(0);
        }
    }

    // eval comparisons
    myInteger eval_equals(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value first = eval(child1);
        Value second = eval(child2);
        if ((first instanceof Closure)||(second instanceof Closure)) {
            if (first.equals(second)) {
                return new myInteger(1);
            } else {
                return new myInteger(0);
            }
        }
        myInteger num1 = (myInteger)first;
        myInteger num2 = (myInteger)second;
        if (num1.theInt == num2.theInt) {
            return new myInteger(1);
        } else {
            return new myInteger(0);
        }
    }

    myInteger eval_not_equal(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value first = eval(child1);
        Value second = eval(child2);
        if ((first instanceof Closure)||(second instanceof Closure)) {
            if (first.equals(second)) {
                return new myInteger(0);
            } else {
                return new myInteger(1);
            }
        }
        myInteger num1 = (myInteger)first;
        myInteger num2 = (myInteger)second;
        if (num1.theInt == num2.theInt) {
            return new myInteger(0);
        } else {
            return new myInteger(1);
        }
    }

    myInteger eval_less_equal(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value int1 = eval(child1);
        Value int2 = eval(child2);
        if (!(int1 instanceof myInteger) || !(int2 instanceof myInteger)) {
            throw new AssertionError("runtime error: math operation on functions");
        } else {
            myInteger temp1 = (myInteger)int1;
            myInteger temp2 = (myInteger)int2;
            if (temp1.theInt <= temp2.theInt) {
                return new myInteger(1);
            } else {
                return new myInteger(0);
            }
        }
    }

    myInteger eval_greater_equal(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value int1 = eval(child1);
        Value int2 = eval(child2);
        if (!(int1 instanceof myInteger) || !(int2 instanceof myInteger)) {
            throw new AssertionError("runtime error: math operation on functions");
        } else {
            myInteger temp1 = (myInteger)int1;
            myInteger temp2 = (myInteger)int2;
            if (temp1.theInt >= temp2.theInt) {
                return new myInteger(1);
            } else {
                return new myInteger(0);
            }
        }
    }

    myInteger eval_less(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value int1 = eval(child1);
        Value int2 = eval(child2);
        if ((int1 instanceof Closure) || (int2 instanceof Closure)) {
            throw new AssertionError("runtime error: math operation on functions");
        } else {
            myInteger temp1 = (myInteger)int1;
            myInteger temp2 = (myInteger)int2;
            if (temp1.theInt < temp2.theInt) {
                return new myInteger(1);
            } else {
                return new myInteger(0);
            }
        }
    }

    myInteger eval_greater(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value int1 = eval(child1);
        Value int2 = eval(child2);
        if ((int1 instanceof Closure) || (int2 instanceof Closure)) {
            throw new AssertionError("runtime error: math operation on functions");
        } else {
            myInteger temp1 = (myInteger)int1;
            myInteger temp2 = (myInteger)int2;
            if (temp1.theInt > temp2.theInt) {
                return new myInteger(1);
            } else {
                return new myInteger(0);
            }
        }
    }

    // int eval functions 
    myInteger eval_plus(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value int1 = eval(child1);
        Value int2 = eval(child2);
        if ((int1 instanceof Closure) || (int2 instanceof Closure)) {
            throw new AssertionError("runtime error: math operation on functions");
        } else {
            myInteger temp1 = (myInteger)int1;
            myInteger temp2 = (myInteger)int2;
            int toRet = temp1.theInt + temp2.theInt;
            return new myInteger(toRet);
        }
    }

    myInteger eval_sub(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value int1 = eval(child1);
        Value int2 = eval(child2);
        if ((int1 instanceof Closure) || (int2 instanceof Closure)) {
            throw new AssertionError("runtime error: math operation on functions");
        } else {
            myInteger temp1 = (myInteger)int1;
            myInteger temp2 = (myInteger)int2;
            int toRet = temp1.theInt - temp2.theInt;
            return new myInteger(toRet);
        }
    }

    myInteger eval_mul(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value int1 = eval(child1);
        Value int2 = eval(child2);
        if (!(int1 instanceof myInteger) || !(int2 instanceof myInteger)) {
            throw new AssertionError("runtime error: math operation on functions");
        } else {
            myInteger temp1 = (myInteger)int1;
            myInteger temp2 = (myInteger)int2;
            int toRet = temp1.theInt * temp2.theInt;
            return new myInteger(toRet);
        }
    }

    myInteger eval_div(Parse node) {
        StatementParse origNode = (StatementParse)node;
        Parse child1 = origNode.children.get(0);
        Parse child2 = origNode.children.get(1);
        Value int1 = eval(child1);
        Value int2 = eval(child2);
        if ((int1 instanceof Closure) || (int2 instanceof Closure)) {
            throw new AssertionError("runtime error: math operation on functions");
        } else {
            myInteger temp2 = (myInteger)int2;
            myInteger temp1 = (myInteger)int1;
            if (temp2.theInt == 0) {
                throw new ArithmeticException("runtime error: divide by zero");
            }
            int toRet = temp1.theInt/temp2.theInt;
            return new myInteger(toRet);
        }
    }

    myInteger eval_int(Parse node) {
        IntegerParse origNode = (IntegerParse)node;
        int value = origNode.getValue();
        myInteger newMyInt = new myInteger(value);
        return newMyInt;
    }

    public static void main(String[] args) {
    }
}

