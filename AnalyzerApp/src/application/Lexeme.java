package application;

public class Lexeme {
    private final int line;
    private final String value;
    private final Token token;

    public Lexeme(int line, String value, Token token) {
        this.line = line;
        this.value = value;
        this.token = token;
    }

    public int getLine() {
        return line;
    }

    public String getValue() {
        return value;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Lexeme{" +
                "line=" + line +
                ", value='" + value + '\'' +
                ", token=" + token +
                '}';
    }
}