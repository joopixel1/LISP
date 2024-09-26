package arithlang;

import java.util.ArrayList;
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
        T visit(AST.Program p);

        T visit(AST.NumExp e);

        T visit(AST.AddExp e);

        T visit(AST.SubExp e);

        T visit(AST.MultExp e);

        T visit(AST.DivExp e);

        T visit(AST.IntDivExp e);

        T visit(AST.PowExp e);
    }

    abstract class ASTNode {
        public abstract Object accept(Visitor visitor);
    }

    class Program extends ASTNode {
        private final Exp _e;

        public Program(Exp e) {
            _e = e;
        }

        public Exp e() {
            return _e;
        }

        public Object accept(Visitor visitor) {
            return visitor.visit(this);
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
        private final double _val;

        public NumExp(double v) {
            _val = v;
        }

        public double v() {
            return _val;
        }

        public Object accept(Visitor visitor) {
            return visitor.visit(this);
        }
    }

    class AddExp extends CompoundArithExp {
        public AddExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor) {
            return visitor.visit(this);
        }
    }

    class SubExp extends CompoundArithExp {
        public SubExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor) {
            return visitor.visit(this);
        }
    }

    class DivExp extends CompoundArithExp {
        public DivExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor) {
            return visitor.visit(this);
        }
    }

    class IntDivExp extends CompoundArithExp {
        public IntDivExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor) {
            return visitor.visit(this);
        }
    }

    class MultExp extends CompoundArithExp {
        public MultExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor) {
            return visitor.visit(this);
        }
    }

    class PowExp extends CompoundArithExp {
        public PowExp(List<Exp> args) {
            super(args);
        }

        public Object accept(Visitor visitor) {
            return visitor.visit(this);
        }
    }

}
