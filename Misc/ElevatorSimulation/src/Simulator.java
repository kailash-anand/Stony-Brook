public class Simulator
{
    private RequestQueue queue = new RequestQueue();
    BooleanSource arrival;
    private Elevator[] elevators;
    private int currentTime;
    private int requests;
    private int waitTime;
    private String display = "";

    public Simulator()
    {}

    public void simulate(double initProbability, int numFloors, int numElevators, int simulationTime)
    {
        int requests = 0;
        int waitTime = 0;

        elevators = new Elevator[numElevators];
        arrival = new BooleanSource(initProbability);

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
                queue.enqueue(newRequest);
            }

            for (int index = 0; index < elevators.length; index++)
            {
                switch (elevators[index].getElevatorState())
                {
                    case 0:
                        Request toProcess = queue.dequeue();
                        if(toProcess == null) {break;}
                        waitTime += currentTime - toProcess.getTimeEntered();
                        elevators[index].setRequest(toProcess);

                        if(elevators[index].getCurrentFloor() == toProcess.getSourceFloor())
                        {
                            if (toProcess.getSourceFloor() == toProcess.getDestinationFloor())
                            {
                                requests++;
                            }
                            else
                            {
                                elevators[index].setElevatorState(ElevatorState.TO_DESTINATION.ordinal());
                                move(elevators[index], "Destination");
                                changeState(elevators[index]);
                            }
                        }
                        else
                        {
                            elevators[index].setElevatorState(ElevatorState.TO_SOURCE.ordinal());
                            move(elevators[index], "Source");
                            changeState(elevators[index]);
                        }
                        break;

                    case 1:
                        changeState(elevators[index]);
                        move(elevators[index], "Source");
                        changeState(elevators[index]);
                        break;

                    case 2:
                        changeState(elevators[index]);
                        move(elevators[index], "Destination");
                        changeState(elevators[index]);
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

    public RequestQueue getQueue()
    {
        return queue;
    }

    public static void move(Elevator elevator, String target)
    {
        if(target.equalsIgnoreCase("Destination"))
        {
            if(elevator.getCurrentFloor() > elevator.getRequest().getDestinationFloor())
            { elevator.setCurrentFloor(elevator.getCurrentFloor() - 1); }
            else
            { elevator.setCurrentFloor(elevator.getCurrentFloor() + 1); }
        }
        else if(target.equalsIgnoreCase("Source"))
        {
            if(elevator.getCurrentFloor() > elevator.getRequest().getSourceFloor())
            { elevator.setCurrentFloor(elevator.getCurrentFloor() - 1); }
            else
            { elevator.setCurrentFloor(elevator.getCurrentFloor() + 1); }
        }
    }

    public static void changeState(Elevator elevator)
    {
        if(elevator.getCurrentFloor() == elevator.getRequest().getSourceFloor())
        {
            elevator.setElevatorState(ElevatorState.TO_DESTINATION.ordinal());
        }
        if(elevator.getCurrentFloor() == elevator.getRequest().getDestinationFloor() && (elevator.getElevatorState() == 2))
        {
            elevator.setElevatorState(ElevatorState.IDLE.ordinal());
            elevator.setRequest(null);
        }
    }
}
