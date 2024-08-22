package arithlang;

import arithlang.AST.Program;
import arithlang.parser.ArithLangLexer;
import arithlang.parser.ArithLangParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reader {

    public Program read() throws IOException {
        String programText = readNextProgram();
        return parse(programText);
    }

    public Program parse(String programText) {
        Lexer l = getLexer(CharStreams.fromString(programText));
        ArithLangParser p = getParser(new org.antlr.v4.runtime.CommonTokenStream(l));
        return p.program().ast;
    }

    protected Lexer getLexer(CharStream s) {
        return new ArithLangLexer(s);
    }

    protected ArithLangParser getParser(org.antlr.v4.runtime.CommonTokenStream s) {
        return new ArithLangParser(s);
    }

    protected String readNextProgram() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("$ ");
        String programText = br.readLine();
        return runFile(programText);
    }

    @SuppressWarnings("SameReturnValue")
    protected String getProgramDirectory() {
        return "arithlang/src/main/java/arithlang/examples/";
    }

    protected String runFile(String programText) throws IOException {
        if (programText.startsWith("run ")) {
            programText = readFile(getProgramDirectory() + programText.substring(4));
        }
        return programText;
    }

    private String readFile(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }

}
