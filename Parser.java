public class Parser {

    // Fail parse case
    static Parse FAIL = new Parse("fail", -1);

    // Wrapper parse - sets general parse index to 0
    public Parse parse(String str, String term) {
        return this.parse(str, 0, term);
    }

    // General parse
    private Parse parse(String str, int index, String term) {
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
        } else {
            throw new AssertionError("Unexpected term " + term);
        }
    }

    // Parses spaces
    private Parse parse_opt_space(String str, int index) {
        // Parse 0 or more spaces
        while (index < str.length()) {
            if (str.charAt(index) == ' ') {
                index += 1;
            } else {
                break;
            }
        }
        return new Parse("space", index);
    }

    private Parse parse_req_space(String str, int index) {
        if (str.charAt(index) != ' ') {
            return Parser.FAIL;
        }
        // Parse 1 or more spaces
        while (index < str.length()) {
            if (str.charAt(index) == ' ') {
                index += 1;
            } else {
                break;
            }
        }
        return new Parse("space", index);
    }

    // Operand parser (parenthesis or integer)
    private Parse parse_operand(String str, int index) {
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        // Check if parse is an integer
        Parse parse = this.parse(str, index, "integer");
        if (!parse.equals(Parser.FAIL)) {
            return parse;
        }
        // Check if parse is parenthesis
        parse = this.parse(str, index, "parenthesis");
        if (!parse.equals(Parser.FAIL)) {
            return parse;
        }
        return Parser.FAIL;
    }

    // Parenthesis parser
    private Parse parse_parenthesis(String str, int index) {
        // Check for spaces
        index = parse_opt_space(str, index).getIndex();
        if (index >= str.length()) {
            return Parser.FAIL;
        }
        // Check for open parenthesis
        if (str.charAt(index) != '(') {
            return Parser.FAIL;
        }
        // Parse operation
        Parse parse = this.parse(str, index + 1, "add_sub");
        index = parse.getIndex();
        if (parse.equals(Parser.FAIL)) {
            return Parser.FAIL;
        }
        // Check for spaces
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

    // Parse addition or subtraction
    private Parse parse_add_sub_expression(String str, int index) {
        Parse leftParse, rightParse;
        StatementParse parent;
        // Parse mul/div expression
        leftParse = this.parse(str, index, "mul_div");
        if (leftParse == Parser.FAIL) {
            return Parser.FAIL;
        }
        // Set result and index
        index = leftParse.getIndex();
        // Loop 0 or more times
        while (index < str.length()) {
            // Parse opt space
            index = parse_opt_space(str, index).getIndex();
            // Parse add/sub operator
            if (index < str.length()) {
                // add
                if (str.charAt(index) == '+') {
                    // Set parent
                    parent = new StatementParse("+", index);
                    // parse opt_space
                    index = parse_opt_space(str, index).getIndex();
                    // parse mul/div
                    rightParse = this.parse(str, index + 1, "mul_div");
                    if (rightParse == Parser.FAIL) {
                        break;
                    }
                    // add child
                    parent.children.add(leftParse);
                    parent.children.add(rightParse);
                    // subtract
                } else if (str.charAt(index) == '-') {
                    // Set parent
                    parent = new StatementParse("-", index);
                    // parse opt space
                    index = parse_opt_space(str, index).getIndex();
                    // parse mul_div
                    rightParse = this.parse(str, index + 1, "mul_div");
                    if (rightParse == Parser.FAIL) {
                        break;
                    }
                    // add child
                    parent.children.add(leftParse);
                    parent.children.add(rightParse);
                } else {
                    break;
                }
                leftParse = parent;
                index = rightParse.getIndex();
            } else {
                break;
            }
        }
        return leftParse;
    }

    // Parse multiplication or division
    private Parse parse_mul_div_expression(String str, int index) {
        Parse leftParse, rightParse;
        StatementParse parent;
        // Parse operand
        leftParse = this.parse(str, index, "operand");
        if (leftParse == Parser.FAIL) {
            return leftParse;
        }
        // Set count
        index = leftParse.getIndex();
        // Loop 0 or more times
        while (index < str.length()) {
            // Parse opt_space
            index = parse_opt_space(str, index).getIndex();
            // Parse mul_div_operator
            if (index < str.length()) {
                // multiply
                if (str.charAt(index) == '*') {
                    // Set parent
                    parent = new StatementParse("*", index);
                    // parse opt space
                    index = parse_opt_space(str, index).getIndex();
                    // parse operand
                    rightParse = this.parse(str, index + 1, "operand");
                    if (rightParse == Parser.FAIL) {
                        break;
                    }
                    // add child
                    parent.children.add(leftParse);
                    parent.children.add(rightParse);
                    // divide
                } else if (str.charAt(index) == '/') {
                    // Set parent
                    parent = new StatementParse("/", index);
                    // parse opt space
                    index = parse_opt_space(str, index).getIndex();
                    // parse operand
                    rightParse = this.parse(str, index + 1, "operand");
                    if (rightParse == Parser.FAIL) {
                        break;
                    }
                    // add child
                    parent.children.add(leftParse);
                    parent.children.add(rightParse);
                } else {
                    break;
                }
                leftParse = parent;
                index = rightParse.getIndex();
            }
        }
        return leftParse;
    }

    // Integer parser
    private Parse parse_integer(String str, int index) {
        // Check for spaces
        index = parse_opt_space(str, index).getIndex();
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

    /*
     * public static void test() { Parser parser = new Parser(); // Integer test
     * cases test(parser, "b", "integer", Parser.FAIL); test(parser, "", "integer",
     * Parser.FAIL); test(parser, "1", "integer", new Parse(1, 1)); test(parser,
     * "2", "integer", new Parse(2, 1)); test(parser, "10", "integer", new Parse(10,
     * 2)); test(parser, "100", "integer", new Parse(100, 3)); // Addition test
     * cases test(parser, "b", "add_sub", Parser.FAIL); test(parser, "", "add_sub",
     * Parser.FAIL); test(parser, "1++", "add_sub", new Parse(1, 1)); test(parser,
     * "1+1+", "add_sub", new Parse(2, 3)); test(parser, "10+5", "add_sub", new
     * Parse(15, 4)); test(parser, "0+0", "add_sub", new Parse(0, 3)); test(parser,
     * "5+0+7", "add_sub", new Parse(12, 5)); test(parser, "200+50", "add_sub", new
     * Parse(250, 6)); test(parser, "10+20+30", "add_sub", new Parse(60, 8)); //
     * Parenthesis test cases test(parser, "(1)", "parenthesis", new Parse(1, 3));
     * test(parser, "(0+0)", "parenthesis", new Parse(0, 5)); test(parser, "(1+1)",
     * "parenthesis", new Parse(2, 5)); test(parser, "(10+20+30)", "parenthesis",
     * new Parse(60, 10)); test(parser, "1+(1+1)", "add_sub", new Parse(3, 7));
     * test(parser, "1+(1+1)+1", "add_sub", new Parse(4, 9)); // Spacing test cases
     * test(parser, " 1", "integer", new Parse(1, 2)); test(parser, "4 ", "integer",
     * new Parse(4, 1)); test(parser, "1 + 1", "add_sub", new Parse(2, 5));
     * test(parser, "( 1 + 1 )", "parenthesis", new Parse(2, 9)); test(parser,
     * "1+ 4", "add_sub", new Parse(5, 4)); test(parser, " 6+ ( 9 )", "add_sub", new
     * Parse(15, 9)); // Mixed equation test cases test(parser, "5*3+2", "add_sub",
     * new Parse(17, 5)); test(parser, "( 5+3 ) * 2", "add_sub", new Parse(16, 11));
     * test(parser, "3*2*3", "mul_div", new Parse(18, 5)); test(parser, " 4/2",
     * "mul_div", new Parse(2, 4)); test(parser, " 4 / (2)", "mul_div", new Parse(2,
     * 8)); test(parser, " 0/4", "mul_div", new Parse(0, 4)); }
     */

    public static void main(String[] args) {
        // test();
        System.out.println((new Parser()).parse("2+2*2", "add_sub").toString());
    }

}