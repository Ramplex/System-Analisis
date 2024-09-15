import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class DNAGenerator {
    private static final char[] BASES = {'A', 'C', 'G', 'T'};  // Nucleotides

    // Generates a random DNA sequence
    public String generateSequence(int m, double[] probabilities) throws IllegalArgumentException {
        validateProbabilities(probabilities);
        
        StringBuilder sequence = new StringBuilder(m);
        for (int i = 0; i < m; i++) {
            sequence.append(getRandomBase(probabilities));
        }
        return sequence.toString();
    }

    // Randomly selects a nucleotide
    private char getRandomBase(double[] probabilities) {
        double p = ThreadLocalRandom.current().nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < BASES.length; i++) {
            cumulativeProbability += probabilities[i];
            if (p <= cumulativeProbability) {
                return BASES[i];
            }
        }
        return BASES[BASES.length - 1];  // fallback
    }

    // Save DNA sequences
    public void saveSequences(String[] sequences, String folderName, String filename) throws IOException {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();  // Create the folder if it doesn't exist
        }
        try (FileWriter writer = new FileWriter(new File(folder, filename))) {
            for (String sequence : sequences) {
                writer.write(sequence + "\n");
            }
        }
    }

    // Validate probabilities
    private void validateProbabilities(double[] probabilities) throws IllegalArgumentException {
        double sum = 0.0;
        for (double prob : probabilities) {
            if (prob < 0.0 || prob > 1.0) {
                throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
            }
            sum += prob;
        }

        if (Math.abs(sum - 1.0) > 1e-6) {
            throw new IllegalArgumentException("Probabilities must sum to 1.0");
        }
    }
}
