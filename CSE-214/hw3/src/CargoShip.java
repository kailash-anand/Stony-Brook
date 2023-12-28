/**
 * This class represents a container ship which holds stacks of containers.
 *
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #3 for CSE 214
 * Date:
 *    March 7th, 2023
 */
import java.util.EmptyStackException;

public class CargoShip
{
    private CargoStack[] stacks;// The CargoStacks array.
    private int maxHeight;
    private double maxWeight;
    public double weightTrack = 0.0;


    /**
     * Default Constructor.
     *
     * @param numStacks
     *  The number of stacks this ship can support.
     *  This parameter should be used to initialize the stacks array to a fixed size.
     * @param initMaxHeight
     *  The maximum height of any stack on this ship.
     * @param initMaxWeight
     *  The maximum weight for all the cargo on the ship.
     *
     * @Preconditions
     *  numStacks > 0
     *  initMaxHeight > 0
     *  initMaxWeight > 0
     *
     * @Postconditions
     *  This object has been initialized to a CargoShip object with the indicated number of stacks, maxHeight, and maxWeight.
     *
     * @Throws IllegalArgumentException
     *  if either of the parameters are now within the appropriate bounds.
     */
    public CargoShip(int numStacks, int initMaxHeight, double initMaxWeight)
    {
        if(numStacks < 0 || initMaxHeight < 0 || initMaxWeight < 0.0)
        {
            throw new IllegalArgumentException();
        }

        stacks = new CargoStack[numStacks];
        for(int i=0 ; i<numStacks ; i++)
        {
            stacks[i] = new CargoStack();
        }
        maxWeight = initMaxWeight;
        maxHeight = initMaxHeight;
    }

    /**
     * Pushes a cargo container to the indicated stack on the cargo ship
     *
     * @param cargo
     *  The container to place on the stack.
     * @param stack
     *  The index of the stack on the ship to push cargo onto.
     *
     * @Preconditions
     *  This CargoShip is initialized and not null.
     *  cargo is initialized and not null
     *  1 ≤ stack ≤ stacks.length.
     *  The size of the stack at the desired index is less than maxHeight.
     *  The total weight of all Cargo on the ship and cargo.getWeight()is less than maxWeight.
     *
     * @Postconditions
     *  The cargo has been successfully pushed to the given stack, or the appropriate exception has been thrown,
     *  in which case the state of the cargo ship should remain unchanged.
     *
     * @throws IllegalArgumentException
     *  If cargo is null or stack is not in the appropriate bounds.
     * @throws FullStackException
     *  If the stack being pushed to is at the max height.
     * @throws ShipOverWeightException
     *  If cargo makes the ship exceed maxWeight and sink.
     * @throws CargoStrengthException
     *  If the cargo is stacked on top of a weaker cargo.
     */
    public void pushCargo(Cargo cargo, int stack) throws FullStackException, ShipOverWeightException, CargoStrengthException
    {
        weightTrack += cargo.getWeight();

        if(cargo == null || stack < 1 || stack > stacks.length)
        {
            weightTrack -= cargo.getWeight();
            throw new IllegalArgumentException();
        }
        if(stacks[stack-1].size()+1 > maxHeight)
        {
            weightTrack -= cargo.getWeight();
            throw new FullStackException();
        }
        if(weightTrack > maxWeight)
        {
            weightTrack -= cargo.getWeight();
            throw new ShipOverWeightException();
        }
        if(!(stacks[stack-1].isEmpty()) && checkStrength(cargo) > checkStrength(stacks[stack-1].peek()))
        {
            weightTrack -= cargo.getWeight();
            throw new CargoStrengthException();
        }

        stacks[stack-1].push(cargo);
    }

    /**
     * Pops a cargo from one of the specified stack.
     *
     * @param stack
     *  The index of the stack to remove the cargo from.
     *
     * @Preconditions
     *  This CargoShip is initialized and not null.
     *  1 ≤ stack ≤ stacks.length.
     *
     * @Postconditions
     *  The cargo has been successfully been popped from the stack, and returned,
     *  or the appropriate exception has been thrown, in which case the state of the cargo ship should remain unchanged.
     *
     * @return
     *  The cargo popped.
     *
     * @throws IllegalArgumentException
     *  If stack is not in the appropriate bounds.
     * @throws EmptyStackException
     *  If the stack being popped from is empty.
     */
    public Cargo popCargo(int stack) throws EmptyStackException
    {

        if(stack < 0 || stack > stacks.length)
        {
            throw new IllegalArgumentException();
        }
        if(stacks[stack-1].isEmpty())
        {
            throw new EmptyStackException();
        }

        weightTrack -= stacks[stack-1].peek().getWeight();
        Cargo poppedCargo = stacks[stack-1].pop();
        return poppedCargo;
    }

