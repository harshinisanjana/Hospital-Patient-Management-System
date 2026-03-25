import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import java.util.Date; // important

public class AppointmentScheduling extends JFrame implements ActionListener {

    JLabel doctorLabel, dateLabel, timeLabel, reasonLabel;
    JComboBox<String> doctorComboBox;
    JSpinner dateSpinner, timeSpinner;
    JTextField reasonField;
    JButton scheduleButton, backButton;
    String patientEmail;
    
    public AppointmentScheduling(String email) {
        this.patientEmail = email;
    
        setTitle("WeCare Hospital - Schedule Appointment");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIUtils.SECONDARY_COLOR);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIUtils.SECONDARY_COLOR);

        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(30, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Schedule Appointment");
        titleLabel.setFont(UIUtils.HEADER_FONT);
        titleLabel.setForeground(UIUtils.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        doctorComboBox = new JComboBox<>();
        doctorComboBox.setFont(UIUtils.MAIN_FONT);
        doctorComboBox.setBackground(Color.WHITE);
        loadDoctors();
    
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setFont(UIUtils.MAIN_FONT);
    
        timeSpinner = new JSpinner(new SpinnerListModel(new String[]{
                "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"
        }));
        timeSpinner.setFont(UIUtils.MAIN_FONT);
    
        reasonField = UIUtils.createStyledTextField("Reason for visit");
    
        scheduleButton = new UIUtils.RoundedButton("Confirm Appointment", 15, UIUtils.PRIMARY_COLOR);
        backButton = new UIUtils.RoundedButton("Back", 15, Color.WHITE);
        backButton.setForeground(UIUtils.PRIMARY_COLOR);
        backButton.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1));

        scheduleButton.addActionListener(this);
        backButton.addActionListener(this);

        // Add to card
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        addFormField(card, "Select Doctor:", doctorComboBox);
        addFormField(card, "Appointment Date:", dateSpinner);
        addFormField(card, "Preferred Time:", timeSpinner);
        addFormField(card, "Reason (optional):", reasonField);

        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(scheduleButton);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(backButton);

        mainPanel.add(card);
        add(mainPanel);
    
        setVisible(true);
    }

    private void addFormField(JPanel panel, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(UIUtils.TEXT_COLOR);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }
    
    private void loadDoctors() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM doctors")) {
    
            while (rs.next()) {
                doctorComboBox.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == scheduleButton) {
            String doctor = (String) doctorComboBox.getSelectedItem();
            Date date = (Date) dateSpinner.getValue();
            String timeStr = (String) timeSpinner.getValue();
            String reason = reasonField.getText().trim();
    
            if (doctor == null || date == null || timeStr == null || patientEmail == null) {
                JOptionPane.showMessageDialog(this, "Please fill all the fields!");
                return;
            }
    
            String patientName = fetchPatientName(patientEmail);
    
            if (scheduleAppointment(patientName, patientEmail, doctor, date, timeStr, reason)) {
                JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
                dispose();
                new PatientDashboard(patientEmail);
            } else {
                JOptionPane.showMessageDialog(this, "Error scheduling appointment.");
            }
        } else if (e.getSource() == backButton) {
            dispose();
            new PatientDashboard(patientEmail);
        }
    }
    
    private String fetchPatientName(String email) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT name FROM patients WHERE email = ?")) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Unknown";
    }
    
    private boolean scheduleAppointment(String patientName, String email, String doctor, Date date, String timeStr, String reason) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("INSERT INTO appointments (patient_name, patient_email, doctor_name, appointment_date, appointment_time, reason) VALUES (?, ?, ?, ?, ?, ?)")) {
            pst.setString(1, patientName);
            pst.setString(2, email);
            pst.setString(3, doctor);
            pst.setDate(4, new java.sql.Date(date.getTime()));
            pst.setTime(5, java.sql.Time.valueOf(timeStr + ":00"));
            pst.setString(6, reason.isEmpty() ? null : reason);
    
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void main(String[] args) {
        new AppointmentScheduling("priya@gmail.com");  // For test
    }
}    