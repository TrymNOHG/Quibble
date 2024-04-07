package edu.ntnu.idatt2105.backend.util;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SortingUtil {
    public static <T> List<T> sortListWithUuidSeed(List<T> list, UUID uuid, Function<T, String> keyExtractor) {
        // Create a comparator that uses a Random object seeded with the UUID and element-specific seed
        Comparator<T> comparator = (o1, o2) -> {
            Random random1 = new Random(
                    uuid.hashCode() + keyExtractor.apply(o1).hashCode()
            );
            Random random2 = new Random(
                    uuid.hashCode() + keyExtractor.apply(o2).hashCode()
            );
            return Long.compare(random1.nextLong(), random2.nextLong());
        };

        // Sort the list using the comparator
        return list.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
