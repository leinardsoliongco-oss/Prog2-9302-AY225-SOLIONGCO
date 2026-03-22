/**
 * MP19 – Generate Dataset Summary Report
 *
 * This program reads the CSV exam dataset and generates a comprehensive
 * summary report including: total records, pass/fail counts and rates,
 * average/min/max score, exam breakdown, type breakdown (Student/Faculty/NTE),
 * and the top 5 highest scorers.
 *
 * Dataset: Exam results from University of Perpetual Help System - Molino
 * Columns: Candidate, Type, Exam, Language, Exam Date, Score, Result, Time Used
 */

import java.io.*;
import java.util.*;

public class MP19_SummaryReport {

    // ---------------------------------------------------------------
    // Inner class representing one exam record
    // ---------------------------------------------------------------
    static class ExamRecord {
        String candidate;   // Candidate's name
        String type;        // Student / Faculty / NTE
        String exam;        // Exam subject/title
        String language;    // Language (English)
        String examDate;    // Exam date
        int    score;       // Numeric score (integer)
        String result;      // PASS or FAIL
        String timeUsed;    // Time taken

        ExamRecord(String candidate, String type, String exam,
                   String language, String examDate,
                   int score, String result, String timeUsed) {
            this.candidate = candidate.trim();
            this.type      = type.trim();
            this.exam      = exam.trim();
            this.language  = language.trim();
            this.examDate  = examDate.trim();
            this.score     = score;
            this.result    = result.trim();
            this.timeUsed  = timeUsed.trim();
        }
    }

    // ---------------------------------------------------------------
    // MAIN – Program entry point
    // ---------------------------------------------------------------
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         MP19 – Generate Dataset Summary Report           ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // Step 1: Prompt user for file path
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

        // Step 3: Generate and print the report
        generateReport(records);

