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
public class Cargo
{
    private String name;//The name of the Cargo container
    private double weight;//The weight of the Cargo container
    private CargoStrength strength;//The Strength of the Cargo container.

    /**
     * Default Constructor.
     *
     * @param initName
     *  Non-null name for the cargo item.
     *
     * @param initWeight
     *  The weight for the cargo. The weight should be greater than 0.
     *
     * @param initStrength
     *  Either FRAGILE, MODERATE, or STURDY.
     *
     * @Preconditions
     *  initName is not null.
     *  initWeight > 0.
     *
     * @Postconditions
     *  This object has been initialized to a Cargo object with the given name, weight, and strength.
     *
     * @Throws IllegalArgumentException
     *  If initName is null.
     *  If initWeight ≤ 0.
     */
    public Cargo(String initName, double initWeight, CargoStrength initStrength)
    {
        if(initName == null || initWeight < 1.0)
        {
            throw new IllegalArgumentException();
        }
        name = initName;
        weight = initWeight;
        strength = initStrength;
    }

    /**
     * Returns the name of the cargo
     *
     * @return
     *  Returns the name of the cargo
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the weight of the cargo
     *
     * @return
     *  Returns the weight of the cargo
     */
    public double getWeight()
    {
        return weight;
    }

    /**
     * Returns the strength of the cargo
     *
     * @return
     *  Either FRAGILE, MODERATE, or STURDY
     */
    public CargoStrength getStrength()
    {
        return strength;
    }
}
