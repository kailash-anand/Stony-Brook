/**
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #4 for CSE 214
 * Date:
 *    March 21st, 2023
 */
public class TwoWayRoad
{
    public final int FORWARD_WAY = 0;
    public final int BACKWARD_WAY = 1;
    public final int NUM_WAYS = 2;
    public final int LEFT_LANE = 0;
    public final int MIDDLE_LANE = 1;
    public final int RIGHT_LANE = 2;
    public final int NUM_LANES = 3;

    private String name;
    private int greenTime;
    private int leftSignalGreenTime;
    public VehicleQueue lanes[][] = new VehicleQueue[NUM_WAYS][NUM_LANES];
    private LightValue lightValue = LightValue.RED;

    public TwoWayRoad()
    {}

    /**
     * Default Constructor.
     *
     * @param initName
     *  The name of the road.
     *
     * @param initGreenTime
     *  The amount of time that the light will be active for this particular road.
     *
     * @throws
     *  if initGreenTime ≤ 0 or initName=null.
     */
    public TwoWayRoad(String initName, int initGreenTime)
    {
        if(initName == null || initGreenTime <= 0)
        {
            throw new IllegalArgumentException();
        }

        for(int i=0 ; i<lanes.length ; i++)
        {
            for(int j=0 ; j<lanes[0].length ; j++)
            {
                lanes[i][j] = new VehicleQueue();
            }
        }
        name = initName;
        greenTime = initGreenTime;
        leftSignalGreenTime = (int)((1.0/NUM_LANES)*initGreenTime);
    }

    /**
     *
     * @return
     *  The green time for this road
     */
    public int getGreenTime()
    {
        return greenTime;
    }

    /**
     *
     * @return
     *  returns the light value of the road
     */
    public LightValue getLightValue()
    {
        return lightValue;
    }

    /**
     *
     * @return
     *  Returns the name of the road
     */
    public String getName()
    {
        return name;
    }

    /**
     * Executes the passage of time in the simulation.
     *
     * @param timerVal
     *  The current timer value, determines the state of the light.
     *
     * @return
     *  An array of Vehicles that has been dequeued during this time step.
     *
     * @throws
     *  if timerval ≤ 0.
     */
    public Vehicle[] proceed(int timerVal)
    {
        if(timerVal <= 0)
        {
            throw new IllegalArgumentException();
        }

        int count = 0;
        Vehicle[] dequeued = new Vehicle[4];

        if(timerVal > leftSignalGreenTime)
        {
            lightValue = LightValue.GREEN;
            for(int i=0 ; i<NUM_LANES ; i++)
            {
                if(!isLaneEmpty(FORWARD_WAY,i)&&i!=0)
                {
                    dequeued[count] = lanes[FORWARD_WAY][i].dequeue();
                    count++;
                }

                if(!isLaneEmpty(BACKWARD_WAY,i)&&i!=2)
                {
                    dequeued[count] = lanes[BACKWARD_WAY][i].dequeue();
                    count++;
                }
            }
        }

        if(timerVal <= leftSignalGreenTime)
        {
            lightValue = LightValue.LEFT_SIGNAL;

            if(!isLaneEmpty(FORWARD_WAY,LEFT_LANE))
            {
                dequeued[count] = lanes[FORWARD_WAY][LEFT_LANE].dequeue();
                count++;
            }

            if(!isLaneEmpty(BACKWARD_WAY,RIGHT_LANE))
            {
                dequeued[count] = lanes[BACKWARD_WAY][RIGHT_LANE].dequeue();
            }
        }
        return dequeued;
    }

    /**
     * Enqueues a vehicle into a the specified lane.
     *
     * @param wayIndex
     *  The direction the car is going in.
     *
     * @param laneIndex
     *  The direction the car is going in.
     *
     * @param vehicle
     *  The vehicle to enqueue; must not be null.
     *
     * @throws IllegalArgumentException
     *  if wayIndex &gt 1 || wayIndex &lt 0 || laneIndex &lt 0 || laneIndex &gt 2 or vehicle==null
     */
    public void enqueueVehicle(int wayIndex, int laneIndex , Vehicle vehicle)
    {
        if(vehicle == null)
        {
            throw new IllegalArgumentException();
        }
        if(wayIndex < 0 || wayIndex > 1 || laneIndex < 0 || laneIndex > 2)
        {
            throw new IllegalArgumentException();
        }

        lanes[wayIndex][laneIndex].enqueue(vehicle);
    }

    /**
     * Checks if a specified lane is empty.
     *
     * @param wayIndex
     *  The direction of the lane.
     *
     * @param laneIndex
     *  The index of the lane to check.
     *
     * @return
     *  true if the lane is empty, else false.
     *
     * @throws IllegalArgumentException
     *  wayIndex &gt 1 || wayIndex &lt 0 || laneIndex &lt 0 || laneIndex &gt 2.
     */
    public boolean isLaneEmpty(int wayIndex, int laneIndex)
    {
        if(wayIndex > 1 || wayIndex < 0 || laneIndex < 0 || laneIndex > 2)
        {
            throw new IllegalArgumentException();
        }

        if(lanes[wayIndex][laneIndex].isEmpty())
        {
            return true;
        }
        return false;
    }
}
