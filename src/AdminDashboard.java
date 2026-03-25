import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame implements ActionListener {

    JButton manageDoctorsButton, viewPatientsButton, manageAppointmentsButton, logoutButton;
    String adminUsername;

    public AdminDashboard(String username) {
        this.adminUsername = username;

        setTitle("WeCare Hospital - Admin Dashboard");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(UIUtils.SECONDARY_COLOR);

        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80)); // Darker slate for admin
        header.setPreferredSize(new Dimension(700, 80));
        header.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel titleLabel = new JLabel("Admin Control Panel");
        titleLabel.setFont(UIUtils.HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Admin: " + adminUsername);
        userLabel.setFont(UIUtils.MAIN_FONT);
        userLabel.setForeground(new Color(189, 195, 199));

        header.add(titleLabel, BorderLayout.WEST);
        header.add(userLabel, BorderLayout.EAST);

        // Content (Card Grid)
        JPanel content = new JPanel(new GridLayout(1, 3, 20, 20));
        content.setBackground(UIUtils.SECONDARY_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        manageDoctorsButton = createDashboardCard("Doctors", "Manage hospital staff");
        manageAppointmentsButton = createDashboardCard("Appointments", "Manage scheduled visits");
        logoutButton = createDashboardCard("Logout", "Exit admin panel");
        logoutButton.setForeground(UIUtils.ACCENT_COLOR);

        content.add(manageDoctorsButton);
        content.add(manageAppointmentsButton);
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
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel tLabel = new JLabel(title);
        tLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tLabel.setForeground(new Color(44, 62, 80));
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
                    BorderFactory.createLineBorder(new Color(44, 62, 80), 1),
                    BorderFactory.createEmptyBorder(20, 10, 20, 10)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(20, 10, 20, 10)
                ));
            }
        });

        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == manageDoctorsButton) {
            new ManageDoctorsPage();
            dispose();
        } else if (e.getSource() == manageAppointmentsButton) {
            new ManageAppointmentsPage();
            dispose();
        } else if (e.getSource() == logoutButton) {
            new AuthenticationPage();
            dispose();
        }
    }
}