import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleUtils
{
    /**
    * Find and return the least element from a collection of given elements that are comparable.
    *
    * @param items: the given collection of elements
    * @param from_start: a <code>boolean</code> flag that decides how ties are broken.
    * If <code>true</code>, the element encountered earlier in the
    * iteration is returned, otherwise the later element is returned.
    * @param <T>: the type parameter of the collection (i.e., the items are all of type T).
    * @return the least element in <code>items</code>, where ties are
    * broken based on <code>from_start</code>.
    */
    public static <T extends Comparable<T>> T least(Collection<T> items, boolean from_start)
    {
        return items.stream().min((p,q) -> 
        {
            int comparison = p.compareTo(q);
            return from_start ? comparison : -comparison;
        }).orElseThrow(() -> new IllegalArgumentException("Empty Collection"));
    }

    /**
    * Flattens a map to a list of <code>String</code>s, where each element in the list is formatted
    * as "key -> value" (i.e., each key-value pair is converted to a string in this specific format).
    *
    * @param aMap the specified input map.
    * @param <K> the type parameter of keys in <code>aMap</code>.
    * @param <V> the type parameter of values in <code>aMap</code>.
    * @return the flattened list representation of <code>aMap</code>.
    */
    public static <K, V> List<String> flatten(Map<K, V> aMap)
    {
        return aMap.entrySet().stream().map(p -> p.getKey().toString() + " -> " + p.getValue().toString()).toList();
    }

   public static void main(String[] args) {
        List<String> str = new ArrayList<String>();
        str.add("a");
        str.add("aa");
        str.add("aaa");
        str.add("bbb");
        str.add("ccc");
        str.add("dd");
        str.add("e");

        System.out.println(least(str, false));

        List<String> y =  Arrays.asList("hello","delhi","apple");
        System.out.println(least(y,true));
    

        str.add("blueberry");
        System.out.println(least(y, true));

        System.out.println();
        HashMap<Integer, String> hm = new HashMap<Integer, String>();
        hm.put(1, "Ram");
        hm.put(2, "Shyam");
        hm.put(3, "Sita");
        System.out.println((flatten(hm)));
    }
}