import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double testTime;
    private double[] fractionArray;
    private double totalMean;
    private double s;
    private double loC;
    private double hiC;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){

        if(n <= 0 || trials <= 0){
            throw new java.lang.IllegalArgumentException();
        }

        testTime = (double)trials;
        fractionArray = new double[trials];

        double totalSites = (double)n*(double)n;
        for(int i = 0; i < testTime; i ++){
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                while(percolation.isOpen(row, col)){
                    row = StdRandom.uniform(n) + 1;
                    col = StdRandom.uniform(n) + 1;
                }
                percolation.open(row, col);
            }
            fractionArray[i] = (double)percolation.numberOfOpenSites() / totalSites;
        }

        totalMean = StdStats.mean(fractionArray);
        s = Math.sqrt(StdStats.var(fractionArray));
        loC = totalMean - 1.96 * s / Math.sqrt(testTime);
        hiC = totalMean + 1.96 * s / Math.sqrt(testTime);
    }

    // sample mean of percolation threshold
    public double mean(){
        return totalMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return s;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return loC;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return hiC;
    }

    public static void main(String[] args) {
//        int n = Integer.parseInt(args[0]);
//        int m = Integer.parseInt(args[1]);
//        PercolationStats stats = new PercolationStats(n, m);
//        System.out.println("mean = " + stats.mean());
//        System.out.println("stddev = " + stats.stddev());
//        System.out.println("95% confidence interval = [ " + stats.confidenceLo() + ", " + stats.confidenceHi() + " ]");
    }
}
