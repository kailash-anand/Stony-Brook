import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation
{
    private int[][] system;
    private WeightedQuickUnionUF uf;
    private int sites;
    private int initVirtualSite;
    private int finalVirtualSite;

    public Percolation(int n)
    {
        if (n <= 0)
        {
            throw new IllegalArgumentException();
        }

        uf = new WeightedQuickUnionUF(n*n + 2);
        initVirtualSite = 0;
        finalVirtualSite = n*n + 1;
        sites = 0;

        system = new int[n][n]; 

        for (int i = 0; i < system.length; i++)
        {
            for (int j = 0; j < system[0].length; j++)
            {
                system[i][j] = 0;
            }
        }
    }

    public void open(int row, int col)
    {
        if (row < 0 || row > system.length-1 || col < 0 || col > system.length-1)
        {
            return;
        }
        if (!isOpen(row, col))
        {
            int[] formulae = new int[2];
            system[row][col] = 1;
            formulae[0] = system.length*row + (col+1);
            if (isOpen(row-1, col))
            {
                formulae[1] = system.length*(row-1) + (col+1);
                uf.union(formulae[0], formulae[1]);
            }
            if (isOpen(row, col+1))
            {
                formulae[1] = system.length*(row) + (col+2);
                uf.union(formulae[0], formulae[1]);
            }
            if (isOpen(row+1, col))
            {
                formulae[1] = system.length*(row+1) + (col+1);
                uf.union(formulae[0], formulae[1]);
            }
            if (isOpen(row, col-1))
            {
                formulae[1] = system.length*(row) + col;
                uf.union(formulae[0], formulae[1]);
            }
            if (row == 0)
            {
                uf.union(initVirtualSite, formulae[0]);
            }
            if (row == (system.length-1))
            {
                uf.union(finalVirtualSite, formulae[0]);
            }
            sites++;
        }
    }

    public boolean isOpen(int row, int col)
    {
        if (row < 0 || row > system.length-1 || col < 0 || col > system.length-1)
        {
            return false;
        }
        return system[row][col] == 1;
    }

    public boolean isFull(int row, int col)
    {
        return isOpen(row, col) && (uf.find(initVirtualSite) == uf.find(system.length*row + (col+1)));
    }

    public int numberOfOpenSites()
    {
        return sites;
    }

    public boolean percolates()
    {
        return uf.find(initVirtualSite) == uf.find(finalVirtualSite);
    }

    public void displayPath()
    {
        for(int i=0 ; i<system.length; i++)
        {
            for(int j=0 ; j<system[0].length ; j++)
            {
                if(system[i][j] == 1)
                {
                    System.out.print(system[i][j] + " ");
                }
                else{
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args)
    {
        int n = 20;
        Percolation p = new Percolation(n);
        while (!p.percolates())
        {
            p.open(StdRandom.uniformInt(n), StdRandom.uniformInt(n));
        }

        p.displayPath();
    }
}
    

