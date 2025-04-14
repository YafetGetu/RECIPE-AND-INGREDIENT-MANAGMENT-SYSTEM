import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchPanel extends JPanel {
    private JTextField idField, nameField, cookingTimeField, caloriesField, priceField;
    private JButton searchButton, clearButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public SearchPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Light blue background

        // Create input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Search Recipes"));
        inputPanel.setBackground(new Color(224, 255, 255)); // Light cyan background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Labels and fields
        idField = createLabeledField("Recipe ID:", inputPanel, gbc, 0);
        nameField = createLabeledField("Recipe Name:", inputPanel, gbc, 1);
        cookingTimeField = createLabeledField("Max Cooking Time (min):", inputPanel, gbc, 2);
        caloriesField = createLabeledField("Max Calories:", inputPanel, gbc, 3);
        priceField = createLabeledField("Max Price ($):", inputPanel, gbc, 4);

        // Search and Clear Buttons
        searchButton = new JButton("Search Recipes");
        searchButton.setBackground(new Color(144, 238, 144)); // Light green
        searchButton.addActionListener(e -> searchRecipes());

        clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(255, 182, 193)); // Light pink
        clearButton.addActionListener(e -> clearFields());

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        inputPanel.add(searchButton, gbc);

        gbc.gridx = 1;
        inputPanel.add(clearButton, gbc);

        // Table to display results
        tableModel = new DefaultTableModel();
        resultsTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(resultsTable);

        // Add components to the panel
        add(inputPanel, BorderLayout.WEST);
        add(tableScroll, BorderLayout.CENTER);
    }

    private JTextField createLabeledField(String labelText, JPanel panel, GridBagConstraints gbc, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        JTextField textField = new JTextField(15);
        panel.add(textField, gbc);
        return textField;
    }

    private void searchRecipes() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT rec_id, rec_name, cooking_time, price FROM Recipe WHERE 1=1";
            PreparedStatement stmt;
            
            if (!idField.getText().trim().isEmpty()) {
                sql += " AND rec_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(idField.getText().trim()));
            } else if (!nameField.getText().trim().isEmpty()) {
                sql += " AND rec_name LIKE ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, "%" + nameField.getText().trim() + "%");
            } else if (!cookingTimeField.getText().trim().isEmpty()) {
                sql += " AND cooking_time <= ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(cookingTimeField.getText().trim()));
            } else if (!caloriesField.getText().trim().isEmpty()) {
                sql += " AND rec_id IN (SELECT ri.rec_id FROM Recipe_Ingredient ri JOIN Ingredient i ON ri.ing_id = i.ing_id JOIN Nutrition n ON i.ing_id = n.ing_id GROUP BY ri.rec_id HAVING SUM(n.calories) <= ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(caloriesField.getText().trim()));
            } else if (!priceField.getText().trim().isEmpty()) {
                sql += " AND price <= ?";
                stmt = conn.prepareStatement(sql);
                stmt.setDouble(1, Double.parseDouble(priceField.getText().trim()));
            } else {
                stmt = conn.prepareStatement(sql);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            // Update table
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            tableModel.addColumn("ID");
            tableModel.addColumn("Recipe Name");
            tableModel.addColumn("Cooking Time (min)");
            tableModel.addColumn("Price ($)");

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("rec_id"),
                    rs.getString("rec_name"),
                    rs.getInt("cooking_time"),
                    rs.getDouble("price")
                });
            }
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        cookingTimeField.setText("");
        caloriesField.setText("");
        priceField.setText("");
    }
}
