import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.jupiter.api.Assertions.*;

class WeatherCheckViewTest {

    private WeatherCheckView view;

    @BeforeEach
    void setUp() {
        DisplayModel model = new DisplayModel();
        view = new WeatherCheckView(model);
    }

    @Test
    void testTextFieldInitialization() {
        JTextField textField = view.getTextField();
        assertNotNull(textField, "TextField should not be null.");
        assertEquals(20, textField.getColumns(), "TextField should have 20 columns.");
    }

    @Test
    void testCariButtonInitialization() {
        JButton cariButton = view.getCari();
        assertNotNull(cariButton, "Cari button should not be null.");
        assertEquals("Cari", cariButton.getText(), "Cari button should have the correct label.");
    }

    @Test
    void testBackButtonInitialization() {
        JButton backButton = view.getBack();
        assertNotNull(backButton, "Back button should not be null.");
        assertEquals("Kembali", backButton.getText(), "Back button should have the correct label.");
    }

    @Test
    void testBackButtonAction() {
        JFrame frame = new JFrame();
        JButton backButton = new JButton("Back");

        frame.add(backButton);
        frame.setSize(300, 200);
        frame.setVisible(true);

        backButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(backButton);
            if (window != null) {
                window.dispose();
            }
        });

        backButton.doClick();

    }
}
