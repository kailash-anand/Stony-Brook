/**
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #4 for CSE 214
 * Date:
 *    March 21st, 2023
 */
public class Intersection {
    private final int MAX_ROADS = 4;
    private TwoWayRoad[] roads;
    private int lightIndex;
    private int countdownTimer;
    private int counter = 1;
    private int count;
    private double probability;
    private Vehicle[] dequeued = new Vehicle[4];

    private String addDirection = "";
    private String addLane = "";
    private String arrivingCars = "";
    private String laneCars;
    BooleanSourceHW4 prob;
    VehicleQueue tempQueue = new VehicleQueue();

    /**
     * Sets the probability.
     *
     * @param p
     *  The probability to be set.
     */
    public void setProbability(double p) {
        probability = p;
        prob = new BooleanSourceHW4(probability);
    }

    /**
     * Constructor which initializes the roads array.
     *
     * @param initRoads
     *  Array of roads to be used by this intersection.
     *
     * @throws IllegalArgumentException
     * If initRoads is null.
     * If any index of initRoads is null.
     * initRoads.length > MAX_ROADS.
     */
    public Intersection(TwoWayRoad[] initRoads) {
        if (initRoads == null || initRoads.length > MAX_ROADS) {
            throw new IllegalArgumentException();
        }

        roads = new TwoWayRoad[initRoads.length];
        for (int i = 0; i < roads.length; i++) {
            roads[i] = new TwoWayRoad();
        }

        for (int i = 0; i < initRoads.length; i++) {
            if (initRoads[i] != null) {
                roads[i] = initRoads[i];
            } else {
                throw new IllegalArgumentException();
            }
        }

        lightIndex = 0;
        countdownTimer = roads[lightIndex].getGreenTime();
    }

    /**
     * Performs a single iteration through the intersection
     *
     * @return
     *  An array of Vehicles which have passed though the intersection during this time step.
     */
    public Vehicle[] timeStep() {
        if (countdownTimer == 0 ) {
            lightIndex = (lightIndex + 1) % roads.length;
            countdownTimer = roads[lightIndex].getGreenTime();
        }

        for (int i = 0; i < roads.length; i++) {
            for (int j = 0; j < roads[i].NUM_WAYS; j++) {
                for (int k = 0; k < roads[i].NUM_LANES; k++) {
                    if (prob.occursHW4()) {
                            Vehicle newVehicle = new Vehicle(roads[i].getGreenTime());

                            if (j == 0 && k == 0) {
                                addDirection = "FORWARD";
                                addLane = "LEFT";
                            } else if (j == 0 && k == 1) {
                                addDirection = "FORWARD";
                                addLane = "MIDDLE";
                            } else if (j == 0 && k == 2) {
                                addDirection = "FORWARD";
                                addLane = "RIGHT";
                            } else if (j == 1 && k == 0) {
                                addDirection = "BACKWARD";
                                addLane = "RIGHT";
                            } else if (j == 1 && k == 1) {
                                addDirection = "BACKWARD";
                                addLane = "MIDDLE";
                            } else if (j == 1 && k == 2) {
                                addDirection = "BACKWARD";
                                addLane = "LEFT";
                            }
                            enqueueVehicle(i, j, k, newVehicle);

                            laneCars += "[" + newVehicle.getSerialId() + "]," + i + "," + j + "," + k + ",";
                            arrivingCars += "\tCar [" + newVehicle.getSerialId() + "] arrived in " + roads[lightIndex].getName()
                                    + " going " + addDirection + " in " + addLane + "\n";
                    }
                }
            }
        }


        dequeued = roads[lightIndex].proceed(countdownTimer);
        count++;
        display();
        arrivingCars = "";
        countdownTimer--;
        return dequeued;
    }

    /**
     * Enqueues a vehicle onto a lane in the intersection.
     *
     * @param roadIndex
     *  Index of the road in roads which contains the lane to enqueue onto.
     *
     * @param wayIndex
     *  Index of the direction the vehicle is headed. Can either be TwoWayRoad.FORWARD or TwoWayRoad.BACKWARD
     *
     * @param laneIndex
     *  Index of the lane on which the vehicle is to be enqueue.
     *
     * @param vehicle
     *  The Vehicle to enqueue onto the lane.
     *
     * @throws IllegalArgumentException
     *  If vehicle is null.
     */
    public void enqueueVehicle(int roadIndex, int wayIndex, int laneIndex, Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException();
        }
        if (roadIndex < 0 || roadIndex >= roads.length || wayIndex < 0 || wayIndex > 1 || laneIndex < 0 || laneIndex > 2) {
            throw new IllegalArgumentException();
        }

