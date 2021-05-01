public class Closure extends Value {
    StatementParse parameters;
    StatementParse body;
    Environment currentEnv;

    public Closure(StatementParse parameters, StatementParse body, Environment currentEnv) {
        this.parameters = parameters;
        this.body = body;
        this.currentEnv = currentEnv;
    }

    @Override
    public String toString() {
        return "closure";
    }
}
