/**
 * MP17 – Find Longest Text Entry
 *
 * This Node.js program reads a CSV dataset from a user-specified path,
 * parses the records, and finds the longest text entry in each column.
 * Results are printed in a formatted table to the console.
 *
 * Dataset: Exam results from University of Perpetual Help System - Molino
 * Columns: Candidate, Type, Exam, Language, Exam Date, Score, Result, Time Used
 *
 * Run: node MP17_LongestText.js
 */

const fs   = require('fs');       // Node.js built-in File System module
const path = require('path');     // Node.js path utilities
const readline = require('readline'); // For interactive user input

// ──────────────────────────────────────────────────────────────────
// parseCSVLine – Splits a CSV row respecting quoted fields
//   @param  {string} line  – a single raw CSV line
//   @return {string[]}     – array of field strings
// ──────────────────────────────────────────────────────────────────
function parseCSVLine(line) {
    const tokens = [];
    let current  = '';
    let inQuotes = false; // tracks whether inside a quoted field

    for (const ch of line) {
        if (ch === '"') {
            inQuotes = !inQuotes; // toggle quote mode
        } else if (ch === ',' && !inQuotes) {
            tokens.push(current.trim());
            current = ''; // reset buffer
        } else {
            current += ch;
        }
    }
    tokens.push(current.trim()); // push last field
    return tokens;
}

// ──────────────────────────────────────────────────────────────────
// readCSV – Reads and parses the dataset CSV file
//   @param  {string} filePath – absolute or relative path to CSV
//   @return {Object[]}        – array of exam record objects
// ──────────────────────────────────────────────────────────────────
function readCSV(filePath) {
    // Read the entire file as UTF-8 text
    const raw   = fs.readFileSync(filePath, 'utf8');
    const lines = raw.split(/\r?\n/); // split on newline (handles Windows \r\n too)

    const records    = []; // will hold parsed record objects
    let headerFound  = false; // flag: have we passed the header row?

    for (const line of lines) {
        if (!line.trim()) continue; // skip blank lines

        // The actual data header row contains the word "Candidate"
        if (!headerFound) {
            if (line.includes('Candidate')) {
                headerFound = true; // data rows follow
            }
            continue; // skip title/metadata rows
        }

        // Parse the data row into tokens
        const tokens = parseCSVLine(line);
        if (tokens.length < 9) continue; // skip short/malformed rows

        // Map token indices to named fields
        // CSV layout: [0]=Candidate [1]=Type [2]=extra [3]=Exam [4]=extra
        //             [5]=Language [6]=ExamDate [7]=Score [8]=Result [9]=TimeUsed
        const candidate = tokens[0];
        const type      = tokens[1];
        const exam      = tokens[3];
        const language  = tokens[4];
        const examDate  = tokens[5];
        const score     = tokens[6];
        const result    = tokens[7];
        const timeUsed  = tokens[8] || '';

        // Skip rows with no candidate name (blank/footer rows)
        if (!candidate) continue;

        records.push({ candidate, type, exam, language, examDate, score, result, timeUsed });
    }

    return records;
}

// ──────────────────────────────────────────────────────────────────
// findLongestEntries – Finds the longest string per column
//   @param {Object[]} records – parsed exam record objects
// ──────────────────────────────────────────────────────────────────
function findLongestEntries(records) {
    // Define all columns to analyze
    const columns = [
        { label: 'Candidate', key: 'candidate' },
        { label: 'Type',      key: 'type'      },
        { label: 'Exam',      key: 'exam'      },
        { label: 'Language',  key: 'language'  },
        { label: 'Exam Date', key: 'examDate'  },
        { label: 'Score',     key: 'score'     },
        { label: 'Result',    key: 'result'    },
        { label: 'Time Used', key: 'timeUsed'  },
    ];

    const results = []; // stores { label, longestValue, length, candidate }

    for (const col of columns) {
        // Reduce: pick the record with the maximum length for this column
        let longest = records[0];
        for (const r of records) {
            if ((r[col.key] || '').length > (longest[col.key] || '').length) {
                longest = r;
            }
        }

        results.push({
            column:    col.label,
            value:     longest[col.key] || '',
            length:    (longest[col.key] || '').length,
            candidate: longest.candidate,
        });
    }

    // ── Find the single overall longest entry ─────────────────────
    const overall = results.reduce((prev, curr) =>
        curr.length > prev.length ? curr : prev
    );

    // ── Print formatted table ─────────────────────────────────────
    console.log('┌─────────────────────────────────────────────────────────────────────────────┐');
    console.log('│              LONGEST TEXT ENTRY PER COLUMN                                  │');
    console.log('├───────────────┬────────────────────────────────────────────┬────────────────┤');
    console.log('│ Column        │ Longest Entry                              │ Length (chars) │');
    console.log('├───────────────┼────────────────────────────────────────────┼────────────────┤');

    for (const r of results) {
        // Truncate value display if too long for column cell
        const display = r.value.length > 42
            ? r.value.substring(0, 39) + '...'
            : r.value;

        console.log(
            `│ ${r.column.padEnd(13)} │ ${display.padEnd(42)} │ ${String(r.length).padStart(14)} │`
        );
    }

    console.log('└───────────────┴────────────────────────────────────────────┴────────────────┘');

    console.log();
    console.log('★  OVERALL LONGEST TEXT ENTRY ACROSS ALL COLUMNS:');
    console.log(`   Column   : ${overall.column}`);
    console.log(`   Entry    : ${overall.value}`);
    console.log(`   Length   : ${overall.length} characters`);
    console.log(`   Candidate: ${overall.candidate}`);
}

// ──────────────────────────────────────────────────────────────────
// MAIN – Program entry point; prompts user then runs analysis
// ──────────────────────────────────────────────────────────────────
function main() {
    console.log('╔══════════════════════════════════════════════════╗');
    console.log('║        MP17 – Find Longest Text Entry            ║');
    console.log('╚══════════════════════════════════════════════════╝');
    console.log();

    // Create readline interface for user input from console
    const rl = readline.createInterface({
        input:  process.stdin,
        output: process.stdout,
    });

    // Step 1: Prompt user for the CSV file path
    rl.question('Enter the path to the CSV dataset file: ', (filePath) => {
        rl.close();
        filePath = filePath.trim();

        // Step 2: Validate that the file exists
        if (!fs.existsSync(filePath)) {
            console.error(`[ERROR] File not found: ${filePath}`);
            process.exit(1);
        }

        try {
            // Step 3: Read and parse the CSV
            const records = readCSV(filePath);

            if (records.length === 0) {
                console.error('[ERROR] No valid records found in the file.');
                process.exit(1);
            }

            console.log(`\nTotal records loaded: ${records.length}\n`);

            // Step 4: Perform analysis and display results
            findLongestEntries(records);

        } catch (err) {
            console.error('[ERROR] Failed to process file:', err.message);
            process.exit(1);
        }
    });
}

// Execute main
main();