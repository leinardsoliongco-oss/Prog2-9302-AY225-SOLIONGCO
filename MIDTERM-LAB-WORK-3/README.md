# MP17 / MP18 / MP19 – Program Submission

**Dataset:** `Sample_Data-Prog-2-csv.csv`
**Institution:** University of Perpetual Help System - Molino
**Source:** Pearson VUE | 168 valid records | Date: 03/14/2026

---

## How to Run

### Java
```bash
# Compile
javac MP17/MP17_LongestText.java
javac MP18/MP18_RemoveEmptyRows.java
javac MP19/MP19_SummaryReport.java

# Run (from the directory containing the .class file)
java -cp MP17 MP17_LongestText
java -cp MP18 MP18_RemoveEmptyRows
java -cp MP19 MP19_SummaryReport
```

### JavaScript (Node.js)
```bash
node MP17/MP17_LongestText.js
node MP18/MP18_RemoveEmptyRows.js
node MP19/MP19_SummaryReport.js
```

When prompted, enter the full path to `Sample_Data-Prog-2-csv.csv`.

---

## CSV Column Mapping

The dataset CSV uses the following column indices (0-based):

| Index | Field      |
|-------|------------|
| 0     | Candidate  |
| 1     | Type       |
| 2     | (blank)    |
| 3     | Exam       |
| 4     | Language   |
| 5     | Exam Date  |
| 6     | Score      |
| 7     | Result     |
| 8     | Time Used  |

---

## MP17 – Find Longest Text Entry

### Program Logic (Java & JavaScript)
The program reads the CSV line by line using a custom quote-aware parser, skipping all metadata rows above the column header. For each of the 8 data columns (Candidate, Type, Exam, Language, Exam Date, Score, Result, Time Used), it iterates through all records and tracks the one whose field value has the greatest string length. After scanning all records, it prints a formatted table showing the longest value found per column along with its character count. A final "overall winner" is computed by comparing the per-column maximums against each other.

### Sample Output
```
╔══════════════════════════════════════════════════╗
║        MP17 – Find Longest Text Entry            ║
╚══════════════════════════════════════════════════╝

Enter the path to the CSV dataset file: Sample_Data-Prog-2-csv.csv

Total records loaded: 168

┌─────────────────────────────────────────────────────────────────────────────┐
│              LONGEST TEXT ENTRY PER COLUMN                                  │
├───────────────┬────────────────────────────────────────────┬────────────────┤
│ Column        │ Longest Entry                              │ Length (chars) │
├───────────────┼────────────────────────────────────────────┼────────────────┤
│ Candidate     │ Hyacinthie,Willoughey                      │             21 │
│ Type          │ Student                                    │              7 │
│ Exam          │ Device Configuration and Management (Wi... │             48 │
│ Language      │ English                                    │              7 │
│ Exam Date     │ 03/14/2026                                 │             10 │
│ Score         │ 860                                        │              3 │
│ Result        │ PASS                                       │              4 │
│ Time Used     │ 36 min 38 sec                              │             13 │
└───────────────┴────────────────────────────────────────────┴────────────────┘

★  OVERALL LONGEST TEXT ENTRY ACROSS ALL COLUMNS:
   Column   : Exam
   Entry    : Device Configuration and Management (Windows 11)
   Length   : 48 characters
   Candidate: Ruthi,Pancoust
```

---

## MP18 – Remove Rows with Empty Fields

### Program Logic (Java & JavaScript)
After reading and parsing all records from the CSV, the program inspects each row by checking whether any of its 8 essential fields is an empty or blank string. Rows with at least one empty field are collected into a "removed" list while fully populated rows go to a "clean" list. The program prints a detailed report listing each removed row and which fields were empty. Finally, the clean records are written to a new output CSV file with proper quoting, and a summary of total/removed/retained counts is displayed.

