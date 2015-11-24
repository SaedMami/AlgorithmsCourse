import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * This class examinees a set of planar points and then computes the maximal line
 * segments consisting of 4 points or more. It uses a "brute force" approach
 * where we look at every possible combination of 4 points and see whether they
 * form a line segment or not.
 */
public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments = null;

    /**
     * Finds all line segments containing 4 points
     * 
     * @param points
     *            The set of coplanar points in which to find the line segments
     * @throws NullPointerException
     *             if the argument of the constructor is null
     * @throws NullPointerException
     *             if any of the points in the input array is null
     * @throws IllegalArgumentException
     *             if two points have the same value
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();
        segments = new ArrayList<LineSegment>();

        for (int i = 0; i < points.length - 3; ++i) {
            for (int j = i + 1; j < points.length - 2; ++j) {
                for (int k = j + 1; k < points.length - 1; ++k) {
                    for (int w = k + 1; w < points.length; ++w) {

                        double slopeij = points[i].slopeTo(points[j]);
                        double slopejk = points[j].slopeTo(points[k]);
                        double slopekw = points[k].slopeTo(points[w]);

                        // extra for duplicate points check
                        double slopeik = points[i].slopeTo(points[k]);
                        double slopeiw = points[i].slopeTo(points[w]);
                        double slopejw = points[j].slopeTo(points[w]);

                        // check for points repetition
                        if (slopeij == Double.NEGATIVE_INFINITY || slopejk == Double.NEGATIVE_INFINITY
                                || slopekw == Double.NEGATIVE_INFINITY || slopeik == Double.NEGATIVE_INFINITY
                                || slopeiw == Double.NEGATIVE_INFINITY || slopejw == Double.NEGATIVE_INFINITY)
                            throw new IllegalArgumentException("repeated points present!");

                        // now choose the maximum line segment if the slopes are
                        // equal
                        if (slopeij == slopejk && slopejk == slopekw) {
                            int maxPoint = maxOutOfFour(points, i, j, k, w);
                            int minPoint = minOutOfFour(points, i, j, k, w);
                            segments.add(new LineSegment(points[minPoint], points[maxPoint]));
                        }
                    }
                }
            }
        }
        
        // throw their fucking exceptions
        if (points.length <= 3) {
            if (points[0].compareTo(points[1]) == 0) 
                throw new IllegalArgumentException();
            if (points.length == 3) {
                if (points[0].compareTo(points[2]) == 0 || points[1].compareTo(points[2]) == 0) {
                    throw new IllegalArgumentException();    
                }
            }
        }
    }

    /**
     * returns the number of maximal line segments found among the set of
     * provided points
     * 
     * @return number of line segments found
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     * returns the maximal line segments found among the set of provided points
     * 
     * @return an array containing the found line segments among the set of
     *         points provided
     */
    public LineSegment[] segments() {
        LineSegment[] a = new LineSegment[segments.size()];
        return (LineSegment[]) segments.toArray(a);

    }

    public static void main(String[] args) {

        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

    }

    private int maxOutOfFour(Point[] points, int i, int j, int k, int w) {
        int max = i;
        if (points[max].compareTo(points[j]) < 0) {
            max = j;
        }

        if (points[max].compareTo(points[k]) < 0) {
            max = k;
        }

        if (points[max].compareTo(points[w]) < 0) {
            max = w;
        }

        return max;
    }

    private int minOutOfFour(Point[] points, int i, int j, int k, int w) {
        int min = i;
        if (points[min].compareTo(points[j]) > 0) {
            min = j;
        }

        if (points[min].compareTo(points[k]) > 0) {
            min = k;
        }

        if (points[min].compareTo(points[w]) > 0) {
            min = w;
        }

        return min;
    }
}
