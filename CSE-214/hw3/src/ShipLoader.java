/**
 * The Main class used to operate the cargo movement in the stacks and dock of the ship.
 *
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #3 for CSE 214
 * Date:
 *    March 7th, 2023
 */
import java.util.*;
public class ShipLoader
{
    /**
     * Prints the currnt status of the dock.
     *
     * @param ship
     *  The current ship.
     * @param dock
     *  The dock of the ship.
     * @param mass
     *  The total weight that the ship can carry.
     *
     */
    public static void printCurrentStatus(CargoShip ship,CargoStack dock,int mass)
    {
        CargoStack tempStack = new CargoStack();
        int once = 0;
        System.out.print("Dock: ");
        while(!(dock.isEmpty()))
        {
            tempStack.push(dock.pop());
        }
        while(!(tempStack.isEmpty()))
        {
            if(once > 0)
            { System.out.print("," + tempStack.peek().getStrength().name().charAt(0) + " "); }
            else{
            System.out.print(tempStack.peek().getStrength().name().charAt(0) + " ");}
            dock.push(tempStack.pop());
            once++;
        }
        System.out.println("\n");
        System.out.println("\nTotal weight = " + (int)ship.weightTrack + "/" + mass +"\n");
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
    public static int checkStrength(Cargo cargo)
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

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        boolean check = true;//The loop condition
        boolean autoRemove = false;// The reove condition.
        String index = "";// Stores the input from the user.
        int once = 1;

        System.out.print("Enter the number of stacks:");
        int s = input.nextInt();
        input.nextLine();
        System.out.print("Enter the max height of stacks:");
        int max = input.nextInt();
        input.nextLine();
        System.out.print("Enter the max weight: ");
        int mass = input.nextInt();
        input.nextLine();

        CargoShip newShip = new CargoShip(s,max,mass);
        CargoStack dock = new CargoStack();


        System.out.println("Welcome to ShipLoader!\n");

        System.out.println("Cargo Ship Parameters");
        System.out.println("-----------------------------");
        System.out.println("Number of stacks: " + 3);
        System.out.println("Maximum height of stacks: " + 3);
        System.out.println("Maximum total cargo weight: " + 5500);

        System.out.println("\nCargo Ship Created.");
        System.out.println("Pulling ship in to dock...");
        System.out.println("Cargo ship ready to be loaded.\n");

        while(check)
        {
            if(!autoRemove)
            {
                System.out.println("Please select an option:\n" +
                        "  C) Create new cargo\n" +
                        "  L) Load cargo from dock\n" +
                        "  U) Unload cargo from ship\n" +
                        "  M) Move cargo between stacks\n" +
                        "  K) Clear dock\n" +
                        "  P) Print ship stacks\n" +
                        "  S) Search for cargo\n" +
                        "  R) Remove\n" +
                        "  Q) Quit\n");
                System.out.print("Select a menu option: ");
                index = input.nextLine();
            }
            else
            {

            }

            switch(index.toUpperCase())
            {
                case"C":
                    System.out.print("Enter the name of the cargo: ");
                    String name = input.nextLine();
                    System.out.print("Enter the weight of the cargo: ");
                    double weight = input.nextDouble();
                    input.nextLine();
                    System.out.print("Enter the container strength (F/M/S) ");
                    String strength = input.nextLine();

                    CargoStrength Strength = CargoStrength.FRAGILE;
                    switch(strength.toUpperCase())
                    {
                        case"F":
                            Strength = CargoStrength.FRAGILE;
                            break;
                        case"M":
                            Strength = CargoStrength.MODERATE;
                            break;
                        case"S":
                            Strength = CargoStrength.STURDY;
                            break;
                    }
                    Cargo newCargo = new Cargo(name,weight,Strength);

                    if(!(dock.isEmpty()) && checkStrength(newCargo) > checkStrength(dock.peek()))
                    {
                        System.out.println("Operation failed! Cargo at top of stack cannot support weight.\n");
                    }
                    else
                    {
                        dock.push(newCargo);
                        System.out.println("Cargo \'" + name + "\'  pushed onto the dock.\n");
                    }
                    System.out.print(newShip.toString());
                    printCurrentStatus(newShip,dock,mass);
                    break;
                case"L":
                    try
                    {
                        System.out.print("\nSelect the load destination stack index: ");
                        int select = input.nextInt();
                        input.nextLine();
                        System.out.println();

                        newShip.pushCargo(dock.peek(),select);

                        System.out.println("Cargo \'" + dock.pop().getName() + "\' moved from dock to stack " + select + ".");
                    }
                    catch(FullStackException e)
                    {
                        System.out.println("Operation failed! Cargo stack is at maximum height.\n");
                    }
                    catch(ShipOverWeightException e)
                    {
                        System.out.println("Operation failed! Cargo would put ship overweight.\n");
                    }
                    catch(CargoStrengthException e)
                    {
                        System.out.println("Operation failed! Cargo at top of stack cannot support weight.\n");
                    }
                    System.out.print(newShip.toString());
                    printCurrentStatus(newShip,dock,mass);
                    break;
                case"U":
                    System.out.print("\nSelect the unload source stack index: \n");
                    int selection = input.nextInt();
                    input.nextLine();

                    try
                    {
                        if(dock.isEmpty())
                        {
                            dock.push(newShip.popCargo(selection));
                            System.out.println("Cargo \'" + dock.peek().getName() + "\' moved from " +
                                               "stack " + selection + " to dock.");
                        }
                        else if(!(dock.isEmpty()) && checkStrength(dock.peek()) >= checkStrength(newShip.getStack(selection).peek()))
                        {
                            dock.push(newShip.popCargo(selection));
                            System.out.println("Cargo \'" + dock.peek().getName() + "\'" +
                                    " moved from " +
                                               "stack " + selection + " to dock.");
                        }
                        else
                        {
                            System.out.println("Operation failed!\n");
                        }
                    }
                    catch(EmptyStackException e)
                    {
                        System.out.println("Stack is empty\n");
                    }

                    System.out.print(newShip.toString());
                    printCurrentStatus(newShip,dock,mass);
                    break;
                case"M":
                    System.out.print("Source stack index:  ");
                    int sourceIndex = input.nextInt();
                    input.nextLine();
                    System.out.print("Destination stack index: ");
                    int destinationIndex = input.nextInt();
                    input.nextLine();
                    try
                    {
                        newShip.pushCargo(newShip.popCargo(sourceIndex),destinationIndex);
                    }
                    catch(EmptyStackException e)
                    {
                        System.out.println("Stack is empty\n");
                        continue;
                    }
                    catch(FullStackException e)
                    {
                        System.out.println("Operation failed! Cargo stack is at maximum height.\n");
                        continue;
                    }
                    catch(ShipOverWeightException e)
                    {
                        System.out.println("Operation failed! Cargo would put ship overweight.\n");
                        continue;
                    }
                    catch(CargoStrengthException e)
                    {
                        System.out.println("Operation failed! Cargo at top of stack cannot support weight.\n");
                        continue;
                    }

                    System.out.println("Cargo \'" + newShip.getStack(destinationIndex).peek().getName() + "\' moved from stack " +
                                       sourceIndex + " to stack " + destinationIndex + ".\n");
                    System.out.print(newShip.toString());
                    printCurrentStatus(newShip,dock,mass);
                    break;
                case"K":
                    while(!(dock.isEmpty()))
                    {
                        dock.pop();
                    }
                    System.out.println("\nDock Cleared.\n");
                    System.out.print(newShip.toString());
                    printCurrentStatus(newShip,dock,mass);

                    if(autoRemove)
                    { index = "R"; }
                    break;
                case"P":
                    System.out.print(newShip.toString());
                    printCurrentStatus(newShip,dock,mass);
                    break;
                case"S":
                    System.out.print("Enter the name of the cargo: ");
                    String nameSearch = input.nextLine();

                    try
                    {
                        newShip.findAndPrint(nameSearch);
                    }
                    catch(EmptyStackException e)
                    {
                        System.out.println("Stack is empty\n");
                    }
                    break;
                case"R":
                    String removeCargo = "";
                    if(once == 1)
                    {
                        System.out.print("Enter the name of the cargo to be removed: ");
                        removeCargo = input.nextLine();
                        once--;
                    }
                    autoRemove = true;
                    index = newShip.remove(removeCargo,dock);
                    break;
                case"Q":
                    check = false;
                    System.out.println("Program terminating normally...");
                    break;
                default:
                    if(autoRemove)
                    {
                        autoRemove = false;
                    }
                    System.out.println("Invalid Option");
            }
        }
    }
}