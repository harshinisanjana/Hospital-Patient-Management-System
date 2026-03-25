import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageAppointmentsPage extends JFrame {

    private JTable appointmentsTable;
    private JButton addAppointmentButton, refreshButton;
    private DefaultTableModel tableModel;

    public ManageAppointmentsPage() {
        setTitle("WeCare Hospital - Appointment Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIUtils.SECONDARY_COLOR);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(1000, 70));
        header.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel titleLabel = new JLabel("Appointment Control Panel");
        titleLabel.setFont(UIUtils.HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        // Content
        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBackground(UIUtils.SECONDARY_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        UIUtils.RoundedPanel tableCard = new UIUtils.RoundedPanel(20, Color.WHITE);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        tableModel = new DefaultTableModel(new String[]{"ID", "Patient Name", "Doctor Name", "Date", "Time", "Reason"}, 0);
        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setFont(UIUtils.MAIN_FONT);
        appointmentsTable.setRowHeight(35);
        appointmentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        appointmentsTable.getTableHeader().setBackground(new Color(240, 240, 240));
        appointmentsTable.setShowGrid(false);
        appointmentsTable.setIntercellSpacing(new Dimension(0, 5));
        
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Sidebar/Actions
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(UIUtils.SECONDARY_COLOR);
        actionPanel.setPreferredSize(new Dimension(200, 0));

        addAppointmentButton = new UIUtils.RoundedButton(" Add Appointment", 15, UIUtils.PRIMARY_COLOR);
        refreshButton = new UIUtils.RoundedButton(" Refresh Data", 15, new Color(46, 204, 113));
        UIUtils.RoundedButton backButton = new UIUtils.RoundedButton(" Dashboard", 15, Color.WHITE);
        backButton.setForeground(UIUtils.PRIMARY_COLOR);
        backButton.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1));

        addAppointmentButton.addActionListener(e -> addAppointment());
        refreshButton.addActionListener(e -> loadAppointments());
        backButton.addActionListener(e -> {
            new AdminDashboard("admin");
            dispose();
        });

        actionPanel.add(addAppointmentButton);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(refreshButton);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(backButton);

        content.add(tableCard, BorderLayout.CENTER);
        content.add(actionPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        setVisible(true);
        loadAppointments();
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM appointments")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("reason")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + e.getMessage());
        }
    }

    private void addAppointment() {
        JTextField patientNameField = new JTextField();
        JTextField doctorNameField = new JTextField();
        JTextField dateField = new JTextField("YYYY-MM-DD");
        JTextField timeField = new JTextField("HH:MM:SS");
        JTextField reasonField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Patient Name:"));
        panel.add(patientNameField);
        panel.add(new JLabel("Doctor Name:"));
        panel.add(doctorNameField);
        panel.add(new JLabel("Appointment Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Appointment Time (HH:MM:SS):"));
        panel.add(timeField);
        panel.add(new JLabel("Reason:"));
        panel.add(reasonField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String patientName = patientNameField.getText().trim();
            String doctorName = doctorNameField.getText().trim();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String reason = reasonField.getText().trim();

            if (!patientName.isEmpty() && !doctorName.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pst = conn.prepareStatement(
                             "INSERT INTO appointments (patient_name, doctor_name, appointment_date, appointment_time, reason) VALUES (?, ?, ?, ?, ?)")) {
                    pst.setString(1, patientName);
                    pst.setString(2, doctorName);
                    pst.setDate(3, Date.valueOf(date)); // Converts String to SQL Date
                    pst.setTime(4, Time.valueOf(time)); // Converts String to SQL Time
                    pst.setString(5, reason);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Appointment added successfully!");
                    loadAppointments();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error adding appointment: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "All fields except reason are required.");
            }
        }
    }
}