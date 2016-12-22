import java.util.*;

public class LinearScanMap<K,V> extends AbstractMap<K,V> {
    public K key;
    public V value;

    Set<Entry<K, V>> tempSet;

    //Constructor
    public LinearScanMap() {
        tempSet = new HashSet<Entry<K, V>>();

    }

    public Set<Entry<K, V>> entrySet() {

        return tempSet;
    }

    public V put(K key, V value) {
        V temp1;
        SimpleEntry<K, V> temp = new SimpleEntry<K, V>(key, value);
        if (this.containsKey(key)) {
            temp1 = this.get(key);
            tempSet.add(temp);
            return temp1;
        }
        tempSet.add(temp);
        return null;
    }


    public V get(Object key){
       for  ( Entry<K, V> entries : entrySet()){
           if ( entries.getKey().equals(key)){
              return entries.getValue();
           }
       }
        return null;
    }
 }
