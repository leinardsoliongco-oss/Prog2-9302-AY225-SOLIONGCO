/**
 * MP18 – Remove Rows with Empty Fields
 *
 * This Node.js program reads the CSV exam dataset, identifies rows that
 * contain one or more empty/blank fields, removes them, and writes the
 * cleaned dataset to a new output CSV file. A summary is displayed.
 *
 * Dataset: Exam results from University of Perpetual Help System - Molino
 * Columns: Candidate, Type, Exam, Language, Exam Date, Score, Result, Time Used
 *
 * Run: node MP18_RemoveEmptyRows.js
 */

const fs       = require('fs');       // File System module for reading/writing files
const path     = require('path');     // Path utilities
const readline = require('readline'); // For interactive console input

// ──────────────────────────────────────────────────────────────────
// parseCSVLine – Splits a CSV row respecting quoted fields
//   @param  {string}   line  – a single raw CSV line
//   @return {string[]}       – array of field strings
// ──────────────────────────────────────────────────────────────────
function parseCSVLine(line) {
    const tokens = [];
    let current  = '';
    let inQuotes = false; // true when inside a double-quoted field

    for (const ch of line) {
        if (ch === '"') {
            inQuotes = !inQuotes; // toggle quoted mode
        } else if (ch === ',' && !inQuotes) {
            tokens.push(current.trim());
            current = ''; // reset buffer for next field
        } else {
            current += ch;
        }
    }
    tokens.push(current.trim()); // push the last field
    return tokens;
}

// ──────────────────────────────────────────────────────────────────
// readCSV – Reads and parses the dataset CSV
//   @param  {string}   filePath – path to input CSV file
//   @return {Object[]}          – array of exam record objects
// ──────────────────────────────────────────────────────────────────
function readCSV(filePath) {
    const raw   = fs.readFileSync(filePath, 'utf8');  // read file content
    const lines = raw.split(/\r?\n/);                  // split into lines

    const records   = [];    // accumulate parsed records here
    let headerFound = false; // flag: have we passed the column header row?

    for (const line of lines) {
        if (!line.trim()) continue; // skip blank lines

        // The column-header row contains "Candidate" keyword
        if (!headerFound) {
            if (line.includes('Candidate')) headerFound = true;
            continue; // skip title/metadata rows
        }

        const tokens = parseCSVLine(line);
        if (tokens.length < 9) continue; // skip malformed rows

        // Map token positions to field names
        const candidate = tokens[0];
        const type      = tokens[1];
        const exam      = tokens[3];
        const language  = tokens[4];
        const examDate  = tokens[5];
        const score     = tokens[6];
        const result    = tokens[7];
        const timeUsed  = tokens[8] || '';

        // Include ALL rows (even those with empty fields) for detection
        records.push({ candidate, type, exam, language, examDate, score, result, timeUsed });
    }

    return records;
}

// ──────────────────────────────────────────────────────────────────
// getEmptyFields – Returns array of field names that are empty
//   @param  {Object}   record – an exam record object
//   @return {string[]}        – list of empty field names
// ──────────────────────────────────────────────────────────────────
function getEmptyFields(record) {
    const fields = [
        { name: 'Candidate', value: record.candidate },
        { name: 'Type',      value: record.type      },
        { name: 'Exam',      value: record.exam      },
        { name: 'Language',  value: record.language  },
        { name: 'Exam Date', value: record.examDate  },
        { name: 'Score',     value: record.score     },
        { name: 'Result',    value: record.result    },
        { name: 'Time Used', value: record.timeUsed  },
    ];
    // Filter to only the fields where value is empty/blank
    return fields.filter(f => !f.value).map(f => f.name);
}