### Sample Output
```
╔══════════════════════════════════════════════════════╗
║       MP18 – Remove Rows with Empty Fields           ║
╚══════════════════════════════════════════════════════╝

Enter the path to the CSV dataset file: Sample_Data-Prog-2-csv.csv
Enter output file path for cleaned CSV: cleaned_data.csv

Total records loaded: 169

─────────────────────────────────────────────────────────
 ROWS REMOVED (contain at least one empty field):
─────────────────────────────────────────────────────────
 • (no name)                      | Empty: Candidate, Type, Exam, Language, Exam Date, Score, Result, Time Used

─────────────────────────────────────────────────────────
 SUMMARY
─────────────────────────────────────────────────────────
 Total records   : 169
 Removed (empty) : 1
 Clean records   : 168

✔ Cleaned dataset saved to: cleaned_data.csv
```

---

## MP19 – Generate Dataset Summary Report

### Program Logic (Java & JavaScript)
The program reads all valid records and performs a single-pass aggregation: it counts PASS/FAIL results, accumulates scores to compute average/min/max, and builds frequency maps for exam titles and candidate types. After the pass, pass rate and fail rate percentages are calculated. The records are then sorted descending by score to extract the top 5 highest scorers. The full report is printed in clearly labeled sections: Overall Statistics, Breakdown by Candidate Type, Breakdown by Exam (with per-exam pass/fail counts), and the Top 5 leaderboard.

### Sample Output
```
╔══════════════════════════════════════════════════════════╗
║         MP19 – Generate Dataset Summary Report           ║
╚══════════════════════════════════════════════════════════╝

Enter the path to the CSV dataset file: Sample_Data-Prog-2-csv.csv

Total records loaded: 168

══════════════════════════════════════════════════════════════════════════════════════════
  DATASET SUMMARY REPORT – EXAM RESULTS
══════════════════════════════════════════════════════════════════════════════════════════

┌─ OVERALL STATISTICS ────────────────────────────────────────────────┐
 Institution   : University of Perpetual Help System - Molino
 Source        : Pearson VUE
 Total Records : 168
 Passed        : 130 (77.4%)
 Failed        : 38 (22.6%)
 Average Score : 769.98
 Highest Score : 980  (Aldous,Chapiro)
 Lowest Score  : 183  (Hernando,McDonand)

┌─ BREAKDOWN BY CANDIDATE TYPE ───────────────────────────────────────┐
 Type                Count
 ─────────────────────────
 Student               161
 Faculty                 4
 NTE                     3

┌─ BREAKDOWN BY EXAM ─────────────────────────────────────────────────┐
 Exam                                               Total    Pass    Fail
 ────────────────────────────────────────────────────────────────────────
 HTML and CSS                                          54      44      10
 Cybersecurity                                         18      16       2
 Information Technology Specialist in Networking       16       7       9
 Python                                                15      12       3
 JavaScript                                            14      10       4
 Device Configuration and Management (Windows 11)      13      11       2
 Artificial Intelligence                               12       7       5
 Data Analytics                                         7       7       0
 Software Development                                   5       4       1
 Databases                                              3       3       0
 Java                                                   3       3       0
 Cloud Computing                                        3       2       1
 Network Security                                       2       1       1
 HTML5 Application Development                          2       2       0
 Python - Next Generation                               1       1       0

┌─ TOP 5 HIGHEST SCORERS ─────────────────────────────────────────────┐
 Rank  Candidate                       Exam                                       Score  Result
 ────────────────────────────────────────────────────────────────────────────────────────────
 1     Aldous,Chapiro                  Information Technology Specialist in ...     980    PASS
 2     Andrei,Lancley                  Databases                                    979    PASS
 3     Anderea,Chatwin                 JavaScript                                   960    PASS
 4     Jody,Coye                       Artificial Intelligence                      953    PASS
 5     Rivy,Inglish                    HTML and CSS                                 947    PASS

══════════════════════════════════════════════════════════════════════════════════════════
 END OF REPORT
══════════════════════════════════════════════════════════════════════════════════════════
```