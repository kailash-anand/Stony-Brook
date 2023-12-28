public class Tester 
{
    private String requests;
    private String display;
    public Simulator simulation;

    public Tester()
    {}

    public void getSimulation(Simulator test)
    {
        simulation = test;
        requests = simulation.getQueue().toString();
        display = simulation.toString();
    }

    public boolean testBasicSimulation()
    {
        return false;
    }

    public static void main(String[] args)
    {
        Tester.tableTester();
    }

    public static void tableTester()
    {
        
    }
}
    

