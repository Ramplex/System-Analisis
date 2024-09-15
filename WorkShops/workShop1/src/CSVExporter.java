import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CSVExporter {

    // export data with entropy variations
    public void createCSVWithEntropyVariations(String fileName, DNAGenerator dnaGenerator, MotifFinder motifFinder, ShannonEntropyFilter entropyFilter) throws IOException, InterruptedException {
        try (FileWriter writer = new FileWriter(fileName)) {
            // header
            writer.append("Database Size,Probability of Bases,Motif Size,Motif,Motif Occurrences,Time to Find Motif,Entropy\n");

            // Parameters for testing
            int[] sequenceCounts = {1000, 500000};  // n: 1000, 500000
            int[] motifSizes = {5, 10};             // s: 5, 10
            int[] sequenceLengths = {10, 50};       // Sequence lengths (previously m)
            double[] entropyThresholds = {0.5, 1.0, 1.5}; // Entropy values

            double[] probabilities = {0.25, 0.25, 0.25, 0.25}; // Fixed probabilities

            for (int n : sequenceCounts) {
                for (int sequenceLength : sequenceLengths) {  // Changed m to sequenceLength
                    for (int motifSize : motifSizes) {
                        for (double entropy : entropyThresholds) {
                            // Generate sequences
                            String[] sequences = new String[n];
                            for (int i = 0; i < n; i++) {
                                sequences[i] = dnaGenerator.generateSequence(sequenceLength, probabilities);
                            }

                            try {
                                // Find motifs
                                long startTime = System.nanoTime();
                                List<String> motifs = motifFinder.findMostFrequentMotifs(sequences, motifSize);
                                long endTime = System.nanoTime();
                                long timeToFindMotif = (endTime - startTime) / 1_000_000;

                                // Filter sequences based on entropy
                                List<String> filteredSequences = entropyFilter.filterSequencesByEntropy(sequences, entropy);

                                // Find motifs in filtered sequences
                                String motif = motifs.isEmpty() ? "N/A" : motifs.get(0);
                                int motifOccurrences = motifs.isEmpty() ? 0 : (int) motifs.stream().filter(m -> m.equals(motif)).count();

                                // Write the data to CSV
                                writer.append(n + ",");
                                writer.append("0.25,0.25,0.25,0.25,");
                                writer.append(motifSize + ",");
                                writer.append(motif + ",");
                                writer.append(motifOccurrences + ",");
                                writer.append(timeToFindMotif + " ms,");
                                writer.append(entropy + "\n");

                            } catch (ExecutionException e) {
                                // Handle ExecutionException and print error message
                                System.err.println("Error during motif finding: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        System.out.println("CSV file " + fileName + " generated successfully!");
    }

    // Method to export data with ideal entropy
    public void createCSVWithIdealEntropy(String fileName, DNAGenerator dnaGenerator, MotifFinder motifFinder, ShannonEntropyFilter entropyFilter, double idealEntropy) throws IOException, InterruptedException {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write the header
            writer.append("Database Size,Probability of Bases,Motif Size,Motif,Motif Occurrences,Time to Find Motif,Entropy\n");

            // Parameters for testing
            int[] sequenceCounts = {1000, 500000};  // n: 1000, 500000
            int[] motifSizes = {5, 10};             // s: 5, 10
            int[] sequenceLengths = {10, 50};       // Sequence lengths (previously m)
            double[] probabilities = {0.25, 0.25, 0.25, 0.25}; // Fixed probabilities

            // Loop through different configurations
            for (int n : sequenceCounts) {
                for (int sequenceLength : sequenceLengths) {  // Changed m to sequenceLength
                    for (int motifSize : motifSizes) {
                        // Generate sequences
                        String[] sequences = new String[n];
                        for (int i = 0; i < n; i++) {
                            sequences[i] = dnaGenerator.generateSequence(sequenceLength, probabilities);
                        }

                        try {
                            // Find motifs
                            long startTime = System.nanoTime();
                            List<String> motifs = motifFinder.findMostFrequentMotifs(sequences, motifSize);
                            long endTime = System.nanoTime();
                            long timeToFindMotif = (endTime - startTime) / 1_000_000;

                            // Filter sequences based on ideal entropy
                            List<String> filteredSequences = entropyFilter.filterSequencesByEntropy(sequences, idealEntropy);

                            // Find motifs in filtered sequences
                            String motif = motifs.isEmpty() ? "N/A" : motifs.get(0);
                            int motifOccurrences = motifs.isEmpty() ? 0 : (int) motifs.stream().filter(m -> m.equals(motif)).count();

                            // Write the data to CSV
                            writer.append(n + ",");
                            writer.append("0.25,0.25,0.25,0.25,");
                            writer.append(motifSize + ",");
                            writer.append(motif + ",");
                            writer.append(motifOccurrences + ",");
                            writer.append(timeToFindMotif + " ms,");
                            writer.append(idealEntropy + "\n");

                        } catch (ExecutionException e) {
                            // Handle ExecutionException and print error message
                            System.err.println("Error during motif finding: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        System.out.println("CSV file " + fileName + " generated successfully!");
    }
}

