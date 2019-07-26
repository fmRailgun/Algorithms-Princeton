import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

    private TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET(){
        set = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty(){
        return set.isEmpty();
    }

    // number of points in the set
    public int size(){
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p){
        if(p == null){
            throw new java.lang.IllegalArgumentException();
        }
        if(!set.contains(p)){
            set.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p){
        if(p == null){
            throw new java.lang.IllegalArgumentException();
        }
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw(){
        for(Point2D p : set){
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        if(rect == null){
            throw new java.lang.IllegalArgumentException();
        }
        return new Point2DIterable(rect);
    }

    private class Point2DIterable implements Iterable<Point2D>{
        private RectHV rect;
        public Point2DIterable(RectHV rect){
            if(rect == null){
                throw new java.lang.IllegalArgumentException();
            }
            this.rect = rect;
        }
        @Override
        public Iterator<Point2D> iterator() {
            return new Point2DIterator(rect);
        }
    }

    private class Point2DIterator implements Iterator<Point2D>{
        private ArrayList<Point2D> list;
        private int index;
        public Point2DIterator(RectHV rect){
            list = new ArrayList<Point2D>();
            for(Point2D p : set){
                if(rect.contains(p)){
                    list.add(p);
                }
            }
            index = 0;
        }
        @Override
        public boolean hasNext() {
            return index < list.size();
        }
        @Override
        public Point2D next() {
            if(!hasNext()){
                throw new java.util.NoSuchElementException();
            }
            index ++;
            return list.get(index - 1);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p){
        if(set.isEmpty()){
            return null;
        }
        if(p == null){
            throw new java.lang.IllegalArgumentException();
        }
        Point2D result = set.first();
        for(Point2D current : set){
            if(current.distanceTo(p) < result.distanceTo(p)){
                result = current;
            }
        }
        return result;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args){
        Point2D p1 = new Point2D(0.0, 0.0);
        Point2D p2 = new Point2D(0.0, 0.0);
        KdTree pSet = new KdTree();
        pSet.insert(p1);
        pSet.insert(p2);
        pSet.draw();
        System.out.println(pSet.size());
    }
}