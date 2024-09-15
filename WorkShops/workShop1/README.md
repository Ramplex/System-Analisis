# Motif Detection and Entropy-Based Filtering in DNA Sequences

## Project Overview

This project is designed to **generate DNA datasets**, **detect frequent motifs** in the sequences, and **filter sequences** based on their Shannon entropy. The tool is also capable of exporting the results in **CSV format** for further analysis.

The project is divided into two parts:
1. **`src1.0/`**: Handles **DNA dataset generation**.
2. **`src/`**: Performs **motif analysis**, **entropy filtering**, and exports the results to CSV.

This structure allows for a modular and scalable workflow that can handle large datasets efficiently. The project is highly configurable, allowing users to explore different combinations of sequence lengths, motif sizes, database sizes, and entropy thresholds.

---

## Project Structure

### 1. Folder `src1.0/`

This folder is responsible for generating datasets of DNA sequences. The datasets can be used for motif analysis in the second part of the project.

- **Files**:
  - `DNAGenerator.java`: Generates random DNA sequences based on configurable nucleotide probabilities.
  - `MotifFinder.java`: Finds the most frequent motifs within the sequences.
  - `ShannonEntropyFilter.java`: Filters sequences based on Shannon entropy.
  - `Main.java`: Generates the DNA sequences and saves them into dataset files.

---

### 2. Folder `src/`

This folder contains the logic for **motif analysis**, **entropy filtering**, and **exporting results** to a CSV file. The DNA datasets generated from `src1.0/` are analyzed here.

- **Files**:
  - `DNAGenerator.java`: Same as in `src1.0/`, generates sequences as needed for motif analysis.
  - `MotifFinder.java`: Same as in `src1.0/`, used to find motifs in both original and filtered sequences.
  - `ShannonEntropyFilter.java`: Same as in `src1.0/`, filters sequences based on entropy before motif detection.
  - `CSVExporter.java`: Handles the export of results into a CSV file, including variations in entropy thresholds and motif sizes.
  - `Main.java`: Runs the complete process—generation, filtering, motif analysis, and CSV export.

---

## How to Run the Project

### 1. Dataset Generation (`src1.0/`)

To generate datasets, navigate to the `src1.0/` folder and run the `Main.java` file. This will create a dataset of DNA sequences with configurable probabilities for the nucleotide bases.

1. **Navigate to `src1.0/`:**
    ```bash
    cd src1.0/
    ```

2. **Run the Dataset Generation:**
    ```bash
    javac Main.java
    java Main
    ```

This will generate datasets of DNA sequences which are saved in files for later analysis.

---

### 2. Motif Analysis and CSV Export (`src/`)

Once the datasets have been generated, you can proceed with the **motif analysis** and **entropy filtering** by running the code in the `src/` folder.

1. **Navigate to `src/`:**
    ```bash
    cd ../src/
    ```

2. **Run Motif Analysis and CSV Export:**
    ```bash
    javac Main.java
    java Main
    ```

The results of the motif analysis will be saved in a CSV file with the following columns:
- **Database Size**: The number of sequences in the dataset.
- **Probability of Bases**: The fixed probabilities for the four nucleotides (A, C, G, T).
- **Motif Size**: The size of the motif that was searched for.
- **Motif**: The most frequent motif found.
- **Motif Occurrences**: How many times the motif was found in the dataset.
- **Time to Find Motif**: The time taken to search for motifs in milliseconds.
- **Entropy**: The entropy threshold applied during filtering.

---

## Configuration Parameters

You can configure the following parameters within the `Main.java` files in both folders:

### 1. DNA Sequence Parameters:
- **Database Size (`n`)**: The number of sequences to generate, e.g., 1000, 500000.
- **Sequence Length (`m`)**: The length of each DNA sequence, e.g., 10, 50.
- **Motif Size (`motifSize`)**: The length of the motif to search for, e.g., 5, 10.
- **Nucleotide Probabilities**: The probability for each base (A, C, G, T), e.g., {0.25, 0.25, 0.25, 0.25}.

### 2. Entropy Parameters:
- **Entropy Thresholds**: Set the thresholds to filter sequences based on entropy, e.g., 0.5, 1.0, 1.5.

---

## Example Usage

Here’s an example of how you can modify `Main.java` in `src/` to generate a CSV with motif analysis:

```java
int[] sequenceCounts = {1000, 500000};  // Database sizes
int[] motifSizes = {5, 10};             // Motif sizes
int[] sequenceLengths = {10, 50};       // DNA sequence lengths
double[] entropyThresholds = {0.5, 1.0, 1.5}; // Entropy thresholds
