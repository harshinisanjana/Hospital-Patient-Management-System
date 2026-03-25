import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PatientDashboard extends JFrame implements ActionListener {

    JButton doctorButton, appointmentButton, recordsButton, logoutButton;
    String patientEmail;

    public PatientDashboard(String email) {
        this.patientEmail = email;

        setTitle("WeCare Hospital - Patient Dashboard");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(UIUtils.SECONDARY_COLOR);

        // Main Layout
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(700, 80));
        header.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel titleLabel = new JLabel("Patient Dashboard");
        titleLabel.setFont(UIUtils.HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Signed in as: " + patientEmail);
        userLabel.setFont(UIUtils.MAIN_FONT);
        userLabel.setForeground(new Color(220, 220, 220));

        header.add(titleLabel, BorderLayout.WEST);
        header.add(userLabel, BorderLayout.EAST);

        // Content (Card Grid)
        JPanel content = new JPanel(new GridLayout(2, 2, 20, 20));
        content.setBackground(UIUtils.SECONDARY_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        doctorButton = createDashboardCard("Doctor Details", "View available specialists");
        appointmentButton = createDashboardCard("Appointments", "Schedule a new visit");
        recordsButton = createDashboardCard("Medical Records", "View your history");
        logoutButton = createDashboardCard("Logout", "Sign out of your account");
        logoutButton.setForeground(UIUtils.ACCENT_COLOR);

        content.add(doctorButton);
        content.add(appointmentButton);
        content.add(recordsButton);
        content.add(logoutButton);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createDashboardCard(String title, String subtitle) {
        JButton card = new JButton();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setFocusPainted(false);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel tLabel = new JLabel(title);
        tLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tLabel.setForeground(UIUtils.PRIMARY_COLOR);
        tLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sLabel = new JLabel(subtitle);
        sLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sLabel.setForeground(Color.GRAY);
        sLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(tLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(sLabel);

        card.addActionListener(this);
        
        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 252, 255));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
        });

        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == doctorButton) {
            new DoctorDetailsPage(patientEmail);
            dispose();
        } else if (e.getSource() == appointmentButton) {
            new AppointmentScheduling(patientEmail);
            dispose();
        } else if (e.getSource() == recordsButton) {
            new PatientRecordsPage(patientEmail);
            dispose();
        } else if (e.getSource() == logoutButton) {
            new AuthenticationPage();
            dispose();
        }
    }
}
