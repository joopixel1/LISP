package arithlang;

import com.ibm.icu.impl.Pair;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Value{
    protected abstract String string();

    @Override
    public String toString() {
        return string();
    }

    public static class UnitVal extends Value {
        static public final UnitVal UNIT_VAL = new UnitVal();

        private UnitVal(){}

        public String string() { return ""; }
    }

    public interface Booleable {
        default BoolVal toBool(){ throw new InterpreterException(this.getClass() + "Assignable has not been Booleable extended") ; }
    }

    public interface Compareable {
        default NumVal comp(Equaleable equal){ throw new InterpreterException(this.getClass() + "Assignable has not been Compareable extended") ; }
    }

    public interface Equaleable {
        default BoolVal eq(Equaleable o){ throw new InterpreterException(this.getClass() + "Assignable has not been Equaleable extended") ; }
    }

    public abstract static class AssignableValue extends Value implements Booleable, Equaleable, Compareable{
    }

    public static final class NumVal extends AssignableValue {
        private final double _val;

        public NumVal(double v) {
            _val = v;
        }

        public double v() {
            return _val;
        }

        public BoolVal toBool(){ return new BoolVal(!(_val == 0)); }

        public BoolVal eq(Equaleable o) {
            if(o == this) return BoolVal.TrueVal;
            if(!(o instanceof NumVal)) throw new InterpreterException("comparing objects of diff types");

            if (Math.abs(_val - ((NumVal) o)._val) < 1e-9) return BoolVal.TrueVal;
            else return BoolVal.FalseVal;
        }

        public NumVal comp(Equaleable o) {
            if(o == this) return new NumVal(0);
            if(!(o instanceof NumVal)) throw new InterpreterException("comparing objects of diff types");
            return new NumVal(_val - ((NumVal) o)._val);
        }

        protected String string() {
            int tmp = (int) _val;
            if (_val == ((double) tmp)) return "" + tmp;
            else return ""+_val;
        }
    }

    public static final class BoolVal extends AssignableValue {
        static public final BoolVal TrueVal = new BoolVal(true);
        static public final BoolVal FalseVal = new BoolVal(false);

        private final boolean _val;

        private BoolVal(boolean v) { _val = v; }

        public boolean v() {
            return _val;
        }

        public BoolVal toBool() { return this; }

        public BoolVal eq(Equaleable o) {
            if(o == this) return BoolVal.TrueVal;
            if(!(o instanceof BoolVal)) throw new InterpreterException("comparing objects of diff types");

            if (_val == ((BoolVal) o)._val) return BoolVal.TrueVal;
            else return BoolVal.FalseVal;
        }

        protected String string() { return ""+_val; }
    }

    public static final class FuncVal extends AssignableValue{
        private final AST.Exp _body;
        private final ArrayList<String> _params;
        private final Env _env;


        public FuncVal(AST.Exp body, ArrayList<String> params, Env env){

            _body = Objects.requireNonNull(body, "Body Exp cannot be null");
            _params = Objects.requireNonNull(params, "Function params cannot be null");
            _env = Objects.requireNonNull(env, "Env cannot be null");
        }

        public AST.Exp body() { return _body; }
        public ArrayList<String> params() { return _params; }
        public Env env() { return _env; }

        public String string() {
            StringBuilder result = new StringBuilder("FuncVal( params(");
            for (String exp : _params) result.append(" ").append(exp);
            return result + "), " + _body.getClass() + ", " + Env.class + " )";
        }
    }

    public interface Pairable{
        AssignableValue first();
        AssignableValue second();
    }

    public static final class PairVal extends AssignableValue implements Pairable{
        private final AssignableValue _first;
        private final AssignableValue _second;

        public PairVal(AssignableValue f, AssignableValue s){
            _first = Objects.requireNonNull(f, "first value cannot be null");
            _second = Objects.requireNonNull(s, "Second Value cannot be null");
        }

        public AssignableValue first() { return _first; }
        public AssignableValue second() { return _second; }

        @Override
        protected String string() {
            return "(" + _first + ", " + _second + ")";
        }
    }

    public static final class ListVal extends AssignableValue implements Pairable {
        public static final ListVal EMPTY_LIST = new ListVal();

        private final AssignableValue _v;
        private final ListVal _sub;

        public ListVal(AssignableValue v, ListVal sub){
            _v = Objects.requireNonNull(v, "Value cannot be null");
            _sub = Objects.requireNonNull(sub, "Sublist cannot be null");
        }

        private ListVal() {
            _v = null;
            _sub = null;
        }

        public AssignableValue first() { return _v; }
        public AssignableValue second() { return _sub; }

        public BoolVal toBool() { return (this == EMPTY_LIST) ? BoolVal.FalseVal : BoolVal.TrueVal; }

        public String string() {
            StringBuilder result = new StringBuilder("ListVal( ");
            for(ListVal cur = this; cur != ListVal.EMPTY_LIST; cur = cur._sub) result.append(Objects.requireNonNull(cur)._v).append(" ");
            return result + ")";
        }
    }

    public static final class PromiseVal extends Value{
        private final AST.Visitor _visitor;
        private final AST.Exp _exp;
        private final Env _env;

        public PromiseVal(AST.Visitor visitor, AST.Exp exp, Env env){
            _visitor = Objects.requireNonNull(visitor, "visitor cannot be null");
            _exp = Objects.requireNonNull(exp, "exp value cannot be null");
            _env = Objects.requireNonNull(env, "env Value cannot be null");
        }

        public AST.Exp exp() { return _exp; }
        public Env env() { return _env; }
        public AssignableValue toAssignableValue(){ return (AssignableValue) _exp.accept(_visitor, _env); }

        @Override
        protected String string() {
            return "Promise<" + _exp + ", " + _env + ">";
        }
    }
}
