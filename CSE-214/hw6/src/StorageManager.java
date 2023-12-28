/**
 * The StorageManager class.(Main class)
 *
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #6 for CSE 214
 * Date:
 *    April 17th, 2023
 */
import java.io.*;
import java.util.*;
public class StorageManager implements Serializable
{
    private static StorageTable newTable = new StorageTable();

    public StorageManager() throws IOException {
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        System.out.print("Hello, and welcome to rocky Stream Storage Manager\n");

        Scanner input = new Scanner(System.in);
        try
        {
            FileInputStream newFile = new FileInputStream("storage.obj");
            ObjectInputStream newStream = new ObjectInputStream(newFile);

            newTable = (StorageTable)newStream.readObject();
            newStream.close();
        }
        catch(FileNotFoundException ignored)
        {}

        boolean run = true;
        while(run)
        {
            System.out.println("P - Print all storage boxes\n" +
                    "A - Insert into storage box\n" +
                    "R - Remove contents from a storage box\n" +
                    "C - Select all boxes owned by a particular client\n" +
                    "F - Find a box by ID and display its owner and contents\n" +
                    "Q - Quit and save workspace\n" +
                    "X - Quit and delete workspace\n" +
                    "\n");

            System.out.print("Please select an option: ");
            String choice = input.nextLine();
            System.out.println();

            switch(choice)
            {
                case "P":
                    System.out.print(newTable.toString(null) + "\n");
                    break;

                case "A" :
                    System.out.print("Please enter id: ");
                    int id = input.nextInt();
                    input.nextLine();

                    System.out.print("Please enter client: ");
                    String client = input.nextLine();

                    System.out.print("Please enter Contents: ");
                    String contents = input.nextLine();

                    Storage newStorage = new Storage(id,client,contents);
                    try
                    {newTable.putStorage(id,newStorage);}
                    catch(IllegalArgumentException e)
                    {
                        System.out.println("Storage id already exists.\n");
                        continue;
                    }

                    System.out.println("Storage " + id + " set!\n");
                    break;

                case "R":
                    System.out.print("Please enter id: ");
                    id = input.nextInt();
                    input.nextLine();

                    if(newTable.getStorage(id) != null)
                    {
                        newTable.remove(id);
                        System.out.print("Box " + id + " is now removed.\n");
                    }
                    else
                    { System.out.println("No such storage found.\n"); }
                    break;

                case "C":
                    System.out.print("Enter the name of the client: ");
                    client = input.nextLine();

                    System.out.print(newTable.toString(client));
                    System.out.print("\n");
                    break;

                case "F":
                    System.out.print("Please enter Id: ");
                    id = input.nextInt();
                    input.nextLine();

                    Storage returned = newTable.getStorage(id);

                    if(returned != null)
                    {
                        System.out.println("Box " + id);
                        System.out.println("Contents: " + returned.getContents());
                        System.out.println("Owner: " + returned.getClient());
                        System.out.print("\n");
                    }
                    else
                    {
                        System.out.println("No storage found.\n");
                    }
                    break;

                case "Q":
                    FileOutputStream file = new FileOutputStream("storage.obj");
                    ObjectOutputStream Stream = new ObjectOutputStream(file);

                    Stream.writeObject(newTable);
                    Stream.close();

                    System.out.print("Storage Manager is quitting, current storage is saved for next session.");
                    run = false;
                    break;

                case "X":
                    System.out.println("Storage Manager is quitting, all data is being erased.");
                    run = false;
                    break;

                default:
            }
        }
    }
}
