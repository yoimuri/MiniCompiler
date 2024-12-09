package application;

import java.util.List;

public class SyntaxAnalyzer {
    public static boolean analyze(List<Lexeme> lexemes) {
        int i = 0;

        while (i < lexemes.size()) {
            Lexeme lexeme = lexemes.get(i);
            System.out.println("Processing: " + lexeme);

            if (lexeme.getToken() == Token.KEYWORD && (lexeme.getValue().equals("int") || lexeme.getValue().equals("boolean"))) {
                if (i + 1 < lexemes.size() && lexemes.get(i + 1).getToken() == Token.IDENTIFIER) {
                    i++; 
                    if (i + 1 < lexemes.size() && lexemes.get(i + 1).getToken() == Token.OPERATOR && lexemes.get(i + 1).getValue().equals("=")) {
                        i++; 
                        if (i + 1 < lexemes.size() && (lexemes.get(i + 1).getToken() == Token.INTEGER || lexemes.get(i + 1).getToken() == Token.IDENTIFIER)) {
                            i++; 
                        } else {
                            System.err.println("Syntax error: Expected value after '=' at line " + lexeme.getLine());
                            return false;
                        }
                    } else {
                        System.err.println("Syntax error: Expected '=' after identifier at line " + lexeme.getLine());
                        return false;
                    }
                } else {
                    System.err.println("Syntax error: Expected identifier after keyword " + lexeme.getValue() + " at line " + lexeme.getLine());
                    return false;
                }
            }

            else if (lexeme.getToken() == Token.IDENTIFIER) {
                if (!isValidIdentifier(lexeme.getValue())) {
                    System.err.println("Syntax error: Invalid identifier '" + lexeme.getValue() + "' at line " + lexeme.getLine());
                    return false;
                }

                if (i + 1 < lexemes.size() && lexemes.get(i + 1).getToken() == Token.OPERATOR && lexemes.get(i + 1).getValue().equals("=")) {
                    i++; 
                    if (i + 1 < lexemes.size() && (lexemes.get(i + 1).getToken() == Token.INTEGER || lexemes.get(i + 1).getToken() == Token.IDENTIFIER)) {
                        i++; 
                        while (i + 1 < lexemes.size() && lexemes.get(i + 1).getToken() == Token.OPERATOR) {
                            i++; 
                            if (i + 1 < lexemes.size() && (lexemes.get(i + 1).getToken() == Token.INTEGER || lexemes.get(i + 1).getToken() == Token.IDENTIFIER)) {
                                i++; 
                            } else {
                                System.err.println("Syntax error: Expected value after operator at line " + lexeme.getLine());
                                return false;
                            }
                        }
                    } else {
                        System.err.println("Syntax error: Expected value after '=' at line " + lexeme.getLine());
                        return false;
                    }
                }
            }

            else if (lexeme.getToken() == Token.IDENTIFIER) {
                if (i + 1 < lexemes.size()) {
                    Lexeme nextLexeme = lexemes.get(i + 1);

                    if ((nextLexeme.getToken() == Token.IDENTIFIER || nextLexeme.getToken() == Token.INTEGER) &&
                        (i + 2 >= lexemes.size() || lexemes.get(i + 2).getToken() != Token.OPERATOR)) {
                        System.err.println("Syntax error: Missing operator between operands at line " + lexeme.getLine());
                        return false;
                    }
                }
            }

            else if (lexeme.getToken() == Token.KEYWORD && lexeme.getValue().equals("print")) {
                if (i + 1 < lexemes.size() && lexemes.get(i + 1).getToken() == Token.IDENTIFIER) {
                    i++; 
                } else {
                    System.err.println("Syntax error: Expected identifier after 'print' at line " + lexeme.getLine());
                    return false;
                }
            }

            i++;
        }

        return true; 
    }

    private static boolean isValidIdentifier(String value) {
        if (value.isEmpty() || (!Character.isLetter(value.charAt(0)) && value.charAt(0) != '_')) {
            return false;
        }

        for (int i = 1; i < value.length(); i++) {
            if (!Character.isLetterOrDigit(value.charAt(i)) && value.charAt(i) != '_') {
                return false;
            }
        }

        return true;
    }
}
