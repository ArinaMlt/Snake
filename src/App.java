import java.awt.GraphicsEnvironment;

public class App {
    public static void main(String[] args) throws Exception {
        // System.out.println("Hello, World!");
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (String fontName : fontNames) {
            System.out.println(fontName);
        }
    }
}
