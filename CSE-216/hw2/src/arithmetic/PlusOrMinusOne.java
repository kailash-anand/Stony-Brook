package arithmetic;
/**
 * An enumeration representing the elements of a finite group of order two, where the elements
 * can be either PLUS_ONE or MINUS_ONE. Each element has an associated integer value.
 */
public enum PlusOrMinusOne {

    /**
     * Represents the element with the value +1 in the finite group.
     */
    PLUS_ONE(1),

    /**
     * Represents the element with the value -1 in the finite group.
     */
    MINUS_ONE(-1);

    /**
     * The integer value associated with each element.
     */
    private int value;

    /**
     * Constructs a PlusOrMinusOne enum with the specified integer value.
     *
     * @param value The integer value associated with the enum element.
     */
    PlusOrMinusOne(int value) {
        this.value = value;
    }

    /**
     * Returns a string representation of the enum element, which is the string
     * representation of its associated integer value.
     *
     * @return The string representation of the enum element.
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}