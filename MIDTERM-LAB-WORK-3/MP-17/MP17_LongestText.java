/**
 * MP17 – Find Longest Text Entry
 * 
 * This program reads a CSV dataset and finds the longest text entry
 * in each column. It prompts the user for the file path, parses
 * the CSV, and displays the result in a formatted table.
 * 
 * Dataset: Exam results from University of Perpetual Help System - Molino
 * Columns: Candidate, Type, Exam, Language, Exam Date, Score, Result, Time Used
 */

import java.io.*;
import java.util.*;

public class MP17_LongestText {

    // ---------------------------------------------------------------
    // Inner class to hold a parsed exam record
    // ---------------------------------------------------------------
    static class ExamRecord {
        String candidate;   // Candidate's name (Last, First)
        String type;        // Student / Faculty / NTE
        String exam;        // Exam subject/title
        String language;    // Language used (e.g., English)
        String examDate;    // Date of exam (MM/DD/YYYY)
        String score;       // Numeric score
        String result;      // PASS or FAIL
        String timeUsed;    // Time taken (e.g., "36 min 38 sec")

        // Constructor: assigns all fields from parsed CSV tokens
        ExamRecord(String candidate, String type, String exam,
                   String language, String examDate,
                   String score, String result, String timeUsed) {
            this.candidate = candidate.trim();
            this.type      = type.trim();
            this.exam      = exam.trim();
            this.language  = language.trim();
            this.examDate  = examDate.trim();
            this.score     = score.trim();
            this.result    = result.trim();
            this.timeUsed  = timeUsed.trim();
        }
    }

    // ---------------------------------------------------------------
    // MAIN – Program entry point
    // ---------------------------------------------------------------
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║        MP17 – Find Longest Text Entry            ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // Step 1: Prompt user for the CSV file path
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path to the CSV dataset file: ");
        String filePath = scanner.nextLine().trim();

        // Step 2: Read and parse the CSV file
        List<ExamRecord> records = readCSV(filePath);

        // Guard: check if any records were loaded
        if (records.isEmpty()) {
            System.out.println("[ERROR] No valid records found. Please check the file.");
            scanner.close();
            return;
        }

        System.out.println("\nTotal records loaded: " + records.size());
        System.out.println();

        // Step 3: Find longest text entry per column
        findLongestEntries(records);

