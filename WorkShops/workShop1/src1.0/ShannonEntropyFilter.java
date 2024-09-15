import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ShannonEntropyFilter {
    private ExecutorService executorService;

    public ShannonEntropyFilter() {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    // Calculates the Shannon entropy
    public double calculateEntropy(String sequence) {
        Map<Character, Long> frequencyMap = sequence.chars()
                .parallel()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        int length = sequence.length();
        return frequencyMap.values().stream()
                .parallel()
                .mapToDouble(count -> {
                    double probability = (double) count / length;
                    return -probability * (Math.log(probability) / Math.log(2));
                })
                .sum();
    }

    // Filters the sequences by entropy
    public List<String> filterSequencesByEntropy(String[] sequences, double threshold) throws InterruptedException, ExecutionException {
        if (executorService.isShutdown()) {
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }

        List<Future<String>> futures = new ArrayList<>();
        List<String> filteredSequences = new ArrayList<>();

        // Filter sequences in parallel
        for (String sequence : sequences) {
            futures.add(executorService.submit(() -> {
                double entropy = calculateEntropy(sequence);
                return entropy > threshold ? sequence : null;
            }));
        }

        // Collect filtered sequences
        for (Future<String> future : futures) {
            String filtered = future.get();
            if (filtered != null) {
                filteredSequences.add(filtered);
            }
        }

        shutdownExecutorService();
        return filteredSequences;
    }

    // Save the filtered sequences
    public void saveFilteredSequences(List<String> sequences, String folderName, String filename) throws IOException {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();  // Create folder if it doesn't exist
        }
        try (FileWriter writer = new FileWriter(new File(folder, filename))) {
            for (String sequence : sequences) {
                writer.write(sequence + "\n");
            }
        }
    }

    private void shutdownExecutorService() throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

    public void shutdown() throws InterruptedException {
        shutdownExecutorService();
    }
}

