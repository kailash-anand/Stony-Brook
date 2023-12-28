public class BooleanSource
{
    private double probability;

    public BooleanSource(double newProbability)
    {
        if(newProbability > 1.0 || newProbability <= 0.0)
        {
            throw new IllegalArgumentException("Invalid Probability");
        }

        probability = newProbability;
    }

    public boolean requestArrived()
    {
        if(Math.random() < probability)
        { return true; }

        return false;
    }
}
