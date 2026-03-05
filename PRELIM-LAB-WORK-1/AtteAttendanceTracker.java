import java.awt.*;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class AtteAttendanceTracker {
    
    public static void main(String[] args) {
        // Create frame on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Create JFrame window
            JFrame frame = new JFrame("AtteAttendance Tracker");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridBagLayout());
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Attendance Name label and text field
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            frame.add(new JLabel("AtteAttendance Name:"), gbc);
            
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            JTextField nameField = new JTextField(20);
            frame.add(nameField, gbc);
            
            // Course/Year label and text field
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            frame.add(new JLabel("Course/Year:"), gbc);
            
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            JTextField courseField = new JTextField(20);
            frame.add(courseField, gbc);
            
            // Time In label and text field (auto-filled with system date/time)
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.EAST;
            frame.add(new JLabel("Time In:"), gbc);
            
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            JTextField timeInField = new JTextField(20);
            timeInField.setEditable(false);
            // Get current system date and time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            timeInField.setText(now.format(formatter));
            frame.add(timeInField, gbc);
            
            // E-Signature label and text field
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.EAST;
            frame.add(new JLabel("E-Signature:"), gbc);
            
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            JTextField signatureField = new JTextField(20);
            signatureField.setEditable(false);
            frame.add(signatureField, gbc);
            
            // Submit button to generate e-signature
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton submitButton = new JButton("Generate E-Signature");
            
            // Button action to generate e-signature programmatically
            submitButton.addActionListener(e -> {
                String name = nameField.getText().trim();
                String course = courseField.getText().trim();
                
                // Validate input
                if (name.isEmpty() || course.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                        "Please fill in all fields!",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Update time
                LocalDateTime currentTime = LocalDateTime.now();
                timeInField.setText(currentTime.format(formatter));
                
                // Generate e-signature using MD5 hash
                try {
                    String data = name + course + timeInField.getText();
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] hash = md.digest(data.getBytes());
                    
                    // Convert to hex string
                    StringBuilder signature = new StringBuilder();
                    for (byte b : hash) {
                        signature.append(String.format("%02x", b));
                    }
                    
                    // Display e-signature (first 16 characters)
                    signatureField.setText(signature.substring(0, 16).toUpperCase());
                    
                    JOptionPane.showMessageDialog(frame,
                        "Attendance recorded successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                        "Error generating signature!",
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
            frame.add(submitButton, gbc);
            
            // Center window on screen
            frame.setLocationRelativeTo(null);
            
            // Make window visible
            frame.setVisible(true);
        });
    }
}
