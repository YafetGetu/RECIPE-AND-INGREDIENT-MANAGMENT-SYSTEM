import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class RecipePanel extends JPanel {

    private JTextField txtrecId, txtName, txtDescription, txtCookingTime, txtServings, txtInstructions, txtPrice;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnVegetarianRecipes, btnAllergyFreeRecipes, btnFastingRecipes, btnBack;
    private JTable table;
    private DefaultTableModel model;

    public RecipePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(144, 238, 144)); // Light green background

        // Input Panel (Similar to AllergyPanel)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Manage Recipes"));
        inputPanel.setBackground(new Color(222, 184, 135)); // Light brown (Burlywood)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Add input fields
        txtrecId = createLabeledField("Recipe ID:", inputPanel, gbc, 0);
        txtName = createLabeledField("Recipe Name:", inputPanel, gbc, 1);
        txtDescription = createLabeledField("Description:", inputPanel, gbc, 2);
        txtCookingTime = createLabeledField("Cooking Time:", inputPanel, gbc, 3);
        txtServings = createLabeledField("Servings:", inputPanel, gbc, 4);
        txtInstructions = createLabeledField("Instructions:", inputPanel, gbc, 5);
        txtPrice = createLabeledField("Price:", inputPanel, gbc, 6);

        // Buttons
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnVegetarianRecipes = new JButton("Vegetarian Recipes");
        btnAllergyFreeRecipes = new JButton("Allergy-Free Recipes");
        btnFastingRecipes = new JButton("Fasting Recipes");
        btnBack = new JButton("Back");

        // Set button colors
        btnAdd.setBackground(new Color(144, 238, 144));
        btnUpdate.setBackground(new Color(255, 255, 102));
        btnDelete.setBackground(new Color(255, 99, 71));
        btnClear.setBackground(new Color(255, 182, 193));
        btnVegetarianRecipes.setBackground(new Color(173, 216, 230));
        btnAllergyFreeRecipes.setBackground(new Color(173, 216, 230));
        btnFastingRecipes.setBackground(new Color(173, 216, 230));
        btnBack.setBackground(new Color(211, 211, 211));

        // Add buttons to input panel
        gbc.gridy = 7;
        gbc.gridx = 0;
        inputPanel.add(btnAdd, gbc);
        gbc.gridx = 1;
        inputPanel.add(btnUpdate, gbc);
        gbc.gridy = 8;
        gbc.gridx = 0;
        inputPanel.add(btnDelete, gbc);
        gbc.gridx = 1;
        inputPanel.add(btnClear, gbc);
        gbc.gridy = 9;
        gbc.gridx = 0;
        inputPanel.add(btnVegetarianRecipes, gbc);
        gbc.gridx = 1;
        inputPanel.add(btnAllergyFreeRecipes, gbc);
        gbc.gridy = 10;
        gbc.gridx = 0;
        inputPanel.add(btnFastingRecipes, gbc);
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        inputPanel.add(btnBack, gbc);

        // Table
        model = new DefaultTableModel();
        table = new JTable(model);
        model.setColumnIdentifiers(new Object[]{"ID", "Name", "Description", "Cooking Time", "Servings", "Instructions", "Price"});
        JScrollPane tableScroll = new JScrollPane(table);

        // Add panels to main panel
        add(inputPanel, BorderLayout.WEST);
        add(tableScroll, BorderLayout.CENTER);

        // Load recipes initially
        loadRecipes();

        // Event Listeners
        btnAdd.addActionListener(e -> addRecipe());
        btnUpdate.addActionListener(e -> updateRecipe());
        btnDelete.addActionListener(e -> deleteRecipe());
        btnClear.addActionListener(e -> clearFields());
        btnVegetarianRecipes.addActionListener(e -> loadVegetarianRecipes());
        btnAllergyFreeRecipes.addActionListener(e -> loadAllergyFreeRecipes());
        btnFastingRecipes.addActionListener(e -> loadFastingRecipes());
        btnBack.addActionListener(e -> loadRecipes()); // Reload recipes when back is clicked

        table.getSelectionModel().addListSelectionListener(e -> populateFieldsFromTable());
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

    private void addRecipe() {
        // Implementation to add a recipe
        if ( txtName.getText().isEmpty() || txtDescription.getText().isEmpty() || 
        txtCookingTime.getText().isEmpty() || txtServings.getText().isEmpty() || 
        txtInstructions.getText().isEmpty() || txtPrice.getText().isEmpty()) {
        
        JOptionPane.showMessageDialog(this, "Please fill in all the fields before adding a recipe.");
        return;
     }

    int recId = Integer.parseInt(txtrecId.getText());

    try (Connection conn = DBConnection.getConnection()) {
        // Check if the ID already exists
        String checkSql = "SELECT rec_id FROM Recipe WHERE rec_id = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, recId);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Recipe ID already exists. Please use a different ID.");
            return;
        }

        // Insert the new recipe
        String sql = "INSERT INTO Recipe (rec_id, rec_name, description, cooking_time, servings, instruction, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, recId);
        stmt.setString(2, txtName.getText());
        stmt.setString(3, txtDescription.getText());
        stmt.setInt(4, Integer.parseInt(txtCookingTime.getText()));
        stmt.setInt(5, Integer.parseInt(txtServings.getText()));
        stmt.setString(6, txtInstructions.getText());
        stmt.setDouble(7, Double.parseDouble(txtPrice.getText()));

        stmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Recipe added successfully!");

        loadRecipes();
        clearFields();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error adding recipe: " + ex.getMessage());
        ex.printStackTrace();
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID, Cooking Time, Servings, and Price.");
    }
    }

    private void updateRecipe() {
        // Implementation to update a recipe
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a recipe entry to update.");
            return;
        }

        int recId = (int) model.getValueAt(selectedRow, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Recipe SET rec_id = ?, rec_name = ?, description = ?, cooking_time = ?, servings = ?, instruction = ?, price = ? WHERE rec_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtrecId.getText())); 
            stmt.setString(2, txtName.getText());
            stmt.setString(3, txtDescription.getText());
            stmt.setInt(4, Integer.parseInt(txtCookingTime.getText()));
            stmt.setInt(5, Integer.parseInt(txtServings.getText()));
            stmt.setString(6, txtInstructions.getText());
            stmt.setFloat(7, Float.parseFloat(txtPrice.getText()));
            
            stmt.setInt(8, recId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Recipe entry updated successfully!");
            loadRecipes();
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error updating recipe entry: " + ex.getMessage());
        } 
    }

    private void deleteRecipe() {
        // Implementation to delete a recipe
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) { // means it is not selected
            JOptionPane.showMessageDialog(this, "Please select a Recipe entry to delete.");
            return;
        }

        int recID = (int) model.getValueAt(selectedRow, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Recipe WHERE rec_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, recID);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Recipe entry deleted successfully!");
            loadRecipes();
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting recipe entry: " + ex.getMessage());
        }
    }

    private void loadRecipes() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Recipe";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("rec_id"),
                        rs.getString("rec_name"),
                        rs.getString("description"),
                        rs.getInt("cooking_time"),
                        rs.getInt("servings"),
                        rs.getString("instruction"),
                        rs.getDouble("price")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading recipes: " + ex.getMessage());
        }
    }

    private void loadVegetarianRecipes() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM VegetarianRecipesView";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("rec_id"),
                        rs.getString("rec_name"),
                        rs.getString("description"),
                        rs.getInt("cooking_time"),
                        rs.getInt("servings"),
                        rs.getDouble("price")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading vegetarian recipes: " + ex.getMessage());
        }
    }

    private void loadAllergyFreeRecipes() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM AllergyFreeRecipesView";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("rec_id"),
                        rs.getString("rec_name"),
                        rs.getString("description"),
                        rs.getInt("cooking_time"),
                        rs.getInt("servings"),
                        rs.getDouble("price")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading allergy-free recipes: " + ex.getMessage());
        }
    }

    private void loadFastingRecipes() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM FastingRecipesView";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("rec_id"), rs.getString("rec_name")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading fasting recipes: " + ex.getMessage());
        }
    }
    private void populateFieldsFromTable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) { // Ensure a row is selected
            txtrecId.setText(model.getValueAt(selectedRow, 0).toString());
            txtName.setText(model.getValueAt(selectedRow, 1).toString());
            txtDescription.setText(model.getValueAt(selectedRow, 2).toString());
            txtCookingTime.setText(model.getValueAt(selectedRow, 3).toString());
            txtServings.setText(model.getValueAt(selectedRow, 4).toString());
            txtInstructions.setText(model.getValueAt(selectedRow, 5).toString());
            txtPrice.setText(model.getValueAt(selectedRow, 6).toString());
        }
    }
    

    private void clearFields() {
        txtrecId.setText("");
        txtName.setText("");
        txtDescription.setText("");
        txtCookingTime.setText("");
        txtServings.setText("");
        txtInstructions.setText("");
        txtPrice.setText("");
    }
}
