package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import java.io.*;
import java.util.*;
import java.nio.file.Files;

public class AnalyzerApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        TextArea codeArea = new TextArea();
        TextArea resultArea = new TextArea();
        Button openFileButton = new Button("Open File");
        Button lexicalButton = new Button("Lexical Analysis");
        Button syntaxButton = new Button("Syntax Analysis");
        Button semanticButton = new Button("Semantic Analysis");
        Button clearButton = new Button("Clear");

        lexicalButton.setDisable(true);
        syntaxButton.setDisable(true);
        semanticButton.setDisable(true);

        openFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    codeArea.setText(content);
                    lexicalButton.setDisable(false); 
                } catch (IOException ex) {
                    resultArea.setText("Error reading file: " + ex.getMessage());
                }
            }
        });

        lexicalButton.setOnAction(e -> {
            try {
                Map<Integer, String> code = parseCode(codeArea.getText());
                LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
                List<Lexeme> lexemes = lexicalAnalyzer.analyzeCode(code);

                for (Lexeme lexeme : lexemes) {
                    if (lexeme.getToken() == Token.INVALID) {
                        resultArea.setText("Lexical Analysis Error: Invalid token detected at Line "
                                            + lexeme.getLine() + ", Position " + code.get(lexeme.getLine()).indexOf(lexeme.getValue()) 
                                            + ": \"" + lexeme.getValue() + "\"");
                        syntaxButton.setDisable(true);  
                        semanticButton.setDisable(true);
                        return;  
                    }
                }

                StringBuilder results = new StringBuilder();
                results.append("Lexical Analysis:\n");
                for (Lexeme lexeme : lexemes) {
                    results.append("Line ").append(lexeme.getLine()).append(": ")
                            .append(lexeme.getValue()).append(" (").append(lexeme.getToken()).append(")\n");
                }
                resultArea.setText(results.toString());
                syntaxButton.setDisable(false); 
                lexicalButton.setDisable(true); 
            } catch (Exception ex) {
                resultArea.setText("Lexical Analysis Error: " + ex.getMessage());
                syntaxButton.setDisable(true); 
                semanticButton.setDisable(true);
            }
        });

        syntaxButton.setOnAction(e -> {
            try {
                Map<Integer, String> code = parseCode(codeArea.getText());
                LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
                List<Lexeme> lexemes = lexicalAnalyzer.analyzeCode(code);

                StringBuilder results = new StringBuilder();
                boolean syntaxValid = SyntaxAnalyzer.analyze(lexemes);
                results.append("\nSyntax Analysis: ").append(syntaxValid ? "No Syntax Errors\n" : "Syntax Errors Found\n");

                resultArea.appendText(results.toString());

                if (syntaxValid) {
                    semanticButton.setDisable(false);
                } else {
                    semanticButton.setDisable(true);  
                }

                syntaxButton.setDisable(true); 
            } catch (Exception ex) {
                resultArea.setText("Syntax Analysis Error: " + ex.getMessage());
                semanticButton.setDisable(true);  
            }
        });

        semanticButton.setOnAction(e -> {
            try {
                Map<Integer, String> code = parseCode(codeArea.getText());
                LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
                List<Lexeme> lexemes = lexicalAnalyzer.analyzeCode(code);

                StringBuilder results = new StringBuilder();
                boolean semanticValid = SemanticAnalyzer.analyze(lexemes, code, results);
                results.append("\nSemantic Analysis: ").append(semanticValid ? "No Semantic Errors\n" : "Semantic Errors Found\n");

                resultArea.appendText(results.toString());
                semanticButton.setDisable(true); 
            } catch (Exception ex) {
                resultArea.setText("Semantic Analysis Error: " + ex.getMessage());
            }
        });

        clearButton.setOnAction(e -> {
            codeArea.clear();
            resultArea.clear();
            lexicalButton.setDisable(true);
            syntaxButton.setDisable(true);
            semanticButton.setDisable(true);
        });

        root.getChildren().addAll(openFileButton, codeArea, lexicalButton, syntaxButton, semanticButton, clearButton, resultArea);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setTitle("Analyzer App");
        primaryStage.show();
    }

    private Map<Integer, String> parseCode(String code) {
        Map<Integer, String> lines = new HashMap<>();
        String[] split = code.split("\\n");
        for (int i = 0; i < split.length; i++) {
            lines.put(i + 1, split[i]);  
        }
        return lines;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
