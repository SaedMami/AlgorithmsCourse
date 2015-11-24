import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * This class examines a set of planar points and then computes the maximal line
 * segments consisting of 4 points or more. It uses a fast approach by first
 * examining every point and then sorting all other points with respect to the
 * slope they make with it, to find a line segment, we find 3 or more adjacent
 * points making the same slope with the examined point..
 */
public class FastCollinearPoints {
    private ArrayList<LineSegment> segments = null;
    private HashSet<SlopePointPair> addedSegments = new HashSet<SlopePointPair>();

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
    public FastCollinearPoints(Point[] thePoints) {

        if (thePoints == null)
            throw new NullPointerException();
        segments = new ArrayList<LineSegment>();
        Point[] points = Arrays.copyOf(thePoints, thePoints.length);
        if (points.length < 4)
            return;

        for (int i = 0; i < points.length - 1; ++i) {
            Arrays.sort(points, i, points.length);
            Arrays.sort(points, i + 1, points.length, points[i].slopeOrder());
            int pointsCount = 1; // we seek 3 equal slope points
            // we examine each element of the remaining array with the element
            // right next to it
            int lastIndex = points.length - 2; // the last index to be examined
                                               // is the one before the last so
                                               // we don't go out of bounds
            for (int j = i + 1; j <= lastIndex; ++j) {
                if (equalSlopes(points[i], points[j], points[i], points[j + 1])) {
                    pointsCount++;
                    if (j == lastIndex) {
                        if (pointsCount >= 3) {
                            // we have a line segment, but the question is, last
                            // point included?
                            if (equalSlopes(points[i], points[lastIndex], points[i], points[lastIndex + 1])) {
                                addLineSegment(points[i], points[lastIndex + 1]);
                            } else {
                                addLineSegment(points[i], points[lastIndex]);
                            }
                        }
                    }
                } else {
                    // see how many previous points had the same slope with the
                    // first points
                    if (pointsCount >= 3) {
                        // we have j pointing to the last point in the line
                        addLineSegment(points[i], points[j]);
                    }
                    pointsCount = 1; // reset the counter
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

        StdOut.println("Done!");
    }

    private class SlopePointPair {
        private Point point;
        private Double slope;

        SlopePointPair(Point apoint, Double aslope) {
            point = apoint;
            slope = aslope;
        }

        @Override
        public boolean equals(Object other) {
            SlopePointPair otherPair = (SlopePointPair) other;
            if (this.point == otherPair.point && this.slope.equals(otherPair.slope)) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + point.toString().hashCode();
            hash = 53 * hash + slope.hashCode();
            return hash;
        }
    }

    private void addLineSegment(Point p1, Point p2) {
        if (p1.compareTo(p2) == 0)
            throw new IllegalArgumentException();
        if (!isLineSegmentPresent(p1, p2))
            segments.add(new LineSegment(p1, p2));

    }

    private boolean isLineSegmentPresent(Point p1, Point p2) {
        Double slope = p1.slopeTo(p2);
        SlopePointPair pair = new SlopePointPair(p2, slope);
        if (addedSegments.contains(pair)) {
            return true;
        }

        addedSegments.add(pair);
        return false;
    }

    private boolean equalSlopes(Point a, Point b, Point c, Point d) {
        double slope1 = a.slopeTo(b);
        double slope2 = c.slopeTo(d);

        if (slope1 == Double.NEGATIVE_INFINITY || slope2 == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException();
        }
        if (slope1 == slope2) {
            return true;
        }
        return false;
    }
}
