import java.util.*;
public class Analyzer
{
    
    public static void main(String[] args)
    {
        Tester testSimulation = new Tester();
        Scanner input = new Scanner(System.in);
        Simulator elevator = new Simulator();
        Optimizer optimizedSimulation = new Optimizer();

        System.out.print("Please enter the probability of arrival for requests: ");
        double probability = input.nextDouble();

        System.out.print("Please enter the number of floors: ");
        int floors = input.nextInt();

        System.out.print("Please enter the number of elevators: ");
        int elevators = input.nextInt();

        System.out.print("Please enter the simulation time: ");
        int length = input.nextInt();

        input.close();

        elevator.simulate(probability,floors,elevators,length);
        testSimulation.getSimulation(elevator);
        optimizedSimulation.weightOptimizedSimulation(probability,floors,elevators,length);

        System.out.println();
        System.out.println(elevator.getQueue().toString());
        System.out.println("\n\n");
        System.out.println(elevator.toString());
        System.out.println("Requests: " + elevator.getRequests());
        System.out.println("Average wait time: " + (elevator.getWaitTime()));

    }
}