import java.awt.*;
import javax.swing.*;

public class MainForm extends JFrame {
    public MainForm() {
        setTitle("Recipe and Ingredient Management System");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //center

        // Show the login panel initially
        setContentPane(new LoginPanel(this));
        revalidate();  
    }

    public void showMainTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Apply light blue background to the tabbed pane
        tabbedPane.setBackground(new Color(150, 105, 225));  // Royal Blue


        tabbedPane.addTab("Recipe", new RecipePanel());
        tabbedPane.addTab("Ingredient", new IngredientPanel());
        tabbedPane.addTab("Nutrition", new NutritionPanel());
        tabbedPane.addTab("Allergy", new AllergyPanel());
        tabbedPane.addTab("Search", new SearchPanel());

        setContentPane(tabbedPane);
        revalidate();
        
    }

    public static void main(String[] args) {
        MainForm mf = new MainForm();
        mf.setVisible(true);
    }
}
