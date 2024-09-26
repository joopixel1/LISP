package arithlang;

import java.util.List;

import static arithlang.AST.*;
import static arithlang.Value.NumVal;

public class Evaluator implements Visitor<Value> {
    final Printer.Formatter ts = new Printer.Formatter();

    Value valueOf(Program p) {
        return (Value) p.accept(this);
    }

    @Override
    public Value visit(Program p) {
        return (Value) p.e().accept(this);
    }

    @Override
    public Value visit(NumExp e) {
        return new NumVal(e.v());
    }

    @Override
    public Value visit(AddExp e) {
        // semantics for add expression -- the result is the result of
        // summing each of the operands
        double result = 0;
        List<Exp> operands = e.all();
        for (Exp operand : operands) {
            NumVal val = (NumVal) operand.accept(this);
            result += val.v();
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(SubExp e) {
        // semantics for sub expression -- the result is the result of
        // subtracting each of the operands, in sequence, from left to
        // right
        List<Exp> operands = e.all();
        NumVal lVal = (NumVal) operands.get(0).accept(this);
        double result = lVal.v();
        for (int i = 1; i < operands.size(); i++) {
            NumVal rVal = (NumVal) operands.get(i).accept(this);
            result = result - rVal.v();
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(MultExp e) {
        // semantics for mult expression -- the result is the result of
        // multiplying each of the operands
        double result = 1;
        List<Exp> operands = e.all();
        for (Exp operand : operands) {
            NumVal val = (NumVal) operand.accept(this);
            result *= val.v();
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(DivExp e) {
        // semantics for div expression -- the result is the result of
        // dividing each of the operands, in sequence, from left to
        // right
        List<Exp> operands = e.all();
        NumVal lVal = (NumVal) operands.get(0).accept(this);
        double result = lVal.v();
        for (int i = 1; i < operands.size(); i++) {
            NumVal rVal = (NumVal) operands.get(i).accept(this);
            result = result / rVal.v();
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(IntDivExp e) {
        // semantics for div expression -- the result is the result of
        // dividing each of the operands, in sequence, from left to
        // right
        List<Exp> operands = e.all();
        NumVal lVal = (NumVal) operands.get(0).accept(this);
        double result = lVal.v();
        for (int i = 1; i < operands.size(); i++) {
            NumVal rVal = (NumVal) operands.get(i).accept(this);
            result = ((int) (result / rVal.v()));
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(PowExp e) {
        // semantics for mult expression -- the result is the result of
        // multiplying each of the operands
        double result = 1;
        List<Exp> operands = e.all();
        for (int i=operands.size()-1; i>=0; i--) {
            NumVal val = (NumVal) operands.get(i).accept(this);
            result = Math.pow(val.v(), result);
        }
        return new NumVal(result);
    }
}
