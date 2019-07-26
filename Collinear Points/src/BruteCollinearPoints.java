import java.util.ArrayList;

public class BruteCollinearPoints {

    private ArrayList<LineSegment> segments;
    private ArrayList<Point> linePoints;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points){

        if(points == null){
            throw new java.lang.IllegalArgumentException();
        }
        for(int i = 0 ; i < points.length; i ++){
            if(points[i] == null){
                throw new java.lang.IllegalArgumentException();
            }
        }
        for(int i = 0 ; i < points.length; i ++){
            for(int j = i + 1; j < points.length; j ++){
                if(points[i].compareTo(points[j]) == 0){
                    throw new java.lang.IllegalArgumentException();
                }
            }
        }

        segments = new ArrayList<LineSegment>();
        linePoints = new ArrayList<Point>();

        for(int i = 0; i < points.length; i ++){

            ArrayList<Point> sameLinePoints = new ArrayList<Point>();
            ArrayList<Double> slopeList = new ArrayList<Double>();

            for(int j = 0; j < points.length; j ++){
                double slope = points[i].slopeTo(points[j]);
                if(slope != Double.NEGATIVE_INFINITY){
                    int check = 0;
                    for(; check < slopeList.size(); check ++){
                        if(slopeList.get(check) == slope){
                            break;
                        }
                    }
                    if(check == slopeList.size()){
                        slopeList.add(slope);
                    }
                }
            }

            for(int k = 0; k < slopeList.size(); k ++){
                sameLinePoints.add(points[i]);
                for(int l = 0; l < points.length; l ++){
                    if(points[i].slopeTo(points[l]) == slopeList.get(k)){
                        sameLinePoints.add(points[l]);
                    }
                }
                if(sameLinePoints.size() >= 4){
                    linePoints.add(findMin(sameLinePoints));
                    linePoints.add(findMax(sameLinePoints));
                }
                sameLinePoints.clear();
            }
        }
        trim();
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

//    private void addToArray(LineSegment l){
//        if(segments.length > lineSegmentNum){
//            segments[lineSegmentNum] = l;
//            lineSegmentNum ++;
//        }else{
//            LineSegment[] newSegments = new LineSegment[segments.length * 2];
//            for(int i = 0; i < segments.length; i ++){
//                newSegments[i] = segments[i];
//            }
//            segments = newSegments;
//            segments[lineSegmentNum] = l;
//            lineSegmentNum ++;
//        }
//    }

    private void trim(){
        ArrayList<Point> result = new ArrayList<Point>();
        for(int i = 0; i < linePoints.size(); i = i + 2){
            boolean had = false;
            for(int j = 0; j < result.size(); j = j + 2){
                if((result.get(j).compareTo(linePoints.get(i)) == 0) && (result.get(j + 1).compareTo(linePoints.get(i + 1)) == 0)){
                    had = true;
                    break;
                }
            }
            if(!had){
                result.add(linePoints.get(i));
                result.add(linePoints.get(i + 1));
            }
        }
        segments = new ArrayList<LineSegment>();
        for(int i = 0; i < result.size(); i = i + 2){
            segments.add(new LineSegment(result.get(i), result.get(i + 1)));
        }
        result = null;
    }

    private Point findMin(ArrayList<Point> p){
        Point min = p.get(0);
        for(int i = 0; i < p.size(); i ++){
            if(p.get(i).compareTo(min) < 0){
                min = p.get(i);
            }
        }
        return min;
    }

    private Point findMax(ArrayList<Point> p){
        Point max = p.get(0);
        for(int i = 0; i < p.size(); i ++){
            if(p.get(i).compareTo(max) > 0){
                max = p.get(i);
            }
        }
        return max;
    }

    public static void main(String[] args) {
        Point a1 = new Point(1, 1);
        Point a2 = new Point(2, 2);
        Point a3 = new Point(3, 3);
        Point a4 = new Point(3, 4);
        Point a5 = new Point(3, 5);
        Point a6 = new Point(6, 6);

        Point[] p = new Point[]{a1, a2, a3, a4, a5, a6};
        BruteCollinearPoints b = new BruteCollinearPoints(p);

        System.out.println(b.numberOfSegments());
        LineSegment[] l = b.segments();
        for(int i = 0; i < l.length; i ++){
            System.out.println(l[i].toString());
        }

    }

}