import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginPanel(MainForm mainForm) {
        // Set layout to BorderLayout
        setLayout(new BorderLayout());

        // Set background image
        JLabel backgroundLabel = new JLabel(new ImageIcon("asset/bg1.png"));
        add(backgroundLabel);

        // Create a panel for form elements
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);  // Make the form panel transparent to show the background image

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo
        JLabel logoLabel = new JLabel(new ImageIcon("asset/logo3.png"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(logoLabel, gbc);

        // Username icon and label
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        usernamePanel.setOpaque(false);  // Make the panel transparent
        usernamePanel.add(new JLabel(new ImageIcon("asset/username.png")));
        usernamePanel.add(new JLabel("Username"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernamePanel, gbc);

        // Username field
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtUsername, gbc);

        // Password icon and label
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        passwordPanel.setOpaque(true);  // Make the panel transparent
        passwordPanel.add(new JLabel(new ImageIcon("asset/password.png")));
        passwordPanel.add(new JLabel("Password"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordPanel, gbc);

        // Password field
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(txtPassword, gbc);

        // Login button
        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(30, 144, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Times New Roman", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(btnLogin, gbc);

        // Add the form panel to the background label
        backgroundLabel.setLayout(new GridBagLayout());
        backgroundLabel.add(formPanel);

        // Handle login button click
        btnLogin.addActionListener(e -> handleLogin(mainForm));
    }

    private void handleLogin(MainForm mainForm) {
        String username = txtUsername.getText().trim();//remove space
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Use the connection once
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                mainForm.showMainTabbedPane();  // Show the main panel
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}