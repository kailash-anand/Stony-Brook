package geometry;

import java.util.*;

public class RadialGraph extends Shape {
    public Point center;
    public List<Point> neighbors;

    public RadialGraph(Point center, List<Point> neighbors) {
        this(center);

        if(neighbors != null)
        {
            for(int i=0 ; i<(neighbors.size()-1) ; i++)
            {
                double prevDist = distance(center,neighbors.get(i));
                if(distance(center,neighbors.get(i+1)) != prevDist)
                {
                    throw new IllegalArgumentException("Edges are not of same length");
                }
            }
            this.neighbors = neighbors;
        }
    }

    public RadialGraph(Point center) {
        if(center == null)
        {
            throw new IllegalArgumentException("Center is null");
        }

        this.center = center;
    }

    @Override
    public RadialGraph rotateBy(int degrees)
    {
        double radian = Math.toRadians(degrees);
        double cos = Math.round((Math.cos(radian)*100.0))/100.0;
        double sin = Math.round((Math.sin(radian)*100.0))/100.0;

        if(neighbors == null)
        { return this; }

        RadialGraph result;
        Point toRotate;
        Point rotated;
        Point reShift = new Point("",center.x,center.y);
        List<Point> newNeighbors = new ArrayList<Point>();

        if(center.x == 0 && center.y == 0)
        {
            for(int i=0 ; i<neighbors.size() ; i++)
            {
                toRotate = neighbors.get(i);
                rotated = new Point(toRotate.name, (toRotate.x*cos - toRotate.y*sin + 0.0), (toRotate.x*sin + toRotate.y*cos + 0.0));
                newNeighbors.add(i,rotated);
            }
        }
        else
        {
            RadialGraph temp = translateBy(-center.x, -center.y);
            temp = temp.rotateBy(degrees);
            return temp.translateBy(center.x,center.y);
        }

        result = new RadialGraph(center, newNeighbors);
        return result;
    }

    @Override
    public RadialGraph translateBy(double x, double y) {
        RadialGraph result;
        Point toTranslate;
        Point translated;
        Point newCenter = new Point(center.name, center.x + x , center.y + y);
        List<Point> newNeighbors = new ArrayList<Point>();

        if(neighbors != null)
        {
            for(int i=0 ; i< neighbors.size() ; i++)
            {
                toTranslate = neighbors.get(i);
                translated = new Point(toTranslate.name, toTranslate.x + x, toTranslate.y + y);
                newNeighbors.add(i, translated);
            }
        }

        result = new RadialGraph(newCenter, newNeighbors);
        return result;
    }

    @Override
    public String toString() {
        String display = "";
        Point[] order;
        RadialGraph temp = new RadialGraph(this.center,this.neighbors);

        if(neighbors == null)
        {
            display += "[" + center.toString() + "]";
            return  display;
        }

        order = getOrder();

        if(!(center.x == 0 && center.y == 0))
        {
            temp = temp.translateBy(-center.x,-center.y);
            order = temp.getOrder();
            List<Point> newNeighbors = new ArrayList<Point>();
            for(int i=0 ; i<order.length ; i++)
            {
                newNeighbors.add(i,order[i]);
            }
            temp.neighbors = newNeighbors;
            temp = temp.translateBy(center.x,center.y);
            display += "[" + temp.center.toString();
            for(int i=0 ; i<temp.neighbors.size() ; i++)
            {
                display += "; " + temp.neighbors.get(i);
            }
            display += "]";
            return display;
        }

        display += "[" + center.toString();
        for(int i=0 ; i<order.length ; i++)
        {
            display += "; " + order[i].toString();
        }
        display += "]";

        return display;
    }

    @Override
    public Point center() {
        return center;
    }

    private double distance(Point a, Point b){
        double distance = Math.sqrt(Math.pow((b.y - a.y),2) + Math.pow((b.x - a.x),2));
        return distance;
    }

    private int angle(Point p) {
        int temp = 0;
        if(p.y == 0)
        {
            if(p.x > 0){
                return 0;
            }
            else{
                return 180;
            }
        }
        else if(p.x == 0)
        {
            if(p.y > 0){
                return 90;
            }
            else{
                return 270;
            }
        }
        temp = (int)(Math.atan2(p.y, p.x) * 180 / Math.PI);
        if(temp  < 0)
        { return temp + 360; }
        else
        { return temp; }
    }

    private void rearrange(Point[] points, int startIndex) {
        for(int i= points.length-1; i >= startIndex ; i--)
        {
            if(points[i] != null)
            {
                points[i+1] = points[i];
            }
        }
    }

    private Point[] getOrder() {
        Point[] order = new Point[neighbors.size()];
        int index = 0;

        for(int i=0 ; i<neighbors.size() ; i++)
        {
            index = 0;
            while(order[index] != null && (angle(neighbors.get(i)) > angle(order[index])))
            {
                index++;
            }
            rearrange(order,index);
            order[index] = neighbors.get(i);
        }

        return order;
    }

    /* Driver method given to you as an outline for testing your code. You can modify this as you want, but please keep
     * in mind that the lines already provided here as expected to work exactly as they are (some lines have additional
     * explanation of what is expected) */
    public static void main(String... args) {
        Point center = new Point("center", 3, 3);
        Point east = new Point("east", 5, 3);
        Point west = new Point("west", 1, 3);
        Point north = new Point("north", 3, 5);
        Point south = new Point("south", 3, 1);
        Point toofarsouth = new Point("south", 3, -0.5);

        RadialGraph graph = new RadialGraph(center, Arrays.asList(east,north,west,toofarsouth,south));
    }
}
