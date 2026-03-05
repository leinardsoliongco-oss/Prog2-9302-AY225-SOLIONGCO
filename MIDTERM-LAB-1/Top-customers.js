const fs = require('fs');
const readline = require('readline');

// Interface Setup
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

// File Validation
function validateFile(filePath) {
    if (!fs.existsSync(filePath)) {
        return { valid: false, error: 'File does not exist.' };
    }

    try {
        fs.accessSync(filePath, fs.constants.R_OK);
    } catch (e) {
        return { valid: false, error: 'File is not readable. Check permissions.' };
    }

    const content = fs.readFileSync(filePath, 'utf8').trim();
    if (!content) {
        return { valid: false, error: 'File is empty.' };
    }

    const firstLine = content.split('\n')[0];
    if (!firstLine.includes(',')) {
        return { valid: false, error: 'File does not appear to be a valid CSV (no commas found).' };
    }

    return { valid: true, content: content };
}

// CSV Parser - detects customer/name column and sales/amount column
function parseCSV(content) {
    const lines = content.split('\n').filter(function(line) {
        return line.trim() !== '';
    });

    const headers = lines[0].split(',').map(function(h) {
        return h.trim().toLowerCase();
    });

    // Look for name/entity column: customer, publisher, developer, name, client, company
    const nameIndex = headers.findIndex(function(h) {
        return h.includes('customer') ||
               h.includes('publisher') ||
               h.includes('developer') ||
               h.includes('client') ||
               h.includes('company') ||
               h === 'name';
    });

    // Look for sales/amount column: total_sales, sales, amount, revenue, total, price
    const amountIndex = headers.findIndex(function(h) {
        return h.includes('total_sales') ||
               h.includes('sales') ||
               h.includes('amount') ||
               h.includes('revenue') ||
               h.includes('price') ||
               h.includes('total');
    });

    if (nameIndex === -1) {
        throw new Error(
            'Could not find a name column.\n' +
            'Available columns: ' + headers.join(', ') + '\n' +
            'Expected a column like: customer, publisher, developer, name, client, or company.'
        );
    }
    if (amountIndex === -1) {
        throw new Error(
            'Could not find a sales/amount column.\n' +
            'Available columns: ' + headers.join(', ') + '\n' +
            'Expected a column like: total_sales, sales, amount, revenue, or price.'
        );
    }

    console.log('Using column "' + headers[nameIndex] + '" as name and "' + headers[amountIndex] + '" as amount.');

    const records = [];
    for (let i = 1; i < lines.length; i++) {
        const cols = lines[i].split(',').map(function(c) {
            return c.trim();
        });
        const name = cols[nameIndex];
        const amount = parseFloat(cols[amountIndex]);

        if (name && !isNaN(amount)) {
            records.push({ customer: name, amount: amount });
        }
    }

    if (records.length === 0) {
        throw new Error('No valid data rows found in the CSV.');
    }

    return records;
}

// Aggregate and Sort
function getTop10Customers(records) {
    const totals = {};

    for (let i = 0; i < records.length; i++) {
        const customer = records[i].customer;
        const amount = records[i].amount;
        if (totals[customer] === undefined) {
            totals[customer] = 0;
        }
        totals[customer] += amount;
    }

    const entries = Object.keys(totals).map(function(customer) {
        return { customer: customer, total: totals[customer] };
    });

    entries.sort(function(a, b) {
        return b.total - a.total;
    });

    return entries.slice(0, 10);
}

// Display Report
function displayReport(top10) {
    const separator = '--------------------------------------------------';

    console.log('\n' + separator);
    console.log('       TOP 10 CUSTOMERS BY REVENUE');
    console.log(separator);
    console.log(' RANK  NAME                       TOTAL SALES');
    console.log(separator);

    for (let i = 0; i < top10.length; i++) {
        const rank = ('#' + (i + 1)).padEnd(5);
        const name = top10[i].customer.padEnd(26);
        const amount = (top10[i].total.toFixed(2)).padStart(12);
        console.log(' ' + rank + ' ' + name + ' ' + amount);
    }

    console.log(separator);

    let grandTotal = 0;
    for (let i = 0; i < top10.length; i++) {
        grandTotal += top10[i].total;
    }

    const label = 'COMBINED TOTAL'.padEnd(31);
    const total = (grandTotal.toFixed(2)).padStart(12);
    console.log(' ' + label + ' ' + total);
    console.log(separator + '\n');
}

// Main Processing
function processFile(filePath, content) {
    try {
        console.log('\nFile found. Processing...\n');
        const records = parseCSV(content);
        console.log('Loaded ' + records.length + ' valid sales records.');
        const top10 = getTop10Customers(records);
        displayReport(top10);
    } catch (err) {
        console.error('\nError processing file: ' + err.message + '\n');
    } finally {
        rl.close();
    }
}

// File Path Prompt (loops until valid)
function askFilePath() {
    rl.question('Enter dataset file path: ', function(filePath) {
        filePath = filePath.trim();

        if (!filePath) {
            console.log('No path entered. Please try again.');
            return askFilePath();
        }

        const result = validateFile(filePath);

        if (!result.valid) {
            console.log('Invalid file: ' + result.error);
            return askFilePath();
        }

        processFile(filePath, result.content);
    });
}

// Entry Point
console.log('\n========================================');
console.log('   TOP 10 CUSTOMERS REPORT GENERATOR');
console.log('========================================\n');

askFilePath();