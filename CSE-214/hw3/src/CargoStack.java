import java.util.EmptyStackException;

/**
 * This class represents a container which holds the ‘cargo’ the program will place on the ship.
 * It contains the following private member variables: name (String), weight (double) and strength (CargoStrength).
 *
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #3 for CSE 214
 * Date:
 *    March 7th, 2023
 */
public class CargoStack
{

    private Cargo data;
    private CargoNode top;
    private int size = 0;

    /**
     * Default constructor.
     * Initializes the top and data to null.
     *
     * @Postconditions
     *  The top and data are null and a new CargoStack object has been instantiated.
     */
    public CargoStack()
    {
        top = null;
        data = null;
    }

    /**
     * Checks is the stack is empty.
     *
     * @return
     *  A boolean value indicating whether the stack is empty.
     */
    public boolean isEmpty()
    {
        if(top == null)
            return true;
        else
            return false;
    }

    /**
     * Pushes a Cargo container in the stack.
     *
     * @param newCargo
     *  The cargo container to be pushed.
     *
     * @Preconditions
     *  The newCargo is not null
     *
     * @Postconditions
     *  The newCargo has been pushed onto the stack.
     *
     * @throws IllegalArgumentException
     *  if the newCargo is null.
     */
    public void push(Cargo newCargo)
    {
        if(newCargo == null)
        {
            throw new IllegalArgumentException();
        }

        CargoNode newNode = new CargoNode(newCargo);
        newNode.setNext(top);
        top = newNode;
        size++;
    }

    /**
     * Pops the cargo at the top from the stack.
     *
     * @returns
     *  The cargo container popped.
     *
     * @Preconditions
     * The CargoStack is initialized and not null
     *
     * @Postconditions
     * The cargo at the top has been popped.
     *
     * @throws EmptyStackException
     *  if the stack is empty.
     */
    public Cargo pop() throws EmptyStackException
    {
        if(top == null)
        {
            throw new EmptyStackException();
        }

        CargoNode newNode = top;
        top = top.getNext();
        size--;
        return newNode.getCargo();
    }

    /**
     * Takes a look at the topmost Cargo container of the stack.
     *
     * @return
     *  The topmost Cargo container.
     *
     * @throws EmptyStackException
     *  if the stack is empty.
     */
    public Cargo peek() throws EmptyStackException
    {
        if(size == 0)
        {
            throw new EmptyStackException();
        }

        return top.getCargo();
    }

    /**
     * Returns the size of the stack.
     *
     * @return
     *  The size of the stack.
     */
    public int size()
    {
        return size;
    }

}
