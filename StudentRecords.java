/*
 Programmer: Leinard Soliongco
 Student ID: 23-1384-490
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class StudentRecords extends JFrame {

    DefaultTableModel model;
    JTable table;
    JTextField txtId, txtName, txtGrade;

    public StudentRecords() {
        this.setTitle("Records - Leinard Soliongco YOUR-STUDENT-ID");
        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Grade"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Input Panel
        JPanel panel = new JPanel(new GridLayout(2, 4));

        txtId = new JTextField();
        txtName = new JTextField();
        txtGrade = new JTextField();

        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");

        panel.add(new JLabel("ID"));
        panel.add(new JLabel("Name"));
        panel.add(new JLabel("Grade"));
        panel.add(new JLabel(""));

        panel.add(txtId);
        panel.add(txtName);
        panel.add(txtGrade);
        panel.add(btnAdd);

        add(panel, BorderLayout.SOUTH);
        add(btnDelete, BorderLayout.NORTH);

        loadCSV();

        // CREATE
        btnAdd.addActionListener(e -> {
            model.addRow(new String[]{
                txtId.getText(),
                txtName.getText(),
                txtGrade.getText()
            });
        });

        // DELETE
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) model.removeRow(row);
        });
    }

    private void loadCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("MOCK_DATA.csv"))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                model.addRow(new String[]{data[0], data[1], data[2]});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading CSV file");
        }
    }

    public static void main(String[] args) {
        new StudentRecords().setVisible(true);
    }
}
