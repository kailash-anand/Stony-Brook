public class Elevator
{
    private int currentFloor;
    private int elevatorState;
    private Request request; //For simple simulation
    private Request[] requests; //For optimized simulation 
    private int totalWeight;
    private int currentWeight;
    private Direction direction;

    public Elevator()
    {
        direction = Direction.UP;
        currentFloor = 1;
        request = null;
        requests = new Request[10];
        totalWeight = (int)(Math.random()*1000 + 500);
        elevatorState = ElevatorState.IDLE.ordinal();
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getElevatorState() {
        return elevatorState;
    }

    public void setElevatorState(int elevatorState) {
        this.elevatorState = elevatorState;
    }

    public Request getRequest() {
        return request;
    }

    /*
     * Gives options to set or reset weight.
     * Handles current weight using refreshWeight.
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    public void setRequests(Request request)
    {
        if(refreshWeight(request))
        {
            for(int i=0 ; i<requests.length ; i++)
            {
                if(requests[i] != null)
                { requests[i] = request; }
            }
        }
    
    }

    public String toString()
    {
        if(request != null)
         return getState(elevatorState) + "\n" + " current floor: " + currentFloor + "\nRequest: " + request.toString();
        else
            return getState(elevatorState) + "\n" + " current floor: " + currentFloor;
    }

    
    public String getState(int elevatorState)
    {
        String value = "";
        switch(elevatorState)
        {
            case 0:
                value = ElevatorState.IDLE.toString();
                break;

            case 1:
                value = ElevatorState.TO_SOURCE.toString();
                break;

            case 2:
                value = ElevatorState.TO_DESTINATION.toString();
                break;
        }
        return value;
    }

    /*
     * Resets the current weight after entry or exit of request
     * Also controls entry possiblity. 
     */
    public boolean refreshWeight(Request request)
    {
        if((request.getWeight() + currentWeight) > totalWeight)
        { return false; }

        currentWeight += request.getWeight();
        return true;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }
}
