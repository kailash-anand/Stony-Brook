/**
 * This class represents a finite group of order two, denoted as Z/2Z, where the group
 * elements are instances of the enum PlusOrMinusOne. The group operations include
 * a binary operation, identity element, and inverse element.
 *
 * @param <PlusOrMinusOne> The type of elements in the group (must be PlusOrMinusOne enum).
 */
package arithmetic;

import core.Group;

public class FiniteGroupOfOrderTwo implements Group<PlusOrMinusOne> {

    /**
     * Performs the binary operation of the finite group, where the result is
     * PlusOrMinusOne.PLUS_ONE if the operands are equal, and PlusOrMinusOne.MINUS_ONE
     * otherwise.
     *
     * @param x The first operand.
     * @param y The second operand.
     * @return The result of the binary operation.
     */
    public PlusOrMinusOne binaryOperation(PlusOrMinusOne x, PlusOrMinusOne y) {
        if (x == y) {
            return PlusOrMinusOne.PLUS_ONE;
        } else {
            return PlusOrMinusOne.MINUS_ONE;
        }
    }

    /**
     * Returns the identity element of the finite group, which is PlusOrMinusOne.PLUS_ONE.
     *
     * @return The identity element of the group.
     */
    @Override
    public PlusOrMinusOne identity() {
        return PlusOrMinusOne.PLUS_ONE;
    }

    /**
     * Computes the inverse element of a given element in the finite group.
     * If the input element is PlusOrMinusOne.PLUS_ONE, the inverse is PlusOrMinusOne.MINUS_ONE,
     * and vice versa.
     *
     * @param x The element for which to find the inverse.
     * @return The inverse element of the given element.
     */
    @Override
    public PlusOrMinusOne inverseOf(PlusOrMinusOne x) {
        if (x == PlusOrMinusOne.PLUS_ONE) {
            return PlusOrMinusOne.MINUS_ONE;
        } else {
            return PlusOrMinusOne.PLUS_ONE;
        }
    }
}