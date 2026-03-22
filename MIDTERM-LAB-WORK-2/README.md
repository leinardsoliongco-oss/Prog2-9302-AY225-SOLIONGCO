# Programming Assignment 1 — 3×3 Matrix Determinant Solver

## Student Information
- **Full Name:** Leinard Emmanuel E. Soliongco
- **Section:** 1203L 9302-AY225
- **Course:** BSIT-GD 1 Game Development, UPHSD Molino Campus
- **Assignment:** Programming Assignment 1 — 3x3 Matrix Determinant Solver
- **Academic Year:** 2025–2026

---

## Assigned Matrix

My assigned matrix:

```
| 2  1  6 |
| 4  3  5 |
| 6  2  4 |
```

---

## How to Run the Java Program

**Step 1 — Compile the program:**
```bash
javac DeterminantSolver.java
```

**Step 2 — Run the compiled program:**
```bash
java DeterminantSolver
```

> Make sure you are inside the `linear-algebra/assignment-01/` folder when running these commands.

---

## How to Run the JavaScript Program

**Run with Node.js:**
```bash
node determinant_solver.js
```

> Requires Node.js to be installed. No additional packages needed.

---

## Sample Output

Both programs produce the same result. Below is the sample output:

```
====================================================
  3x3 MATRIX DETERMINANT SOLVER
  Student: Leinard Emmanuel E. Soliongco
  Assigned Matrix:
====================================================
┌               ┐
│   2   1   6  │
│   4   3   5  │
│   6   2   4  │
└               ┘
====================================================

Expanding along Row 1 (cofactor expansion):

  Step 1 — Minor M₁₁: det([3,5],[2,4]) = (3×4) - (5×2) = 12 - 10 = 2
  Step 2 — Minor M₁₂: det([4,5],[6,4]) = (4×4) - (5×6) = 16 - 30 = -14
  Step 3 — Minor M₁₃: det([4,3],[6,2]) = (4×2) - (3×6) = 8 - 18 = -10

  Cofactor C₁₁ = (+1) × 2 × 2 = 4
  Cofactor C₁₂ = (-1) × 1 × -14 = 14
  Cofactor C₁₃ = (+1) × 6 × -10 = -60

  det(M) = 4 + (14) + -60
====================================================
  ✓  DETERMINANT = -42
====================================================
```

---

## Final Determinant Value

**det(M) = -42**

Since the determinant is not zero, the matrix is **non-singular** and has an inverse.

---

## Files in This Repository

| File | Description |
|------|-------------|
| `DeterminantSolver.java` | Java solution — computes the determinant with step-by-step console output |
| `determinant_solver.js` | JavaScript (Node.js) solution — identical logic and output |
| `README.md` | This documentation file |

---

## Repository

**GitHub:** `https://github.com/leinardsoliongco-oss/Prog2-9302-AY225-SOLIONGCO.git`