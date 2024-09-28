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

    public String build(Exp e, Env env) {
        return (new Formatter()).visit(new Program(e), env);
    }

    private static class Formatter implements AST.Visitor<String> {
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

        @Override
        public String visit(DefExp e, Env env) {
            return "(Define " + e.name() + " " + e.exp().accept(this, env) + ")";
        }

        @Override
        public String visit(LambdaExp e, Env env) {
            StringBuilder result = new StringBuilder("(lambda ( ");
            for (String exp : e.params()) result.append(exp).append(" ");
            return result + ") " + e.body().accept(this, env) + ")";
        }

        @Override
        public String visit(CallExp e, Env env) {
            StringBuilder result = new StringBuilder("( ").append(e.e().accept(this, env)).append(" ");
            for (Exp exp : e.args()) result.append(exp.accept(this, env)).append(" ");
            return result + ")";
        }

        @Override
        public String visit(IfExp e, Env env) {
            return "( " + e.cond().accept(this, env) + " ? " + e.t_exp().accept(this, env) + " : " + e.f_exp().accept(this, env) + " )";
        }

        @Override
        public String visit(EqualExp e, Env env) {
            StringBuilder result = new StringBuilder("( = ");
            for (Exp exp : e.all()) result.append(exp.accept(this, env)).append(" ");
            return result + ")";
        }

        @Override
        public String visit(GtExp e, Env env) {
            StringBuilder result = new StringBuilder("( > ");
            for (Exp exp : e.all()) result.append(exp.accept(this, env)).append(" ");
            return result + ")";
        }

        @Override
        public String visit(LtExp e, Env env) {
            StringBuilder result = new StringBuilder("( < ");
            for (Exp exp : e.all()) result.append(exp.accept(this, env)).append(" ");
            return result + ")";
        }

        @Override
        public String visit(AndExp e, Env env) {
            StringBuilder result = new StringBuilder("( and ");
            for (Exp exp : e.all()) result.append(exp.accept(this, env)).append(" ");
            return result + ")";
        }

        @Override
        public String visit(OrExp e, Env env) {
            StringBuilder result = new StringBuilder("( or ");
            for (Exp exp : e.all()) result.append(exp.accept(this, env)).append(" ");
            return result + ")";
        }

        @Override
        public String visit(BoolExp e, Env env) {
            return "" + e.v();
        }

        @Override
        public String visit(PairExp e, Env env) {
            return "( pair " + e.first().accept(this, env) + ", " + e.second().accept(this, env) + " )";
        }

        @Override
        public String visit(FirstExp e, Env env) {
            return "( first " + e.exp().accept(this, env) + " )";
        }

        @Override
        public String visit(SecondExp e, Env env) {
            return "( second " + e.exp().accept(this, env) + " )";
        }

        @Override
        public String visit(ListExp e, Env env) {
            StringBuilder result = new StringBuilder("( list ");
            for (Exp exp : e.all()) result.append(exp.accept(this, env)).append(" ");
            return result + ")";
        }
    }
}
