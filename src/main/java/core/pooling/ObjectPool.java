package core.pooling;

import java.util.*;
import java.util.function.Supplier;

/**
 * Generic ObjectPool - Thread-safe object pooling for performance.
 */
public class ObjectPool<T> {
    private Queue<T> available;
    private Set<T> inUse;
    private Supplier<T> factory;
    private int maxSize;

    public ObjectPool(Supplier<T> factory, int initialSize, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.available = new LinkedList<>();
        this.inUse = new HashSet<>();
        
        for (int i = 0; i < initialSize; i++) {
            available.add(factory.get());
        }
    }

    public synchronized T acquire() {
        T obj;
        if (available.isEmpty()) {
            if (inUse.size() < maxSize) {
                obj = factory.get();
            } else {
                return null; // Pool exhausted
            }
        } else {
            obj = available.poll();
        }
        inUse.add(obj);
        return obj;
    }

    public synchronized void release(T obj) {
        if (inUse.remove(obj)) {
            available.add(obj);
        }
    }

    public int getAvailableCount() {
        return available.size();
    }

    public int getInUseCount() {
        return inUse.size();
    }

    public void clear() {
        available.clear();
        inUse.clear();
    }
}
