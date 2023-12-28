import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

public class BijectionGroup {

    public static void main(String... args) {
        Set<Integer> a_few = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toSet());
        // you have to figure out the data type in the line below
        Set<Function<Integer, Integer>> bijections = bijectionsOf(a_few);
        bijections.forEach(aBijection -> {
            a_few.forEach(n -> System.out.printf("%d --> %d; ", n, aBijection.apply(n)));
            System.out.println();
        });

        // you have to figure out the data types in the lines below
        // some of these data types are functional objects, so look into
        // java.util.function.Function
        Group<Function<Integer, Integer>> g = bijectionGroup(a_few);
        Function<Integer, Integer> f1 = bijectionsOf(a_few).stream().findFirst().get();
        Function<Integer,Integer> f2 = g.inverseOf(f1);
        Function<Integer, Integer> id = g.identity();
        System.out.println("function: " + bijectionToString(f1, a_few));
        System.out.println("Inverse: " + bijectionToString(f2, a_few));
        System.out.println("Identity: " + bijectionToString(id, a_few));

    }

    /**
     * Finds all the bijections of a given set.
     * 
     * @param <T> The type of the elements in the set.
     * @param a_few The set of elements.
     * @return The set of bijections of the given set.
     */
    private static <T> Set<Function<T, T>> bijectionsOf(Set<T> a_few) {
        List<List<T>> permutations = combinations(new ArrayList<>(a_few)).collect(Collectors.toList());
        Set<Function<T, T>> bijections = new HashSet<Function<T, T>>();

        permutations.forEach(permutation -> {
            bijections.add(t -> {
                return permutation.get((a_few.stream().collect(Collectors.toList())).indexOf(t));
            });
        });

        return bijections;
    }

    /**
     * Helper method to find all the possible permutations of a given list of elements.
     * 
     * @param <T> The type of elements in the list.
     * @param input The list of elements.
     * @return A Stream of List containing all permutations.
     */
    public static <T> Stream<List<T>> combinations(List<T> input) {
        if (input.size() == 0) {
            return Stream.of(Collections.emptyList());
        } else {
            return input.stream().flatMap(e -> {
                List<T> remaining = new ArrayList<>(input);
                remaining.remove(e);
                return combinations(remaining)
                        .map(t -> {
                            List<T> newList = new ArrayList<>();
                            newList.add(e);
                            newList.addAll(t);
                            return newList;
                        });
            });
        }
    }

    /**
     * Creates a Group of bijections for the given input set.
     * 
     * @param <T> The type of the elements in the input set.
     * @param input The input set.
     * @return A group object with the inverse and identity of the bijections.
     */
    public static <T> Group<Function<T, T>> bijectionGroup(Set<T> input) {
        return new Group<Function<T, T>>() {
            Set<Function<T, T>> bijections = bijectionsOf(input);

            @Override
            public Function<T, T> binaryOperation(Function<T, T> one, Function<T, T> other) {
                return null;
            }

            @Override
            public Function<T, T> identity() {
                return null;
            }

            @Override
            public Function<T, T> inverseOf(Function<T, T> t) {
                Map<T,T> inverted = new HashMap<>();
                input.forEach(i -> inverted.put(t.apply(i), i));
                return inverted::get;
            }
        };
    }

    /**
     * Helper method to convert a bijection to a string representation.
     *
     * @param bijection The bijection to convert.
     * @param domain    The domain set.
     * @param <T>       The type of elements in the set.
     * @return The string representation of the bijection.
     */
    private static <T> String bijectionToString(Function<T, T> bijection, Set<T> domain) {
        return domain.stream().map(e -> e + " --> " + bijection.apply(e)).collect(Collectors.joining("; "));
    }
}
