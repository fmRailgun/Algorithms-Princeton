import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private ArrayList<LineSegment> segments;
    private ArrayList<Point> linePoints;

    // finds all line segments containing 4 or more pointsCopy
    public FastCollinearPoints(Point[] points){
        if(points == null){
            throw new java.lang.IllegalArgumentException();
        }
        for(int i = 0 ; i < points.length; i ++){
            if(points[i] == null){
                throw new java.lang.IllegalArgumentException();
            }
        }
        Point[] pointsCopy = new Point[points.length];
        System.arraycopy(points, 0, pointsCopy, 0, points.length);
        Arrays.sort(pointsCopy);
        for(int i = 0 ; i < pointsCopy.length - 1; i ++){
            if(pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0){
                throw new java.lang.IllegalArgumentException();
            }
        }

        segments = new ArrayList<LineSegment>();
        linePoints = new ArrayList<Point>();

        // outer loop checks each point as a starting point
        for(int i = 0; i < pointsCopy.length; i ++){
            // store all points except the starting point
            ArrayList<Point> pointListAL = new ArrayList<Point>();
            for(int j = 0; j < pointsCopy.length; j ++){
                if(j != i){
                    pointListAL.add(pointsCopy[j]);
                }
            }
            Point[] pointList = new Point[pointListAL.size()];
            for(int j = 0; j < pointList.length; j ++){
                pointList[j] = pointListAL.get(j);
            }
            pointListAL = null;
            // sort
            Comparator<Point> c = pointsCopy[i].slopeOrder();
            Arrays.sort(pointList, c);
            // go through the ordered point list，检查斜率相同的点有没有超过4个（包括原点）
            int j = 0;
            while(j < pointList.length){
                double slope = pointsCopy[i].slopeTo(pointList[j]);
                Point first = pointList[j];
                int count = 2;
                j ++;
                while(j < pointList.length && slope == pointsCopy[i].slopeTo(pointList[j])){
                    count ++;
                    j ++;
                }
                Point last = pointList[j - 1];
                if(count >= 4){
                    if(pointsCopy[i].compareTo(first) < 0){
                        segments.add(new LineSegment(pointsCopy[i], last));
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments(){
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments(){
        LineSegment[] result = new LineSegment[segments.size()];
        for(int i = 0; i < segments.size(); i ++){
            result[i] = segments.get(i);
        }
        return result;
    }

    public static void main(String[] args) {
        Point a0 = new Point(1, 1);
        Point a1 = new Point(2, 2);
        Point a2 = new Point(3, 3);
        Point a3 = new Point(4, 4);
        Point a4 = new Point(1, 2);
        Point a5 = new Point(1, 3);
        Point a6 = new Point(1, 4);
        Point a7 = new Point(0, 1);
        Point a8 = new Point(-2, 1);
        Point a9 = new Point(-3, 1);

        Point a10 = new Point(8, 1);
        Point a11 = new Point(7, 2);
        Point a12 = new Point(6, 3);
        Point a13 = new Point(5, 4);
        Point a14 = new Point(4, 5);

        Point[] p = new Point[]{a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14};
        FastCollinearPoints f = new FastCollinearPoints(p);

        System.out.println(f.numberOfSegments());
        LineSegment[] l = f.segments();
        for(int i = 0; i < l.length; i ++){
            System.out.println(l[i].toString());
        }

    }

}