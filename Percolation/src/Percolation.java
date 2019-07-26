import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size;
    private int openSiteNum = 0;
    private boolean[] openList;             // 0 is blocked, 1 is open
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF grid2;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if(n <= 0){
            throw new java.lang.IllegalArgumentException();
        }
        grid = new WeightedQuickUnionUF(n*n + 2);
        for(int i = 1; i <= n; i ++){
            grid.union(0, i);
            grid.union(n*n + 1, n*n - i + 1);
        }
        openList = new boolean[n*n + 2];        // by default, all blocked
        openList[0] = true;
        openList[n*n + 1] = true;
        size = n;

        grid2 = new WeightedQuickUnionUF(n*n + 1);
        for(int i = 1; i <= n; i ++){
            grid2.union(0, i);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        if(validate(row, col) && !isOpen(row, col)){
            openSiteNum ++;
            openList[indexInArray(row, col)] = true;
            if (row - 1 > 0 && isOpen(row - 1, col)){
                grid.union(indexInArray(row - 1, col), indexInArray(row, col));
                grid2.union(indexInArray(row - 1, col), indexInArray(row, col));
            }
            if (row + 1 <= size && isOpen(row + 1, col)){
                grid.union(indexInArray(row + 1, col), indexInArray(row, col));
                grid2.union(indexInArray(row + 1, col), indexInArray(row, col));
            }
            if (col - 1 > 0 && isOpen(row, col - 1)){
                grid.union(indexInArray(row, col - 1), indexInArray(row, col));
                grid2.union(indexInArray(row, col - 1), indexInArray(row, col));
            }
            if (col + 1 <= size && isOpen(row, col + 1)){
                grid.union(indexInArray(row, col + 1), indexInArray(row, col));
                grid2.union(indexInArray(row, col + 1), indexInArray(row, col));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        validate(row, col);
        return openList[indexInArray(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        return (grid2.connected(0, indexInArray(row, col)) && openList[indexInArray(row, col)]);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return openSiteNum;
    }

    // does the system percolate?
    public boolean percolates(){
        if(size == 1 && !openList[indexInArray(1, 1)]){
            return false;
        }
        return grid.connected(0, size*size + 1);
    }

    private int indexInArray(int row, int col){
        validate(row, col);
        return (row - 1) * size + col;
    }

    private boolean validate(int row, int col){
        if(row <= 0 || row > size || col <= 0 || col > size){
            throw new java.lang.IllegalArgumentException();
        }
        return true;
    }

    // test client (optional)
    public static void main(String[] args){

    }
}
