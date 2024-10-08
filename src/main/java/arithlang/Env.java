package arithlang;

import java.util.HashMap;

public interface Env {
    Value.AssignableValue get(String k);

    class EmptyEnv implements Env {
        public Value.AssignableValue get(String k){
            throw new InterpreterException("No binding found for name: " + k);
        }
    }

    class ExtendEnv implements Env {
        private final Env nestedEnv;
        private final String key;
        private final Value val;

        public ExtendEnv(Env nestedEnv, String k, Value v){
            assert nestedEnv!= null && k!=null && v!=null;
            assert (v instanceof Value.AssignableValue || v instanceof Value.PromiseVal);
            this.nestedEnv = nestedEnv;
            this.key = k;
            this.val = v;
        }

        public synchronized Value.AssignableValue get(String k){
            if(key.equals(k)) {
                if(val instanceof Value.AssignableValue) return  (Value.AssignableValue) val;
                else return ((Value.PromiseVal) val).toAssignableValue();

            }
            return nestedEnv.get(k);
        }
    }

    class GlobalEnv implements Env {
        private final HashMap<String, Value> _globals;

        public GlobalEnv(){
            this._globals = new HashMap<>();
        }

        public synchronized Value.AssignableValue get(String k){
            if(!_globals.containsKey(k)) throw new InterpreterException("No binding found for name: " + k);
            Value val = _globals.get(k);
            Value.AssignableValue ans;
            if(val instanceof Value.AssignableValue) ans = (Value.AssignableValue) val;
            else ans = ((Value.PromiseVal) val).toAssignableValue();
            return ans;
        }

        public synchronized void extend(String k, Value v){
            if (_globals.containsKey(k)) {
                throw new InterpreterException("Re-declaration of variable " + k +" detected. Discarding redefinition.");
            } else {
                assert (v instanceof Value.AssignableValue || v instanceof Value.PromiseVal);
                _globals.put(k, v);
            }
        }
    }
}
