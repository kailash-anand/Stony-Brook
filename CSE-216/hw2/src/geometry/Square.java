package geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Square extends Shape {

    Point a;
    Point b;
    Point c;
    Point d;
    Point center;
    String display;

    @Override
    public Point center() {
       return midPoint(a,c);
    }

    @Override
    public Square rotateBy(int degrees) {
        double[] selection = {degrees};
        return graphTransformation(this, "rotate", selection);
    }

    @Override
    public Square translateBy(double x, double y) {
        double[] selection = {x,y};
        return graphTransformation(this, "translate", selection);
    }

    @Override
    public String toString() {
        double[] selection = {-1};
        graphTransformation(this, "display", selection);
        return display;
    }

    public Square(Point a, Point b, Point c, Point d) {
        if(!(isSquare(a, b, c, d)))
        {
            throw new IllegalArgumentException("Not a valid set of points that form a square");
        }

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.center = center();
    }

    private boolean isSquare(Point a, Point b, Point c, Point d) {
        double d1 = Math.round(distance(a, c)*100.0)/100;
        double d2 = Math.round(Math.sqrt(2)*distance(a, b))*100.0/100;
        if(distance(a, b) == distance(a, d))
        {
            if(d1 == d2)
            {
                return true;
            }
        }
        return false;
    }

    private double distance(Point a, Point b){
        double distance = Math.sqrt(Math.pow((b.y - a.y),2) + Math.pow((b.x - a.x),2));
        return distance;
    }

    private Point midPoint(Point a, Point b){
        Point mid = new Point("center", ((a.x+b.x)/2), ((a.y+b.y)/2));
        return mid;
    }

    private Square graphTransformation(Square s, String mode, double[] selection){
        RadialGraph transformedSquare = new RadialGraph(center, Arrays.asList(a,b,c,d));
        Point[] vertices = new Point[4];

        if(mode.equals("rotate")) {
            transformedSquare = transformedSquare.rotateBy((int)selection[0]);
            //System.out.println(transformedSquare);
        }
        else if(mode.equals("translate")) {
            transformedSquare = transformedSquare.translateBy(selection[0], selection[1]);
        }
        else if(mode.equals("display")){
            display = transformedSquare.toString();
            return null;
        }

        for(int i=0 ; i<transformedSquare.neighbors.size() ; i++)
        {
            vertices[i] = transformedSquare.neighbors.get(i);
        }
        Square reTransformed = new Square(vertices[0], vertices[1], vertices[2], vertices[3]);
        return reTransformed;
    }

    public static void main(String... args) {
        Point upright   = new Point("upright", 1, 1);
        Point upleft    = new Point("upleft", -1, 1);
        Point downleft  = new Point("downleft", -1, -1);
        Point downright = new Point("downright", 1, -1);
        Point east      = new Point("east", 1, 0);
        Point north     = new Point("north", 0, 1);
        Point west      = new Point("west", -1, 0);
        Point south     = new Point("south", 0, -1);
        Square square = new Square(upright, upleft, downleft, downright);
    }
}