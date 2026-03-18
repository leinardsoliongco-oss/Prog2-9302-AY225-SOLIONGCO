/*
 Programmer: Leinard Soliongco
 Student ID: 23-1384-490
*/

// CSV DATA (from MOCK_DATA.csv)
const csvData = `
id,name,grade
1,Juan Dela Cruz,90
2,Maria Santos,88
3,Jose Rizal,95
`;

let students = csvData.trim().split("\n").slice(1).map(row => {
    const [id, name, grade] = row.split(",");
    return { id, name, grade };
});

function render() {
    const tbody = document.getElementById("tableBody");
    tbody.innerHTML = "";

    students.forEach((s, index) => {
        tbody.innerHTML += `
            <tr>
                <td>${s.id}</td>
                <td>${s.name}</td>
                <td>${s.grade}</td>
                <td><button onclick="deleteStudent(${index})">Delete</button></td>
            </tr>
        `;
    });
}

function addStudent() {
    students.push({
        id: document.getElementById("id").value,
        name: document.getElementById("name").value,
        grade: document.getElementById("grade").value
    });
    render();
}

function deleteStudent(index) {
    students.splice(index, 1);
    render();
}

render();
