package application;

import java.util.*;
import java.util.regex.*;

public class LexicalAnalyzer {
    private static final Map<Token, String> TOKEN_PATTERNS = new LinkedHashMap<>();
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "int", "boolean", "for", "while", "do", "if", "else", "print", "switch", "case", "default", "null", "System"
    ));

    static {
        TOKEN_PATTERNS.put(Token.KEYWORD, "\\b(int|boolean|System)\\b");
        TOKEN_PATTERNS.put(Token.IDENTIFIER, "[a-zA-Z][a-zA-Z0-9_]*");
        TOKEN_PATTERNS.put(Token.INTEGER, "\\b\\d+\\b");
        TOKEN_PATTERNS.put(Token.OPERATOR, "=");
        TOKEN_PATTERNS.put(Token.PLUS, "\\+");
        TOKEN_PATTERNS.put(Token.TIMES, "\\*");
        TOKEN_PATTERNS.put(Token.SEMICOLON, ";");
        TOKEN_PATTERNS.put(Token.LEFT_PARENTHESIS, "\\(");
        TOKEN_PATTERNS.put(Token.RIGHT_PARENTHESIS, "\\)");
        TOKEN_PATTERNS.put(Token.DOT, "\\.");
        TOKEN_PATTERNS.put(Token.INVALID, ".+"); 
    }

    public List<Lexeme> analyzeCode(Map<Integer, String> code) {
        List<Lexeme> lexemes = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : code.entrySet()) {
            int line = entry.getKey();
            String content = entry.getValue().trim(); 

            int position = 0;
            while (position < content.length()) {
                boolean matched = false;

                if (Character.isWhitespace(content.charAt(position))) {
                    position++;
                    continue;
                }

                for (Map.Entry<Token, String> patternEntry : TOKEN_PATTERNS.entrySet()) {
                    Pattern pattern = Pattern.compile(patternEntry.getValue());
                    Matcher matcher = pattern.matcher(content.substring(position));

                    if (matcher.lookingAt()) {
                        String tokenValue = matcher.group();
                        Token tokenType = patternEntry.getKey();

                        if (tokenType == Token.IDENTIFIER && KEYWORDS.contains(tokenValue)) {
                            tokenType = Token.KEYWORD; 
                        }


                        if (tokenType == Token.IDENTIFIER && !isValidIdentifier(tokenValue)) {
                            tokenType = Token.INVALID; 
                        }

                        lexemes.add(new Lexeme(line, tokenValue, tokenType));
                        position += tokenValue.length();
                        matched = true;
                        break; 
                    }
                }

                if (!matched) {
                    lexemes.add(new Lexeme(line, String.valueOf(content.charAt(position)), Token.INVALID));
                    position++;
                }
            }
        }
        return lexemes;
    }

    private boolean isValidIdentifier(String value) {
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