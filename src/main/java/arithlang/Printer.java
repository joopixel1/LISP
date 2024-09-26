package arithlang;

import java.util.Map;

import static arithlang.AST.*;

public class Printer {
    public void print(Value v) {
        System.out.println(v.toString());
    }

    public void print(Exception e) {
        System.err.println("Error:" + e.getMessage());
    }

    public void print(Program p) {
        System.out.println((new Formatter()).visit(p, new Env.EmptyEnv()));
    }

    private class Formatter implements AST.Visitor<String> {
        public String visit(Program p, Env env) {
            return (String) p.e().accept(this, env);
        }

        public String visit(NumExp e, Env env) {
            return "" + e.v();
        }

        public String visit(AddExp e, Env env) {
            StringBuilder result = new StringBuilder("(+");
            for (AST.Exp exp : e.all())
                result.append(" ").append(exp.accept(this, env));
            return result + ")";
        }

        public String visit(SubExp e, Env env) {
            StringBuilder result = new StringBuilder("(-");
            for (AST.Exp exp : e.all())
                result.append(" ").append(exp.accept(this, env));
            return result + ")";
        }

        public String visit(MultExp e, Env env) {
            StringBuilder result = new StringBuilder("(*");
            for (AST.Exp exp : e.all())
                result.append(" ").append(exp.accept(this, env));
            return result + ")";
        }

        public String visit(DivExp e, Env env) {
            StringBuilder result = new StringBuilder("(/");
            for (AST.Exp exp : e.all())
                result.append(" ").append(exp.accept(this, env));
            return result + ")";
        }

        public String visit(IntDivExp e, Env env) {
            StringBuilder result = new StringBuilder("(//");
            for (AST.Exp exp : e.all())
                result.append(" ").append(exp.accept(this, env));
            return result + ")";
        }

        @Override
        public String visit(PowExp e, Env env) {
            StringBuilder result = new StringBuilder("(^");
            for (AST.Exp exp : e.all())
                result.append(" ").append(exp.accept(this, env));
            return result + ")";
        }

        @Override
        public String visit(VarExp e, Env env) {
            return e.name();
        }

        @Override
        public String visit(LetExp e, Env env) {
            StringBuilder result = new StringBuilder("(let (");
            for (Map.Entry<String, Exp> entry : e.getDeclaration().entrySet())
                result.append("(").append(entry.getKey()).append(" ").append(entry.getValue().accept(this, env)).append(")");
            return result + ") (" + e.getBody().accept(this, env) + ") )";
        }
    }
}
