/**
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #3 for CSE 214
 * Date:
 *    February 22nd, 2023
 */
public class CargoNode
{
    private Cargo cargo;
    private CargoNode next;

    /**
     * Default constructor.
     *
     * @param newCargo
     *  The data to be wrapped by this CargoNode.
     *  This parameter should not be null, since we should never have a CargoNode with null data
     *
     * @Preconditions
     *  initData is not null.
     *
     * @PostConditions
     *  This CargoNode has been initialized to wrap the indicated Slide, and prev and next have been set to null.
     *
     * @Throws IllegalArgumentException
     *   Thrown if initData is null.
     */
    public CargoNode(Cargo newCargo)
    {
        if(newCargo == null)
        {
            throw new IllegalArgumentException();
        }

        cargo = newCargo;
    }

    /**
     * Gets the reference to the data member variable of the list node.
     *
     * @return
     *  The reference of the data member variable.
     */
    public Cargo getCargo()
    {
        return cargo;
    }

    /**
     * Updates the data member variable with a reference to a new cargo
     *
     * @param newCargo
     *  Reference to a new cargo object to update the data member variable
     *  This parameter should not be null, since we should never have a CargoNode with null data
     *
     * @Preconditions
     *  newData is not null
     *
     * @Throws IllegalArgumentException
     *  Thrown if newCargo is null
     */
    public void setCargo(Cargo newCargo)
    {
        if(newCargo == null)
        {
            throw new IllegalArgumentException();
        }

        cargo = newCargo;
    }

    /**
     * Updates the data member variable with a reference to a new Cargo.
     *
     * @return The reference of the next member variable.
     * Note that this return value can be null, meaning that there is no next CargoNode in the list.
     */
    public CargoNode getNext()
    {
        return next;
    }

    /**
     * Updates the next member variable with a new CargoNode reference.
     *
     * @param newNext
     *  Reference to a new CargoNode object to update the next member variable.
     *  This parameter may be null, since it is okay to have no next CargoNode
     */
    public void setNext(CargoNode newNext)
    {
        next = newNext;
    }
}
