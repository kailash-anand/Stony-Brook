package geometry;

import java.util.Arrays;
import java.util.List;

/**
 * This class is given to you as an outline for testing your code. You can modify this as you want, but please keep in
 * mind that the lines already provided here as expected to work exactly as they are.
 *
 * @author Ritwik Banerjee
 */
public class GeometryTest {

    public static void main(String[] args) {
        //testRadialGraphSymmetries();
        testSquareSymmetries();
    }

    private static void testRadialGraphSymmetries() {
        Point center    = new Point("center", 1, 1);
        Point north     = new Point("north", 1, (1 + Math.sqrt(2)));
        Point southwest = new Point("southwest", 0, 0);
        Point southeast = new Point("southeast", 2, 0);

        RadialGraph g1 = new RadialGraph(center, Arrays.asList(north, southeast, southwest));
        RadialGraph g2 = g1.rotateBy(120);
        RadialGraphSymmetries graphSymmetries = new RadialGraphSymmetries();
        System.out.println(graphSymmetries.areSymmetric(g1, g2)); // must return false

        // obtain all the symmetries (including the identity) of g1, and print them one by one (remember that printing
        // will give the string representation of each radial graph, which must follow the specification of Shape's
        // toString() method)
        // List<RadialGraph> symmetries = (List<RadialGraph>) graphSymmetries.symmetriesOf(g1);
        // for (RadialGraph g : symmetries) System.out.println(g);
        // System.out.println();
    }

    private static void testSquareSymmetries() {
        Point  origin       = new Point("origin", 0, 0);
        Point  right        = new Point("right", 1, 0);
        Point  upright      = new Point("upright", 1, 1);
        Point  up           = new Point("up", 0, 1);
        Square sq1 = new Square(origin, right, upright, up);

        SquareSymmetries squareSymmetries = new SquareSymmetries();

        // obtain all the 8 symmetries (including the identity) of sq1, and print them one by one (remember that printing
        // will give the string representation of each square, which must follow the specification of Shape's toString()
        // method)
        List<Square> symmetries = (List<Square>) squareSymmetries.symmetriesOf(sq1);
        for (Square s : symmetries) System.out.println(s);
    }
}