package arithlang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    }

    abstract class ASTNode {
        public abstract Object accept(Visitor visitor, Env env);
    }

    class Program extends ASTNode {
        private final Exp _e;

        public Program(Exp e) {
            _e = e;
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

    abstract class CompoundArithExp extends Exp {
        final List<Exp> _rep;

        public CompoundArithExp(List<Exp> args) {
            _rep = new ArrayList<>();
            _rep.addAll(args);
        }

        public List<Exp> all() {
            return _rep;
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
            this._name = name;
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
            this._decl = map;
            this._body = body;
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
            _name = name;
            _e = e;
        }

        public String name() { return _name; }

        public Exp exp() { return _e; }

        public Object accept(Visitor visitor, Env env){
            return visitor.visit(this, env);
        }
    }
}
