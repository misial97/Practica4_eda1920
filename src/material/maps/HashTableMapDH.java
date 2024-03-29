package material.maps;

/**
 * @param <K> The hey
 * @param <V> The stored value
 */
public class HashTableMapDH<K, V> extends AbstractHashTableMap<K, V> {

    public HashTableMapDH(int size) {
        super(size);
    }

    public HashTableMapDH() {
        super();
    }

    public HashTableMapDH(int p, int cap) {
        super(p, cap);
    }

    // Realizado por: Miguel Sierra
    @Override
    protected int offset(K key, int i) {
        //TODO: Practica 4 Ejercicio 1
        return (this.prime - (key.hashCode() % this.prime))*i;
    }
}