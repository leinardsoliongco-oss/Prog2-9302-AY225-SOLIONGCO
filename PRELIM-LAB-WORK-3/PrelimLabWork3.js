// Console-based version using Node.js
const readline = require("readline");

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

let inputs = [];

const questions = [
  "Enter number of attendances: ",
  "Enter Lab Work 1 grade: ",
  "Enter Lab Work 2 grade: ",
  "Enter Lab Work 3 grade: "
];

function askQuestion(i) {
  if (i < questions.length) {
    rl.question(questions[i], (answer) => {
      inputs.push(parseFloat(answer));
      askQuestion(i + 1);
    });
  } else {
    // Computations
    const attendance = inputs[0];
    const lab1 = inputs[1];
    const lab2 = inputs[2];
    const lab3 = inputs[3];

    const labAverage = (lab1 + lab2 + lab3) / 3.0;
    const classStanding = (attendance * 0.2) + (labAverage * 0.8);

    const requiredPassing = 75 - classStanding;
    const requiredExcellent = 100 - classStanding;

    // Output
    console.log("\n===== Student Standing Report =====");
    console.log("Attendance Score: " + attendance);
    console.log("Lab Work 1: " + lab1);
    console.log("Lab Work 2: " + lab2);
    console.log("Lab Work 3: " + lab3);
    console.log("Lab Work Average: " + labAverage);
    console.log("Class Standing: " + classStanding);
    console.log("Required Prelim Exam Score to Pass (75): " + requiredPassing);
    console.log("Required Prelim Exam Score to be Excellent (100): " + requiredExcellent);

    if (classStanding >= 75) {
      console.log("Remarks: You are already passing based on class standing.");
    } else {
      console.log("Remarks: You need to perform well in the Prelim Exam to pass.");
    }

    rl.close();
  }
}

askQuestion(0);