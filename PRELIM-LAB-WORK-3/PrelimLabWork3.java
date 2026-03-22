
import java.util.Scanner;

public class PrelimLabWork3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input
        System.out.print("Enter number of attendances: ");
        int attendance = sc.nextInt();

        System.out.print("Enter Lab Work 1 grade: ");
        double lab1 = sc.nextDouble();

        System.out.print("Enter Lab Work 2 grade: ");
        double lab2 = sc.nextDouble();

        System.out.print("Enter Lab Work 3 grade: ");
        double lab3 = sc.nextDouble();

        // Computations
        double labAverage = (lab1 + lab2 + lab3) / 3.0;
        double classStanding = (attendance * 0.2) + (labAverage * 0.8);

        // Required Prelim Exam scores
        double requiredPassing = (75 - classStanding);
        double requiredExcellent = (100 - classStanding);

        // Output
        System.out.println("\n===== Student Standing Report =====");
        System.out.println("Attendance Score: " + attendance);
        System.out.println("Lab Work 1: " + lab1);
        System.out.println("Lab Work 2: " + lab2);
        System.out.println("Lab Work 3: " + lab3);
        System.out.println("Lab Work Average: " + labAverage);
        System.out.println("Class Standing: " + classStanding);
        System.out.println("Required Prelim Exam Score to Pass (75): " + requiredPassing);
        System.out.println("Required Prelim Exam Score to be Excellent (100): " + requiredExcellent);

        // Remarks
        if (classStanding >= 75) {
            System.out.println("Remarks: You are already passing based on class standing.");
        } else {
            System.out.println("Remarks: You need to perform well in the Prelim Exam to pass.");
        }

        sc.close();
    }
}