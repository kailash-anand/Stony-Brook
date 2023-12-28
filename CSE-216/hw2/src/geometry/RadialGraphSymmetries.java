package geometry;

import java.util.ArrayList;
import java.util.Collection;

public class RadialGraphSymmetries implements Symmetries<RadialGraph>{

    @Override
    public boolean areSymmetric(RadialGraph s1, RadialGraph s2) {

        RadialGraph r1 = s1.translateBy(-s1.center().x, -s1.center().y);
        RadialGraph r2 = s2.translateBy(-s2.center().x, -s2.center().y);
        Point[] firstOrder = getOrder(r1);
        Point[] secondOrder = getOrder(r2);

        for(int i=0 ; i<firstOrder.length ; i++)
        {
            if(distance(firstOrder[i],secondOrder[i]) != 0)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public Collection<RadialGraph> symmetriesOf(RadialGraph radialGraph) {
        RadialGraph temp;
        Collection<RadialGraph> symmetries = new ArrayList<RadialGraph>();
        for(int i=0 ; i<360 ; i++)
        {
            temp = radialGraph.rotateBy(i);
            if(areSymmetric(radialGraph,temp))
            {
                symmetries.add(temp);
            }
        }
        return symmetries;
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

    private Point[] getOrder(RadialGraph r) {
        Point[] order = new Point[r.neighbors.size()];
        int index = 0;

        for(int i=0 ; i<r.neighbors.size() ; i++)
        {
            index = 0;
            while(order[index] != null && (angle(r.neighbors.get(i)) > angle(order[index])))
            {
                index++;
            }
            rearrange(order,index);
            order[index] = r.neighbors.get(i);
        }

        return order;
    }
}
