import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageDoctorsPage extends JFrame {

    private JTable doctorsTable;
    private JButton addDoctorButton, refreshButton;
    private DefaultTableModel tableModel;

    public ManageDoctorsPage() {
        setTitle("WeCare Hospital - Staff Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIUtils.SECONDARY_COLOR);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(900, 70));
        header.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel titleLabel = new JLabel("Doctor Management");
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

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Specialization", "Contact"}, 0);
        doctorsTable = new JTable(tableModel);
        doctorsTable.setFont(UIUtils.MAIN_FONT);
        doctorsTable.setRowHeight(35);
        doctorsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        doctorsTable.getTableHeader().setBackground(new Color(240, 240, 240));
        doctorsTable.setShowGrid(false);
        doctorsTable.setIntercellSpacing(new Dimension(0, 5));
        
        JScrollPane scrollPane = new JScrollPane(doctorsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Sidebar/Actions
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(UIUtils.SECONDARY_COLOR);
        actionPanel.setPreferredSize(new Dimension(200, 0));

        addDoctorButton = new UIUtils.RoundedButton(" Add Doctor", 15, UIUtils.PRIMARY_COLOR);
        refreshButton = new UIUtils.RoundedButton(" Refresh Data", 15, new Color(46, 204, 113));
        UIUtils.RoundedButton backButton = new UIUtils.RoundedButton(" Dashboard", 15, Color.WHITE);
        backButton.setForeground(UIUtils.PRIMARY_COLOR);
        backButton.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1));

        addDoctorButton.addActionListener(e -> addDoctor());
        refreshButton.addActionListener(e -> loadDoctors());
        backButton.addActionListener(e -> {
            new AdminDashboard("admin");
            dispose();
        });

        actionPanel.add(addDoctorButton);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(refreshButton);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(backButton);

        content.add(tableCard, BorderLayout.CENTER);
        content.add(actionPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        setVisible(true);
        loadDoctors();
    }

    private void loadDoctors() {
        tableModel.setRowCount(0); // Clear old data
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM doctors")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getString("contact")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + e.getMessage());
        }
    }

    private void addDoctor() {
        JTextField nameField = new JTextField();
        JTextField specializationField = new JTextField();
        JTextField contactField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Specialization:"));
        panel.add(specializationField);
        panel.add(new JLabel("Contact:"));
        panel.add(contactField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Doctor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String specialization = specializationField.getText();
            String contact = contactField.getText();

            if (!name.isEmpty() && !specialization.isEmpty() && !contact.isEmpty()) {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pst = conn.prepareStatement("INSERT INTO doctors (name, specialization, contact) VALUES (?, ?, ?)")) {
                    pst.setString(1, name);
                    pst.setString(2, specialization);
                    pst.setString(3, contact);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Doctor added successfully!");
                    loadDoctors(); // Reload table
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error adding doctor: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "All fields are required.");
            }
        }
    }
}