/**
 * The StorageTable Class
 *
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #6 for CSE 214
 * Date:
 *    April 17th, 2023
 */
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class StorageTable extends Hashtable implements Serializable
{
     private static int serialVersionUID;
     Hashtable<Integer,Storage> newTable = new Hashtable();

    /**
     * Manually inserts a Storage object into the table using the specified key.
     *
     * @param storageId
     *  The unique key for the Storage object.
     * @param storage
     *  The Storage object to insert into the table.
     *
     * @Preconditions
     *  storageId ≥ 0 and does not already exist in the table.
     *  Storage ≠ null
     *
     * @Postconditions
     *  The Storage has been inserted into the table with the indicated key.
     *
     * @throws IllegalArgumentException
     *  If any of the preconditions is not met.
     */
     public void putStorage(int storageId, Storage storage) throws IllegalArgumentException
     {
         if(storage == null || storageId < 0)
         {
             throw new IllegalArgumentException();
         }

         if(getStorage(storageId) == null)
         {  newTable.put(storageId,storage); }
         else
         {
             throw new IllegalArgumentException();
         }
     }

    /**
     * Retrieve the Storage from the table having the indicated storageID.
     *
     * @param storageId
     *  Key of the Storage to retrieve from the table.
     *
     * @return
     *  A Storage object with the given key, null otherwise.
     */
     public Storage getStorage(int storageId)
     {
         Storage storage;
         try
         { storage = (Storage)newTable.get(storageId); }
         catch(NullPointerException n)
         { return null; }
         return storage;
     }

    /**
     * Returns a printable representation of the hashtable.
     *
     * @param decision
     *  The string which decides the contents to be printed.
     *
     * @return
     *  A printable representation of the hashtable.
     */
     public String toString(String decision)
     {
         Enumeration keys = newTable.keys();
         String print = "";
         print += "Box# \t\t\t" + "Contents \t\t\t" + "Owner \t\t\t\n";
         print += "--------------------------------------------------\n";

         while(keys.hasMoreElements())
         {
             if(decision == null)
             {
                 print += getStorage((int)keys.nextElement()).toString();
                 print += "\n";
             }
             else
             {
                 Storage track = getStorage((int)keys.nextElement());
                 if(track.getClient().equals(decision))
                 {
                     print += track.toString();
                     print += "\n";
                 }
             }

         }

         return print;
     }

    /**
     * Removes the value corresponding to the key.
     *
     * @param  key
     *  The key which points to the corresponding value.
     */
     public void remove(int key)
     {
         newTable.remove(key);
     }
}
