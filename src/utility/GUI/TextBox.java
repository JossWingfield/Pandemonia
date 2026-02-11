package utility.GUI;

import main.GamePanel;
import main.renderer.BitmapFont;
import main.renderer.Colour;
import main.renderer.Renderer;

public class TextBox {

    private int x, y, width, height;
    private String text = "";

    private double caretBlinkCounter = 0;
    private double caretBlinkSpeed = 0.5; // seconds

    private BitmapFont font;
    private GamePanel gp;

    public TextBox(GamePanel gp, int x, int y, int width, int height, BitmapFont font) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.font = font;
    }

    // ---------------- UPDATE ----------------
    public void update(double delta) {
    	
        // Mouse click → focus
        if (gp.mouseL.mouseButtonDown(0)) {
            active = isMouseInside();
        }

        // Caret blink
        caretBlinkCounter += delta;
        if (caretBlinkCounter >= caretBlinkSpeed * 2) {
            caretBlinkCounter = 0;
        }
    }
    
    // ---------------- DRAW ----------------
    public void draw(Renderer renderer) {

        // Box background
        renderer.setColour(new Colour(0, 0, 0, 180));
        renderer.fillRect(x, y, width, height);

        renderer.setColour(Colour.BLACK);
        renderer.fillRect(x, y, width, height);

        // Text
        renderer.setFont(font);
        renderer.setColour(Colour.WHITE);
        renderer.drawString(text, x + 10, y + height - 15);

        // Caret
        if (active && caretBlinkCounter < caretBlinkSpeed) {
            int caretX = x + 10 + (int)font.getTextWidth(text);
            renderer.fillRect(caretX, y + 10, 2, height - 20);
        }
    }

    // ---------------- INPUT ----------------
    public void onCharTyped(char c) {
        if (!active) return;

        if (c == '\b') {
            if (text.length() > 0) {
                text = text.substring(0, text.length() - 1);
            }
        } else if (c >= 32 && c <= 126) {
            text += c;
        }
    }

    // ---------------- HELPERS ----------------
    private boolean isMouseInside() {
        int mx = (int)gp.mouseL.getWorldX();
        int my = (int)gp.mouseL.getWorldY();
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }
    private boolean active = false;public void setActive(boolean active) {
		this.active = active;
	}
    // ---------------- GETTERS ----------------
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isActive() {
        return active;
    }
}

