/**
 * The DiameterComparator class.
 *
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #7 for CSE 214
 * Date:
 *    April 30th, 2023
 */
import java.util.Comparator;

public class DiameterComparator implements Comparator<NearEarthObject>
{

    /**
     * Compares two NearEarthObjects based on Diameter.
     *
     * @param leftSide
     *  the first object to be compared.
     * @param rightSide
     *  the second object to be compared.
     *
     * @return
     *  An integer value of 1,0 or -1 indicating comparison.
     */
    @Override
    public int compare(NearEarthObject leftSide, NearEarthObject rightSide)
    {
        if(leftSide.getAverageDiameter() == rightSide.getAverageDiameter())
        {
            return 0;
        }
        if(leftSide.getAverageDiameter() > rightSide.getAverageDiameter())
        {
            return 1;
        }
        return -1;
    }
}
