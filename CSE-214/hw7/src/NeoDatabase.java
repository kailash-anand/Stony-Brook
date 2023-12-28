/**
 * The NeoDatabase class which manages a collection of NearEarthObjects.
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

public class NeoDatabase extends Hashtable
{
    //API Key specific to this application used to perform queries to the NASA NeoW API.
    public static final String API_KEY = "yoI7nyIB6YnClqnPu0daRAwPYf83ZgGd1HdG2Src";
    //URL of the REST API used to conduct queries.
    public static final String API_ROOT = "https://api.nasa.gov/neo/rest/v1/neo/browse?";

    DataSource ds;
    ArrayList<NearEarthObject> newList = new ArrayList<>();
    NearEarthObject[] n;

    /**
     *  Default Constructor.
     *
     * @PostConditions
     *  The database has been constructed and is empty.
     */
    public NeoDatabase()
    {}

    /**
     *  Builds a query URL given a page number.
     *
     * @param pageNumber
     *  Integer ranging from 0 to 715 indicating the page the user wishes to load.
     *
     * @return
     *  0 ≤ page ≤ 715.
     *
     * @throws IllegalArgumentException
     *  If pageNumber is not in the valid range.
     */
    public String buildQueryURL(int pageNumber) throws IllegalArgumentException
    {
        if(pageNumber < 0 || pageNumber > 715)
        {
            throw new IllegalArgumentException();
        }

        return API_ROOT + "page=" + pageNumber + "&api_key=" + API_KEY;
    }

    /**
     * Opens a connection to the data source indicated by queryURL and adds all NearEarthObjects found in the dataset.
     *
     * @param queryURL
     *  String containing the URL requesting a dataset from the NASA NeoW service
     *
     * @throws IllegalArgumentException
     *  If queryURL is null or could not be resolved by the server.
     */
    public void addAll(String queryURL) throws IllegalArgumentException
    {
        if(queryURL == null)
        {
            throw new IllegalArgumentException();
        }

        ds = DataSource.connectJSON(queryURL);
        ds.load();
        n = ds.fetchArray("NearEarthObject","near_earth_objects/neo_reference_id"
                ,"near_earth_objects/name","near_earth_objects/absolute_magnitude_h"
        ,"near_earth_objects/estimated_diameter/kilometers/estimated_diameter_min",
                "near_earth_objects/estimated_diameter/kilometers/estimated_diameter_max",
                "near_earth_objects/is_potentially_hazardous_asteroid",
                "near_earth_objects/close_approach_data/epoch_date_close_approach",
                "near_earth_objects/close_approach_data/miss_distance/kilometers",
                "near_earth_objects/close_approach_data/orbiting_body");

        for(int i=0 ; i<n.length ; i++)
        {
            newList.add(n[i]);
        }
    }

    /**
     * Sorts the database using the specified Comparator of NearEarthObjects.
     *
     * @param selection
     *  Comparator of NearEarthObjects which will be used to sort the database.
     *
     * @PostConditions
     *  The database has been sorted based on the order specified by the inidcated Comparator of NearEarthObjects.
     */
    public void sort(String selection)
    {
        switch(selection)
        {
            case "R":
                Collections.sort((List)newList, new ReferenceIdComparator());
                break;

            case "D":
                Collections.sort((List)newList, new DiameterComparator());
                break;

            case "A":
                Collections.sort((List)newList, new ApproachDateComparator());
                break;

            case "M":
                Collections.sort((List)newList, new MissDistanceComparator());
                break;
        }
    }

    /**
     * Displays the database in a neat, tabular form, listing all member variables for each NearEarthObject.
     *
     * @Preconditions
     *  This NeoDatabase is initialized and not null.
     *
     * @PostConditions
     *  The table has been printed to the console but remains unchanged.
     */
    public void printTable()
    {
        System.out.printf("%5s%3s%14s%13s%6s%3s%10s%3s%5s%1s%11s%5s%9s%5s%8s\n","ID","|","Name","|","Mag.","|","Diameter","|",
                "Danger","|","Close Date","|","Miss Dist","|","Orbits");
        System.out.println("======================================================================================" +
                           "=========================\n");
        for(int i=0 ; i<newList.size() ; i++)
        {
            System.out.println(newList.get(i).toString());
        }
    }
}
