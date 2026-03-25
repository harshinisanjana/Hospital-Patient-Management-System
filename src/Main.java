import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        // Set look and feel to match system
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch the application with the Authentication Page
        SwingUtilities.invokeLater(() -> new AuthenticationPage());
    }
}