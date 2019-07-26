import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Iterator;

public class KdTree {

    private Node root;

    private class Node{
        public Point2D point;
        public Node left, right;
        public boolean vertical;
        public int N;
        public RectHV rect;

        public Node(Point2D p, boolean v, int N, RectHV rect){
            if(p == null){
                throw new java.lang.IllegalArgumentException();
            }
            point = p;
            vertical = v;
            this.N = N;
            this.rect = rect;
        }
    }

    // construct an empty set of points
    public KdTree(){}

    // is the set empty?
    public boolean isEmpty(){
        return root == null;
    }

    // number of points in the set
    public int size(){
        return size(root);
    }

    private int size(Node x){
        if(x == null) return 0;
        else return x.N;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p){
        if(p == null){
            throw new java.lang.IllegalArgumentException();
        }
        if(isEmpty()){
            root = new Node(p, true, 1, new RectHV(0.0, 0.0, 1.0 ,1.0));
        }
        if(!contains(p)){
            root = insert(null, root, p, true);
        }
    }

    private Node insert(Node parent, Node current, Point2D p, boolean vertical){
        if(current == null){
            RectHV r;
            if(parent.vertical){
                if(p.x() < parent.point.x()){
                    r = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.point.x(), parent.rect.ymax());
                }else{
                    r = new RectHV(parent.point.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                }
            }else{
                if(p.y() >= parent.point.y()){
                    r = new RectHV(parent.rect.xmin(), parent.point.y(), parent.rect.xmax(), parent.rect.ymax());
                }else{
                    r = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.point.y());
                }
            }
            return new Node(p, vertical, 1, r);
        }
        if(vertical){
            if(p.x() < current.point.x()){
                current.left = insert(current, current.left, p, !vertical);
            }else{
                current.right = insert(current, current.right, p, !vertical);
            }
        }else{
            if(p.y() >= current.point.y()){
                current.left = insert(current, current.left, p, !vertical);
            }else{
                current.right = insert(current, current.right, p, !vertical);
            }
        }
        current.N = size(current.left) + size(current.right) + 1;
        return current;
    }

    // does the set contain point p?
    public boolean contains(Point2D p){
        if(p == null){
            throw new java.lang.IllegalArgumentException();
        }
        if(isEmpty()){
            return false;
        }
        Node current = root;
        while(current != null){
            if(current.point.equals(p)){
                return true;
            }
            if(current.vertical){
                if(p.x() < current.point.x()){
                    current = current.left;
                }else{
                    current = current.right;
                }
            }else{
                if(p.y() >= current.point.y()){
                    current = current.left;
                }else{
                    current = current.right;
                }
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw(){
        draw(root);
    }

    private void draw(Node n){
        if(n == null){
            return;
        }else{
//            StdDraw.setPenColor(StdDraw.BLACK);
//            StdDraw.setPenRadius(0.03);
            n.point.draw();
//            StdDraw.setPenRadius(0.01);
//            if(n.vertical){
//                StdDraw.setPenColor(StdDraw.RED);
//                StdDraw.line(n.point.x(), n.rect.ymin(), n.point.x(), n.rect.ymax());
//            }else{
//                StdDraw.setPenColor(StdDraw.BLUE);
//                StdDraw.line(n.rect.xmin(), n.point.y(), n.rect.xmax(), n.point.y());
//            }
        }
        draw(n.left);
        draw(n.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        if(rect == null){
            throw new java.lang.IllegalArgumentException();
        }
        return new rangeIterable(rect);
    }

    private class rangeIterable implements Iterable<Point2D>{
        private RectHV rect;
        public rangeIterable(RectHV rect){
            this.rect = rect;
        }
        @Override
        public Iterator<Point2D> iterator() {
            return new rangeIterator(rect);
        }
    }

    private class rangeIterator implements Iterator<Point2D>{
        private RectHV rect;
        private ArrayList<Point2D> list;
        private int index;
        public rangeIterator(RectHV rect){
            index = 0;
            list = new ArrayList<Point2D>();
            this.rect = rect;
            search(root);
        }
        private void search(Node x){
            if(x == null){
                return;
            }
            if(rect.contains(x.point)){
                list.add(x.point);
            }
            if(x.left != null && x.left.rect.intersects(rect)){
                search(x.left);
            }
            if(x.right != null && x.right.rect.intersects(rect)){
                search(x.right);
            }
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
        if(p == null){
            throw new java.lang.IllegalArgumentException();
        }
        if(isEmpty()){
            return null;
        }
        Node best = root;
        Node current = root;
        best = nearest(best, current, p);
        return best.point;
    }

    private Node nearest(Node b, Node current, Point2D p){
        if(current == null){
            return b;
        }
        Node best = b;
        if(current.point.distanceSquaredTo(p) < best.point.distanceSquaredTo(p)){
            best = current;
        }
        if(current.vertical){
            if(p.x() < current.point.x()){
                best = nearest(best, current.left, p);
                if(current.right != null && current.right.rect.distanceSquaredTo(p) < best.point.distanceSquaredTo(p)){
                    best = nearest(best, current.right, p);
                }
            }else if(p.x() >= current.point.x()){
                best = nearest(best, current.right, p);
                if(current.left != null && current.left.rect.distanceSquaredTo(p) < best.point.distanceSquaredTo(p)){
                    best = nearest(best, current.left, p);
                }
            }
        }else{
            if(p.y() >= current.point.y()){
                best = nearest(best, current.left, p);
                if(current.right != null && current.right.rect.distanceSquaredTo(p) < best.point.distanceSquaredTo(p)){
                    best = nearest(best, current.right, p);
                }
            }else if(p.y() < current.point.y()){
                best = nearest(best, current.right, p);
                if(current.left != null && current.left.rect.distanceSquaredTo(p) < best.point.distanceSquaredTo(p)){
                    best = nearest(best, current.left, p);
                }
            }
        }
        return best;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args){
        String filename = args[0];
        In in = new In(filename);
        KdTree t = new KdTree();
        while (!in.isEmpty()){
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            t.insert(p);
        }
        t.draw();
        StdDraw.show();
    }

}