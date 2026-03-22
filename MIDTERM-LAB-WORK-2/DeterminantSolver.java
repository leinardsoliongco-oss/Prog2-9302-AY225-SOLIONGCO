/**
 * =====================================================
 * Student Name    : Leinard Emmanuel E. Soliongco
 * Course          : BSIT-GD 1 — Linear Algebra
 * Assignment      : 3x3 Matrix Determinant Solver
 * School          : University of Perpetual Help System DALTA, Molino Campus
 * Date            : March 18, 2026
 * GitHub Repo     : https://github.com/leinardsoliongco-oss/Prog2-9302-AY225-SOLIONGCO.git
 *
 * Description:
 *   This program computes the determinant of a hardcoded 3x3 matrix assigned
 *   to Leinard Emmanuel E. Soliongco for BSIT-GD 1 — Linear Algebra. The solution is computed using cofactor
 *   expansion along the first row. Each intermediate step (2x2 minor,
 *   cofactor term, running sum) is printed to the console in a readable format.
 * =====================================================
 */
public class DeterminantSolver {

    // ── SECTION 1: Matrix Declaration ───────────────────────────────────
    // Declare the 3x3 matrix assigned to this student.
    // Values are hardcoded as a 2D integer array in row-major order.
    // Replace the values below with YOUR assigned matrix from the assignment sheet.
    static int[][] matrix = {
        { 2, 1, 6 },   // Row 1 of assigned matrix
        { 4, 3, 5 },   // Row 2 of assigned matrix
        { 6, 2, 4 }    // Row 3 of assigned matrix
    };

    // ── SECTION 2: 2×2 Determinant Helper ───────────────────────────────
    // Computes the determinant of a 2x2 matrix given its four elements.
    // Formula: det = (a * d) - (b * c)
    // This method is called three times during cofactor expansion,
    // once for each of the three 2x2 minors of the first row.
    static int computeMinor(int a, int b, int c, int d) {
        // Apply the 2x2 determinant formula: ad - bc
        return (a * d) - (b * c);
    }

    // ── SECTION 3: Matrix Printer ────────────────────────────────────────
    // Prints the 3x3 matrix to the console in a formatted, readable layout.
    // Used at the start of output to display the problem clearly.
    static void printMatrix(int[][] m) {
        System.out.println("┌               ┐");
        for (int[] row : m) {
            System.out.printf("│  %2d  %2d  %2d  │%n", row[0], row[1], row[2]);
        }
        System.out.println("└               ┘");
    }

    // ── SECTION 4: Step-by-Step Determinant Solver ──────────────────────
    // Computes the determinant of a 3x3 matrix using cofactor expansion
    // along the first row. Prints each step clearly:
    //   (a) Label the step (Minor M₁₁, M₁₂, M₁₃)
    //   (b) Show the 2x2 sub-matrix
    //   (c) Show the arithmetic of the minor
    //   (d) Compute and display each signed cofactor term
    //   (e) Display the final sum and determinant value
    static void solveDeterminant(int[][] m) {

        // Print the header and the matrix
        System.out.println("=".repeat(52));
        System.out.println("  3x3 MATRIX DETERMINANT SOLVER");
        System.out.println("  Student: Leinard Emmanuel E. Soliongco");
        System.out.println("  Assigned Matrix:");
        System.out.println("=".repeat(52));
        printMatrix(m);
        System.out.println("=".repeat(52));

        // ── Step 1: Compute minor M₁₁ ──
        // Remove row 0 and column 0 to get the 2x2 minor.
        // The remaining elements are m[1][1], m[1][2], m[2][1], m[2][2].
        int minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
        System.out.printf("  Step 1 — Minor M₁₁: det([%d,%d],[%d,%d]) = (%d×%d)-(%d×%d) = %d%n",
            m[1][1], m[1][2], m[2][1], m[2][2],
            m[1][1], m[2][2], m[1][2], m[2][1], minor11);

        // ── Step 2: Compute minor M₁₂ ──
        // Remove row 0 and column 1. Remaining: m[1][0], m[1][2], m[2][0], m[2][2].
        int minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
        System.out.printf("  Step 2 — Minor M₁₂: det([%d,%d],[%d,%d]) = (%d×%d)-(%d×%d) = %d%n",
            m[1][0], m[1][2], m[2][0], m[2][2],
            m[1][0], m[2][2], m[1][2], m[2][0], minor12);

        // ── Step 3: Compute minor M₁₃ ──
        // Remove row 0 and column 2. Remaining: m[1][0], m[1][1], m[2][0], m[2][1].
        int minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);
        System.out.printf("  Step 3 — Minor M₁₃: det([%d,%d],[%d,%d]) = (%d×%d)-(%d×%d) = %d%n",
            m[1][0], m[1][1], m[2][0], m[2][1],
            m[1][0], m[2][1], m[1][1], m[2][0], minor13);

        // ── Cofactor Terms ──
        // Apply the alternating sign pattern: C₁₁ = +m[0][0]·minor11
        //                                     C₁₂ = −m[0][1]·minor12
        //                                     C₁₃ = +m[0][2]·minor13
        int c11 =  m[0][0] * minor11;
        int c12 = -m[0][1] * minor12;
        int c13 =  m[0][2] * minor13;

        System.out.println();
        System.out.printf("  Cofactor C₁₁ = (+1) × %d × %d = %d%n", m[0][0], minor11, c11);
        System.out.printf("  Cofactor C₁₂ = (-1) × %d × %d = %d%n", m[0][1], minor12, c12);
        System.out.printf("  Cofactor C₁₃ = (+1) × %d × %d = %d%n", m[0][2], minor13, c13);

        // ── Final Determinant ──
        // Sum the three cofactor terms to get the determinant value.
        int det = c11 + c12 + c13;
        System.out.printf("%n  det(M) = %d + (%d) + %d%n", c11, c12, c13);
        System.out.println("=".repeat(52));
        System.out.printf("  ✓  DETERMINANT = %d%n", det);

        // ── Singular Matrix Check ──
        // A determinant of zero means the matrix is singular (non-invertible).
        if (det == 0) {
            System.out.println("  ⚠ The matrix is SINGULAR — it has no inverse.");
        }
        System.out.println("=".repeat(52));
    }

    // ── SECTION 5: Entry Point ───────────────────────────────────────────
    // The main method is the program's entry point.
    // It calls solveDeterminant() with the student's assigned matrix.
    public static void main(String[] args) {
        solveDeterminant(matrix);
    }

}