package mylie.async;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
public abstract class Cache {
    final String id;
    @Setter(AccessLevel.PACKAGE)
    Cache parent;

    abstract <R> Result<R> result(Async.Hash hash, long version);

    abstract <R> void result(Result<R> result);

    abstract void invalidate();

    abstract void remove(Async.Hash hash);

    public abstract void reset();

    public static class NoOpCache extends Cache {
        public NoOpCache(String id) {
            super(id, null);
        }

        @Override
        <R> Result<R> result(Async.Hash hash, long version) {
            return null;
        }

        @Override
        <R> void result(Result<R> result) {
            // NoOp intentional
        }

        @Override
        void invalidate() {
            // NoOp intentional
        }

        @Override
        void remove(Async.Hash hash) {
            // NoOp intentional
        }

        @Override
        public void reset() {
            // NoOp intentional
        }
    }

    static final class InvalidateAll extends NoOpCache {
        private final Map<Async.Hash, Result<?>> store = new ConcurrentHashMap<>();

        public InvalidateAll(String id) {
            super(id);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected <R> Result<R> result(Async.Hash hash, long version) {

            Result<R> result = (Result<R>) store.get(hash);
            if (result == null) {
                result = parent().result(hash, version);
            }
            return result;
        }

        @Override
        <R> void result(Result<R> result) {
            store.put(result.hash(), result);
            parent().result(result);
        }

        @Override
        void invalidate() {
            store.keySet().forEach(parent::remove);
            store.clear();

        }

        @Override
        public void reset() {
            invalidate();
        }
    }

    static final class InvalidateOlder extends NoOpCache {

        public InvalidateOlder(String id) {
            super(id);
        }

        @Override
        protected <R> Result<R> result(Async.Hash hash, long version) {
            Result<R> result = parent().result(hash, version);
            if (result != null && result.version() < version) {
                parent().remove(hash);
                return null;
            }
            return result;
        }

        @Override
        <R> void result(Result<R> result) {
            parent().result(result);
        }
    }

    static final class InvalidateDifferent extends NoOpCache {
        public InvalidateDifferent(String id) {
            super(id);
        }

        @Override
        protected <R> Result<R> result(Async.Hash hash, long version) {
            Result<R> result = parent().result(hash, version);
            if (result != null && result.version() != version) {
                parent().remove(hash);
                return null;
            }
            return result;
        }

        @Override
        <R> void result(Result<R> result) {
            parent().result(result);
        }
    }

    static final class NoInvalidation extends NoOpCache {
        public NoInvalidation(String id) {
            super(id);
        }

        @Override
        protected <R> Result<R> result(Async.Hash hash, long version) {
            return parent().result(hash, version);
        }

        @Override
        <R> void result(Result<R> result) {
            parent().result(result);
        }
    }

}
