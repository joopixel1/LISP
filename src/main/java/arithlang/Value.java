package arithlang;

public interface Value {
    String toString();

    abstract class AssignableValue implements Value{
    }

    class NumVal extends AssignableValue {
        private final double _val;

        public NumVal(double v) {
            _val = v;
        }

        public double v() {
            return _val;
        }

        public String toString() {
            int tmp = (int) _val;
            if (_val == ((double) tmp)) return "" + tmp;
            else return ""+_val;
        }
    }

    class UnitVal implements Value {
        public String toString() { return ""; }
    }
}
