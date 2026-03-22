/**
 * MP18 – Remove Rows with Empty Fields
 *
 * This program reads the CSV exam dataset, identifies and removes any rows
 * that contain one or more empty/blank fields, then saves the cleaned data
 * to a new CSV file. A summary of removed vs retained rows is displayed.
 *
 * Dataset: Exam results from University of Perpetual Help System - Molino
 * Columns: Candidate, Type, Exam, Language, Exam Date, Score, Result, Time Used
 */

import java.io.*;
import java.util.*;

public class MP18_RemoveEmptyRows {

    // ---------------------------------------------------------------
    // Inner class to hold a parsed exam record plus its raw CSV line
    // ---------------------------------------------------------------
    static class ExamRecord {
        String candidate;   // Candidate's name
        String type;        // Student / Faculty / NTE
        String exam;        // Exam subject/title
        String language;    // Language used
        String examDate;    // Date of exam
        String score;       // Numeric score
        String result;      // PASS or FAIL
        String timeUsed;    // Time taken
        String rawLine;     // Original CSV line (used when writing output)

        ExamRecord(String candidate, String type, String exam,
                   String language, String examDate,
                   String score, String result,
                   String timeUsed, String rawLine) {
            this.candidate = candidate.trim();
            this.type      = type.trim();
            this.exam      = exam.trim();
            this.language  = language.trim();
            this.examDate  = examDate.trim();
            this.score     = score.trim();
            this.result    = result.trim();
            this.timeUsed  = timeUsed.trim();
            this.rawLine   = rawLine;
        }

        // hasEmptyField – returns true if any essential field is blank
        boolean hasEmptyField() {
            return candidate.isEmpty() || type.isEmpty()     ||
                   exam.isEmpty()      || language.isEmpty() ||
                   examDate.isEmpty()  || score.isEmpty()    ||
                   result.isEmpty()    || timeUsed.isEmpty();
        }

        // emptyFieldsList – returns names of empty fields for reporting
        List<String> emptyFieldsList() {
            List<String> empty = new ArrayList<>();
            if (candidate.isEmpty()) empty.add("Candidate");
            if (type.isEmpty())      empty.add("Type");
            if (exam.isEmpty())      empty.add("Exam");
            if (language.isEmpty())  empty.add("Language");
            if (examDate.isEmpty())  empty.add("Exam Date");
            if (score.isEmpty())     empty.add("Score");
            if (result.isEmpty())    empty.add("Result");
            if (timeUsed.isEmpty())  empty.add("Time Used");
            return empty;
        }
    }

    // ---------------------------------------------------------------
    // MAIN – Program entry point
    // ---------------------------------------------------------------
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║       MP18 – Remove Rows with Empty Fields           ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // Step 1: Prompt user for the CSV file path
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path to the CSV dataset file: ");
        String filePath = scanner.nextLine().trim();

        // Step 2: Read and parse the CSV
        List<ExamRecord> records = readCSV(filePath);

        if (records.isEmpty()) {
            System.out.println("[ERROR] No valid records found. Please check the file.");
            scanner.close();
            return;
        }

        System.out.println("\nTotal records loaded: " + records.size());

        // Step 3: Separate complete rows from rows with empty fields
        List<ExamRecord> cleanRecords  = new ArrayList<>();
        List<ExamRecord> removedRecords = new ArrayList<>();

        for (ExamRecord r : records) {
            if (r.hasEmptyField()) {
                removedRecords.add(r);
            } else {
                cleanRecords.add(r);
            }
        }

        // Step 4: Display removed rows
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println(" ROWS REMOVED (contain at least one empty field):");
        System.out.println("─────────────────────────────────────────────────────────");

        if (removedRecords.isEmpty()) {
            System.out.println(" ✔ No rows with empty fields found.");
        } else {
            for (ExamRecord r : removedRecords) {
                System.out.printf(" • %-30s | Empty: %s%n",
                    r.candidate.isEmpty() ? "(no name)" : r.candidate,
                    String.join(", ", r.emptyFieldsList()));
            }
        }

        // Step 5: Display summary
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println(" SUMMARY");
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.printf(" Total records   : %d%n", records.size());
        System.out.printf(" Removed (empty) : %d%n", removedRecords.size());
        System.out.printf(" Clean records   : %d%n", cleanRecords.size());

        // Step 6: Write cleaned records to output file
        System.out.print("\nEnter output file path for cleaned CSV (e.g., cleaned_data.csv): ");
        String outputPath = scanner.nextLine().trim();

        writeCleanCSV(cleanRecords, outputPath);

        System.out.println();
        System.out.println("✔ Cleaned dataset saved to: " + outputPath);

        scanner.close();
    }

    // ---------------------------------------------------------------
    // readCSV – Reads and parses the CSV file into ExamRecord list
    //   @param filePath – path to input CSV file
    //   @return         – list of ExamRecord objects
    // ---------------------------------------------------------------
    static List<ExamRecord> readCSV(String filePath) {
        List<ExamRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            boolean headerFound = false; // flag: passed the data-header row

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                // Detect the actual column-header row
                if (!headerFound) {
                    if (line.contains("Candidate")) {
                        headerFound = true;
                    }
                    continue;
                }

                // Parse the data row
                String[] tokens = parseCSVLine(line);
                if (tokens.length < 9) continue;

                String candidate = tokens[0];
                String type      = tokens[1];
                String exam      = tokens[3];
                String language  = tokens[4];
                String examDate  = tokens[5];
                String score     = tokens[6];
                String result    = tokens[7];
                String timeUsed  = (tokens.length > 8) ? tokens[8] : "";

                // Create record even if some fields are empty (we need to detect them)
                records.add(new ExamRecord(
                    candidate, type, exam, language,
                    examDate, score, result, timeUsed, line
                ));
            }

        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("[ERROR] Could not read file: " + e.getMessage());
        }

        return records;
    }

    // ---------------------------------------------------------------
    // writeCleanCSV – Writes only clean records to a new CSV file
    //   @param records    – list of records with no empty fields
    //   @param outputPath – destination file path
    // ---------------------------------------------------------------
    static void writeCleanCSV(List<ExamRecord> records, String outputPath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {

            // Write CSV header row
            bw.write("Candidate,Type,Exam,Language,Exam Date,Score,Result,Time Used");
            bw.newLine();

            // Write each clean record as a CSV row
            for (ExamRecord r : records) {
                // Wrap fields that may contain commas in double quotes
                bw.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                    r.candidate, r.type, r.exam, r.language,
                    r.examDate, r.score, r.result, r.timeUsed));
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("[ERROR] Could not write output file: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // parseCSVLine – Splits CSV line respecting quoted fields
    //   @param line – raw CSV line
    //   @return     – array of field strings
    // ---------------------------------------------------------------
    static String[] parseCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        tokens.add(current.toString().trim());

        return tokens.toArray(new String[0]);
    }
}