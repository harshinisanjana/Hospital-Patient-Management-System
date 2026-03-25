import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DoctorDetailsPage extends JFrame {

    JTextArea doctorDetailsArea;
    JButton backButton;
    String patientEmail;

    public DoctorDetailsPage(String email) {
        this.patientEmail = email;
        setTitle("WeCare Hospital - Doctor Details");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIUtils.SECONDARY_COLOR);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIUtils.SECONDARY_COLOR);

        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(30, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(400, 400));

        JLabel titleLabel = new JLabel("Available Specialists");
        titleLabel.setFont(UIUtils.HEADER_FONT);
        titleLabel.setForeground(UIUtils.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        doctorDetailsArea = new JTextArea();
        doctorDetailsArea.setEditable(false);
        doctorDetailsArea.setFont(UIUtils.MAIN_FONT);
        doctorDetailsArea.setBackground(UIUtils.OFF_WHITE);
        doctorDetailsArea.setLineWrap(true);
        doctorDetailsArea.setWrapStyleWord(true);
        doctorDetailsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(doctorDetailsArea);
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

        loadDoctorDetails();
        setVisible(true);
    }

    private void loadDoctorDetails() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, specialization FROM doctors")) {

            while (rs.next()) {
                String name = rs.getString("name");
                String specialization = rs.getString("specialization");
                doctorDetailsArea.append("Dr. " + (name.startsWith("Dr. ") ? name : "Dr. " + name) + " - " + specialization + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DoctorDetailsPage("patient@example.com");
    }
}