// ──────────────────────────────────────────────────────────────────
// writeCleanCSV – Writes cleaned records to a new CSV file
//   @param {Object[]} records    – array of clean exam record objects
//   @param {string}   outputPath – destination file path
// ──────────────────────────────────────────────────────────────────
function writeCleanCSV(records, outputPath) {
    // Build CSV header
    const lines = ['Candidate,Type,Exam,Language,Exam Date,Score,Result,Time Used'];

    // Build each data row (wrap fields in quotes to handle commas)
    for (const r of records) {
        lines.push(
            `"${r.candidate}","${r.type}","${r.exam}","${r.language}",` +
            `"${r.examDate}","${r.score}","${r.result}","${r.timeUsed}"`
        );
    }

    // Write all lines to the output file
    fs.writeFileSync(outputPath, lines.join('\n'), 'utf8');
}

// ──────────────────────────────────────────────────────────────────
// processData – Core logic: filter, report, and write cleaned data
//   @param {string} filePath   – source CSV file path
//   @param {string} outputPath – destination CSV file path
// ──────────────────────────────────────────────────────────────────
function processData(filePath, outputPath) {
    // Step 1: Read and parse the CSV
    const records = readCSV(filePath);

    if (records.length === 0) {
        console.error('[ERROR] No valid records found in the file.');
        process.exit(1);
    }

    console.log(`\nTotal records loaded: ${records.length}`);

    // Step 2: Separate clean records from records with empty fields
    const cleanRecords   = [];
    const removedRecords = [];

    for (const r of records) {
        const emptyFields = getEmptyFields(r);
        if (emptyFields.length > 0) {
            removedRecords.push({ record: r, emptyFields }); // mark for removal
        } else {
            cleanRecords.push(r); // keep this record
        }
    }

    // Step 3: Display removed rows
    console.log();
    console.log('─────────────────────────────────────────────────────────');
    console.log(' ROWS REMOVED (contain at least one empty field):');
    console.log('─────────────────────────────────────────────────────────');

    if (removedRecords.length === 0) {
        console.log(' ✔ No rows with empty fields found.');
    } else {
        for (const item of removedRecords) {
            const name = item.record.candidate || '(no name)';
            console.log(` • ${name.padEnd(30)} | Empty: ${item.emptyFields.join(', ')}`);
        }
    }

    // Step 4: Print summary
    console.log();
    console.log('─────────────────────────────────────────────────────────');
    console.log(' SUMMARY');
    console.log('─────────────────────────────────────────────────────────');
    console.log(` Total records   : ${records.length}`);
    console.log(` Removed (empty) : ${removedRecords.length}`);
    console.log(` Clean records   : ${cleanRecords.length}`);

    // Step 5: Write cleaned data to output file
    writeCleanCSV(cleanRecords, outputPath);
    console.log();
    console.log(`✔ Cleaned dataset saved to: ${outputPath}`);
}

// ──────────────────────────────────────────────────────────────────
// MAIN – Entry point: prompts user for input and output paths
// ──────────────────────────────────────────────────────────────────
function main() {
    console.log('╔══════════════════════════════════════════════════════╗');
    console.log('║       MP18 – Remove Rows with Empty Fields           ║');
    console.log('╚══════════════════════════════════════════════════════╝');
    console.log();

    // Create readline interface for interactive console input
    const rl = readline.createInterface({ input: process.stdin, output: process.stdout });

    // Step 1: Ask for source CSV path
    rl.question('Enter the path to the CSV dataset file: ', (filePath) => {
        filePath = filePath.trim();

        // Validate source file exists
        if (!fs.existsSync(filePath)) {
            console.error(`[ERROR] File not found: ${filePath}`);
            rl.close();
            process.exit(1);
        }

        // Step 2: Ask for output file path
        rl.question('Enter output file path for cleaned CSV (e.g., cleaned_data.csv): ', (outputPath) => {
            rl.close();
            outputPath = outputPath.trim();

            try {
                processData(filePath, outputPath);
            } catch (err) {
                console.error('[ERROR] Failed to process file:', err.message);
                process.exit(1);
            }
        });
    });
}

// Execute program
main();