import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class PatientRecordsPage extends JFrame {

    JTextArea recordsArea;
    JButton backButton;
    String patientEmail;

    public PatientRecordsPage(String email) {
        this.patientEmail = email;

        setTitle("WeCare Hospital - Patient Records");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIUtils.SECONDARY_COLOR);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIUtils.SECONDARY_COLOR);

        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(30, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(500, 500));

        JLabel titleLabel = new JLabel("Your Medical Records");
        titleLabel.setFont(UIUtils.HEADER_FONT);
        titleLabel.setForeground(UIUtils.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        recordsArea = new JTextArea();
        recordsArea.setEditable(false);
        recordsArea.setFont(UIUtils.MAIN_FONT);
        recordsArea.setBackground(UIUtils.OFF_WHITE);
        recordsArea.setLineWrap(true);
        recordsArea.setWrapStyleWord(true);
        recordsArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(recordsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        backButton = new UIUtils.RoundedButton("Back", 15, UIUtils.PRIMARY_COLOR);
        backButton.addActionListener(e -> {
            new PatientDashboard(patientEmail);
            dispose();
        });

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);
        card.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(card);
        add(mainPanel);

        loadPatientDetails();
        loadAppointmentDetails();
        setVisible(true);
    }

    private void loadPatientDetails() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM patients WHERE email = ?")) {
            pst.setString(1, patientEmail);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    String contact = rs.getString("contact");
                    String treatment = rs.getString("treatment");
        
                    recordsArea.append("Patient Details:\n");
                    recordsArea.append("Name: " + name + "\n");
                    recordsArea.append("Age: " + age + "\n");
                    recordsArea.append("Gender: " + gender + "\n");
                    recordsArea.append("Contact: " + contact + "\n");
                    if (treatment != null) recordsArea.append("Treatment: " + treatment + "\n");
                    recordsArea.append("\n");
                } else {
                    recordsArea.append("No patient found with email: " + patientEmail + "\n");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patient details: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadAppointmentDetails() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT doctor_name, appointment_date, appointment_time, reason FROM appointments WHERE patient_email = ?")) {
            pst.setString(1, patientEmail);
            try (ResultSet rs = pst.executeQuery()) {
                recordsArea.append("Your Appointments:\n");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    String doctorName = rs.getString("doctor_name");
                    Date date = rs.getDate("appointment_date");
                    Time time = rs.getTime("appointment_time");
                    String reason = rs.getString("reason");
        
                    recordsArea.append("- Dr. " + (doctorName.startsWith("Dr. ") ? doctorName : "Dr. " + doctorName) + " on " + date.toString() + " at " + time.toString());
                    if (reason != null && !reason.isEmpty()) {
                        recordsArea.append(" (Reason: " + reason + ")");
                    }
                    recordsArea.append("\n");
                }
                if (!found) {
                    recordsArea.append("No appointments found.\n");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading appointment details: " + e.getMessage());
            e.printStackTrace();
        }
    }
}