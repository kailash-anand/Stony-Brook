/**
 * The Storage class
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

public class Storage implements Serializable
{
    private static long serialVersionUID;
    private int id;//The unique ID of the storage box.
    private String client;//The name of the client storing the box with the company.
    private String contents;//A brief description of the contents of the box.

    public Storage()
    {}

    /**
     * Parametrized constructor.
     *
     * @param id
     *  The id of the box.
     * @param client
     *  The client name.
     * @param contents
     *  The description of contents.
     */
    public Storage(int id, String client , String contents)
    {
        this.id = id;
        this.client = client;
        this.contents = contents;
    }

    /**
     * Getter method for id.
     *
     * @return
     *  The id of the storage box
     */
    public int getId()
    { return id; }

    /**
     * Getter method for client.
     *
     * @return
     *  The client of the storage box
     */
    public String getClient()
    { return client; }

    /**
     * Getter method for contents.
     *
     * @return
     *  The contents of the storage box
     */
    public String getContents()
    { return contents; }

    /**
     * Setter method for id
     *
     * @param id
     *  The id to be set.
     */
    public void setId(int id)
    { this.id = id; }

    /**
     * Setter method for client.
     *
     * @param client
     *  The client to be set.
     */
    public void setClient(String client)
    { this.client = client; }

    /**
     * Setter method for contents.
     *
     * @param contents
     *  The contents to be set.
     */
    public void setContents(String contents)
    { this.contents = contents; }

    /**
     * Methos to return a printable representation of the storage box.
     *
     * @return
     *  A string containing the id, client and contents of the storage box.
     */
    public String toString()
    {
        String print = "";
        print += id + " \t\t\t" + contents + " \t\t\t" + client;
        return print;
    }
}
