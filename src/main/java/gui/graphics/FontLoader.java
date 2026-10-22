package gui.graphics;

import javafx.scene.text.Font;

/**
 * Utility class for loading custom fonts from resources.
 */
public class FontLoader {
    public static Font loadFont(String fontPath, double size) {
        try {
            var stream = FontLoader.class.getResourceAsStream(fontPath);
            if (stream != null) {
                Font font = Font.loadFont(stream, size);
                if (font != null) {
                    return font;
                }
            }
            System.err.println("Font not found: " + fontPath + ", using Arial fallback");
            return Font.font("Arial", size);
        } catch (Exception e) {
            System.err.println("Error loading font: " + fontPath + " - " + e.getMessage());
            return Font.font("Arial", size);
        }
    }
    public static Font getCookieRunBold(double size) {
        return loadFont("/fonts/CookieRun Bold.ttf", size);
    }
}