    /**
     * Finds and prints a formatted table of all the cargo on the ship with a given name.
     * If the item could not be found in the stacks, notify the user accordingly.
     *
     * @param name
     *  The name of the cargo to find and print.
     *
     * @Preconditions
     *  This CargoShip is initialized and not null.
     *
     * @Postconditions
     *  The details of the cargo with the given name have been found and printed,
     *  or the user has been notified that no such cargo has been found.
     *  The state of the cargo ship should remain unchanged.
     */
    public void findAndPrint(String name)
    {
        CargoStack temp = new CargoStack();
        boolean check = false;
        int totalCount = 0;
        double totalWeight = 0.0;
        String answer = "Stacks\tDepth\tWeight\tStrength\n";
        answer += "=======+=======+=======+=======\n";
        int stackSize = stacks[0].size();


        for(int i=0 ; i<stacks.length ; i++)
        {
            stackSize = stacks[i].size();

            for(int j=0 ; j<stackSize ; j++)
            {
                temp.push(stacks[i].peek());
                if(stacks[i].pop().getName().equals(name))
                {
                    answer += (i+1) + " | " + j + " | " + (int)temp.peek().getWeight() + " | " + temp.peek().getStrength();
                    answer += "\n";
                    check = true;
                    totalCount++;
                    totalWeight += (int)temp.peek().getWeight();
                }
            }
            for(int j=0 ; j<stackSize ; j++)
            {
                stacks[i].push(temp.pop());
            }
        }

        if(check == false)
        { System.out.println("Cargo 'bananas' could not be found on the ship\n");}
        else
        {
            System.out.println(answer + "\n");
            System.out.println("Total count: " + totalCount);
            System.out.println("Total weight: " + totalWeight);
        }
    }

    /**
     * Removes the cargo with the name inputted and pushes them onto the dock.
     *
     * @param name
     *  The name of the cargo to be removed
     *
     * @param dock
     *  The dock of the ship.
     * @return
     */
    public String remove(String name, CargoStack dock)
    {
        CargoStack temp = new CargoStack();
        String answer = "";
        int stackSize = stacks[0].size();


        for(int i=0 ; i<stacks.length ; i++)
        {
            stackSize = stacks[i].size();

            for(int j=0 ; j<stackSize ; j++)
            {
                temp.push(stacks[i].peek());
                if(stacks[i].pop().getName().equals(name))
                {
                    answer = temp.peek().getStrength().name().substring(0,1);
                }

            }
            for(int j=0 ; j< stackSize ; j++)
            {
                stacks[i].push(temp.pop());
            }
        }

        if(answer.equals("F"))
        {
            int stacksSize = stacks[0].size();
            for(int i=0 ; i<stacksSize ; i++)
            {
                if(stacks[0].peek().getStrength().name().charAt(0) == 'F')
                {
                    return "U";
                }
            }
            stacksSize = stacks[1].size();
            for(int i=0 ; i<stacksSize ; i++)
            {
                if(stacks[1].peek().getStrength().name().charAt(0) == 'F')
                {
                    return "U";
                }
            }
            stacksSize = stacks[2].size();
            for(int i=0 ; i<stacksSize ; i++)
            {
                if(stacks[2].peek().getStrength().name().charAt(0) == 'F')
                {
                    return "U";
                }
            }
        }
        else {
            return "";
        }
        return "";
    }

    /**
     * Converts the strength as a number that can be used to compare.
     *
     * @param cargo
     *  The cargo object whose strength is to be converted.
     *
     * @return
     *  The numerical equivalent of the strength.
     */
    public int checkStrength(Cargo cargo)
    {
        int cargoStrength = 0;
        String strength = cargo.getStrength().name();

        switch(strength)
        {
            case "STURDY" :
                cargoStrength = 3;
                break;
            case "MODERATE":
                cargoStrength = 2;
                break;
            case "FRAGILE":
                cargoStrength = 1;
                break;
        }
        return cargoStrength;
    }

    /**
     * Returns the stack at the specified index.
     *
     * @param index
     *  The index of the stack to be returned.
     *
     * @return
     *  The stack at the specified index.
     */
    public CargoStack getStack(int index)
    {
        return stacks[index-1];
    }

    /**
     * Returns a printable representation of the stacks in the ship.
     *
     * @return
     *  A Printable representation of the stacks.
     */
    public String toString()
    {
        String print = "";//The string that stores the data to be printed.
        int once = 0;// To take care of commas in print.
        CargoStack tempStack = new CargoStack();// A temporary stack to help iterate through the entire stack.
        for(int i=0 ; i<stacks.length ; i++)//First for loop for number of stacks
        {
            once = 0;
            print += "Stack " + (i+1) + ": ";
            if(stacks[i] != null)
            {
                int stackSize = stacks[i].size();
                for(int j=0 ; j<stackSize ; j++)//Nested loop to use temporary stack.
                {
                    tempStack.push(stacks[i].pop());
                }
                for(int j=0 ; j<stackSize ; j++)//Nested loop for number of cargos in 1 stack.
                {
                    if(once > 0)
                    {print +=  "," + tempStack.peek().getStrength().name().charAt(0) + " "; }
                    else
                    { print +=  " " + tempStack.peek().getStrength().name().charAt(0) + " "; }
                    stacks[i].push(tempStack.pop());
                    once++;
                }
            }
            print += "\n";
        }
        return print;
    }

}
