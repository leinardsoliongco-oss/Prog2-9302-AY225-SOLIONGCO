/**
 * MP19 – Generate Dataset Summary Report
 *
 * This Node.js program reads the CSV exam dataset and generates a
 * comprehensive summary report: overall statistics, pass/fail rates,
 * score analysis, breakdown by exam and by candidate type, and
 * a top-5 scorers leaderboard.
 *
 * Dataset: Exam results from University of Perpetual Help System - Molino
 * Columns: Candidate, Type, Exam, Language, Exam Date, Score, Result, Time Used
 *
 * Run: node MP19_SummaryReport.js
 */

const fs       = require('fs');       // File System module
const readline = require('readline'); // Interactive console input

// ──────────────────────────────────────────────────────────────────
// parseCSVLine – Splits a CSV row respecting quoted fields
//   @param  {string}   line  – a single raw CSV line
//   @return {string[]}       – array of field strings
// ──────────────────────────────────────────────────────────────────
function parseCSVLine(line) {
    const tokens = [];
    let current  = '';
    let inQuotes = false; // tracks double-quoted field state

    for (const ch of line) {
        if (ch === '"') {
            inQuotes = !inQuotes;
        } else if (ch === ',' && !inQuotes) {
            tokens.push(current.trim());
            current = '';
        } else {
            current += ch;
        }
    }
    tokens.push(current.trim()); // push last field
    return tokens;
}

// ──────────────────────────────────────────────────────────────────
// readCSV – Reads and parses the dataset into record objects
//   @param  {string}   filePath – path to CSV file
//   @return {Object[]}          – array of exam record objects
// ──────────────────────────────────────────────────────────────────
function readCSV(filePath) {
    const raw   = fs.readFileSync(filePath, 'utf8'); // read full file
    const lines = raw.split(/\r?\n/);                 // split by newline

    const records   = [];
    let headerFound = false; // flag: passed the column-header row

    for (const line of lines) {
        if (!line.trim()) continue; // skip blank lines

        if (!headerFound) {
            if (line.includes('Candidate')) headerFound = true;
            continue; // skip metadata rows before header
        }

        const tokens = parseCSVLine(line);
        if (tokens.length < 9) continue;

        // Map token indices to named fields
        const candidate = tokens[0];
        const type      = tokens[1];
        const exam      = tokens[3];
        const language  = tokens[4];
        const examDate  = tokens[5];
        const scoreStr  = tokens[6];
        const result    = tokens[7];
        const timeUsed  = tokens[8] || '';

        // Skip rows missing candidate name or score
        if (!candidate || !scoreStr) continue;

        const score = parseInt(scoreStr, 10); // parse score as integer
        if (isNaN(score)) continue;           // skip non-numeric scores

        records.push({ candidate, type, exam, language, examDate, score, result, timeUsed });
    }

    return records;
}

// ──────────────────────────────────────────────────────────────────
// pad – Right-pads a string to a given width
//   @param  {string} str   – input string
//   @param  {number} width – target width
//   @return {string}       – padded string
// ──────────────────────────────────────────────────────────────────
function pad(str, width) {
    return String(str).padEnd(width);
}

// ──────────────────────────────────────────────────────────────────
// rpad – Right-aligns a string to a given width
//   @param  {string|number} val   – input value
//   @param  {number}        width – target width
//   @return {string}              – right-aligned string
// ──────────────────────────────────────────────────────────────────
function rpad(val, width) {
    return String(val).padStart(width);
}

