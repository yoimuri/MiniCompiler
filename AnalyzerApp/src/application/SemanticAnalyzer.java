package application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class SemanticAnalyzer {

    public static boolean analyze(List<Lexeme> lexemes, Map<Integer, String> code, StringBuilder results) {
        Map<String, Boolean> symbolTable = new HashMap<>();
        boolean valid = true;

        for (int i = 0; i < lexemes.size(); i++) {
            Lexeme lexeme = lexemes.get(i);

            if (lexeme.getToken() == Token.IDENTIFIER) {
                if (!symbolTable.containsKey(lexeme.getValue())) {
                    results.append("Semantic Error on line " + lexeme.getLine() + ": Variable '"
                                    + lexeme.getValue() + "' not declared.\n");
                    valid = false;
                }
            }

            if (lexeme.getToken() == Token.OPERATOR) {
                Lexeme prev = lexemes.get(i - 1);
                if (!symbolTable.containsKey(prev.getValue()) && !(prev.getValue().equals("out") && i > 0 && lexemes.get(i - 2).getValue().equals("System"))) {
                    results.append("Semantic Error on line " + lexeme.getLine() + ": Variable '" + prev.getValue() + "' not declared.\n");
                    valid = false;
                }
            }

            if (lexeme.getToken() == Token.KEYWORD && lexeme.getValue().equals("int")) {
                if (i + 1 < lexemes.size() && lexemes.get(i + 1).getToken() == Token.IDENTIFIER) {
                    symbolTable.put(lexemes.get(i + 1).getValue(), true);
                }
            }
        }
        return valid;
    }
}