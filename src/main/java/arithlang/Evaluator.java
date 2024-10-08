package arithlang;

import java.util.*;

import static arithlang.AST.*;
import static arithlang.Value.*;
import static arithlang.Env.*;

public class Evaluator implements Visitor<Value> {
    private final Printer.Formatter formatter = new Printer.Formatter();
    private final GlobalEnv initialEnv = new GlobalEnv();


    public Value valueOf(Program p) {
        return (Value) p.accept(this, initialEnv);
    }

    @Override
    public Value visit(Program p, Env env) {
        for (DefDecl d : p.defs()) d.accept(this, initialEnv);
        return (Value) p.e().accept( this, env);
    }

    @Override
    public Value visit(DefDecl e, Env env) {
        Object o = e.exp().accept(this, env);
        if (!(o instanceof AssignableValue val)) throw new InterpreterException("Expression does not evaluate to assignable value " + formatter.visit(e, env));
        initialEnv.extend(e.name(), val);
        return UnitVal.UNIT_VAL;
    }

    @Override
    public Value visit(UnitExp e, Env env) {
        return UnitVal.UNIT_VAL;
    }

    @Override
    public Value visit(NumExp e, Env env) {
        return e.v();
    }

    @Override
    public Value visit(AddExp e, Env env) {
        // semantics for add expression -- the result is the result of
        // summing each of the operands
        double result = 0;
        List<Exp> operands = e.all();
        for (Exp operand : operands) {
            Object o = operand.accept(this, env);
            if (!(o instanceof NumVal val)) throw new InterpreterException("Expression does not evaluate to numerical value " + formatter.visit(e, env));
            result += val.v();
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(SubExp e, Env env) {
        // semantics for sub expression -- the result is the result of
        // subtracting each of the operands, in sequence, from left to right
        List<Exp> operands = e.all();
        NumVal lVal = (NumVal) operands.get(0).accept( this, env);
        double result = lVal.v();
        for (int i = 1; i < operands.size(); i++) {
            Object o = operands.get(i).accept(this, env);
            if (!(o instanceof NumVal rVal)) throw new InterpreterException("Expression does not evaluate to numerical value " + formatter.visit(e, env));
            result = result - rVal.v();
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(MultExp e, Env env) {
        // semantics for mult expression -- the result is the result of
        // multiplying each of the operands
        double result = 1;
        List<Exp> operands = e.all();
        for (Exp operand : operands) {
            Object o = operand.accept(this, env);
            if (!(o instanceof NumVal val)) throw new InterpreterException("Expression does not evaluate to numerical value " + formatter.visit(e, env));
            result *= val.v();
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(DivExp e, Env env) {
            // semantics for div expression -- the result is the result of
            // dividing each of the operands, in sequence, from left to
            // right
            List<Exp> operands = e.all();
            NumVal lVal = (NumVal) operands.get(0).accept(this, env);
            double result = lVal.v();
            for (int i = 1; i < operands.size(); i++) {
                Object o = operands.get(i).accept(this, env);
                if (!(o instanceof NumVal rVal)) throw new InterpreterException("Expression does not evaluate to numerical value " + formatter.visit(e, env));
                result = result / rVal.v();
            }
            return new NumVal(result);
    }

    @Override
    public Value visit(IntDivExp e, Env env) {
        // semantics for div expression -- the result is the result of
        // dividing each of the operands, in sequence, from left to
        // right
        List<Exp> operands = e.all();
        NumVal lVal = (NumVal) operands.get(0).accept(this, env);
        double result = lVal.v();
        for (int i = 1; i < operands.size(); i++) {
            Object o = operands.get(i).accept(this, env);
            if (!(o instanceof NumVal rVal)) throw new InterpreterException("Expression does not evaluate to numerical value " + formatter.visit(e, env));
            result = ((int) (result / rVal.v()));
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(PowExp e, Env env) {
        // semantics for mult expression -- the result is the result of
        // multiplying each of the operands
        double result = 1;
        List<Exp> operands = e.all();
        for (int i = operands.size() - 1; i >= 0; i--) {
            Object o = operands.get(i).accept(this, env);
            if (!(o instanceof NumVal val)) throw new InterpreterException("Expression does not evaluate to numerical value " + formatter.visit(e, env));
            result = Math.pow(val.v(), result);
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(VarExp e, Env env) {
        return env.get(e.name());
    }

    @Override
    public Value visit(LetExp e, Env env) {
        LinkedHashMap<String, Exp> exps = e.getDeclaration();

        Env new_env = env;
        for (Map.Entry<String, Exp> entry : e.getDeclaration().entrySet()){
            new_env = new ExtendEnv(new_env, entry.getKey(), (AssignableValue) entry.getValue().accept(this, new_env));
        }

        return (Value) e.getBody().accept(this, new_env);
    }

    @Override
    public Value visit(LambdaExp e, Env env) {
        return new FuncVal(e.body(), e.params(), env);
    }

    @Override
    public Value visit(CallExp e, Env env) {
        Exp operator = e.e();
        List<Exp> operands = e.args();

        Object o = operator.accept(this, env);
        if (!(o instanceof FuncVal func)) throw new InterpreterException("Operator not a function in call " + formatter.visit(e, env));

        if (func.params().size() != operands.size()) throw new InterpreterException("Argument mismatch in call " + formatter.visit(e, env));

//        // Call by Value Semantics
//        List<AssignableValue> args = operands.stream().map(i -> {
//            Object t = i.accept(this, env);
//            if (!(t instanceof AssignableValue val)) throw new InterpreterException("Expression does not evaluate to assignable value " + formatter.visit(e, env));
//            return (AssignableValue) val;
//        }).toList();

        // Call by Promise Semantics
        List<PromiseVal> args = operands.stream().map(i -> new PromiseVal(this, i, env)).toList();

        Env func_env = func.env();
        for (int i = 0; i < args.size(); i++) func_env = new ExtendEnv(func_env, func.params().get(i), args.get(i));

        Object t = func.body().accept(this, func_env);
        if (!(t instanceof AssignableValue val)) throw new InterpreterException("Expression does not evaluate to assignable value " + formatter.visit(e, env));
        return val;
    }

    @Override
    public Value visit(IfExp e, Env env) {
        Object o = e.cond().accept(this, env);
        if (!(o instanceof AssignableValue val)) throw new InterpreterException("Expression does not evaluate to an assignable value " + formatter.visit(e, env));
        if(val.toBool().v()) return (Value) e.t_exp().accept(this, env);
        else return (Value) e.f_exp().accept(this, env);
    }

    @Override
    public Value visit(EqualExp e, Env env) {
        List<Exp> operands = e.all();
        Object o = operands.get(0).accept(this, env);
        if (!(o instanceof AssignableValue l)) throw new InterpreterException("Expression does not evaluate to Assignable value " + formatter.visit(e, env));
        AssignableValue r;
        for (int i=1; i<operands.size(); i++) {
            o = operands.get(i).accept(this, env);
            if (!(o instanceof AssignableValue)) throw new InterpreterException("Expression does not evaluate to Assignable value " + formatter.visit(e, env));
            r = (AssignableValue) o;
            if(!l.eq(r).v()) return BoolVal.FalseVal;
            l = r;
        }
        return BoolVal.TrueVal;
    }

    @Override
    public Value visit(GtExp e, Env env) {
        List<Exp> operands = e.all();
        Object o = operands.get(0).accept(this, env);
        if (!(o instanceof AssignableValue l)) throw new InterpreterException("Expression does not evaluate to Assignable value " + formatter.visit(e, env));
        AssignableValue r;
        for (int i=1; i<operands.size(); i++) {
            o = operands.get(i).accept(this, env);
            if (!(o instanceof AssignableValue)) throw new InterpreterException("Expression does not evaluate to Assignable value " + formatter.visit(e, env));
            r = (AssignableValue) o;
            if(l.comp(r).v() <= 0) return BoolVal.FalseVal;
            l = r;
        }
        return BoolVal.TrueVal;
    }

    @Override
    public Value visit(LtExp e, Env env) {
        List<Exp> operands = e.all();
        Object o = operands.get(0).accept(this, env);
        if (!(o instanceof AssignableValue l)) throw new InterpreterException("Expression does not evaluate to Assignable value " + formatter.visit(e, env));
        AssignableValue r;
        for (int i=1; i<operands.size(); i++) {
            o = operands.get(i).accept(this, env);
            if (!(o instanceof AssignableValue)) throw new InterpreterException("Expression does not evaluate to Assignable value " + formatter.visit(e, env));
            r = (AssignableValue) o;
            if(l.comp(r).v() >= 0) return BoolVal.FalseVal;
            l = r;
        }
        return BoolVal.TrueVal;
    }

    @Override
    public Value visit(AndExp e, Env env) {
        List<Exp> operands = e.all();
        for (Exp operand : operands) {
            Object o = operand.accept(this, env);
            if (!(o instanceof AssignableValue a)) throw new InterpreterException("Expression does not evaluate to Assignable value " + formatter.visit(e, env));
            if (!a.toBool().v()) return BoolVal.FalseVal;
        }
        return BoolVal.TrueVal;
    }

    @Override
    public Value visit(OrExp e, Env env) {
        List<Exp> operands = e.all();
        for (Exp operand : operands) {
            Object o = operand.accept(this, env);
            if (!(o instanceof AssignableValue a)) throw new InterpreterException("Expression does not evaluate to Assignable value " + formatter.visit(e, env));
            if (a.toBool().v()) return BoolVal.TrueVal;
        }
        return BoolVal.FalseVal;
    }

    @Override
    public Value visit(BoolExp e, Env env) {
        return e.v();
    }

    @Override
    public Value visit(PairExp e, Env env) {
        Object a = e.first().accept(this, env), b = e.second().accept(this, env);
        if(!(a instanceof AssignableValue i)) throw new InterpreterException("Exps in a pair must evaluate to an assignable value");
        if(!(b instanceof AssignableValue j)) throw new InterpreterException("Exps in a pair must evaluate to an assignable value");
        return new PairVal(i, j);
    }

    @Override
    public Value visit(FirstExp e, Env env) {
        Object a = e.exp().accept(this, env);
        if(!(a instanceof Pairable i)) throw new InterpreterException("Exps in a first must evaluate to a Pair value");
        return i.first();
    }

    @Override
    public Value visit(SecondExp e, Env env) {
        Object a = e.exp().accept(this, env);
        if(!(a instanceof Pairable i)) throw new InterpreterException("Exps in a first must evaluate to a Pair value");
        return i.second();
    }

    @Override
    public Value visit(ListExp e, Env env) {
        ListVal ans = ListVal.EMPTY_LIST;
        for(Exp i: e.all()){
            Object o = i.accept(this, env);
            if(!(o instanceof AssignableValue a)) throw new InterpreterException("Exps in a List must evaluate to an assignable value");
            ans = new ListVal(a, ans);
        }
        return ans;
    }

    @Override
    public Value visit(AppendExp e, Env env) {
        Object o = e.list().accept(this, env);
        if(!(o instanceof ListVal old)) throw new InterpreterException("l exp must evaluate to a list");
        Object p = e.e().accept(this, env);
        if(!(p instanceof AssignableValue item)) throw new InterpreterException("Exps in a List must evaluate to an assignable value");
        return new ListVal(item, old);
    }
}
