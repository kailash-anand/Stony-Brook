import java.util.Arrays;

public class BinaryTreeClient 
{
    public static void main(String[] args)
    {
        BinarySearchTree<Integer> t1 = new BinarySearchTree<>("Oak"); 
        t1.addAll(Arrays.asList(5, 3, 0, 9));
        BinarySearchTree<Integer> t2 = new BinarySearchTree<>("Maple");
        t2.addAll(Arrays.asList(9, 5, 10));
        System.out.println(t1);
        t1.forEach(System.out::println);
        System.out.println(t2);
        t2.forEach(System.out::println);
        BinarySearchTree<String> t3 = new BinarySearchTree<>("Cornucopia"); 
        t3.addAll(Arrays.asList("coconut", "apple", "banana", "plum", "durian", "no durians on this tree!","tamarind"));
        System.out.println(t3);
        t3.forEach(System.out::println);
        //BinarySearchTree<List<String>> treeOfLists;
    }
}
