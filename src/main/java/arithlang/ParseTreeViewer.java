package arithlang;

import arithlang.parser.ArithLangLexer;
import arithlang.parser.ArithLangParser;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ParseTreeViewer {
    public static void main(String[] args) throws IOException {
        System.out.println("Type a program to and press the enter key to display the parse tree for the program," + " e.g. (+ (* 3 100) (/ 84 (- 279 277)))");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("$ ");
        String programText = br.readLine();

        System.out.println("\nLoading parse tree, please wait...");

        CharStream s = CharStreams.fromString(programText);
        Lexer l = new ArithLangLexer(s);
        ArithLangParser p = new ArithLangParser(new org.antlr.v4.runtime.CommonTokenStream(l));

        //show AST in GUI
        JFrame frame = new JFrame("Antlr AST");
        JPanel panel = new JPanel();
        TreeViewer viewer = new TreeViewer(Arrays.asList(p.getRuleNames()), p.program());
        viewer.setScale(1.5); // Scale a little
        panel.add(viewer);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
