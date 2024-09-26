package arithlang;

public interface Env {
    Value get(String k);

    class LookupException extends RuntimeException {
        public LookupException(String message){
            super(message);
        }
    }

    class EmptyEnv implements Env {
        public Value get(String k){
            throw new LookupException("No binding found for name: " + k);
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
}
