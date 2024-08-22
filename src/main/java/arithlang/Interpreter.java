package arithlang;

import arithlang.AST.Program;

import java.io.IOException;

/**
 * This main class implements the Read-Eval-Print-Loop of the interpreter with
 * the help of Reader, Evaluator, and Printer classes.
 *
 * @author hridesh
 */
public class Interpreter {
    public static void main(String[] args) {
        System.out.println("Type a program to evaluate and press the enter key," + " e.g. (+ (* 3 100) (/ 84 (- 279 277))) \n" + "Press Ctrl + C to exit.");
        Reader reader = new Reader();
        Evaluator eval = new Evaluator();
        Printer printer = new Printer();
        // Read-Eval-Print-Loop (also known as REPL)
        //noinspection InfiniteLoopStatement
        while (true) {
            Program p;
            try {
                p = reader.read();
                if (p._e == null) continue;
                Value val = eval.valueOf(p);
                printer.print(val);
            } catch (IOException e) {
                System.out.println("Error reading input:" + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
    }
}
