package arithlang;

public interface Value {
    String toString();

    class NumVal implements Value {
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

    class DynamicError implements Value {
        private final String _errorMsg;

        public DynamicError(String v) {
            _errorMsg = v;
        }

        public String v() {
            return _errorMsg;
        }

        public String toString() {
            return _errorMsg;
        }
    }
}
