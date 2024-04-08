package edu.ntnu.idatt2105.backend.util;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class contains logic surrounding the sorting of UUID seeds. This functionality is necessary for the
 * multiplayer mode.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.0 07.04.2024
 */
public class SortingUtil {

    /**
     * This method sorts the list with UUID seeds.
     * @param list          The list containing UUIDs.
     * @param uuid          The UUID.
     * @param keyExtractor  The function for extracting.
     * @return              A sorted list.
     * @param <T>           The type of list.
     */
    public static <T> List<T> sortListWithUuidSeed(List<T> list, UUID uuid, Function<T, String> keyExtractor) {
        Comparator<T> comparator = (o1, o2) -> {
            Random random1 = new Random(
                    uuid.hashCode() + keyExtractor.apply(o1).hashCode()
            );
            Random random2 = new Random(
                    uuid.hashCode() + keyExtractor.apply(o2).hashCode()
            );
            return Long.compare(random1.nextLong(), random2.nextLong());
        };

        return list.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
