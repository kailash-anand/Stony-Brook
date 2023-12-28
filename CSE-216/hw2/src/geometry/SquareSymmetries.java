package geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SquareSymmetries implements Symmetries<Square>
{
    RadialGraphSymmetries check = new RadialGraphSymmetries();
    @Override
    public boolean areSymmetric(Square s1, Square s2)
    {
        List<Point> firstNeighbors = Arrays.asList(s1.a,s1.b,s1.c,s1.d);
        List<Point> secondNeighbors = Arrays.asList(s2.a,s2.b,s2.c,s2.d);

        RadialGraph transformedSquareOne = new RadialGraph(s1.center(),firstNeighbors);
        RadialGraph transformedSquareTwo = new RadialGraph(s2.center(),secondNeighbors);
        if(check.areSymmetric(transformedSquareOne,transformedSquareTwo))
        {
            return true;
        }
        return false;
    }

    @Override
   public Collection<Square> symmetriesOf(Square square)
    {
        Collection<Square> symmetries = new ArrayList<Square>();
        Square temp ;

        symmetries.add(square);
        for(int i=0 ; i<=270;)
        {
            temp = square.rotateBy(i+90);
            symmetries.add(temp);
            i += 90;
        }

        {
            Point newA = new Point(square.a.name, square.b.x, square.b.y);
            Point newB = new Point(square.b.name, square.a.x, square.a.y);
            Point newC = new Point(square.c.name, square.d.x, square.d.y);
            Point newD = new Point(square.d.name, square.c.x, square.c.y);
            Square vertical = new Square(newA, newB, newC, newD);
            symmetries.add(vertical);
        }

        {
            Point newA = new Point(square.a.name, square.d.x, square.d.y);
            Point newB = new Point(square.b.name, square.c.x, square.c.y);
            Point newC = new Point(square.c.name, square.b.x, square.b.y);
            Point newD = new Point(square.d.name, square.a.x, square.a.y);
            Square horizontal = new Square(newA, newB, newC, newD);
            symmetries.add(horizontal);
        }

        {
            Point newA = new Point(square.a.name, square.c.x, square.c.y);
            Point newB = new Point(square.b.name, square.b.x, square.b.y);
            Point newC = new Point(square.c.name, square.a.x, square.a.y);
            Point newD = new Point(square.d.name, square.d.x, square.d.y);
            Square diagonalOne = new Square(newA, newB, newC, newD);
            symmetries.add(diagonalOne
            );
        }

        {
            Point newA = new Point(square.a.name, square.a.x, square.a.y);
            Point newB = new Point(square.b.name, square.d.x, square.d.y);
            Point newC = new Point(square.c.name, square.c.x, square.c.y);
            Point newD = new Point(square.d.name, square.b.x, square.b.y);
            Square diagonalTwo = new Square(newA, newB, newC, newD);
            symmetries.add(diagonalTwo);
        }

        return symmetries;
    }
}
