import java.util.HashMap;
import java.util.Map;

public class Affinity {
    private static final Map<String, Integer> affinityMap = new HashMap<>();

    static {
        // Example affinities (customize as you like)
        affinityMap.put("A_A",  -4);
        affinityMap.put("A_B",5);
        affinityMap.put("A_C",  -2);
        affinityMap.put("A_D",  1);
        affinityMap.put("A_H", 3);

        affinityMap.put("B_B",  -4);
        affinityMap.put("B_C", 5);
        affinityMap.put("B_D",  -2);
        affinityMap.put("B_H", -3);

        affinityMap.put("C_C",  -4);
        affinityMap.put("C_D",  5);
        affinityMap.put("C_H", -3);

        affinityMap.put("D_D",  -4);
        affinityMap.put("D_H", 3);

        affinityMap.put("H_H",  -1); // hole with hole
    }

    public static int getAffinity(F f1, F f2) {
        String key1 = f1.name() + "_" + f2.name();
        String key2 = f2.name() + "_" + f1.name();
        return affinityMap.getOrDefault(key1,
               affinityMap.getOrDefault(key2, 0));
    }
    public static boolean hasAffinity(String key) {
        return affinityMap.containsKey(key);
    }
}
