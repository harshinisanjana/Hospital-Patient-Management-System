import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AuthenticationPage extends JFrame implements ActionListener {

    JButton loginButton, registerButton;
    JTextField emailField;
    JPasswordField passwordField;

    public AuthenticationPage() {
        setTitle("WeCare Hospital - Authentication");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(UIUtils.SECONDARY_COLOR);

        // Main Container with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIUtils.SECONDARY_COLOR);
        
        // Card Panel
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(30, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(350, 400));
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(UIUtils.HEADER_FONT);
        titleLabel.setForeground(UIUtils.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Please login to your account");
        subtitleLabel.setFont(UIUtils.MAIN_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        emailField = UIUtils.createStyledTextField("Email");
        passwordField = UIUtils.createStyledPasswordField();
        
        loginButton = new UIUtils.RoundedButton("Login", 15, UIUtils.PRIMARY_COLOR);
        registerButton = new UIUtils.RoundedButton("Create New Account", 15, Color.WHITE);
        registerButton.setForeground(UIUtils.PRIMARY_COLOR);
        registerButton.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1));

        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        // Adding components to card
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(subtitleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        card.add(new JLabel("Email Address:"));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(emailField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        card.add(new JLabel("Password:"));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        card.add(loginButton);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(registerButton);

        mainPanel.add(card);
        add(mainPanel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (e.getSource() == loginButton) {
            if (isAdmin(email, password)) {
                // Admin login validation
                JOptionPane.showMessageDialog(this, "Admin Login Successful!");
                dispose();
                new AdminDashboard(email);  // Redirect to Admin Dashboard
            } else if (authenticatePatient(email, password)) {
                // Patient login validation
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new PatientDashboard(email);  // Redirect to Patient Dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Email or Password!");
            }
        } else if (e.getSource() == registerButton) {
            dispose();
            new RegistrationPage();  // Redirect to Patient Registration Page
        }
    }

    private boolean isAdmin(String username, String password) {
        // Admin authentication logic (pre-configured admin in the database)
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return true;  // Admin found
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;  // No admin found
    }

    private boolean authenticatePatient(String email, String password) {
        // Patient authentication logic (check against the patients table)
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM patients WHERE email=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return true;  // Patient found
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;  // No patient found
    }

    public static void main(String[] args) {
        new AuthenticationPage();
    }
}