        roads[roadIndex].enqueueVehicle(wayIndex, laneIndex, vehicle);
    }

    /**
     * Prints the intersection to the terminal in a neatly formatted manner.
     */
    public void display() {
        String print = "";

        print += "############################################\n";

        print += "Time Step: " + count + "\n";
        print += "  " + roads[lightIndex].getLightValue().name() + " light for " + roads[lightIndex].getName() + "\n";
        print += "  Timer = " + countdownTimer + "\n\n";

        print += "  ARRIVING CARS:\n";

        System.out.println(print);
        if(probability == 0.0)
        { System.out.println("Cars are no longer arriving"); }
        else {
            System.out.println(arrivingCars);
        }

        print = "";
        print += "\n";
        print += "  PASSING CARS:\n";

        for (int i = 0; i < dequeued.length; i++) {
            if (dequeued[i] != null) {
                print += "    Car [" + dequeued[i].getSerialId() + "] passed through.";
                print += " Wait time of " + dequeued[i].getTimeArrived() + "\n";
            }
        }

        System.out.println(print);

        for (int i = 0; i < roads.length; i++) {
            System.out.print("  " + roads[i].getName() + ":\n");
            System.out.print("\t\t\t\tFORWARD\t\t\t\tBACKWARD\n");
            System.out.print(" ======================\t\t\t\t======================\n");

            System.out.print(carDisplay(i, roads[i].FORWARD_WAY, roads[i].LEFT_LANE));
            System.out.printf("%26s", "[L]");
            System.out.printf("%10s", "[R]");
            System.out.print(carDisplay(i, roads[i].BACKWARD_WAY, roads[i].LEFT_LANE) + "\n");
            System.out.print(" ----------------------\t\t\t\t----------------------\n");

            System.out.print(carDisplay(i, roads[i].FORWARD_WAY, roads[i].MIDDLE_LANE));
            System.out.printf("%26s", "[M]");
            System.out.printf("%10s", "[M]");
            System.out.print(carDisplay(i, roads[i].BACKWARD_WAY, roads[i].MIDDLE_LANE) + "\n");
            System.out.print(" ----------------------\t\t\t\t----------------------\n");

            System.out.print(carDisplay(i, roads[i].FORWARD_WAY, roads[i].RIGHT_LANE));
            System.out.printf("%26s", "[R]");
            System.out.printf("%10s", "[L]");
            System.out.print(carDisplay(i, roads[i].BACKWARD_WAY, roads[i].RIGHT_LANE) + "\n");
            System.out.print(" ======================\t\t\t\t======================\n\n");

        }

        System.out.println("Statistics: ");


        System.out.print("############################################\n");


    }

    /**
     * Displays the cars in a single lane
     *
     * @param roadIndex
     *  the road to be selected
     *
     * @param wayIndex
     *  the direction of the car
     *
     * @param laneIndex
     *  the lane in which the car is
     *
     * @return
     *  a string containing the cars in the lane
     */
    public String carDisplay(int roadIndex, int wayIndex, int laneIndex)
    {
        String display = "";
        String[] arrivingCar = laneCars.split(",");

        if (roads[roadIndex].isLaneEmpty(wayIndex, laneIndex))
        {
            return "";
        }

        for (int i = 0; i < dequeued.length; i++) {
            if (dequeued[i] != null) {

            }
        }


        for (int i = 0; i < arrivingCar.length - 3; i++)
        {
            if (!(arrivingCar[i].matches("[0-9]")) && (Integer.parseInt(arrivingCar[i + 1]) == roadIndex))
            {
                if (Integer.parseInt(arrivingCar[i + 2]) == (wayIndex) && Integer.parseInt(arrivingCar[i + 3]) == (laneIndex))
                {
                    display += arrivingCar[i];
                }
            }
        }
        return display;
    }

}
