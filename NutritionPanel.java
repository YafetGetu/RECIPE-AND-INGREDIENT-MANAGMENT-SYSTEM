

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class NutritionPanel extends JPanel {
    private JTextField txtIngId, txtCalories, txtProtein, txtFat, txtCarbohydrates, txtFiber, txtSugar;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnHighCalories, btnBack;
    private JTable table;
    private DefaultTableModel model;

    public NutritionPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Manage Nutrition"));
        inputPanel.setBackground(new Color(224, 200, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtIngId = createLabeledField("Ingredient ID:", inputPanel, gbc, 0);
        txtCalories = createLabeledField("Calories:", inputPanel, gbc, 1);
        txtProtein = createLabeledField("Protein:", inputPanel, gbc, 2);
        txtFat = createLabeledField("Fat:", inputPanel, gbc, 3);
        txtCarbohydrates = createLabeledField("Carbohydrates:", inputPanel, gbc, 4);
        txtFiber = createLabeledField("Fiber:", inputPanel, gbc, 5);
        txtSugar = createLabeledField("Sugar:", inputPanel, gbc, 6);

        // Buttons
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnHighCalories = new JButton("High Calories");
        btnBack = new JButton("Back");

        btnAdd.setBackground(new Color(144, 238, 144));
        btnUpdate.setBackground(new Color(255, 255, 102));
        btnDelete.setBackground(new Color(255, 99, 71));
        btnClear.setBackground(new Color(255, 182, 193));
        btnHighCalories.setBackground(new Color(173, 216, 230));
        btnBack.setBackground(new Color(211, 211, 211));

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
        
        inputPanel.add(btnHighCalories, gbc);
        gbc.gridy = 9;
        gbc.gridx = 1;
        inputPanel.add(btnBack, gbc);

        // Table
        model = new DefaultTableModel();
        table = new JTable(model);
        model.setColumnIdentifiers(new Object[]{"Nutrition ID", "Ingredient ID", "Calories", "Protein", "Fat", "Carbohydrates", "Fiber", "Sugar"});
        JScrollPane tableScroll = new JScrollPane(table);

        add(inputPanel, BorderLayout.WEST);
        add(tableScroll, BorderLayout.CENTER);

        loadNutritionData();

        // Event listeners
        btnAdd.addActionListener(e -> addNutrition());
        btnUpdate.addActionListener(e -> updateNutrition());
        btnDelete.addActionListener(e -> deleteNutrition());
        btnClear.addActionListener(e -> clearFields());
        btnHighCalories.addActionListener(e -> showHighCalories());
        btnBack.addActionListener(e -> loadNutritionData());
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

    private void addNutrition() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Nutrition (ing_id, calories, protein, fat, carbohydrates, fiber, sugar) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtIngId.getText()));
            stmt.setFloat(2, Float.parseFloat(txtCalories.getText()));
            stmt.setFloat(3, Float.parseFloat(txtProtein.getText()));
            stmt.setFloat(4, Float.parseFloat(txtFat.getText()));
            stmt.setFloat(5, Float.parseFloat(txtCarbohydrates.getText()));
            stmt.setFloat(6, Float.parseFloat(txtFiber.getText()));
            stmt.setFloat(7, Float.parseFloat(txtSugar.getText()));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Nutrition entry added successfully!");
            loadNutritionData();
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error adding nutrition entry: " + ex.getMessage());
        }
    }

    private void updateNutrition() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a nutrition entry to update.");
            return;
        }

        int nutId = (int) model.getValueAt(selectedRow, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Nutrition SET ing_id = ?, calories = ?, protein = ?, fat = ?, carbohydrates = ?, fiber = ?, sugar = ? WHERE nut_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtIngId.getText()));
            stmt.setFloat(2, Float.parseFloat(txtCalories.getText()));
            stmt.setFloat(3, Float.parseFloat(txtProtein.getText()));
            stmt.setFloat(4, Float.parseFloat(txtFat.getText()));
            stmt.setFloat(5, Float.parseFloat(txtCarbohydrates.getText()));
            stmt.setFloat(6, Float.parseFloat(txtFiber.getText()));
            stmt.setFloat(7, Float.parseFloat(txtSugar.getText()));
            stmt.setInt(8, nutId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Nutrition entry updated successfully!");
            loadNutritionData();
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error updating nutrition entry: " + ex.getMessage());
        }
    }

    private void deleteNutrition() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a nutrition entry to delete.");
            return;
        }

        int nutId = (int) model.getValueAt(selectedRow, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Nutrition WHERE nut_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nutId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Nutrition entry deleted successfully!");
            loadNutritionData();
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting nutrition entry: " + ex.getMessage());
        }
    }

    private void loadNutritionData() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Nutrition";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("nut_id"),
                    rs.getInt("ing_id"),
                    rs.getFloat("calories"),
                    rs.getFloat("protein"),
                    rs.getFloat("fat"),
                    rs.getFloat("carbohydrates"),
                    rs.getFloat("fiber"),
                    rs.getFloat("sugar")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading nutrition data: " + ex.getMessage());
        }
    }
    private void showHighCalories() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Nutrition WHERE calories > ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setFloat(1, 500); // Adjust threshold as needed
            ResultSet rs = stmt.executeQuery();
            
            model.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("nut_id"),
                    rs.getInt("ing_id"),
                    rs.getFloat("calories"),
                    rs.getFloat("protein"),
                    rs.getFloat("fat"),
                    rs.getFloat("carbohydrates"),
                    rs.getFloat("fiber"),
                    rs.getFloat("sugar")
                });
            }
            
            JOptionPane.showMessageDialog(this, "Showing ingredients with high calories!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching high-calorie nutrition data: " + ex.getMessage());
        }
    }
    

    private void clearFields() {
        txtIngId.setText("");
        txtCalories.setText("");
        txtProtein.setText("");
        txtFat.setText("");
        txtCarbohydrates.setText("");
        txtFiber.setText("");
        txtSugar.setText("");
    }
    private void HighCalories(){
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "select * from High_Calorie_Ingredients";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            model.setColumnIdentifiers(new Object[]{"Ingredient Name", "Calories"});
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("ing_name"),
                    rs.getFloat("calories")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading  data: " + ex.getMessage());
        }
    }


    private void populateFieldsFromTable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            txtIngId.setText(model.getValueAt(selectedRow, 1).toString());
            txtCalories.setText(model.getValueAt(selectedRow, 2).toString());
            txtProtein.setText(model.getValueAt(selectedRow, 3).toString());
            txtFat.setText(model.getValueAt(selectedRow, 4).toString());
            txtCarbohydrates.setText(model.getValueAt(selectedRow, 5).toString());
            txtFiber.setText(model.getValueAt(selectedRow, 6).toString());
            txtSugar.setText(model.getValueAt(selectedRow, 7).toString());
        }
    }
}

