import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class IngredientPanel extends JPanel {
    private JTextField txtName, txtCategory, txtUnit;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnViewIngredients, btnBack;
    private JTable table;
    private DefaultTableModel model;
    private boolean showingRecipeView = false;

    public IngredientPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Light blue background
        
        // Create input panel for Ingredient Management
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Manage Ingredients"));
        inputPanel.setBackground(new Color(95, 187, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Labels and fields
        txtName = createLabeledField("Ingredient Name:", inputPanel, gbc, 0);
        txtCategory = createLabeledField("Category:", inputPanel, gbc, 1);
        txtUnit = createLabeledField("Unit:", inputPanel, gbc, 2);

        // Buttons
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnViewIngredients = new JButton("View Ingredients");
        btnBack = new JButton("Back");

        // Set button colors for better UI
        btnAdd.setBackground(new Color(144, 238, 144));
        btnUpdate.setBackground(new Color(255, 255, 102));
        btnDelete.setBackground(new Color(255, 99, 71));
        btnClear.setBackground(new Color(255, 182, 193));
        btnViewIngredients.setBackground(new Color(173, 216, 230));
        btnBack.setBackground(new Color(211, 211, 211));

        // Add buttons to input panel with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        inputPanel.add(btnAdd, gbc);

        gbc.gridx = 1;
        inputPanel.add(btnUpdate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(btnDelete, gbc);

        gbc.gridx = 1;
        inputPanel.add(btnClear, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        inputPanel.add(btnViewIngredients, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(btnBack, gbc);

        // Table to display results
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(table);

        // Add components to the panel
        add(inputPanel, BorderLayout.WEST);
        add(tableScroll, BorderLayout.CENTER);

        // Load ingredients initially
        loadIngredients();

        // Event listeners
        btnAdd.addActionListener(e -> addIngredient());
        btnUpdate.addActionListener(e -> updateIngredient());
        btnDelete.addActionListener(e -> deleteIngredient());
        btnClear.addActionListener(e -> clearFields());
        btnViewIngredients.addActionListener(e -> loadRecipesWithIngredients());
        btnBack.addActionListener(e -> loadIngredients());

        // Table row selection listener
        table.getSelectionModel().addListSelectionListener(e -> populateFieldsFromTable());
        
        // Initially show the back button only
        btnBack.setVisible(true);
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

    private void addIngredient() {
        // Validate the input fields
        String name = txtName.getText().trim();
        String category = txtCategory.getText().trim();
        String unit = txtUnit.getText().trim();
    
        if (name.isEmpty() || category.isEmpty() || unit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Ingredient (ing_name, category, unit) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setString(3, unit);
    
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Ingredient added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadIngredients();  // Refresh the ingredient list
                clearFields();  // Clear the input fields
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add the ingredient.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding ingredient: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateIngredient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an ingredient to update.");
            return;
        }

        int ingId = (int) model.getValueAt(selectedRow, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Ingredient SET ing_name = ?, category = ?, unit = ? WHERE ing_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtName.getText());
            stmt.setString(2, txtCategory.getText());
            stmt.setString(3, txtUnit.getText());
            stmt.setInt(4, ingId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ingredient updated successfully!");
            loadIngredients();
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating ingredient: " + ex.getMessage());
        }
    }

    private void deleteIngredient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an ingredient to delete.");
            return;
        }

        int ingId = (int) model.getValueAt(selectedRow, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Ingredient WHERE ing_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ingId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ingredient deleted successfully!");
            loadIngredients();
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting ingredient: " + ex.getMessage());
        }
    }

    private void loadIngredients() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Ingredient";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);  // Clear previous rows
            model.setColumnIdentifiers(new Object[]{"ID", "Name", "Category", "Unit"});

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ing_id"),
                    rs.getString("ing_name"),
                    rs.getString("category"),
                    rs.getString("unit")
                });
            }
            btnBack.setVisible(false);
            showingRecipeView = false;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading ingredients: " + ex.getMessage());
        }
    }

    private void loadRecipesWithIngredients() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM RecipeIngredientsView";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);  // Clear previous rows
            model.setColumnIdentifiers(new Object[]{"Recipe ID", "Recipe Name", "Ingredient", "Quantity", "Unit"});

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("rec_id"),
                    rs.getString("rec_name"),
                    rs.getString("ing_name"),
                    rs.getInt("quantity"),
                    rs.getString("unit")
                });
            }

            btnBack.setVisible(true);
            showingRecipeView = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading recipes with ingredients: " + ex.getMessage());
        }
    }

    private void populateFieldsFromTable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1 && !showingRecipeView) {
            txtName.setText(model.getValueAt(selectedRow, 1).toString());
            txtCategory.setText(model.getValueAt(selectedRow, 2).toString());
            txtUnit.setText(model.getValueAt(selectedRow, 3).toString());
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtCategory.setText("");
        txtUnit.setText("");
    }
}
