import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AllergyPanel extends JPanel {
    private JTextField ingIdField, allergNameField, allergDescField;
    private JButton addButton, updateButton, deleteButton, clearButton, recipeAllergenButton, backButton, allergyButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private boolean showingAllergenView = false;

    public AllergyPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
    
        // Create input panel for Allergy Management
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Manage Allergies"));
        inputPanel.setBackground(new Color(94, 250, 194));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
    
        // Labels and fields
        ingIdField = createLabeledField("Ingredient ID:", inputPanel, gbc, 0);
        allergNameField = createLabeledField("Allergy Name:", inputPanel, gbc, 1);
        allergDescField = createLabeledField("Allergy Description:", inputPanel, gbc, 2);
    
        // Buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        recipeAllergenButton = new JButton("Recipe & Allergen");
        allergyButton = new JButton("Allergy");
        backButton = new JButton("Back");
    
        allergyButton.setBackground(new Color(144, 238, 144));
        addButton.setBackground(new Color(144, 238, 144));
        updateButton.setBackground(new Color(255, 255, 102));
        deleteButton.setBackground(new Color(255, 99, 71));
        clearButton.setBackground(new Color(255, 182, 193));
        recipeAllergenButton.setBackground(new Color(173, 216, 230));
        backButton.setBackground(new Color(211, 211, 211));
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        inputPanel.add(addButton, gbc);
    
        gbc.gridx = 1;
        inputPanel.add(updateButton, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(deleteButton, gbc);
    
        gbc.gridx = 1;
        inputPanel.add(clearButton, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        inputPanel.add(recipeAllergenButton, gbc);
    
        
    
        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(backButton, gbc);
    
        // Table to display results
        tableModel = new DefaultTableModel();
        resultsTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(resultsTable);
    
        // Add components to the panel
        add(inputPanel, BorderLayout.WEST);
        add(tableScroll, BorderLayout.CENTER);
    
        // Load allergies initially
        loadAllergies();
    
        // Event listeners
        addButton.addActionListener(e -> addAllergy());
        updateButton.addActionListener(e -> updateAllergy());
        deleteButton.addActionListener(e -> deleteAllergy());
        clearButton.addActionListener(e -> clearFields());
        recipeAllergenButton.addActionListener(e -> showRecipeAllergens());
        
        backButton.addActionListener(e -> loadAllergies());
        
        resultsTable.getSelectionModel().addListSelectionListener(e -> populateFieldsFromTable());
    
        // Set the back button to be visible initially
        backButton.setVisible(true);
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

    private void addAllergy() {
        String ingId = ingIdField.getText();
        String allergName = allergNameField.getText();
        String allergDesc = allergDescField.getText();

        if (ingId.isEmpty() || allergName.isEmpty() || allergDesc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Allergy (ing_id, allerg_name, allerg_desc) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(ingId));
            stmt.setString(2, allergName);
            stmt.setString(3, allergDesc);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Allergy added successfully!");
            loadAllergies();  // Refresh the allergy list
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding allergy: " + ex.getMessage());
        }
    }

    private void updateAllergy() {
        String ingId = ingIdField.getText();
        String allergName = allergNameField.getText();
        String allergDesc = allergDescField.getText();

        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an allergy to update.");
            return;
        }

        if (ingId.isEmpty() || allergName.isEmpty() || allergDesc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.");
            return;
        }

        int allergId = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Allergy SET ing_id = ?, allerg_name = ?, allerg_desc = ? WHERE allerg_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(ingId));
            stmt.setString(2, allergName);
            stmt.setString(3, allergDesc);
            stmt.setInt(4, allergId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Allergy updated successfully!");
            loadAllergies();  // Refresh the allergy list
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating allergy: " + ex.getMessage());
        }
    }

    private void loadAllergies() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Allergy";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);  // Clear previous rows
            tableModel.setColumnIdentifiers(new Object[]{"Allergy ID", "Ingredient ID", "Allergy Name"});

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("allerg_id"), rs.getInt("ing_id"), rs.getString("allerg_name")});
            }
            backButton.setVisible(false);
            showingAllergenView = false;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading allergies: " + ex.getMessage());
        }
    }

    private void showRecipeAllergens() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM RecipeAllergensView";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);  // Clear previous rows
            tableModel.setColumnIdentifiers(new Object[]{"Recipe ID", "Recipe Name", "Allergen Name"});

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("rec_id"), rs.getString("rec_name"), rs.getString("allerg_name")});
            }

            backButton.setVisible(true);
            showingAllergenView = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading recipe allergens: " + ex.getMessage());
        }
    }

    private void populateFieldsFromTable() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow != -1 && !showingAllergenView) {
            ingIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            allergNameField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        }
    }

    private void deleteAllergy() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an allergy to delete.");
            return;
        }

        int allergId = (int) tableModel.getValueAt(selectedRow, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Allergy WHERE allerg_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, allergId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Allergy deleted successfully!");
            loadAllergies();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting allergy: " + ex.getMessage());
        }
    }

    private void clearFields() {
        ingIdField.setText("");
        allergNameField.setText("");
        allergDescField.setText("");
    }
}




