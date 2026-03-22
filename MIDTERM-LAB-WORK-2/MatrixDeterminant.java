public class MatrixDeterminant {
    public static void main(String[] args) {
        int[][] m = {
            { 2, 1, 6 },
            { 4, 3, 5 },
            { 6, 2, 4 }
        };

       
        int M11 = (3 * 4) - (5 * 2);
        int M12 = ((4 * 4) - (5 * 6));
        int M13 = ((4 * 2) - (3 * 6));

        // Cofactors
        int C11 = (+1) * m[0][0] * M11;
        int C12 = (-1) * m[0][1] * M12;
        int C13 = (+1) * m[0][2] * M13;

        int det = C11 + C12 + C13;

       
        System.out.println("================================");
        System.out.println("3x3 MATRIX DETERMINANT SOLVER");
        System.out.println("Student: [Soliongco Leinard Emmanuel E.]");
        System.out.println("Assigned Matrix: 33 ");
        System.out.println("================================");
        System.out.println("|  2   1   6 |");
        System.out.println("|  4   3   5 |");
        System.out.println("|  6   2   4 |");
        System.out.println("================================");
        System.out.println("Expanding along Row 1 (cofactor expansion):");
        System.out.println("Minor M11: " + M11);
        System.out.println("Minor M12: " + M12);
        System.out.println("Minor M13: " + M13);
        System.out.println("Cofactor C11: " + C11);
        System.out.println("Cofactor C12: " + C12);
        System.out.println("Cofactor C13: " + C13);
        System.out.println("================================");
        System.out.println("DETERMINANT = " + det);
    }
}