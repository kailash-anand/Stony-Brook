public class Optimizer {
    /*
     * Weight Capacity and Control
     * Direction Control
     * Failure Control
     * Request Rejection
     * Emergency Situations
     * Door Operations
     * Elevator Types
     * Animation (needs javaFX)
     */

    private RequestTable upTable;
    private RequestTable downTable;
    BooleanSource arrival;
    private Elevator[] elevators;
    private int currentTime;
    private int requests;
    private int waitTime;
    private String display = "";

    public Optimizer()
    {}

    public void weightOptimizedSimulation(double initProbability, int numFloors, int numElevators, int simulationTime)
    {
        int requests = 0;
        int waitTime = 0;

        elevators = new Elevator[numElevators];
        arrival = new BooleanSource(initProbability);
        upTable = new RequestTable(numFloors);
        downTable = new RequestTable(numFloors);

        for(int i = 0; i<elevators.length ; i++)
        {
            elevators[i] = new Elevator();
        }

        for (currentTime = 1; currentTime <= simulationTime; currentTime++)
        {
            if (arrival.requestArrived())
            {
                Request newRequest = new Request(numFloors);
                newRequest.setTimeEntered(currentTime);
                if(newRequest.getDirection() == 0)
                {
                    upTable.add(newRequest);
                }
                else if(newRequest.getDirection() == 1)
                {
                    downTable.add(newRequest);
                }
            }


            for (int index = 0; index < elevators.length; index++)
            {
                switch (elevators[index].getDirection())
                {
                    case DOWN:
                        if((downTable.getRequests(elevators[index].getCurrentFloor())) != null)
                        {
                            Request[] toAdd = downTable.getRequests(elevators[index].getCurrentFloor());
                            for(int i=0 ; i<toAdd.length ; i++)
                            {
                                elevators[index].setRequests(toAdd[i]);
                            }
                        }
                        move(elevators[index],"Down");
                        break;
                    case UP:
                        if((upTable.getRequests(elevators[index].getCurrentFloor())) != null)
                        {
                            Request[] toAdd = upTable.getRequests(elevators[index].getCurrentFloor());
                            for(int i=0 ; i<toAdd.length ; i++)
                            {
                                elevators[index].setRequests(toAdd[i]);
                            }
                        }
                        move(elevators[index],"Up");
                        break;
                }

                display += "Elevator " + (index+1) + ", Time: " + currentTime + "\n";
                display += elevators[index].toString() + "\n\n";
            }

        }
        this.requests += requests;
        this.waitTime = waitTime;
    }

    public int getRequests()
    {
        return requests;
    }

    public int getWaitTime()
    {
        return  waitTime;
    }

    public String toString()
    {
        return display ;
    }

    public static void move(Elevator elevator, String target)
    {
        if(target.equalsIgnoreCase("Up"))
        {
            elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
        }
        else if(target.equalsIgnoreCase("Down"))
        {
           elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
        }
    }

    public static void changeState(Elevator elevator)
    {
        
    }
    
}
