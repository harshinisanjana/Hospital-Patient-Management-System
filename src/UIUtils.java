import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class UIUtils {

    // --- COLOR PALETTE ---
    public static final Color PRIMARY_COLOR = new Color(0, 102, 204); // Professional Blue
    public static final Color SECONDARY_COLOR = new Color(240, 245, 255); // Soft BG
    public static final Color ACCENT_COLOR = new Color(255, 107, 107); // Logout/Danger
    public static final Color TEXT_COLOR = new Color(51, 51, 51); // Dark Gray Text
    public static final Color OFF_WHITE = new Color(250, 250, 250);

    // --- FONTS ---
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 22);

    // --- CUSTOM COMPONENTS ---

    // 1. ROUNDED BUTTON
    public static class RoundedButton extends JButton {
        private int radius;
        private Color hoverColor;
        private Color originalColor;

        public RoundedButton(String text, int radius, Color bgColor) {
            super(text);
            this.radius = radius;
            this.originalColor = bgColor;
            this.hoverColor = bgColor.brighter();
            
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setBackground(bgColor);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(originalColor);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // 2. ROUNDED PANEL (CARDS)
    public static class RoundedPanel extends JPanel {
        private int radius;
        private Color shadowColor = new Color(0, 0, 0, 30);

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            setBackground(bgColor);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw Shadow
            g2.setColor(shadowColor);
            g2.fill(new RoundRectangle2D.Double(3, 3, getWidth()-5, getHeight()-5, radius, radius));

            // Draw Card Body
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth()-3, getHeight()-3, radius, radius));
            
            g2.dispose();
        }
    }

    // 3. STYLED TEXT FIELD
    public static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(250, 40));
        field.setFont(MAIN_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    public static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(250, 40));
        field.setFont(MAIN_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
}