        scanner.close();
    }

    // ---------------------------------------------------------------
    // readCSV – Reads and parses the CSV file
    //   @param filePath – path to the CSV file
    //   @return         – list of ExamRecord objects
    // ---------------------------------------------------------------
    static List<ExamRecord> readCSV(String filePath) {
        List<ExamRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            boolean headerFound = false; // flag: have we found the data header row?

            while ((line = br.readLine()) != null) {

                // Skip blank lines
                if (line.trim().isEmpty()) continue;

                // The actual data header contains "Candidate" keyword
                if (!headerFound) {
                    if (line.contains("Candidate")) {
                        headerFound = true; // next lines are data rows
                    }
                    continue; // skip metadata/title rows
                }

                // Parse each data row
                String[] tokens = parseCSVLine(line);

                // Data rows need at least 8 columns (col 0,1,3,5,6,7,8 used)
                if (tokens.length < 9) continue;

                String candidate = tokens[0];
                String type      = tokens[1];
                String exam      = tokens[3];
                String language  = tokens[4];
                String examDate  = tokens[5];
                String score     = tokens[6];
                String result    = tokens[7];
                String timeUsed  = (tokens.length > 8) ? tokens[8] : "";

                // Skip rows with empty candidate name
                if (candidate.trim().isEmpty()) continue;

                records.add(new ExamRecord(
                    candidate, type, exam, language,
                    examDate, score, result, timeUsed
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
    // parseCSVLine – Splits a CSV line respecting quoted fields
    //   @param line – a raw CSV line string
    //   @return     – array of field strings
    // ---------------------------------------------------------------
    static String[] parseCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes; // toggle quoted mode
            } else if (c == ',' && !inQuotes) {
                tokens.add(current.toString().trim());
                current.setLength(0); // reset buffer
            } else {
                current.append(c);
            }
        }
        tokens.add(current.toString().trim()); // add last field

        return tokens.toArray(new String[0]);
    }

    // ---------------------------------------------------------------
    // findLongestEntries – Finds the longest text entry per column
    //   @param records – list of all parsed exam records
    // ---------------------------------------------------------------
    static void findLongestEntries(List<ExamRecord> records) {

        // Track the record with the longest text for each column
        ExamRecord longestCandidate = records.get(0);
        ExamRecord longestType      = records.get(0);
        ExamRecord longestExam      = records.get(0);
        ExamRecord longestLanguage  = records.get(0);
        ExamRecord longestDate      = records.get(0);
        ExamRecord longestScore     = records.get(0);
        ExamRecord longestResult    = records.get(0);
        ExamRecord longestTime      = records.get(0);

        // Iterate through every record and compare lengths
        for (ExamRecord r : records) {
            if (r.candidate.length() > longestCandidate.candidate.length()) longestCandidate = r;
            if (r.type.length()      > longestType.type.length())           longestType      = r;
            if (r.exam.length()      > longestExam.exam.length())           longestExam      = r;
            if (r.language.length()  > longestLanguage.language.length())   longestLanguage  = r;
            if (r.examDate.length()  > longestDate.examDate.length())       longestDate      = r;
            if (r.score.length()     > longestScore.score.length())         longestScore     = r;
            if (r.result.length()    > longestResult.result.length())       longestResult    = r;
            if (r.timeUsed.length()  > longestTime.timeUsed.length())       longestTime      = r;
        }

        // Also find overall longest text entry across all columns
        String overallLongestText  = "";
        String overallLongestCol   = "";
        String overallLongestOwner = "";

        // Helper: check each column's current max and compare globally
        String[][] checks = {
            {"Candidate", longestCandidate.candidate, longestCandidate.candidate},
            {"Type",      longestType.type,            longestType.candidate},
            {"Exam",      longestExam.exam,             longestExam.candidate},
            {"Language",  longestLanguage.language,     longestLanguage.candidate},
            {"Exam Date", longestDate.examDate,         longestDate.candidate},
            {"Score",     longestScore.score,           longestScore.candidate},
            {"Result",    longestResult.result,         longestResult.candidate},
            {"Time Used", longestTime.timeUsed,         longestTime.candidate},
        };

        for (String[] check : checks) {
            if (check[1].length() > overallLongestText.length()) {
                overallLongestCol   = check[0];
                overallLongestText  = check[1];
                overallLongestOwner = check[2];
            }
        }

        // ── Print results ────────────────────────────────────────
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│              LONGEST TEXT ENTRY PER COLUMN                                  │");
        System.out.println("├───────────────┬────────────────────────────────────────────┬────────────────┤");
        System.out.printf( "│ %-13s │ %-42s │ %14s │%n", "Column", "Longest Entry", "Length (chars)");
        System.out.println("├───────────────┼────────────────────────────────────────────┼────────────────┤");

        printRow("Candidate", longestCandidate.candidate);
        printRow("Type",      longestType.type);
        printRow("Exam",      longestExam.exam);
        printRow("Language",  longestLanguage.language);
        printRow("Exam Date", longestDate.examDate);
        printRow("Score",     longestScore.score);
        printRow("Result",    longestResult.result);
        printRow("Time Used", longestTime.timeUsed);

        System.out.println("└───────────────┴────────────────────────────────────────────┴────────────────┘");

        System.out.println();
        System.out.println("★  OVERALL LONGEST TEXT ENTRY ACROSS ALL COLUMNS:");
        System.out.printf( "   Column   : %s%n", overallLongestCol);
        System.out.printf( "   Entry    : %s%n", overallLongestText);
        System.out.printf( "   Length   : %d characters%n", overallLongestText.length());
        System.out.printf( "   Candidate: %s%n", overallLongestOwner);
    }

    // ---------------------------------------------------------------
    // printRow – Helper to format and print one result table row
    //   @param column – column name label
    //   @param value  – the longest text value for that column
    // ---------------------------------------------------------------
    static void printRow(String column, String value) {
        // Truncate display value if too long for cell
        String display = value.length() > 42 ? value.substring(0, 39) + "..." : value;
        System.out.printf("│ %-13s │ %-42s │ %14d │%n", column, display, value.length());
    }
}