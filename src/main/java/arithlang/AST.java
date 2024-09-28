package arithlang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This class hierarchy represents expressions in the abstract syntax tree
 * manipulated by this interpreter.
 *
 * @author hridesh
 */
@SuppressWarnings("rawtypes")
public interface AST {

    // This interface should contain a signature for each concrete AST node.
    interface Visitor<T> {
        T visit(AST.Program p, Env env);

        T visit(AST.NumExp e, Env env);

        T visit(AST.AddExp e, Env env);

        T visit(AST.SubExp e, Env env);

        T visit(AST.MultExp e, Env env);

        T visit(AST.DivExp e, Env env);

        T visit(AST.IntDivExp e, Env env);

        T visit(AST.PowExp e, Env env);

        T visit(AST.VarExp e, Env env);

        T visit(AST.LetExp e, Env env);

        T visit(AST.DefExp d, Env env);

        T visit(AST.LambdaExp e, Env env);

        T visit(AST.CallExp e, Env env);

        T visit(AST.IfExp e, Env env);

        T visit(AST.EqualExp e, Env env);

        T visit(AST.GtExp e, Env env);

        T visit(AST.LtExp e, Env env);

        T visit(AST.AndExp e, Env env);

        T visit(AST.OrExp e, Env env);

        T visit(AST.BoolExp e, Env env);

        T visit(AST.PairExp e, Env env);

        T visit(AST.FirstExp e, Env env);

        T visit(AST.SecondExp e, Env env);

        T visit(AST.ListExp e, Env env);
    }

    abstract class ASTNode {
        public abstract Object accept(Visitor visitor, Env env);
    }

    class Program extends ASTNode {
        private final Exp _e;

        public Program(Exp e) {
            _e = Objects.requireNonNull(e, "Programs Exp cannot be Null");
        }

        public Exp e() {
            return _e;
        }
        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    abstract class Exp extends ASTNode {
    }

    abstract class CompoundExp extends Exp {
        private final List<Exp> _rep;

        public CompoundExp(List<Exp> args) {
            _rep = Objects.requireNonNull(args, "CompoundExp arguments cannot be Null");
        }

        public List<Exp> all() {
            return _rep;
        }
    }

     abstract class CompoundArithExp extends CompoundExp {
         public CompoundArithExp(List<Exp> args) {
             super(args);
         }
     }

    class NumExp extends Exp {
        private final Value.NumVal _val;

        public NumExp(double v) {
            _val = new Value.NumVal(v);
        }

        public Value.NumVal v() {
            return _val;
        }
        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class AddExp extends CompoundArithExp {
        public AddExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class SubExp extends CompoundArithExp {
        public SubExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class DivExp extends CompoundArithExp {
        public DivExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class IntDivExp extends CompoundArithExp {
        public IntDivExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class MultExp extends CompoundArithExp {
        public MultExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class PowExp extends CompoundArithExp {
        public PowExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class VarExp extends Exp {
        private final String _name;

        public VarExp(String name) {
            _name = Objects.requireNonNull(name, "Variable Name cannot be Null");
        }

        public String name() { return _name; }
        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class LetExp extends Exp {
        private final HashMap<String, Exp> _decl;
        private final Exp _body;

        public LetExp(HashMap<String, Exp> map, Exp body) {
            this._decl = Objects.requireNonNull(map, "Let 'list of assignments' cannot be Null");
            this._body = Objects.requireNonNull(body, "Let Body cannot be Null");
        }

        public HashMap<String, Exp> getDeclaration(){ return _decl; }
        public Exp getBody(){ return _body; }
        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class DefExp extends Exp {
        private final String _name;
        private final Exp _e;

        public DefExp(String name, Exp e) {
            _name = Objects.requireNonNull(name, "Definition Name cannot be Null");
            _e = Objects.requireNonNull(e, "Definition Exp cannot be Null");
        }

        public String name() { return _name; }
        public Exp exp() { return _e; }
        public Object accept(Visitor visitor, Env env){
            return visitor.visit(this, env);
        }
    }

    class LambdaExp extends Exp {
        private final ArrayList<String> _params;
        private final Exp _body;

        public LambdaExp(ArrayList<String> params, Exp body) {
            this._params = Objects.requireNonNull(params, "Lambda params cannot be Null");
            this._body = Objects.requireNonNull(body, "Lambda Body cannot be Null");
        }

        public ArrayList<String> params(){ return _params; }
        public Exp body(){ return _body; }
        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class CallExp extends Exp {
        private final Exp _e;
        private final ArrayList<Exp> _args;

        public CallExp(Exp e, ArrayList<Exp> args) {
            _e = Objects.requireNonNull(e, "Callable Exp cannot be Null");
            _args = Objects.requireNonNull(args, "Callable Args cannot be Null");
        }

        public Exp e() { return _e; }
        public ArrayList<Exp> args() { return _args; }
        public Object accept(Visitor visitor, Env env){
            return visitor.visit(this, env);
        }
    }

    class IfExp extends Exp {
        private final Exp _cond;
        private final Exp _t;
        private final Exp _f;

        public IfExp(Exp cond, Exp t, Exp f) {
            this._cond = Objects.requireNonNull(cond, "IfExp Cond cannot be Null");
            _t = Objects.requireNonNull(t, "IfExp t_exp cannot be Null");
            _f = Objects.requireNonNull(f, "IfExp f_exp cannot be Null");
        }

        public Exp cond(){ return _cond; }
        public Exp t_exp(){ return _t; }
        public Exp f_exp(){ return _f; }
        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    abstract class CompoundBoolExp extends CompoundExp {
        public CompoundBoolExp(List<Exp> args) {
            super(args);
        }
    }

    class EqualExp extends CompoundBoolExp {
        public EqualExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class GtExp extends CompoundBoolExp {
        public GtExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class LtExp extends CompoundBoolExp {
        public LtExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class AndExp extends CompoundBoolExp {
        public AndExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class OrExp extends CompoundBoolExp {
        public OrExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class BoolExp extends Exp {
        private final Value.BoolVal _val;

        public BoolExp(String text) {
            if(text.equals("true"))_val = Value.BoolVal.TrueVal;
            else if(text.equals("false"))_val = Value.BoolVal.FalseVal;
            else throw new InterpreterException("This is not a boolean value");
        }

        public Value.BoolVal v() {
            return _val;
        }

        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class PairExp extends Exp {
        private final Exp _f;
        private final Exp _s;

        public PairExp(Exp f, Exp s) {
            _f = Objects.requireNonNull(f, "Pair First Exp cannot be Null");
            _s = Objects.requireNonNull(s, "Pair Second Exp cannot be Null");
        }

        public Exp first() { return _f; }
        public Exp second() { return _s; }
        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class FirstExp extends Exp {
        private final Exp _e;

        public FirstExp(Exp e) {
            _e = Objects.requireNonNull(e, "Exp cannot be Null");
        }

        public Exp exp() { return _e; }
        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class SecondExp extends Exp {
        private final Exp _e;

        public SecondExp(Exp e) {
            _e = Objects.requireNonNull(e, "Exp cannot be Null");
        }

        public Exp exp() { return _e; }
        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    class ListExp extends Exp {
        private final ArrayList<Exp> _list;

        public ListExp(ArrayList<Exp> list) {
            _list = Objects.requireNonNull(list, "List of Exp cannot be Null");
        }

        public ArrayList<Exp> all() { return _list; }
        public Object accept(Visitor visitor, Env env) {
            return visitor.visit(this, env);
        }
    }
}
