import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Solver {

    private SearchNode end;

    private class SearchNode {
        Board board;
        SearchNode previous;
        int moveNum;
        int hammingPriority;
        int manhattanPriority;

        SearchNode(Board board, SearchNode previous, int moveNum){
            this.board = board;
            this.previous = previous;
            this.moveNum = moveNum;
            this.hammingPriority = board.hamming() + moveNum;
            this.manhattanPriority = board.manhattan() + moveNum;
        }

        public Comparator<SearchNode> hammingPriority(){
            return new byHamming();
        }

        public Comparator<SearchNode> manhattanPriority(){
            return new byManhattan();
        }

        private class byHamming implements Comparator<SearchNode>{
            @Override
            public int compare(SearchNode s1, SearchNode s2) {
                int s1step = s1.hammingPriority;
                int s2step = s2.hammingPriority;
                if(s1step < s2step){
                    return -1;
                }else if(s1step == s2step){
                    return 0;
                }else{
                    return 1;
                }
            }
        }

        private class byManhattan implements Comparator<SearchNode>{
            @Override
            public int compare(SearchNode s1, SearchNode s2) {
                int s1step = s1.manhattanPriority;
                int s2step = s2.manhattanPriority;
                if(s1step < s2step){
                    return -1;
                }else if(s1step == s2step){
                    return 0;
                }else{
                    return 1;
                }
            }
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){

        if(initial == null){
            throw new java.lang.IllegalArgumentException();
        }

        SearchNode first = new SearchNode(initial, null, 0);
        SearchNode firstTwin = new SearchNode(initial.twin(), null, 0);
        MinPQ<SearchNode> minPQmanhattan = new MinPQ<SearchNode>(first.manhattanPriority());
        MinPQ<SearchNode> minPQmanhattanTwin = new MinPQ<SearchNode>(firstTwin.manhattanPriority());
        minPQmanhattan.insert(first);
        minPQmanhattanTwin.insert(firstTwin);
        first = null;
        firstTwin = null;

        SearchNode current = minPQmanhattan.delMin();
        SearchNode currentTwin = minPQmanhattanTwin.delMin();
        boolean lock = true;

        while(!current.board.isGoal() && !currentTwin.board.isGoal()){
            if(lock){
                Iterator<Board> cni = current.board.neighbors().iterator();
                while(cni.hasNext()){
                    Board nextStep = cni.next();
                    if(current.previous == null){
                        minPQmanhattan.insert(new SearchNode(nextStep, current, current.moveNum + 1));
                    }
                    if(current.previous != null && !nextStep.equals(current.previous.board)){
                        minPQmanhattan.insert(new SearchNode(nextStep, current, current.moveNum + 1));
                    }
                }
                current = minPQmanhattan.delMin();
            }else{
                Iterator<Board> cni = currentTwin.board.neighbors().iterator();
                while(cni.hasNext()){
                    Board nextStep = cni.next();
                    if(currentTwin.previous == null){
                        minPQmanhattanTwin.insert(new SearchNode(nextStep, currentTwin, currentTwin.moveNum + 1));
                    }
                    if(currentTwin.previous != null && !nextStep.equals(currentTwin.previous.board)){
                        minPQmanhattanTwin.insert(new SearchNode(nextStep, currentTwin, currentTwin.moveNum + 1));
                    }
                }
                currentTwin = minPQmanhattanTwin.delMin();
            }
            lock = !lock;
        }
        minPQmanhattan = null;
        minPQmanhattanTwin = null;

        if(current.board.isGoal()){
            end = current;
        }else{
            end = null;
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return end != null;
    }

    // min number of moves to solve initial board
    public int moves(){
        if(end == null){
            return -1;
        }
        return end.moveNum;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution(){
        if(end == null){
            return null;
        }
        return new BoardIterable();
    }

    private class BoardIterable implements Iterable<Board>{

        @Override
        public Iterator<Board> iterator() {
            return new BoardIterator();
        }

        private class BoardIterator implements Iterator<Board>{

            private ArrayList<Board> solution;
            private int index;

            public BoardIterator(){
                solution = new ArrayList<Board>();
                SearchNode s = end;
                while(s != null){
                    solution.add(s.board);
                    s = s.previous;
                }
                index = solution.size() - 1;
            }

            @Override
            public boolean hasNext() {
                return index != -1;
            }

            @Override
            public Board next() {
                index --;
                return solution.get(index + 1);
            }
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

//        int[][] board = new int[3][3];
//
//        board[0][0] = 0; board[0][1] = 1; board[0][2] = 3;
//        board[1][0] = 4; board[1][1] = 2; board[1][2] = 5;
//        board[2][0] = 7; board[2][1] = 8; board[2][2] = 6;
//
//        Board initial = new Board(board);
//        Solver solver = new Solver(initial);
//        System.out.println("======================================================");
//        SearchNode current = solver.end;
//        while(current != null){
//            System.out.println(current.moveNum + "      " + current.board.toString());
//            current = current.previous;
//        }
    }

}