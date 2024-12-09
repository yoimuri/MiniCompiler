# MiniCompiler

Lexical Analysis - Read the input file, turn it into string, read it char per char, and turn it into a list of tokens.
Syntax Analysis - Read the list of tokens, checks if the syntax is valid through recursive descent parsing.
Semantic Analysis - Read the list of tokens, and checks if there are any semantic errors while parsing.

HOW TO RUN:
1. You need to have JavaFX installed on your PC
2. Open using IDE (ex. Eclipse, IntelliJ)
3. Right click the folder then find Build Path -> Configure Build Path
4. Remove JavaFX in Classpath if it is there.
5. Click Modulepath then click Add Library -> User Library -> tick box JavaFX
6. Apply and close.
7. Before running AnalyzerApp.java, click Run Configurations... then Arguments then proceed to VM Arguments
8. use this command to link your downloaded javaFX file path: --module-path "path-to-javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
9. Click Apply and Run. It should show something like this:

    
    ![analyzer](https://github.com/user-attachments/assets/01f1b38f-a463-426b-a352-29afef3ab7c3)


![correct](https://github.com/user-attachments/assets/c6d04748-02b7-4c88-9316-77fc0014ff36)

