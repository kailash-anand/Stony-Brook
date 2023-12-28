/**
 * The Main class to manage to database.
 *
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #7 for CSE 214
 * Date:
 *    April 30th, 2023
 */
import java.util.*;
import big.data.*;
public class NeoViewer
{

    public static void main(String[] args)
    {
        NeoDatabase database = new NeoDatabase();// Instantiates a NeoDatabase object.

        Scanner input = new Scanner(System.in);
        boolean run = true;

        while(run)
        {
            System.out.print(" A) Add a page to the database <page>\n" +
                             " S) Sort the database \n" +
                             " P) Print the database as a table.\n" +
                             " Q) Quit\n");

            System.out.print("Select a menu option: ");
            String index = input.nextLine();


            switch(index.toUpperCase())
            {
                case "A":
                    System.out.print("Enter the page to load: ");
                    int page = input.nextInt();
                    input.nextLine();

                    try
                    { database.addAll(database.buildQueryURL(page)); }
                    catch(IllegalArgumentException e)
                    { System.out.println("Invalid page number or page does not exist in database.\n"); }

                    System.out.println("Page loaded successfully!\n");
                    break;

                case "S":
                    System.out.println("            (R) Sort by referenceID\n" +
                            "            (D) Sort by diameter\n" +
                            "            (A) Sort by approach date\n" +
                            "            (M) Sort by miss distance\n" );

                    System.out.print("Please select a menu option: ");
                    String choose = input.nextLine();

                    switch(choose.toUpperCase())
                    {
                        case "R":
                            database.sort("R");
                            System.out.println("Table sorted on Reference ID.\n");
                            break;

                        case "D":
                            database.sort("D");
                            System.out.println("Table sorted on Diameter.\n");
                            break;

                        case "A":
                            database.sort("A");
                            System.out.println("Table sorted on Approach Date.\n");
                            break;

                        case "M":
                            database.sort("M");
                            System.out.println("Table sorted on Miss Distance\n");
                            break;
                    }
                    break;

                case "P":
                    database.printTable();
                    System.out.println("\n");
                    break;

                case "Q":
                    System.out.println("Program terminating normally...");
                    run = false;
                    break;
            }
        }
    }
}
