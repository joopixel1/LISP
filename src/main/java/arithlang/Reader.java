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

public class Reader implements AutoCloseable {

    private final BufferedReader br;

    public Reader() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void close() throws Exception {
        br.close();
    }

    public Program read() throws IOException {
        String programText = readNextProgram();
        if (programText == null) {
            return null;
        } else {
            return parse(programText);
        }
    }

    private String readNextProgram() throws IOException {
        String programText = br.readLine();
        if (programText == null) {
            return null;
        } else {
            return runFile(programText);
        }
    }

    Program parse(String programText) {
        Lexer l = new ArithLangLexer(CharStreams.fromString(programText));
        ArithLangParser p = new ArithLangParser(new org.antlr.v4.runtime.CommonTokenStream(l));
        return p.program().ast;
    }

    String runFile(String programText) throws IOException {
        if (programText.startsWith("run ")) {
            programText = readFile(getProgramDirectory() + programText.substring(4));
        }
        return programText;
    }

    private String getProgramDirectory() {
        return "src/main/java/arithlang/examples/";
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
