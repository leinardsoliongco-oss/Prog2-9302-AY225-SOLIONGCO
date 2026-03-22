const m = [
    [2, 1, 6],
    [4, 3, 5],
    [6, 2, 4]
];

// Minors
const M11 = (3 * 4) - (5 * 2);
const M12 = (4 * 4) - (5 * 6);
const M13 = (4 * 2) - (3 * 6);

// Cofactors
const C11 = (+1) * m[0][0] * M11;
const C12 = (-1) * m[0][1] * M12;
const C13 = (+1) * m[0][2] * M13;

// Determinant
const det = C11 + C12 + C13;

// Output
console.log("================================");
console.log("3x3 MATRIX DETERMINANT SOLVER");
console.log("Student: [Soliongco Leinard Emmanuel E.]");
console.log("Assigned Matrix: 33 ");
console.log("================================");
console.log("|  2   1   6 |");
console.log("|  4   3   5 |");
console.log("|  6   2   4 |");
console.log("================================");
console.log("Expanding along Row 1 (cofactor expansion):");
console.log("Minor M11: " + M11);
console.log("Minor M12: " + M12);
console.log("Minor M13: " + M13);
console.log("Cofactor C11: " + C11);
console.log("Cofactor C12: " + C12);
console.log("Cofactor C13: " + C13);
console.log("================================");
console.log("DETERMINANT = " + det);