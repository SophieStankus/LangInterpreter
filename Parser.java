public class Parser {

    // Fail parse case
    static Parse FAIL = new Parse("fail", -1);
    static String[] illegalVarNames = {"print", "var", "if", "else", "while", "func", "ret", "class", "int", "bool", "string"};

    // Wrapper parse - sets general parse index to 0
    public Parse parse(String str, String term) {
        return this.parse(str, 0, term);
    }

    // Wrapper parse - sets general parse index to 0
    public Parse parse(String str) {
        Parse newParse = this.parse(str, 0, "program");
        //System.out.println(newParse);
        // Check not Parse.FAIL and return Null
        if (newParse.equals(Parser.FAIL)) {
            return null;
        }
        // Check if index = length of string
        if (newParse.getIndex() != str.length()) {
            return null;
        }
        return newParse;
    }

    // General parse
    public Parse parse(String str, int index, String term) {
        // Check for empty string
        if (str == "") {
            return Parser.FAIL;
        }
        // Check string length
        if (index > str.length()) {
            return Parser.FAIL;
        }
        // delegates to other parse functions
        if (term.equals("integer")) {
            return this.parse_integer(str, index);
        } else if (term.equals("add_sub")) {
            return this.parse_add_sub_expression(str, index);
        } else if (term.equals("mul_div")) {
            return this.parse_mul_div_expression(str, index);
        } else if (term.equals("operand")) {
            return this.parse_operand(str, index);
        } else if (term.equals("parenthesis")) {
            return this.parse_parenthesis(str, index);
        } else if (term.equals("opt_space")) {
            return this.parse_opt_space(str, index);
        } else if (term.equals("req_space")) {
            return this.parse_req_space(str, index);
        } else if (term.equals("expression")) {
            return this.parse_expression(str, index);
        } else if (term.equals("comment")) {
            return this.parse_comment(str, index);
        } else if (term.equals("print")) {
            return this.parse_print_statement(str, index);
        } else if (term.equals("statement")) {
            return this.parse_statement(str, index);
        } else if (term.equals("program")) {
            return this.parse_program(str, index);
        } else if (term.equals("identifier")) {
            return this.parse_identifier(str, index);
        } else if (term.equals("location")){
            return this.parse_location(str, index);
        } else if (term.equals("assignment")) {
            return this.parse_assignment(str, index);
        } else if (term.equals("declaration")) {
            return this.parse_declaration(str, index);
        } else if (term.equals("space")) {
            return this.parse_space(str, index);
        } else if (term.equals("exp_statement")) {
            return this.parse_expression_statement(str, index);
        } else if (term.equals("if")) {
            return this.parse_if_statement(str, index);
        } else if (term.equals("if_else")) {
            return this.parse_if_else_statement(str, index);
        } else if (term.equals("while")) {
            return this.parse_while_statement(str, index);
        } else if (term.equals("or")) {
            return this.parse_or_expression(str, index);
        } else if (term.equals("or_op")) {
            return this.parse_or_operator(str, index);
        } else if (term.equals("and")) {
            return this.parse_and_expression(str, index);
        } else if (term.equals("and_op")) {
            return this.parse_and_operator(str, index);
        } else if (term.equals("optional_not")) {
            return this.parse_optional_not_expression(str, index);
        } else if (term.equals("not")) {
            return this.parse_not_expression(str, index);
        } else if (term.equals("comp")) {
            return this.parse_comp_expression(str, index);
        } else if (term.equals("comp_op")) {
            return this.parse_comp_operator(str, index);
        } else if (term.equals("return")) {
            return this.parse_return_statement(str, index);
        } else if (term.equals("arguments")) {
            return this.parse_arguments(str, index);
        } else if (term.equals("call_expression")) {
            return this.parse_call_expression(str, index);
        } else if (term.equals("function_call")) {
            return this.parse_function_call(str, index);
        } else if (term.equals("parameters")) {
            return this.parse_parameters(str, index);
        } else if (term.equals("function")) {
            return this.parse_function(str, index);
        } else {
            throw new AssertionError("Unexpected term " + term);
        }
    }

    // Parse program
    private Parse parse_program(String str, int index) {
        StatementParse newParse = new StatementParse("sequence", index);
        // Parse opt space
        System.out.println("parsing optsp program" + " " + index + " " + str.charAt(index));
        Parse parse = this.parse(str, index, "opt_space");
        index = parse.getIndex();
        // Parse statement & space combo
        while (str.length() != index && str.charAt(index) != '}') {
            System.out.println("parsing statement program" + " " + index + " " + str.charAt(index));
            parse = this.parse(str, index, "statement");
            System.out.println(parse);  
            newParse.children.add(parse);
            if (parse.equals(Parser.FAIL)) {
                return Parser.FAIL;
            }
            index = parse.getIndex();
            newParse.setIndex(index);
            if (str.length() != index) {
                System.out.println("parsing optsp program" + " " + index + " " + str.charAt(index));
                parse = this.parse(str, index, "opt_space");
                index = parse.getIndex();
                newParse.setIndex(index);
            }
            if ((index >= str.length()-1)||(this.parse(str, index, "statement").equals(Parser.FAIL))) {
                return newParse;
            }
        }
        //System.out.println(newParse);
        return newParse;
    }

    // Parse statement
    private Parse parse_statement(String str, int index) {
        Parse statement;
        // Parse declaration  
        System.out.println("parsing dec statement" + " " + index + " " + str.charAt(index));
        statement = this.parse(str, index, "declaration");
        if (!statement.equals(Parser.FAIL)) {
            return statement;
        }
        // Parse assignment
        System.out.println("parsing assign statement" + " " + index + " " + str.charAt(index));
        statement = this.parse(str, index, "assignment");
        if (!statement.equals(Parser.FAIL)) {
            return statement;
        }
        // Parse if else statement
        System.out.println("parsing ife statement" + " " + index + " " + str.charAt(index));
        statement = this.parse(str, index, "if_else");
        if (!statement.equals(Parser.FAIL)) {
            return statement;
        }
        // Parse if statement
        System.out.println("parsing if statement" + " " + index + " " + str.charAt(index));
        statement = this.parse(str, index, "if");
        if (!statement.equals(Parser.FAIL)) {
            return statement;
        }
        // Parse while statement
        System.out.println("parsing while statement" + " " + index + " " + str.charAt(index));
        statement = this.parse(str, index, "while");
        if (!statement.equals(Parser.FAIL)) {
            return statement;
        }
        // Parse return statement
        System.out.println("parsing return statement" + " " + index + " " + str.charAt(index));
        statement = this.parse(str, index, "return");
        if (!statement.equals(Parser.FAIL)) {
            return statement;
        }
        // Parse print statement 
        System.out.println("parsing print statement" + " " + index + " " + str.charAt(index));
        statement = this.parse(str, index, "print");
        if (!statement.equals(Parser.FAIL)) {
            return statement;
        }
        // Parse expression
        System.out.println("parsing exp statement" + " " + index + " " + str.charAt(index));
        statement = this.parse(str, index, "exp_statement");
        if (!statement.equals(Parser.FAIL)) {
            return statement;
        } else {
            return Parser.FAIL;
        }
    }

    // Parse if statement
    private Parse parse_if_statement(String str, int index) {
        Parse counter;
        StatementParse _if = new StatementParse("if", index);
        // Parse if
        if (str.indexOf("if", index) != index) {
            return Parser.FAIL;
        } else {
            index += 2;
        }
        // Parse opt_space
        System.out.println("parsing optsp if1 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse (
        if (str.indexOf("(", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt_space
        System.out.println("parsing optsp if2 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse expression
        System.out.println("parsing exp if " + index + " " + str.charAt(index));
        Parse child1 = this.parse(str, index, "expression");
        if (child1.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child1.getIndex();
        _if.children.add(child1);
        // Parse opt_space
        System.out.println("parsing optsp if3 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse )
        if (str.indexOf(")", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        System.out.println("parsing optsp if4 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse {
        if (str.indexOf("{", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        System.out.println("parsing optsp if5" + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse program
        System.out.println("parsing prog if " + index + " " + str.charAt(index));
        Parse child2 = this.parse(str, index, "program");
        if (child2.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child2.getIndex();
        _if.children.add(child2);
        // Parse opt space
        System.out.println("parsing optsp if6 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse }
        if (str.indexOf("}", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        _if.setIndex(index);
        return _if;
    }

    // Parse if else statement 
    private Parse parse_if_else_statement(String str, int index) {
        Parse counter;
        StatementParse ifelse = new StatementParse("ifelse", index);
        // Parse if
        if (str.indexOf("if", index) != index) {
            return Parser.FAIL;
        } else {
            index += 2;
        }
        // Parse opt_space
        System.out.println("parsing optsp ife1 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse (
        if (str.indexOf("(", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt_space
        System.out.println("parsing optsp ife2 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse expression
        System.out.println("parsing exp if " + index + " " + str.charAt(index));
        Parse child1 = this.parse(str, index, "expression");
        if (child1.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child1.getIndex();
        ifelse.children.add(child1);
        // Parse opt_space
        System.out.println("parsing optsp ife3 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse )
        if (str.indexOf(")", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        System.out.println("parsing optsp ife4 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse {
        if (str.indexOf("{", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        System.out.println("parsing optsp ife5 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse program
        System.out.println("parsing prog ife " + index + " " + str.charAt(index));
        Parse child2 = this.parse(str, index, "program");
        if (child2.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child2.getIndex();
        ifelse.children.add(child2);
        // Parse opt space
        System.out.println("parsing optsp ife6 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse }
        if (str.indexOf("}", index) != index || str.length() == index + 1) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        System.out.println("parsing optsp ife7 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse else
        if (str.indexOf("else", index) != index) {
            return Parser.FAIL;
        } else {
            index += 4;
        }
        // Parse opt space
        System.out.println("parsing optsp ife8 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse {
        if (str.indexOf("{", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        System.out.println("parsing optsp ife9 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse program
        System.out.println("parsing prog ife2 " + index + " " + str.charAt(index));
        Parse child3 = this.parse(str, index, "program");
        if (child3.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child3.getIndex();
        ifelse.children.add(child3);
        // Parse opt space 
        System.out.println("parsing optsp ife10 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse }
        if (str.indexOf("}", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        ifelse.setIndex(index);
        return ifelse;
    }

    // Parse while statement 
    private Parse parse_while_statement(String str, int index) {
        Parse counter;
        StatementParse _while = new StatementParse("while", index);
        // Parse while 
        if (str.indexOf("while", index) != index) {
            return Parser.FAIL;
        } else {
            index += 5;
        }
        // Parse opt_space
        System.out.println("parsing optsp while1 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse (
        if (str.indexOf("(", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        System.out.println("parsing optsp while2 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse expression 
        System.out.println("parsing exp while " + index + " " + str.charAt(index));
        Parse child1 = this.parse(str, index, "expression");
        if (child1.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child1.getIndex();
        _while.children.add(child1);
        // Parse opt space 
        System.out.println("parsing optsp while3 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse )
        if (str.indexOf(")", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        System.out.println("parsing optsp while4 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse {
        if (str.indexOf("{", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        System.out.println("parsing optsp while5 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse program
        System.out.println("first");
        System.out.println("parsing prog while " + index + " " + str.charAt(index));
        Parse child2 = this.parse(str, index, "program");
        if (child2.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child2.getIndex();
        _while.children.add(child2);
        // Parse opt space
        System.out.println("parsing optsp while6 " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse }
        if (str.indexOf("}", index) != index) {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        _while.setIndex(index);
        return _while;
    }

    // Parse return statement
    private Parse parse_return_statement(String str, int index) {
        System.out.println(index);
        StatementParse toReturn = new StatementParse("return", index);
        Parse counter, child;
        // Parse "ret"
        if (str.charAt(index) != 'r' || str.charAt(index+1) != 'e' || str.charAt(index+2) != 't') {
            return Parser.FAIL;
        } else {
            index += 3;
        }
        // Parse req space
        System.out.println("parsing reqsp return" + " " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "req_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse expression
        System.out.println("parsing exp return" + " " + index + " " + str.charAt(index));
        child = this.parse(str, index, "expression");
        if (child.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child.getIndex();
        toReturn.children.add(child);
        // Parse opt space
        System.out.println("parsing optsp return" + " " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse ;
        if (str.charAt(index) != ';') {
            return Parser.FAIL;
        } else {
            toReturn.setIndex(index+1);
            return toReturn;
        }
    }

    // Parse declaration
    private Parse parse_declaration(String str, int index) {
        Parse counter;
        // Parse var
        System.out.println("Parsing var dec " + index + " " + str.charAt(index));
        StatementParse var = new StatementParse("declare", index);
        if (str.indexOf("var", index) != index) {
            return Parser.FAIL;
        } else {
            index += 3;
        }
        // Parse req_space
        System.out.println("Parsing reqsp dec " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "req_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse assignment statement
        System.out.println("Parsing assg dec " + index + " " + str.charAt(index));
        Parse newParse = this.parse(str, index, "assignment");
        if (newParse.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = newParse.getIndex();
        var.setIndex(index);
        // change names (newParse.children[0] = varloc, varloc.children[0] = name, newParse.children[1] = value)
        StatementParse parent = (StatementParse)newParse; // assign
        StatementParse child = (StatementParse)parent.children.get(0); // varloc
        Parse name = child.children.get(0);
        Parse value = parent.children.get(1); 
        var.children.add(name);
        var.children.add(value);
        return var;
    }

    // Parse assignment
    private Parse parse_assignment(String str, int index) {
        StatementParse toReturn = new StatementParse("assign", index);
        // Parse location
        System.out.println("Parsing loc assg " + index + " " + str.charAt(index));
        Parse location = this.parse(str, index, "location");
        if (location.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        location.setName("varloc");
        index = location.getIndex();
        toReturn.children.add(location);
        // Parse opt space
        System.out.println("Parsing optsp assg " + index + " " + str.charAt(index));
        Parse counter = this.parse(str, index, "opt_space");
        index = counter.getIndex();
        // Parse = 
        System.out.println("Parsing = assg " + index + " " + str.charAt(index));
        if (str.charAt(index) != '=') {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt_space
        System.out.println("Parsing optsp assg " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        index = counter.getIndex();
        // Parse expression
        System.out.println("Parsing exp assg " + index + " " + str.charAt(index));
        Parse expression = this.parse(str, index, "expression");
        if (expression.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = expression.getIndex();
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        toReturn.children.add(expression);
        // Parse opt space
        System.out.println("Parsing opstp assg " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        index = counter.getIndex();
        // Parse ;
        System.out.println("Parsing ; assg " + index + " " + str.charAt(index));
        if (str.charAt(index) != ';') {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        toReturn.setIndex(index);
        return toReturn;
    }

    // Parse location
    private Parse parse_location(String str, int index) {
        // Parse identifier
        System.out.println("Parsing iden loc " + index + " " + str.charAt(index));
        Parse location = this.parse_identifier(str, index);
        if (location.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        return location;
    }

    // Parse identifier
    private Parse parse_identifier(String str, int index) {
        Parse identifier;
        String varName = "";
        // parse first char
        System.out.println("parsing first char iden " + index + " " + str.charAt(index));
        identifier = parse_identifier_first_char(str, index);
        if (identifier.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        varName += identifier.getName();
        index = identifier.getIndex();
        // parse other chars 
        while (str.charAt(index) != ' ' && str.charAt(index) != '\n' && str.charAt(index) != ';' 
        && str.charAt(index) != '#' && str.charAt(index) != '+' && str.charAt(index) != '-'
        && str.charAt(index) != '/' && str.charAt(index) != '*' && str.charAt(index) != ')' 
        && str.charAt(index) != '(' && str.charAt(index) != '<' && str.charAt(index) != '>' && 
        str.charAt(index) != '=' && str.charAt(index) != '!' && str.charAt(index) != '&' && str.charAt(index) != '|' 
        && str.charAt(index) != '}' && str.charAt(index) != '{' && str.charAt(index) != ',') {
            System.out.println("parsing char iden " + index + " " + str.charAt(index));
            identifier = parse_identifier_char(str, index);
            if (identifier.equals(Parser.FAIL)) {
                return Parser.FAIL;
            } else {
                varName += identifier.getName();
                index = identifier.getIndex();
            }
        }
        // Check for illegal names
        System.out.println("parsing ill names iden " + index + " " + str.charAt(index));
        for (int i = 0; i < 11; i++) {
            if (varName.equals(Parser.illegalVarNames[i])) {
                return Parser.FAIL;
            }
        }
        // Set children
        StatementParse toReturn = new StatementParse("lookup", index);
        IdentifierParse child = new IdentifierParse(varName, index);
        toReturn.children.add(child);
        return toReturn;
    }

    // Parse identifier char
    private Parse parse_identifier_char(String str, int index) {
        System.out.println("parsing char " + index + " " + str.charAt(index));
        char first = str.charAt(index);
        String toReturn = "";
        if (Character.isLetterOrDigit(first) || first == '_') {
            toReturn += first;
            return new StatementParse(toReturn, index+1);
        } else {
            return Parser.FAIL;
        }
    }

    // Parse identifier first char
    private Parse parse_identifier_first_char(String str, int index) {
        System.out.println("first char " + index + " " + str.charAt(index));
        char first = str.charAt(index);
        String toReturn = "";
        if (Character.isLetter(first) || first == '_') {
            toReturn += first;
            return new StatementParse(toReturn, index+1);
        } else {
            return Parser.FAIL;
        }
    }

    // Parse print 
    private Parse parse_print_statement(String str, int index) {
        StatementParse print = new StatementParse("print", index);
        Parse counter;
        // Check for print
        if (str.indexOf("print", index) != index) {
            return Parser.FAIL;
        } else {
            index += 5;
        }
        // Parse req space
        System.out.println("parsing reqsp print " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "req_space");
        index = counter.getIndex();
        if (counter.equals(Parser.FAIL)) {
            return counter;
        }
        // Parse expression
        System.out.println("parsing exp print" + " " + index + " " + str.charAt(index));
        Parse childParse = this.parse(str, index, "expression");
        index = childParse.getIndex();
        if (childParse.equals(Parser.FAIL)) {
            return childParse;
        }
        print.children.add(childParse);
        // Parse opt_space
        System.out.println("parsing optsp print " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        index = counter.getIndex();
        // Parse semicolon
        System.out.println("parsing semicolon print " + index + " " + str.charAt(index));
        if (str.charAt(index) != ';') {
            return Parser.FAIL;
        } else {
            print.setIndex(index+1);
            return print;
        }
    }

    // Expression statement 
    private Parse parse_expression_statement(String str, int index) {
        Parse parse;
        Parse childParse;
        // Parse expression
        System.out.println("parsing exp expst" + " " + index + " " + str.charAt(index));
        parse = this.parse(str, index, "expression");
        index = parse.getIndex();
        if (parse.equals(Parser.FAIL)) {
            return parse;
        }
        // Parse opt_space
        System.out.println("parsing optsp expst " + index + " " + str.charAt(index));
        childParse = this.parse(str, index, "opt_space");
        index = childParse.getIndex();
        // Parse semicolon
        System.out.println("parsing semicolon expst " + index + " " + str.charAt(index));
        if (str.charAt(index) != ';') {
            return Parser.FAIL;
        } else {
            parse.setIndex(index+1);
            return parse;
        }
    }

    // Parse comment 
    private Parse parse_comment(String str, int index) {
        // Parse hashtag
        if (str.charAt(index) != '#') {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse text
        while (str.charAt(index) != '\n') {
            if (index != str.length() - 1) {
                index += 1;
            } else {
                break;
            }
        }
        return new Parse("#", index+1);
    }

    // Parse space
    private Parse parse_space(String str, int index) {
        Parse newParse;
        if (str.charAt(index) == '#') {
            // Parse comments 
            System.out.println("parsing comment space " + index + " " + str.charAt(index));
            newParse = this.parse(str, index, "comment");
            if (!newParse.equals(Parser.FAIL)) {
                return new Parse(str, newParse.getIndex());
            } 
        } else if (str.charAt(index) == ' ' || str.charAt(index) == '\t'){
            // Parse blank 
            return new Parse(str, index + 1);
        } else {
            // Parse newline
            if (str.charAt(index) != '\n') {
                return Parser.FAIL;
            } else {
                return new Parse(str, index + 1);
            }
        }
        return Parser.FAIL;
    }

    // Parse expression 
    private Parse parse_expression(String str, int index) {
        System.out.println("parsing or exp" + " " + index + " " + str.charAt(index));
        Parse expression = this.parse(str, index, "or");
        return expression;
    }

    // Parse or expression
    private Parse parse_or_expression(String str, int index) {
        Parse leftParse, rightParse;
        StatementParse parent;
        // Parse and expression
        System.out.println("parsing andex orex" + " " + index + " " + str.charAt(index));
        leftParse = this.parse(str, index, "and");
        if (leftParse == Parser.FAIL) {
            return Parser.FAIL;
        }
        // Set result and index
        index = leftParse.getIndex();
        // Loop 0 or more times
        while (index < str.length() && str.charAt(index) != ';') {
            // Parse opt space
            System.out.println("parsing optsp orex " + index + " " + str.charAt(index));
            index = parse_opt_space(str, index).getIndex();
            if (index < str.length()) {
                // or operator
                if (str.charAt(index) == '|' && str.charAt(index+1) == '|') {
                    System.out.println("parsing orop orex " + index + " " + str.charAt(index));
                    Parse temp = this.parse_or_operator(str, index);
                    // Set parent
                    parent = new StatementParse(temp.getName(), temp.getIndex());
                    // parse opt_space
                    System.out.println("parsing optsp orex " + index + " " + str.charAt(index));
                    index = parse_opt_space(str, temp.getIndex()).getIndex();
                    // parse optnot
                    System.out.println("parsing andex orex2" + " " + index + " " + str.charAt(index));
                    rightParse = this.parse(str, index, "and");
                    if (rightParse == Parser.FAIL) {
                        break;
                    }
                } else {
                    leftParse.setIndex(index);
                    break;
                }
                // update index
                parent.setIndex(rightParse.getIndex());
                // add child
                parent.children.add(leftParse);
                parent.children.add(rightParse);
                // finish
                leftParse = parent;
                index = rightParse.getIndex();
            } else {
                break;
            }
        }
        return leftParse;
    }

    // Parse or operator
    private Parse parse_or_operator(String str, int index) {
        // Parse ||
        if (str.indexOf("||", index) == index) {
            return new StatementParse("||", index + 2);
        } else {
            return Parser.FAIL;
        }
    }

    // Parse add sub expression 
    private Parse parse_add_sub_expression(String str, int index) {
        Parse leftParse, rightParse;
        StatementParse parent;
        // Parse mul/div expression
        System.out.println("parsing muldiv addsub1" + " " + index + " " + str.charAt(index));
        leftParse = this.parse(str, index, "mul_div");
        if (leftParse == Parser.FAIL) {
            return Parser.FAIL;
        }
        // Set result and index
        index = leftParse.getIndex();
        // Loop 0 or more times
        while (index < str.length() && str.charAt(index) != ';') {
            // Parse opt space
            System.out.println("parsing optsp addsub1 " + index + " " + str.charAt(index));
            index = parse_opt_space(str, index).getIndex();
            if (index < str.length()) {
                // add/sub operator
                if (str.charAt(index) == '+' || str.charAt(index) == '-') {
                    System.out.println("parsing asop print " + index + " " + str.charAt(index));
                    Parse temp = this.parse_as_operator(str, index);
                    // Set parent
                    parent = new StatementParse(temp.getName(), temp.getIndex());
                    // parse opt_space
                    System.out.println("parsing optsp addsub2 " + index + " " + str.charAt(index));
                    index = parse_opt_space(str, temp.getIndex()).getIndex();
                    // parse mul/div
                    System.out.println("parsing muldiv addsub2" + " " + index + " " + str.charAt(index));
                    rightParse = this.parse(str, index, "mul_div");
                    if (rightParse == Parser.FAIL) {
                        break;
                    }
                } else {
                    leftParse.setIndex(index);
                    break;
                }
                // update index
                parent.setIndex(rightParse.getIndex());
                // add child
                parent.children.add(leftParse);
                parent.children.add(rightParse);
                // finish
                leftParse = parent;
                index = rightParse.getIndex();
            } else {
                break;
            }
        }
        return leftParse;
    }

    // Parse and operator 
    private Parse parse_and_operator(String str, int index) {
        // Parse &&
        if (str.indexOf("&&", index) == index) {
            return new StatementParse("&&", index + 2);
        } else {
            return Parser.FAIL;
        }
    }

    // Parse call expression
    private Parse parse_call_expression(String str, int index) {
        Parse leftParse, rightParse;
        StatementParse parent;
        // Parse mul/div expression
        System.out.println("parsing operand callex" + " " + index + " " + str.charAt(index));
        leftParse = this.parse(str, index, "operand");
        if (leftParse == Parser.FAIL) {
            return Parser.FAIL;
        }
        // Set result and index
        index = leftParse.getIndex();
        // Loop 0 or more times
        while (index < str.length() && str.charAt(index) != ';') {
            // Parse opt space
            System.out.println("parsing optsp callexp " + index + " " + str.charAt(index));
            index = parse_opt_space(str, index).getIndex();
            if (index < str.length()) {
                // Set parent
                parent = new StatementParse("call", index);
                // parse funccall
                System.out.println("parsing funcall callex" + " " + index + " " + str.charAt(index));
                rightParse = this.parse(str, index, "function_call");
                if (rightParse == Parser.FAIL) {
                    break;
                }
                // update index
                parent.setIndex(rightParse.getIndex());
                // add child
                parent.children.add(leftParse);
                parent.children.add(rightParse);
                // finish
                leftParse = parent;
                index = rightParse.getIndex();
            } else {
                break;
            }
        }
        return leftParse;
    }

    // Parse function call
    private Parse parse_function_call(String str, int index) {
        Parse child, counter;
        // Parse (
        if (str.charAt(index) != '(') {
            return Parser.FAIL;
        }
        index += 1;
        // Parse opt space
        System.out.println("parsing optsp funccall" + " " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse arguments
        System.out.println("parsing arg funccall" + " " + index + " " + str.charAt(index));
        child = this.parse(str, index, "arguments");
        if (child.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child.getIndex();
        // Parse opt space
        System.out.println("parsing optsp funccall" + " " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse )
        if (str.charAt(index) != ')') {
            return Parser.FAIL;
        }
        index += 1;
        child.setIndex(index);
        return child;
    }

    // Parse arguments
    private Parse parse_arguments(String str, int index) {
        StatementParse toReturn = new StatementParse("arguments", index);
        Parse counter, child;
        // 0 or 1 times
        if (!this.parse_expression(str, index).equals(Parser.FAIL)) {
            // parse identifier
            System.out.println("parsing exp arg" + " " + index + " " + str.charAt(index));
            child = this.parse(str, index, "expression");
            if (child.equals(Parser.FAIL)) {
                return toReturn;
            }
            toReturn.children.add(child);
            index = child.getIndex();
            // Parse opt space
            System.out.println("parsing optsp arg" + " " + index + " " + str.charAt(index));
            counter = this.parse(str, index, "opt_space");
            if (counter.equals(Parser.FAIL)) {
                return Parser.FAIL;
            }
            index = counter.getIndex();
            // 0+ times
            while (true) {
                // Parse comma
                if (str.charAt(index) != ',') {
                    break;
                }
                index += 1;
                // Parse opt space
                System.out.println("parsing optsp arg" + " " + index + " " + str.charAt(index));
                counter = this.parse(str, index, "opt_space");
                if (counter.equals(Parser.FAIL)) {
                    break;
                }
                index = counter.getIndex();
                // Parse identifier
                System.out.println("parsing exp arg" + " " + index + " " + str.charAt(index));
                child = this.parse(str, index, "expression");
                if (child.equals(Parser.FAIL)) {
                    return Parser.FAIL;
                }
                toReturn.children.add(child);
                index = child.getIndex();
                // Parse opt space
                System.out.println("parsing optsp arg" + " " + index + " " + str.charAt(index));
                counter = this.parse(str, index, "opt_space");
                if (counter.equals(Parser.FAIL)) {
                    break;
                }
                index = counter.getIndex();
            }
        }
        toReturn.setIndex(index);
        return toReturn;
    }

    // Parse function
    private Parse parse_function(String str, int index) {
        Parse counter;
        StatementParse toReturn = new StatementParse("function", index);
        // Parse func
        if (!(str.charAt(index) == 'f' && str.charAt(index+1) == 'u' && str.charAt(index+2) == 'n' && str.charAt(index+3) == 'c')) {
            System.out.println(index + " " +  str.charAt(index));
            return Parser.FAIL;
        }
        index += 4;
        // Parse opt space
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        System.out.println("parsing optsp function" + " " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse (
        if (str.charAt(index) != '(') {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        System.out.println("parsing optsp function" + " " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // parse parameters 
        System.out.println("parsing para function" + " " + index + " " + str.charAt(index));
        Parse child = this.parse(str, index, "parameters");
        if (child.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child.getIndex();
        toReturn.children.add(child);
        // Parse opt space
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        System.out.println("parsing optsp function" + " " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse )
        if (str.charAt(index) != ')') {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space 
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        System.out.println("parsing optsp function" + " " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse {
            if (str.charAt(index) != '{') {
                return Parser.FAIL;
            } else {
                index += 1;
            }
        // Parse opt space
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        System.out.println("parsing optsp function" + " " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse program
        System.out.println("parsing program function" + " " + index + " " + str.charAt(index));
        child = this.parse(str, index, "program");
        if (child.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child.getIndex();
        toReturn.children.add(child);
        // Parse opt space
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        System.out.println("parsing optsp function" + " " + index + " " + str.charAt(index));
        counter = this.parse(str, index, "opt_space");
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // }
        if (str.charAt(index) != '}') {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        toReturn.setIndex(index);
        return toReturn;
    }

    // Parse parameters 
    private Parse parse_parameters(String str, int index) {
        StatementParse toReturn = new StatementParse("parameters", index);
        Parse counter, child;
        StatementParse childa;
        // 0 or 1 times
        if (!this.parse_identifier(str, index).equals(Parser.FAIL)) {
            // parse identifier
            System.out.println("parsing iden para" + " " + index + " " + str.charAt(index));
            child = this.parse(str, index, "identifier");
            if (child.equals(Parser.FAIL)) {
                return toReturn;
            }
            childa = (StatementParse)child;
            toReturn.children.add(childa.children.get(0));
            index = child.getIndex();
            // Parse opt space
            System.out.println("parsing optsp para" + " " + index + " " + str.charAt(index));
            counter = this.parse(str, index, "opt_space");
            if (counter.equals(Parser.FAIL)) {
                return Parser.FAIL;
            }
            index = counter.getIndex();
            // 0+ times
            while (true) {
                // Parse comma
                if (str.charAt(index) != ',') {
                    break;
                }
                index += 1;
                // Parse opt space
                System.out.println("parsing optsp para" + " " + index + " " + str.charAt(index));
                counter = this.parse(str, index, "opt_space");
                if (counter.equals(Parser.FAIL)) {
                    break;
                }
                index = counter.getIndex();
                // Parse identifier
                System.out.println("parsing iden para" + " " + index + " " + str.charAt(index));
                child = this.parse(str, index, "identifier");
                if (child.equals(Parser.FAIL)) {
                    return Parser.FAIL;
                }
                index = child.getIndex();
                childa = (StatementParse)child;
                toReturn.children.add(childa.children.get(0));
                // Parse opt space
                System.out.println("parsing optsp para" + " " + index + " " + str.charAt(index));
                counter = this.parse(str, index, "opt_space");
                if (counter.equals(Parser.FAIL)) {
                    break;
                }
                index = counter.getIndex();
            }
        }
        toReturn.setIndex(index);
        return toReturn;
    }

    // Parse optional not
    private Parse parse_optional_not_expression(String str, int index) {
        Parse toReturn;
        // parse comp expression
        System.out.println("parsing compex  opnot" + index + " " + str.charAt(index));
        toReturn = this.parse_comp_expression(str, index);
        if (!toReturn.equals(Parser.FAIL)) {
            return toReturn;
        }
        // or parse not expression 
        System.out.println("parsing notex opnot " + index + " " + str.charAt(index));
        toReturn = this.parse_not_expression(str, index);
        if (!toReturn.equals(Parser.FAIL)) {
            return toReturn;
        }
        return Parser.FAIL;
    }

    // Parse not expression
    private Parse parse_not_expression(String str, int index) {
        StatementParse toReturn = new StatementParse("!", index);
        // Parse !
        if (str.charAt(index) != '!') {
            return Parser.FAIL;
        } else {
            index += 1;
        }
        // Parse opt space
        System.out.println("parsing optsp notex " + index + " " + str.charAt(index));
        Parse counter = this.parse_opt_space(str, index);
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = counter.getIndex();
        // Parse comp expression
        System.out.println("parsing compex notex " + index + " " + str.charAt(index));
        counter = this.parse_comp_expression(str, index);
        index = counter.getIndex();
        if (counter.equals(Parser.FAIL)) {
            return Parser.FAIL;
        } else {
            toReturn.children.add(counter);
            toReturn.setIndex(index);
            return toReturn;
        }
    }

    // Parse comp expression
    private Parse parse_comp_expression(String str, int index) {
        StatementParse toReturn = new StatementParse("temp", index);
        // parse add sub expression 
        System.out.println("parsing addsub compex " + index + " " + str.charAt(index));
        Parse child1 = this.parse_add_sub_expression(str, index);
        if (child1.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        index = child1.getIndex();
        toReturn.children.add(child1);
        // 0 or 1 times
            // parse opt space
            if (index >= str.length()) {
                return Parser.FAIL;
            }
            System.out.println("parsing optsp compex " + index + " " + str.charAt(index));
            Parse counter = this.parse_opt_space(str, index);
            if (!counter.equals(Parser.FAIL)) {
                index = counter.getIndex();
                // parse comp operator
                System.out.println("parsing compop compex " + index + " " + str.charAt(index));
                counter = this.parse_comp_operator(str, index);
                if (!counter.equals(Parser.FAIL)) {
                    index = counter.getIndex();
                    // get name of operator
                    String name = counter.getName();
                    // parse opt space
                    System.out.println("parsing optsp compex " + index + " " + str.charAt(index));
                    counter = this.parse_opt_space(str, index);
                    if (!counter.equals(Parser.FAIL)) {
                        index = counter.getIndex();
                        // Parse add sub
                        System.out.println("parsing addsub compex " + index + " " + str.charAt(index));
                        counter = this.parse_add_sub_expression(str, index);
                        if (!counter.equals(Parser.FAIL)) {
                            // set name
                            toReturn.setName(name);
                            // set index
                            index = counter.getIndex();
                            toReturn.setIndex(index);
                            // set child
                            toReturn.children.add(counter);
                            return toReturn;
                        }
                    }
                    
                }
            }
            child1.setIndex(index);
            return child1;
    }

    // Parse comp operator
    private Parse parse_comp_operator(String str, int index) {
        // Parse ==
        if (str.indexOf("==", index) == index) {
            return new StatementParse("==", index + 2);
        }
        // or parse !=
        if (str.indexOf("!=", index) == index) {
            return new StatementParse("!=", index + 2);
        }
        // or parse <=
        if (str.indexOf("<=", index) == index) {
            return new StatementParse("<=", index + 2);
        }
        // or parse >=
        if (str.indexOf(">=", index) == index) {
            return new StatementParse(">=", index + 2);
        }
        // or parse <
        if (str.indexOf("<", index) == index) {
            return new StatementParse("<", index + 1);
        }
        // or parse >
        if (str.indexOf(">", index) == index) {
            return new StatementParse(">", index + 1);
        }
        return Parser.FAIL;
    }

    // Parses spaces
    private Parse parse_opt_space(String str, int index) {
        Parse newParse;
        // Parse zero or more spaces
        while (index < str.length()) {
            System.out.println("parsing space optsp " + index + " " + str.charAt(index));
            newParse = this.parse(str, index, "space");
            if (newParse.equals(Parser.FAIL)) {
                break;
            } else {
                index = newParse.getIndex();
            }
        }
        return new Parse("space", index);
    }

    private Parse parse_req_space(String str, int index) {
        Parse newParse;
        //System.out.println("reqspace " + index);
        if (str.charAt(index) != ' ' && str.charAt(index) != '\n') {
            return Parser.FAIL;
        }
        // Parse 1 or more spaces
        while (index <= str.length() + 1) {
            System.out.println("parsing space reqsp " + index + " " + str.charAt(index));
            newParse = this.parse(str, index, "space");
            if (newParse.equals(Parser.FAIL)) {
                break;
            } else {
                index = newParse.getIndex();
            }
        }
        return new Parse("space", index);
    }

    // Operand parser 
    private Parse parse_operand(String str, int index) {
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        // Check if parse is parenthesis
        System.out.println("parsing paren operand " + index + " " + str.charAt(index));
        Parse parse = this.parse(str, index, "parenthesis");
        if (!parse.equals(Parser.FAIL)) {
            return parse;
        }
        // Check if parse is function
        System.out.println("parsing func operand " + index + " " + str.charAt(index));
        parse = this.parse(str, index, "function");
        if (!parse.equals(Parser.FAIL)) {
            return parse;
        }
        // Check if parse is identifier
        System.out.println("parsing iden operand " + index + " " + str.charAt(index));
        parse = this.parse(str, index, "identifier");
        if (!parse.equals(Parser.FAIL)) {
            return parse;
        }
        // Check if parse is an integer
        System.out.println("parsing int operand " + index + " " + str.charAt(index));
        parse = this.parse(str, index, "integer");
        if (!parse.equals(Parser.FAIL)) {
            return parse;
        }
        return Parser.FAIL;
    }

    // Parenthesis parser
    private Parse parse_parenthesis(String str, int index) {
        // Check for open parenthesis
        if (str.charAt(index) != '(') {
            return Parser.FAIL;
        }
        index += 1;
        // Check for spaces
        System.out.println("parsing optsp paren " + index + " " + str.charAt(index));
        index = parse_opt_space(str, index).getIndex();
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        // Parse expression
        System.out.println("parsing exp paren " + index + " " + str.charAt(index));
        Parse parse = this.parse(str, index, "expression");
        index = parse.getIndex();
        if (parse.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        // Check for spaces
        System.out.println("parsing optsp paren " + index + " " + str.charAt(index));
        index = parse_opt_space(str, index).getIndex();
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        // Check for close parenthesis
        if (str.charAt(index) != ')') {
            return Parser.FAIL;
        }
        parse.setIndex(parse.getIndex() + 1); // to account for closing parenthesis
        return parse;
    }

    // Parse and expression
    private Parse parse_and_expression(String str, int index) {
        Parse leftParse, rightParse;
        StatementParse parent;
        // Parse optnot expression
        System.out.println("parsing optnot andex" + " " + index + " " + str.charAt(index));
        leftParse = this.parse(str, index, "optional_not");
        if (leftParse == Parser.FAIL) {
            return Parser.FAIL;
        }
        // Set result and index
        index = leftParse.getIndex();
        // Loop 0 or more times
        while (index < str.length() && str.charAt(index) != ';') {
            // Parse opt space
            System.out.println("parsing optsp andex " + index + " " + str.charAt(index));
            index = parse_opt_space(str, index).getIndex();
            if (index < str.length()) {
                // and operator
                if (str.charAt(index) == '&' && str.charAt(index+1) == '&') {
                    System.out.println("parsing andop andex " + index + " " + str.charAt(index));
                    Parse temp = this.parse_and_operator(str, index);
                    // Set parent
                    parent = new StatementParse(temp.getName(), temp.getIndex());
                    // parse opt_space
                    System.out.println("parsing optsp andex " + index + " " + str.charAt(index));
                    index = parse_opt_space(str, temp.getIndex()).getIndex();
                    // parse optnot
                    System.out.println("parsing optnot andex2" + " " + index + " " + str.charAt(index));
                    rightParse = this.parse(str, index, "optional_not");
                    if (rightParse == Parser.FAIL) {
                        break;
                    }
                } else {
                    leftParse.setIndex(index);
                    break;
                }
                // update index
                parent.setIndex(rightParse.getIndex());
                // add child
                parent.children.add(leftParse);
                parent.children.add(rightParse);
                // finish
                leftParse = parent;
                index = rightParse.getIndex();
            } else {
                break;
            }
        }
        return leftParse;
    }

    // Parse add / sub operator
    private Parse parse_as_operator(String str, int index) {
        if (str.charAt(index) == '+' || str.charAt(index) == '-') {
            String toReturn = Character.toString(str.charAt(index));
            return new StatementParse(toReturn, index + 1);
        } else {
            return Parser.FAIL;
        }
    }

    // Parse mul/div operator
    private Parse parse_md_operator(String str, int index) {
        if (str.charAt(index) == '*' || str.charAt(index) == '/') {
            String toReturn = Character.toString(str.charAt(index));
            return new StatementParse(toReturn, index + 1);
        } else {
            return Parser.FAIL;
        }
    }

    // Parse multiplication or division
    private Parse parse_mul_div_expression(String str, int index) {
        Parse leftParse, rightParse;
        StatementParse parent;
        // Parse call
        System.out.println("parsing callexp muldiv1 " + index + " " + str.charAt(index));
        leftParse = this.parse(str, index, "call_expression");
        if (leftParse == Parser.FAIL) {
            return leftParse;
        }
        // Set count
        index = leftParse.getIndex();
        // Loop 0 or more times
        while (index < str.length() && str.charAt(index) != ';') {
            // Parse opt_space
            System.out.println("parsing optsp muldiv1 " + index + " " + str.charAt(index));
            index = parse_opt_space(str, index).getIndex();
            // Parse mul_div_operator
            if (index < str.length()) {
                // multiply
                if (str.charAt(index) == '*' || str.charAt(index) == '/') {
                    System.out.println("parsing mdop muldiv " + index + " " + str.charAt(index));
                    Parse temp = this.parse_md_operator(str, index);
                    // Set parent
                    parent = new StatementParse(temp.getName(), temp.getIndex());
                    // parse opt space
                    System.out.println("parsing optsp muldiv2 " + index + " " + str.charAt(index));
                    index = parse_opt_space(str, temp.getIndex()).getIndex();
                    // parse call
                    System.out.println("parsing callexp muldiv2 " + index + " " + str.charAt(index));
                    rightParse = this.parse(str, index, "call_expression");
                    if (rightParse == Parser.FAIL) {
                        break;
                    }
                } else {
                    break;
                }
                // update index
                parent.setIndex(rightParse.getIndex());
                // add child
                parent.children.add(leftParse);
                parent.children.add(rightParse);
                // finishing
                leftParse = parent;
                index = rightParse.getIndex();
            }
        }
        return leftParse;
    }

    // Integer parser
    private Parse parse_integer(String str, int index) {
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        String parsed = "";
        // Start at starting index, continue for all digits
        while ((index < str.length()) && Character.isDigit(str.charAt(index))) {
            parsed += str.charAt(index);
            index++;
        }
        // Check if parse string is empty
        if (parsed.equals("")) {
            return Parser.FAIL;
        }
        // Return parsed string and index
        return new IntegerParse(Integer.parseInt(parsed), index);
    }

    private static void test(Parser parser, String str, String term, Parse expected) {
        Parse actual = parser.parse(str, term);
        if (actual == null) {
            throw new AssertionError("Got null when parsing \"" + str + "\"");
        }
        if (!actual.equals(expected)) {
            throw new AssertionError("Parsing \"" + str + "\"; expected " + expected + " but got " + actual);
        }
    }

    public static void main(String[] args) {
        // test();
        //System.out.println(Interpreter.exec((new Parser()).parse("5+(2/2)*4", 0, "program").toString()));
    }
}
