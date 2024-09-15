import java.util.*;
import java.util.concurrent.*;

public class MotifFinder {
    private ExecutorService executorService;

    public MotifFinder() {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    // Finds the most frequent motifs
    public List<String> findMostFrequentMotifs(String[] sequences, int motifSize) throws InterruptedException, ExecutionException {
        if (executorService.isShutdown()) {
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }

        // Store motif counts
        Map<String, Integer> motifCount = new ConcurrentHashMap<>();
        List<Future<Void>> futures = new ArrayList<>();

        // Search for motifs in paralell
        for (String sequence : sequences) {
            futures.add(executorService.submit(() -> {
                for (int i = 0; i <= sequence.length() - motifSize; i++) {
                    String motif = sequence.substring(i, i + motifSize);
                    motifCount.merge(motif, 1, Integer::sum);  // Update motif count
                }
                return null;
            }));
        }

        for (Future<Void> future : futures) {
            future.get();
        }

        executorService.shutdown();

        // Find the motif with the highest frequency
        int maxFrequency = Collections.max(motifCount.values());

        // motifs with the highest frequency
        List<String> mostFrequentMotifs = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : motifCount.entrySet()) {
            if (entry.getValue() == maxFrequency) {
                mostFrequentMotifs.add(entry.getKey());
            }
        }

        return mostFrequentMotifs;
    }

    public void shutdown() throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();  // Force shutdown if tasks are not finished
        }
    }
}
