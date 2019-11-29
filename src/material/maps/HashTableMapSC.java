package material.maps;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.*;

/**
 * Separate chaining table implementation of hash tables. Note that all
 * "matching" is based on the equals method.
 *
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro, JD. Quintana
 */

// Realizado por Miguel Sierra (partido desde AbastractHashTableMap)
public class HashTableMapSC<K, V> implements Map<K, V> {
    //TODO: Practica 4 Ejercicio 2

    private class HashEntry<T, U> implements Entry<T, U> {

        protected T key;
        protected U value;

        public HashEntry(T k, U v) {
            key = k;
            value = v;
        }

        @Override
        public U getValue() {
            return value;
        }

        @Override
        public T getKey() {
            return key;
        }

        public U setValue(U val) {
            U oldValue = value;
            value = val;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != this.getClass()) {
                return false;
            }
            HashEntry<T, U> ent;
            try {
                ent = (HashEntry<T, U>) o;
            } catch (ClassCastException ex) {
                return false;
            }
            return (ent.getKey().equals(this.key))
                    && (ent.getValue().equals(this.value));
        }
    }

    //Falta por corregir iterador
    private class HashTableMapIterator<T, U> implements Iterator<Entry<T, U>> {

        private int pos, listPos;
        private List<HashEntry<T, U>>[] bucket;
        private List<HashEntry<T, U>> AVAILABLE;

        public HashTableMapIterator(List<HashEntry<T, U>>[] b, List<HashEntry<T, U>> av, int numElems) {
            this.bucket = b;
            this.AVAILABLE = av;
            this.listPos = -1;
            if (numElems == 0) {
                this.pos = bucket.length;
            } else {
                this.pos = 0;
                goToNextElement(0);
            }
        }

        private void goToNextElement(int start) {
            this.pos = start;
            while ((this.pos < bucket.length) && ((this.bucket[this.pos] == null) || (this.bucket[this.pos].equals(this.AVAILABLE)))) {
                this.pos++;
            }
        }

        @Override
        public boolean hasNext() {
            return (this.pos < this.bucket.length);
        }

        @Override
        public Entry<T, U> next() {
            if (hasNext()) {
                int currentListPos = this.listPos;
                int currentPos = this.pos;
                if(bucket[currentPos].size()==0)
                    goToNextElement(this.pos + 1);
                else{
                    this.listPos++;
                    currentListPos++;
                    if(this.listPos == bucket[currentPos].size() - 1) {
                        this.listPos = -1;
                        goToNextElement(this.pos + 1);
                    }
                }
                return this.bucket[currentPos].get(currentListPos);
            } else {
                throw new IllegalStateException("The map has not more elements");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");

        }

    }

    private class HashTableMapKeyIterator<T, U> implements Iterator<T> {

        public HashTableMapIterator<T, U> it;

        public HashTableMapKeyIterator(HashTableMapIterator<T, U> it) {
            this.it = it;
        }

        @Override
        public T next() {
            return it.next().getKey();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private class HashTableMapValueIterator<T, U> implements Iterator<U> {

        public HashTableMapIterator<T, U> it;

        public HashTableMapValueIterator(HashTableMapIterator<T, U> it) {
            this.it = it;
        }

        @Override
        public U next() {
            return it.next().getValue();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public void remove() {
            //NO HAY QUE IMPLEMENTARLO
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    protected class HashEntryIndex {

        int index;
        int indexInList;
        boolean found;

        public HashEntryIndex(int index, int indexInList, boolean f) {
            this.index = index;
            this.indexInList = indexInList;
            this.found = f;
        }

        //Easy visualization
        @Override
        public String toString() {
            return "(" + this.index + ", " + this.found + ")";
        }
    }

    protected int n; // number of entries in the dictionary
    protected int prime, capacity; // prime factor and capacity of bucket array
    protected long scale, shift; // the shift and scaling factors
    protected List<HashEntry<K, V>>[] bucket;// bucket array
    protected final List<HashEntry<K, V>> AVAILABLE = new ArrayList<>();


    /**
     * Creates a hash table with prime factor 109345121 and capacity 1000.
     */
    public HashTableMapSC() {
        this(109345121, 1000);
    }

    /**
     * Creates a hash table with prime factor 109345121 and given capacity.
     *
     * @param cap initial capacity
     */
    public HashTableMapSC(int cap) {
        this(109345121, cap);
    }

    /**
     * Creates a hash table with the given prime factor and capacity.
     *
     * @param p   prime number
     * @param cap initial capacity
     */
    public HashTableMapSC(int p, int cap) {
        this.n = 0;
        this.prime = p;
        this.capacity = cap;
        this.bucket = (List<HashEntry<K, V>>[]) new ArrayList[capacity]; // safe cast
        Random rand = new Random();
        this.scale = rand.nextInt(prime - 1) + 1;
        this.shift = rand.nextInt(prime);
        fillBucket();
    }

    private void fillBucket(){
        for(int i = 0; i < this.bucket.length; i++){
            this.bucket[i] = new ArrayList<>();
        }
    }

    protected HashEntryIndex findEntry(K key) throws IllegalStateException {
        checkKey(key);
        int index = hashValue(key);
        int indexInListFound = 0;
        Entry<K, V> e = null;
        if(bucket[index].size() > 0)
            e = bucket[index].get(0);
        if (e != null) {
            if (key.equals(e.getKey())) { // we have found our key
                return new HashEntryIndex(index, indexInListFound, true); // key found
            } else { // bucket is  deactivated
                for (Entry<K, V> entry : bucket[index]) {
                    if (key.equals(entry.getKey())) { // we have found our key
                        return new HashEntryIndex(index, indexInListFound, true); // key found
                    }
                    indexInListFound++;
                }
            }
        }
        return new HashEntryIndex(index, -1,false); // first empty or available slot
    }
    /**
     * Hash function applying MAD method to default hash code.
     *
     * @param key Key
     * @return the hash value
     */
    protected int hashValue(K key) {
        return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
    }

    @Override
    public int size(){
        return n;
    }

    @Override
    public boolean isEmpty() {
        return (n == 0);
    }

    @Override
    public V get(K key) {
        HashEntryIndex i = findEntry(key); // helper method for finding a key
        if (!i.found) {
            return null; // there is no value for this key, so return null
        }
        return bucket[i.index].get(i.indexInList).getValue(); // return the found value in this case
    }
    @Override
    public V put(K key, V value) {
        HashEntryIndex i = findEntry(key); // find the appropriate spot for this entry
        if (i.found) { // this key has a previous value
            return bucket[i.index].get(i.indexInList).setValue(value); // set new value
        }
        if (n >= capacity / 2) {
            rehash(2 * this.capacity); // rehash to keep the load factor <= 0.5
            i = findEntry(key); // find again the appropriate spot for this entry
        }
        bucket[i.index].add(new HashEntry<>(key, value));
        n++;
        return null; // there was no previous value
    }

    @Override
    public V remove(K key) {
        HashEntryIndex i = findEntry(key); // find this key first
        if (!i.found) {
            return null; // nothing to remove
        }
        Entry<K, V> entryToRemove = bucket[i.index].get(i.indexInList);
        V entryValue = entryToRemove.getValue();
        bucket[i.index].remove(entryToRemove);
        n--;
        return entryValue;
        }


    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableMapIterator<K, V>(this.bucket, this.AVAILABLE, this.n);
    }

    @Override
    public Iterable<K> keys() {
        return new Iterable<K>() {
            public Iterator<K> iterator() {
                return new HashTableMapKeyIterator<K, V>(new HashTableMapIterator<K, V>(bucket, AVAILABLE, n));
            }
        };
    }

    @Override
    public Iterable<V> values() {
        return new Iterable<V>() {
            public Iterator<V> iterator() {
                return new HashTableMapValueIterator<K, V>(new HashTableMapIterator<K, V>(bucket, AVAILABLE, n));
            }
        };
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        return new Iterable<Entry<K, V>>() {
            public Iterator<Entry<K, V>> iterator() {
                return new HashTableMapIterator<K, V>(bucket, AVAILABLE, n);
            }
        };    }

    /**
     * Determines whether a key is valid.
     *
     * @param k Key
     */
    protected void checkKey(K k) {
        // We cannot check the second test (i.e., k instanceof K) since we do not know the class K
        if (k == null) {
            throw new IllegalStateException("Invalid key: null.");
        }
    }

    /**
     * Increase/reduce the size of the hash table and rehashes all the entries.
     */
    protected void rehash(int newCap) {
        //Prevent rehashing when decreasing the capacity
        // and the load factor constrain is not met
        if (newCap < 2 * this.size())
            return;

        capacity = newCap;
        List<HashEntry<K, V>>[] old = bucket;
        bucket = (List<HashEntry<K, V>>[]) new ArrayList[capacity];
        fillBucket();
        Random rand = new Random();
        // new hash scaling factor
        scale = rand.nextInt(prime - 1) + 1;
        // new hash shifting factor
        shift = rand.nextInt(prime);
        for (List<HashEntry<K, V>> subList : old) {
            if ((subList != null) && (!subList.equals(AVAILABLE))) { // a valid entry
                for(HashEntry<K, V> e : subList){
                    int bucketPos = findEntry(e.getKey()).index;
                    bucket[bucketPos].add(e);
                }
            }
        }
    }
}