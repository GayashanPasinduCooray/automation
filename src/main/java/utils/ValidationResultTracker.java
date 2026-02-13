package utils;

import java.util.HashMap;
import java.util.Map;

public class ValidationResultTracker {

    // Page -> PASS / FAIL counts
    private static final Map<String, Map<String, Integer>> results = new HashMap<>();

    // Page -> total execution time (ms)
    private static final Map<String, Long> executionTime = new HashMap<>();

    public static synchronized void recordPass(String pageClass) {
        record(pageClass, "PASS");
    }

    public static synchronized void recordFail(String pageClass) {
        record(pageClass, "FAIL");
    }

    private static void record(String pageClass, String status) {
        results.putIfAbsent(pageClass, new HashMap<>());
        Map<String, Integer> pageResult = results.get(pageClass);

        pageResult.put("PASS", pageResult.getOrDefault("PASS", 0));
        pageResult.put("FAIL", pageResult.getOrDefault("FAIL", 0));
        pageResult.put(status, pageResult.get(status) + 1);
    }

    // ⏱ execution time
    public static synchronized void addExecutionTime(String pageClass, long millis) {
        executionTime.put(
                pageClass,
                executionTime.getOrDefault(pageClass, 0L) + millis
        );
    }

    public static Map<String, Map<String, Integer>> getResults() {
        return results;
    }

    public static Map<String, Long> getExecutionTime() {
        return executionTime;
    }
}