// ──────────────────────────────────────────────────────────────────
// generateReport – Computes statistics and prints the full report
//   @param {Object[]} records – all valid exam record objects
// ──────────────────────────────────────────────────────────────────
function generateReport(records) {
    const total = records.length; // total number of records

    // ── Pass / fail tallies ────────────────────────────────────
    let passCount = 0;
    let failCount = 0;

    // ── Score accumulators ─────────────────────────────────────
    let totalScore = 0;
    let minScore   = Infinity;
    let maxScore   = -Infinity;
    let topScorer  = records[0];
    let botScorer  = records[0];

    // ── Frequency maps (exam and type breakdowns) ──────────────
    const examCount = {}; // total attempts per exam
    const examPass  = {}; // pass count per exam
    const examFail  = {}; // fail count per exam
    const typeCount = {}; // count per candidate type

    // ── Single-pass computation ────────────────────────────────
    for (const r of records) {

        // Count pass / fail
        if (r.result.toUpperCase() === 'PASS') {
            passCount++;
            examPass[r.exam] = (examPass[r.exam] || 0) + 1;
        } else {
            failCount++;
            examFail[r.exam] = (examFail[r.exam] || 0) + 1;
        }

        // Score stats
        totalScore += r.score;
        if (r.score < minScore) { minScore = r.score; botScorer = r; }
        if (r.score > maxScore) { maxScore = r.score; topScorer = r; }

        // Exam and type frequency
        examCount[r.exam]  = (examCount[r.exam]  || 0) + 1;
        typeCount[r.type]  = (typeCount[r.type]  || 0) + 1;
    }

    const avgScore = totalScore / total;
    const passRate = (passCount / total * 100).toFixed(1);
    const failRate = (failCount / total * 100).toFixed(1);

    // ── Top 5 scorers ──────────────────────────────────────────
    const sorted = [...records].sort((a, b) => b.score - a.score); // desc score
    const top5   = sorted.slice(0, 5);

    // ──────────────────────────────────────────────────────────
    // PRINT REPORT
    // ──────────────────────────────────────────────────────────

    const sep = '═'.repeat(90);

    console.log();
    console.log(sep);
    console.log('  DATASET SUMMARY REPORT – EXAM RESULTS');
    console.log(sep);

    // OVERALL STATISTICS
    console.log('\n┌─ OVERALL STATISTICS ' + '─'.repeat(48) + '┐');
    console.log(` Institution   : University of Perpetual Help System - Molino`);
    console.log(` Source        : Pearson VUE`);
    console.log(` Total Records : ${total}`);
    console.log(` Passed        : ${passCount} (${passRate}%)`);
    console.log(` Failed        : ${failCount} (${failRate}%)`);
    console.log(` Average Score : ${avgScore.toFixed(2)}`);
    console.log(` Highest Score : ${maxScore}  (${topScorer.candidate})`);
    console.log(` Lowest Score  : ${minScore}  (${botScorer.candidate})`);

    // TYPE BREAKDOWN
    console.log('\n┌─ BREAKDOWN BY CANDIDATE TYPE ' + '─'.repeat(39) + '┐');
    console.log(` ${'Type'.padEnd(15)}  ${'Count'.padStart(8)}`);
    console.log(' ' + '─'.repeat(25));
    for (const [type, count] of Object.entries(typeCount)) {
        console.log(` ${pad(type, 15)}  ${rpad(count, 8)}`);
    }

    // EXAM BREAKDOWN
    console.log('\n┌─ BREAKDOWN BY EXAM ' + '─'.repeat(49) + '┐');
    console.log(` ${'Exam'.padEnd(48)}  ${'Total'.padStart(6)}  ${'Pass'.padStart(6)}  ${'Fail'.padStart(6)}`);
    console.log(' ' + '─'.repeat(72));

    // Sort exams by total count descending
    const examEntries = Object.entries(examCount).sort((a, b) => b[1] - a[1]);
    for (const [exam, cnt] of examEntries) {
        const p   = examPass[exam] || 0;
        const f   = examFail[exam] || 0;
        const disp = exam.length > 48 ? exam.slice(0, 45) + '...' : exam;
        console.log(` ${pad(disp, 48)}  ${rpad(cnt, 6)}  ${rpad(p, 6)}  ${rpad(f, 6)}`);
    }

    // TOP 5 SCORERS
    console.log('\n┌─ TOP 5 HIGHEST SCORERS ' + '─'.repeat(45) + '┐');
    console.log(` ${'Rank'.padEnd(4)}  ${'Candidate'.padEnd(30)}  ${'Exam'.padEnd(40)}  ${'Score'.padStart(6)}  ${'Result'.padStart(6)}`);
    console.log(' ' + '─'.repeat(92));
    top5.forEach((r, i) => {
        const examDisp = r.exam.length > 40 ? r.exam.slice(0, 37) + '...' : r.exam;
        console.log(
            ` ${String(i + 1).padEnd(4)}  ${pad(r.candidate, 30)}  ${pad(examDisp, 40)}  ${rpad(r.score, 6)}  ${rpad(r.result, 6)}`
        );
    });

    console.log();
    console.log(sep);
    console.log(' END OF REPORT');
    console.log(sep);
}

// ──────────────────────────────────────────────────────────────────
// MAIN – Entry point: prompts user for file path then runs report
// ──────────────────────────────────────────────────────────────────
function main() {
    console.log('╔══════════════════════════════════════════════════════════╗');
    console.log('║         MP19 – Generate Dataset Summary Report           ║');
    console.log('╚══════════════════════════════════════════════════════════╝');
    console.log();

    // Create readline interface for reading user input
    const rl = readline.createInterface({ input: process.stdin, output: process.stdout });

    // Step 1: Ask user for the CSV file path
    rl.question('Enter the path to the CSV dataset file: ', (filePath) => {
        rl.close();
        filePath = filePath.trim();

        // Validate file existence
        if (!fs.existsSync(filePath)) {
            console.error(`[ERROR] File not found: ${filePath}`);
            process.exit(1);
        }

        try {
            // Step 2: Read and parse CSV
            const records = readCSV(filePath);

            if (records.length === 0) {
                console.error('[ERROR] No valid records found in the file.');
                process.exit(1);
            }

            console.log(`\nTotal records loaded: ${records.length}`);

            // Step 3: Generate and display report
            generateReport(records);

        } catch (err) {
            console.error('[ERROR] Failed to process file:', err.message);
            process.exit(1);
        }
    });
}

// Execute program
main();