        scanner.close();
    }

    // ---------------------------------------------------------------
    // readCSV – Reads and parses the CSV into ExamRecord list
    //   @param filePath – path to the CSV file
    //   @return         – list of ExamRecord objects
    // ---------------------------------------------------------------
    static List<ExamRecord> readCSV(String filePath) {
        List<ExamRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            boolean headerFound = false;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                if (!headerFound) {
                    if (line.contains("Candidate")) headerFound = true;
                    continue;
                }

                String[] tokens = parseCSVLine(line);
                if (tokens.length < 9) continue;

                String candidate = tokens[0];
                String type      = tokens[1];
                String exam      = tokens[3];
                String language  = tokens[4];
                String examDate  = tokens[5];
                String scoreStr  = tokens[6];
                String result    = tokens[7];
                String timeUsed  = (tokens.length > 8) ? tokens[8] : "";

                // Skip rows with missing candidate or score
                if (candidate.trim().isEmpty() || scoreStr.trim().isEmpty()) continue;

                int score;
                try {
                    score = Integer.parseInt(scoreStr.trim()); // parse score as integer
                } catch (NumberFormatException e) {
                    continue; // skip rows where score is not a number
                }

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
    // generateReport – Computes all statistics and prints the report
    //   @param records – list of all valid exam records
    // ---------------------------------------------------------------
    static void generateReport(List<ExamRecord> records) {

        int total = records.size(); // total number of records

        // ── Pass / Fail counters ──────────────────────────────────
        int passCount = 0;
        int failCount = 0;

        // ── Score accumulators ────────────────────────────────────
        int totalScore = 0;
        int minScore   = Integer.MAX_VALUE;
        int maxScore   = Integer.MIN_VALUE;
        ExamRecord topScorer    = records.get(0);
        ExamRecord bottomScorer = records.get(0);

        // ── Frequency maps ────────────────────────────────────────
        Map<String, Integer> examCount  = new LinkedHashMap<>(); // count per exam
        Map<String, Integer> typeCount  = new LinkedHashMap<>(); // count per type
        Map<String, Integer> examPass   = new LinkedHashMap<>(); // pass count per exam
        Map<String, Integer> examFail   = new LinkedHashMap<>(); // fail count per exam

        // ── Single-pass computation ───────────────────────────────
        for (ExamRecord r : records) {

            // Tally pass/fail
            if ("PASS".equalsIgnoreCase(r.result)) {
                passCount++;
                examPass.merge(r.exam, 1, Integer::sum);
            } else {
                failCount++;
                examFail.merge(r.exam, 1, Integer::sum);
            }

            // Accumulate score stats
            totalScore += r.score;
            if (r.score < minScore) { minScore = r.score; bottomScorer = r; }
            if (r.score > maxScore) { maxScore = r.score; topScorer    = r; }

            // Count by exam title and type
            examCount.merge(r.exam,  1, Integer::sum);
            typeCount.merge(r.type,  1, Integer::sum);
        }

        double avgScore  = (double) totalScore / total;
        double passRate  = (double) passCount  / total * 100.0;
        double failRate  = (double) failCount  / total * 100.0;

        // ── Find top 5 scorers ────────────────────────────────────
        List<ExamRecord> sorted = new ArrayList<>(records);
        sorted.sort((a, b) -> b.score - a.score); // descending order by score

        // ──────────────────────────────────────────────────────────
        // PRINT REPORT
        // ──────────────────────────────────────────────────────────

        System.out.println();
        printSectionHeader("OVERALL STATISTICS");
        System.out.printf(" Institution   : University of Perpetual Help System - Molino%n");
        System.out.printf(" Source        : Pearson VUE%n");
        System.out.printf(" Total Records : %d%n", total);
        System.out.printf(" Passed        : %d (%.1f%%)%n", passCount, passRate);
        System.out.printf(" Failed        : %d (%.1f%%)%n", failCount, failRate);
        System.out.printf(" Average Score : %.2f%n", avgScore);
        System.out.printf(" Highest Score : %d  (%s)%n", maxScore, topScorer.candidate);
        System.out.printf(" Lowest Score  : %d  (%s)%n", minScore, bottomScorer.candidate);

        // ── Type breakdown ────────────────────────────────────────
        printSectionHeader("BREAKDOWN BY CANDIDATE TYPE");
        System.out.printf(" %-15s  %8s%n", "Type", "Count");
        System.out.println(" " + "─".repeat(25));
        for (Map.Entry<String, Integer> e : typeCount.entrySet()) {
            System.out.printf(" %-15s  %8d%n", e.getKey(), e.getValue());
        }

        // ── Exam breakdown ────────────────────────────────────────
        printSectionHeader("BREAKDOWN BY EXAM");

        // Sort exams by count descending for readability
        List<Map.Entry<String, Integer>> examEntries = new ArrayList<>(examCount.entrySet());
        examEntries.sort((a, b) -> b.getValue() - a.getValue());

        System.out.printf(" %-48s  %6s  %6s  %6s%n", "Exam", "Total", "Pass", "Fail");
        System.out.println(" " + "─".repeat(70));
        for (Map.Entry<String, Integer> e : examEntries) {
            String exam = e.getKey();
            int cnt  = e.getValue();
            int p    = examPass.getOrDefault(exam, 0);
            int f    = examFail.getOrDefault(exam, 0);
            // Truncate exam name if too long
            String examDisplay = exam.length() > 48 ? exam.substring(0, 45) + "..." : exam;
            System.out.printf(" %-48s  %6d  %6d  %6d%n", examDisplay, cnt, p, f);
        }

        // ── Top 5 scorers ─────────────────────────────────────────
        printSectionHeader("TOP 5 HIGHEST SCORERS");
        System.out.printf(" %-4s  %-30s  %-40s  %6s  %6s%n",
            "Rank", "Candidate", "Exam", "Score", "Result");
        System.out.println(" " + "─".repeat(92));

        int rank = 1;
        for (ExamRecord r : sorted.subList(0, Math.min(5, sorted.size()))) {
            String examDisplay = r.exam.length() > 40 ? r.exam.substring(0, 37) + "..." : r.exam;
            System.out.printf(" %-4d  %-30s  %-40s  %6d  %6s%n",
                rank++, r.candidate, examDisplay, r.score, r.result);
        }

        System.out.println();
        System.out.println("═".repeat(94));
        System.out.println(" END OF REPORT");
        System.out.println("═".repeat(94));
    }

    // ---------------------------------------------------------------
    // printSectionHeader – Prints a formatted section separator
    //   @param title – section title text
    // ---------------------------------------------------------------
    static void printSectionHeader(String title) {
        System.out.println();
        System.out.println("┌─ " + title + " " + "─".repeat(Math.max(0, 60 - title.length())) + "┐");
    }

    // ---------------------------------------------------------------
    // parseCSVLine – Splits CSV line respecting quoted fields
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