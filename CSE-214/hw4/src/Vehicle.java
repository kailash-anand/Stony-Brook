/**
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #4 for CSE 214
 * Date:
 *    March 21st, 2023
 */
public class Vehicle
{
    private static int serialCounter = 0;
    private int serialId;
    private int timeArrived;

    /**
     * Default Constructor
     *
     * @param initTimeArrived
     *  Time the vehicle arrived at the intersection.
     *
     * @throws IllegalArgumentException
     *  If initTimeArrived ≤ 0.
     */
    public Vehicle(int initTimeArrived)
    {
        if(initTimeArrived <= 0)
        {
            throw new IllegalArgumentException();
        }

        serialCounter++;
        timeArrived = initTimeArrived;
        serialId = serialCounter;
    }

    /**
     *
     * @return
     * the serial id
     */
    public int getSerialId()
    {
        return serialId;
    }

    /**
     *
     * @return
     *  The time the car arrived
     */
    public int getTimeArrived()
    {
        return timeArrived;
    }
}
