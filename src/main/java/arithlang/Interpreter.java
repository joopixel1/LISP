package arithlang;

import arithlang.AST.Program;

/**
 * This main class implements the Read-Eval-Print-Loop of the interpreter with
 * the help of Reader, Evaluator, and Printer classes.
 *
 * @author hridesh, clay
 */
public class Interpreter {
    public static void main(String[] args) {
        System.out.println("""
                Type a program to evaluate and press the enter key, e.g. (+ (* 3 100) (/ 84 (- 279 277)))
                Press Ctrl + C to exit.
                """);
        try (Reader reader = new Reader()) {
            Evaluator eval = new Evaluator();
            Printer printer = new Printer();
            // Read-Eval-Print-Loop (also known as REPL)
            while (true) {
                Program p;
                try {
                    p = reader.read();
                    if (p == null) {
                        System.out.println();
                        break;
                    } else if (p.e() == null) {
                        System.out.println();
                        continue;
                    }
                    printer.print(p);
                    Value val = eval.valueOf(p);
                    printer.print(val);
                } catch (Exception e) {
                    printer.print(e);
                }
            }
        } catch (Exception e) {
            System.err.println("Error closing input stream:" + e.getMessage());
        }
    }
}
