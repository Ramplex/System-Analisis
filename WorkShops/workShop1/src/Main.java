import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        DNAGenerator dnaGenerator = new DNAGenerator();
        MotifFinder motifFinder = new MotifFinder();
        ShannonEntropyFilter entropyFilter = new ShannonEntropyFilter();
        CSVExporter csvExporter = new CSVExporter();

        // Create first CSV file (Entropy variations)
        csvExporter.createCSVWithEntropyVariations("Motif_Data_Entropy_Variations.csv", dnaGenerator, motifFinder, entropyFilter);

        // Create second CSV file  (ideal entropy)
        double idealEntropy = 1.0;
        csvExporter.createCSVWithIdealEntropy("Motif_Data_Ideal_Entropy.csv", dnaGenerator, motifFinder, entropyFilter, idealEntropy);

        motifFinder.shutdown();
        entropyFilter.shutdown();
    }
}


