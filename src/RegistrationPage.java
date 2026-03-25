import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegistrationPage extends JFrame implements ActionListener {

    JLabel nameLabel, ageLabel, genderLabel, contactLabel, emailLabel, passwordLabel;
    JTextField nameField, ageField, contactField, emailField;
    JPasswordField passwordField;
    JComboBox<String> genderComboBox;
    JButton submitButton, backButton;

    public RegistrationPage() {
        setTitle("WeCare Hospital - Patient Registration");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIUtils.SECONDARY_COLOR);

        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(30, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(UIUtils.HEADER_FONT);
        titleLabel.setForeground(UIUtils.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields Initialization
        nameField = UIUtils.createStyledTextField("Full Name");
        ageField = UIUtils.createStyledTextField("Age");
        contactField = UIUtils.createStyledTextField("Contact Number");
        emailField = UIUtils.createStyledTextField("Email");
        passwordField = UIUtils.createStyledPasswordField();
        
        String[] genders = { "Male", "Female", "Other" };
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(UIUtils.MAIN_FONT);
        genderComboBox.setBackground(Color.WHITE);

        submitButton = new UIUtils.RoundedButton("Register Now", 15, UIUtils.PRIMARY_COLOR);
        backButton = new UIUtils.RoundedButton("Back to Login", 15, Color.WHITE);
        backButton.setForeground(UIUtils.PRIMARY_COLOR);
        backButton.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1));

        submitButton.addActionListener(this);
        backButton.addActionListener(this);

        // Add components to card
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        addFormField(card, "Full Name:", nameField);
        addFormField(card, "Age:", ageField);
        
        JLabel gLabel = new JLabel("Gender:");
        gLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        card.add(gLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(genderComboBox);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        addFormField(card, "Contact Number:", contactField);
        addFormField(card, "Email Address:", emailField);
        addFormField(card, "Password:", passwordField);

        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(submitButton);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(backButton);

        mainPanel.add(card);
        add(mainPanel);

        setVisible(true);
    }

    private void addFormField(JPanel panel, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (e.getSource() == submitButton) {
            // Validate the fields
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                return;
            }

            if (!name.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(this, "Name should contain only alphabets and spaces!");
                return;
            }

            // Age validation
            int age = -1;
            try {
                age = Integer.parseInt(ageStr);
                if (age <= 0 || age > 120) {
                    JOptionPane.showMessageDialog(this, "Age must be between 1 and 120!");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for age!");
                return;
            }

            // Validate other fields
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email and Password cannot be empty!");
                return;
            }

            if (registerPatient(name, age, gender, contact, email, password)) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                dispose();
                new AuthenticationPage();
            } else {
                JOptionPane.showMessageDialog(this, "Error! Email might be already registered.");
            }
        } else if (e.getSource() == backButton) {
            dispose();
            new AuthenticationPage();
        }
    }

    private boolean registerPatient(String name, int age, String gender, String contact, String email, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO patients (name, age, gender, contact, email, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setString(3, gender);
            pst.setString(4, contact);
            pst.setString(5, email);
            pst.setString(6, password);

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        new RegistrationPage();
    }
}
