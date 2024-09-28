package arithlang;

import java.util.HashMap;

public interface Env {
    Value get(String k);

    class EmptyEnv implements Env {
        public Value get(String k){
            throw new InterpreterException("No binding found for name: " + k);
        }
    }

    class ExtendEnv implements Env {
        private final Env nestedEnv;
        private final String key;
        private final Value val;

        public ExtendEnv(Env nestedEnv, String k, Value v){
            assert nestedEnv!= null && k!=null && v!=null;
            this.nestedEnv = nestedEnv;
            this.key = k;
            this.val = v;
        }

        public synchronized Value get(String k){
            if(key.equals(k)) return val;
            return nestedEnv.get(k);
        }
    }

    class GlobalEnv implements Env {
        private final HashMap<String, Value> _globals;

        public GlobalEnv(){
            this._globals = new HashMap<>();
        }

        public synchronized Value get(String k){
            if(!_globals.containsKey(k)) throw new InterpreterException("No binding found for name: " + k);
            return _globals.get(k);
        }

        public synchronized void extend(String k, Value v){
            _globals.put(k, v);
        }
    }
}
