import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
public class PercolationStats 
{
    private double[] probabilities;
    private int count;

    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0)
        {
            throw new IllegalArgumentException();
        }

        count = 0;
        probabilities = new double[trials];

        for (int i = 0; i < trials; i++)
        {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates())
            {
                percolation.open(StdRandom.uniformInt(n), StdRandom.uniformInt(n));
            }
            probabilities[count++] = ((double) percolation.numberOfOpenSites()/(n*n));
        }
    }

    public double mean()
    {
        return StdStats.mean(probabilities);
    }

    public double stddev()
    {
        return StdStats.stddev(probabilities);
    }

    public double confidenceLo()
    {
        return mean() - 1.960*(stddev()/Math.sqrt(probabilities.length));
    }

    public double confidenceHi()
    {
        return mean() + 1.960*(stddev()/Math.sqrt(probabilities.length));
    }

    public static void main(String[] args)
    {
        System.out.print("Enter the side: ");
        int n = StdIn.readInt();

        System.out.print("Enter the number of trials: ");
        int trials = StdIn.readInt();

        PercolationStats stats = new PercolationStats(n, trials);

        System.out.println("Mean: " + stats.mean());
        System.out.println("StdDev: " + stats.stddev());
        System.out.println("95% confidence interval: " + "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
    

