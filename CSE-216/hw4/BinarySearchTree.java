import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> implements Iterable<T>
{
    private BTNode<T> root;
    private String name;

    public BinarySearchTree(String name)
    {
        root = null;
        this.name = name;
    }

    public boolean isEmpty()
    {
        return root == null;
    }

    public void add(T element)
    {
        if(isEmpty())
        {
            root = new BTNode<T>(element);
        }
        else
        {
            addHelp(root, element);
        }
    }

    private void addHelp(BTNode<T> node, T element)
    {
        if(element.compareTo(node.data) > 0)
        {
            if(node.right != null)
            {
                addHelp(node.right, element);
            }
            else 
            {
                BTNode<T> val = new BTNode<>(element);
                node.setRight(val);
            }
        }
        else if(element.compareTo(node.data) < 0)
        {
            if(node.left != null)
            {
                addHelp(node.left, element);
            }
            else
            {
                BTNode<T> val = new BTNode<>(element);
                node.setLeft(val);
            }
        }
    }

    public void addAll(List<T> elements)
    {
        elements.forEach(i -> add(i));
    }

    @Override
    public Iterator<T> iterator() 
    {
        List<T> order = new ArrayList<>();
        iteratorHelp(root, order);
        return order.listIterator();
    }

    public void iteratorHelp(BTNode<T> node, List<T> order)
    {
        if(node.left != null)
        {
            iteratorHelp(node.left, order);
        }

        order.add(node.data);

        if(node.right != null)
        {
            iteratorHelp(node.right, order);
        }
    }

    public String toString()
    {
        String display = "[" + name + "]";
        display += " " + toStringHelp(root,"");
        return display;
    }

    private String toStringHelp(BTNode<T> node, String display)
    {
        display += node.toString();
        
        if(node.left != null)
        
        {
            display += " L:(" + toStringHelp(node.left, "") + ")";
        }

        if(node.right != null)
        {
            display += " R:(" +  toStringHelp(node.right, "") + ")";
        }

        return display;
    }

    public class BTNode<E extends Comparable<E>> 
    {
        private E data;
        private BTNode<E> left;
        private BTNode<E> right;

        public BTNode(E data)
        {
            if(data == null)
            {
                throw new IllegalArgumentException("Invalid data entered");
            }

            this.data = data;
            this.left = null;
            this.right = null;
        }

        public void setLeft(BTNode<E> left)
        {
            this.left = left;
        }

        public void setRight(BTNode<E> right)
        {
            this.right = right;
        }
        
        public String toString()
        {
            return "" + data;
        } 
    }
}


    

