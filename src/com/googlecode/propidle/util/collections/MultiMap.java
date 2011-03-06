package com.googlecode.propidle.util.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;

import java.util.*;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public class MultiMap<K, V> implements Iterable<Pair<K, ? extends Iterable<V>>>{
    private final Map<K, List<V>> underlying = new HashMap<K, List<V>>();

    public Option<V> first(K key) {
        return sequence(get(key)).headOption();
    }

    public Iterable<V> get(K key) {
        return getOrCreate(key);
    }

    public MultiMap<K, V> put(K key, V value) {
        getOrCreate(key).add(value);
        return this;
    }

    public Iterable<V> remove(K key) {
        List<V> existing = underlying.remove(key);
        return existing == null ? Sequences.<V>sequence() : existing;
    }

    public Option<V> remove(K key, V value) {
        if (getOrCreate(key).remove(value)) {
            return some(value);
        } else {
            return none();
        }
    }

    public Iterator<Pair<K, ? extends Iterable<V>>> iterator() {
        return sequence(underlying.entrySet()).map(entriesToPairs()).iterator();
    }

    private Callable1<Map.Entry<K, List<V>>, Pair<K, ? extends Iterable<V>>> entriesToPairs() {
        return new Callable1<Map.Entry<K, List<V>>, Pair<K, ? extends Iterable<V>>>() {
            public Pair<K, ? extends Iterable<V>> call(Map.Entry<K, List<V>> entry) throws Exception {
                return pair(entry.getKey(), entry.getValue());
            }
        };
    }

    public boolean containsKey(K key) {
        return underlying.containsKey(key);
    }

    private List<V> getOrCreate(K key) {
        if (!underlying.containsKey(key)) {
            underlying.put(key, new ArrayList<V>());
        }
        return underlying.get(key);
    }
}
