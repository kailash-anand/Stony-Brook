/**
 * @author
 *   Kailash Anand ID:115158238
 * Assignment:
 *    Recitation: R01
 *    Homework #4 for CSE 214
 * Date:
 *    March 21st, 2023
 */
import java.util.*;
public class IntersectionSimulator
{
    private static TwoWayRoad[] newRoads;

    public static void simulate(int simulationTime, double arrivalProbability, String[] roadNames, int[] maxGreenTimes)
    {

        newRoads = new TwoWayRoad[roadNames.length];
        for(int i=0 ; i< roadNames.length ; i++)
        {
            newRoads[i] = new TwoWayRoad(roadNames[i],maxGreenTimes[i]);
        }

        int timeStep = 1;
        Intersection one = new Intersection(newRoads);

        one.setProbability(arrivalProbability);
        Vehicle[] dequeued = new Vehicle[4];

        while(timeStep <= simulationTime)
        {
            one.timeStep();
            timeStep++;
        }
        one.setProbability(0.0);
        for(int i=0 ; i<50 ; i++)
        {
            one.timeStep();
        }

    }

    public static void main(String[] args)
    {



        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Intersection Simulator 2021\n\n");

        System.out.print("Input the simulation time: ");
        int simulationTime = input.nextInt();
        input.nextLine();

        System.out.print("Input the arrival probability: ");
        double arrivalProb = input.nextDouble();
        input.nextLine();

        System.out.print("Input the number of streets: ");
        int numStreets = input.nextInt();
        input.nextLine();

        String[] roadnames = new String[numStreets];
        int[] maxGreenTimes = new int[numStreets];
        for(int i=0 ; i<numStreets ; i++)
        {
            System.out.print("Input Street " + i  +" name: ");
            roadnames[i] = input.nextLine();
        }

        for(int i=0 ; i< roadnames.length ; i++)
        {
            System.out.print("Input max green time for " + roadnames[i] + ": ");
            maxGreenTimes[i] = input.nextInt();
            input.nextLine();
        }

        System.out.println("Starting simulation...\n");


        simulate(simulationTime,arrivalProb,roadnames,maxGreenTimes);
    }
}
