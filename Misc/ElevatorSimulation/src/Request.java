public class Request
{
    private int sourceFloor;
    private int destinationFloor;
    private int timeEntered;
    private int weight;
    public int direction;

    public Request(int numFloors)
    {
        weight = (int)(Math.random()*100);
        sourceFloor = (int)(Math.random()*numFloors) + 1;
        destinationFloor = (int)(Math.random()*numFloors) + 1;
        setDirection();
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public void setSourceFloor(int sourceFloor) {
        this.sourceFloor = sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public int getTimeEntered() {
        return timeEntered;
    }

    public int getWeight() {
        return weight;
    }

    public void setTimeEntered(int timeEntered) {
        this.timeEntered = timeEntered;
    }

    public String toString()
    {
        return "Request from floor " + sourceFloor + " to " + destinationFloor + ", " +
               "Time entered: " + timeEntered;
    }

    /*
     * Manages the request acceptance and makes neccessary changes.
     */
    public void status(boolean value)
    {
        boolean willWait = true;
        if(!value)
        {
            setTimeEntered(0);
            if(Math.random()*10 < 2)
            { willWait = false; }
        }
        if(!willWait)
        {
            setSourceFloor(-1);
            setDestinationFloor(-1);
            direction = -1;
        }
    }

    private void setDirection()
    {
        if(destinationFloor > sourceFloor)
        { direction = Direction.UP.ordinal(); }
        else
        { direction = Direction.DOWN.ordinal(); }
    }

    public int getDirection()
    {
        return direction;
    }
}
