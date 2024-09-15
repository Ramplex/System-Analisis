import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        DNAGenerator dnaGenerator = new DNAGenerator();
        MotifFinder motifFinder = new MotifFinder();
        ShannonEntropyFilter entropyFilter = new ShannonEntropyFilter();

        // Parameters for testing
        int[] sequenceCounts = {1000, 5000, 10000};  // Different sequence counts
        int[] motifSizes = {5, 7, 10};               // Different motif sizes
        double[][] baseProbabilities = {             // Different base probabilities
            {0.25, 0.25, 0.25, 0.25},
            {0.4, 0.3, 0.2, 0.1},
            {0.1, 0.2, 0.3, 0.4}
        };
        double[] entropyThresholds = {0.3, 0.5, 0.7}; // Entropy thresholds

        // Folder for saving datasets
        String datasetFolder = "datasets";

        // Perform experiments
        for (int n : sequenceCounts) {
            for (double[] probabilities : baseProbabilities) {
                for (int motifSize : motifSizes) {
                    // Generate sequences
                    System.out.println("Generating sequences for n=" + n + ", motifSize=" + motifSize);
                    String[] sequences = new String[n];
                    for (int i = 0; i < n; i++) {
                        sequences[i] = dnaGenerator.generateSequence(100, probabilities);
                    }

                    // Save generated sequences
                    String filename = "dna_sequences_" + n + "_prob" + probabilities[0] + ".txt";
                    dnaGenerator.saveSequences(sequences, datasetFolder, filename);

                    // Find frequent motifs in original dataset
                    long startTime = System.nanoTime();
                    List<String> motifs = motifFinder.findMostFrequentMotifs(sequences, motifSize);
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime) / 1_000_000;

                    // Display results
                    System.out.println("Frequent motifs: " + motifs);
                    System.out.println("Motif search time: " + duration + " ms");

                    // Apply Shannon Entropy filter
                    for (double threshold : entropyThresholds) {
                        System.out.println("Applying entropy filter with threshold: " + threshold);
                        List<String> filteredSequences = entropyFilter.filterSequencesByEntropy(sequences, threshold);

                        // Save filtered sequences
                        String filteredFilename = "filtered_sequences_" + n + "_threshold" + threshold + ".txt";
                        entropyFilter.saveFilteredSequences(filteredSequences, datasetFolder, filteredFilename);

                        // Search motifs in filtered sequences
                        if (!filteredSequences.isEmpty()) {
                            String[] filteredSeqArray = filteredSequences.toArray(new String[0]);
                            long startTimeFiltered = System.nanoTime();
                            List<String> filteredMotifs = motifFinder.findMostFrequentMotifs(filteredSeqArray, motifSize);
                            long endTimeFiltered = System.nanoTime();
                            long durationFiltered = (endTimeFiltered - startTimeFiltered) / 1_000_000;

                            // Display results
                            System.out.println("Frequent motifs after filter: " + filteredMotifs);
                            System.out.println("Motif search time after filter: " + durationFiltered + " ms");
                        } else {
                            System.out.println("No sequences passed the filter.");
                        }
                    }
                }
            }
        }

        motifFinder.shutdown();
        entropyFilter.shutdown();
    }
}
