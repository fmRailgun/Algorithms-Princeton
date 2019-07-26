import java.util.ArrayList;
import java.util.Iterator;

public class Board {

    private final int[][] tiles;
    private final int dimension;
    private int blankRow;
    private int blankCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles){
        dimension = tiles.length;
        this.tiles = new int[dimension][dimension];
        for(int i = 0; i < dimension; i ++){
            for(int j = 0; j < dimension; j ++){
                this.tiles[i][j] = tiles[i][j];
                if(tiles[i][j] == 0){
                    blankRow = i;
                    blankCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString(){
        String result = Integer.toString(dimension);
        result = result + "\n";
        for(int i = 0; i < dimension; i ++){
            for(int j = 0; j < dimension; j ++){
                result = result + " " + Integer.toString(tiles[i][j]);
            }
            result = result + "\n";
        }
        return result;
    }

    // board dimension n
    public int dimension(){
        return dimension;
    }

    // number of tiles out of place
    public int hamming(){
        int result = 0;
        int num = 1;
        for(int i = 0; i < dimension; i ++){
            for(int j = 0; j < dimension; j ++){
                if(tiles[i][j] != num){
                    result ++;
                }
                num ++;
            }
        }
        if(result > 0){
            result --;
        }

        return result;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        int result = 0;
        for(int i = 0; i < dimension; i ++){
            for(int j = 0; j < dimension; j ++){
                if(tiles[i][j] != 0){
                    int trueRow = (tiles[i][j] - 1) / dimension;
                    int trueCol = ((tiles[i][j] % dimension - 1) == -1)?dimension - 1:(tiles[i][j] % dimension - 1);
                    int distance = Math.abs(i - trueRow) + Math.abs(j - trueCol);
                    //System.out.println(tiles[i][j] + " trueRow:" + trueRow + " trueCol:" + trueCol + " distance:" + distance);
                    result = result + distance;
                }
            }
        }
        return result;
    }

    // is this board the goal board?
    public boolean isGoal(){
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y){
        if(y == null){
            return false;
        }
        String s1 = toString();
        String s2 = y.toString();
        return s1.equals(s2);
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){
        return new BoardIterable();
    }

    private class BoardIterable implements Iterable<Board>{
        @Override
        public Iterator<Board> iterator() {
            return new BoardIterator();
        }

        private class BoardIterator implements Iterator<Board>{

            public int index;
            public ArrayList<Board> list;

            public BoardIterator(){
                list = new ArrayList<Board>();
                index = 0;
                if(blankRow - 1 >= 0){
                    list.add(new Board(exchange(blankRow, blankCol, blankRow - 1, blankCol)));
                }
                if(blankCol + 1 < dimension){
                    list.add(new Board(exchange(blankRow, blankCol, blankRow, blankCol + 1)));
                }
                if(blankRow + 1 < dimension){
                    list.add(new Board(exchange(blankRow, blankCol, blankRow + 1, blankCol)));
                }
                if(blankCol - 1 >= 0){
                    list.add(new Board(exchange(blankRow, blankCol, blankRow, blankCol - 1)));
                }
            }

            @Override
            public boolean hasNext() {
                return index != list.size();
            }

            @Override
            public Board next() {
                index ++;
                return list.get(index - 1);
            }

            public int[][] exchange(int rowA, int colA, int rowB, int colB){
                int[][] result = new int[dimension][dimension];
                for(int i = 0; i < dimension; i ++){
                    for(int j = 0; j < dimension; j ++){
                        result[i][j] = tiles[i][j];
                    }
                }
                int temp = result[rowA][colA];
                result[rowA][colA] = result[rowB][colB];
                result[rowB][colB] = temp;
                return result;
            }
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        int[][] result = new int[dimension][dimension];
        for(int i = 0; i < dimension; i ++){
            for(int j = 0; j < dimension; j ++){
                result[i][j] = tiles[i][j];
            }
        }
        if(result[0][0] != 0 && result[0][1] != 0){
            int temp = result[0][0];
            result[0][0] = result[0][1];
            result[0][1] = temp;
        }else{
            int temp = result[1][0];
            result[1][0] = result[1][1];
            result[1][1] = temp;
        }

        return new Board(result);
    }

    // unit testing (not graded)
    public static void main(String[] args){
        int[][] board = {{1, 0}, {2, 3}};
        Board b = new Board(board);
        Board b2 = new Board(board);
        System.out.println(b);
    }

}