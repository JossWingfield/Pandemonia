package utility.GUI;
import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.BitmapFont;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
public class TextBox {
    private int x, y, width, height;
    private String text = "";
    private double caretBlinkCounter = 0;
    private double caretBlinkSpeed = 0.5;
    private int maxChars; // -1 = unlimited
    private int scrollOffset = 0; // horizontal scroll offset in pixels
    
    private TextureRegion typeSpace, typeSpaceHover;
    private TextureRegion leftBorder, leftBorderHover;
    private TextureRegion rightBorder, rightBorderHover;
    private BitmapFont font;
    private GamePanel gp;
    private Colour colour;

    // Constructor WITH max character limit
    public TextBox(GamePanel gp, int x, int y, int width, int height, BitmapFont font, Colour textColour, int maxChars) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.font = font;
        this.colour = textColour;
        this.maxChars = maxChars;
        loadTextures();
    }

    private void loadTextures() {
        typeSpace = importImage("/UI/TextSpace.png").getSubimage(1, 0, 44, 10);
        typeSpaceHover = importImage("/UI/TextSpace.png").getSubimage(47, 0, 44, 10);
        leftBorder = importImage("/UI/TextSpace.png").getSubimage(0, 0, 1, 10);
        leftBorderHover = importImage("/UI/TextSpace.png").getSubimage(46, 0, 1, 10);
        rightBorder = importImage("/UI/TextSpace.png").getSubimage(45, 0, 1, 10);
        rightBorderHover = importImage("/UI/TextSpace.png").getSubimage(91, 0, 1, 10);
    }

    protected Texture importImage(String filePath) {
        Texture texture = AssetPool.getTexture(filePath);
        return texture;
    }

    // ---------------- UPDATE ----------------
    public void update(double delta) {
        if (gp.mouseL.mouseButtonDown(0)) {
            active = isMouseInside();
        }
        caretBlinkCounter += delta;
        if (caretBlinkCounter >= caretBlinkSpeed * 2) {
            caretBlinkCounter = 0;
        }
    }

    // ---------------- DRAW ----------------
    public void draw(Renderer renderer) {
        TextureRegion center;
        TextureRegion left;
        TextureRegion right;
        if (isMouseInside() || active) {
            center = typeSpaceHover;
            left = leftBorderHover;
            right = rightBorderHover;
        } else {
            center = typeSpace;
            left = leftBorder;
            right = rightBorder;
        }

        renderer.draw(left, x, y, 1 * 5, height);
        renderer.draw(center, x + 5, y, width - 10, height);
        renderer.draw(right, x + width - 5, y, 1 * 5, height);

        // Inner text area bounds
        int textAreaX = x + 10;
        int textAreaWidth = width - 20;
        int textY = y + height - 11;

        // Recalculate scroll so caret is always visible
        int fullTextWidth = (int) font.getTextWidth(text);
        int caretXInText = fullTextWidth; // caret is always at end of text
        // Scroll right if caret runs past the visible area
        if (caretXInText - scrollOffset > textAreaWidth) {
            scrollOffset = caretXInText - textAreaWidth;
        }
        // Scroll left if text shrinks back (e.g. backspace)
        if (caretXInText - scrollOffset < 0) {
            scrollOffset = caretXInText;
        }
        // Never scroll into negative territory
        if (scrollOffset < 0) scrollOffset = 0;

        // Clip and draw the visible portion of text by finding the visible substring
        // Walk through the text to find which characters fall in the visible window
        String visibleText = getVisibleText(textAreaWidth);
        int visibleTextStartX = getVisibleTextStartX(textAreaWidth);

        renderer.setFont(font);
        renderer.setColour(colour);
        renderer.drawString(visibleText, textAreaX + visibleTextStartX, textY);

        // Caret — drawn relative to scroll
        if (active && caretBlinkCounter < caretBlinkSpeed) {
            int caretScreenX = textAreaX + (caretXInText - scrollOffset);
            // Only draw caret if it's within the text area
            if (caretScreenX >= textAreaX && caretScreenX <= textAreaX + textAreaWidth) {
                renderer.fillRect(caretScreenX, y + 10, 2, height - 20);
            }
        }
    }

    /**
     * Returns the substring of text that fits within the visible text area,
     * accounting for the current scrollOffset.
     */
    private String getVisibleText(int textAreaWidth) {
        if (text.isEmpty()) return "";
        int start = 0;
        int end = text.length();
        // Find the first character visible after scrollOffset
        int accumulated = 0;
        for (int i = 0; i < text.length(); i++) {
            int charWidth = (int) font.getTextWidth(String.valueOf(text.charAt(i)));
            if (accumulated + charWidth > scrollOffset) {
                start = i;
                break;
            }
            accumulated += charWidth;
        }
        // Find the last character that fits within textAreaWidth from start
        int visibleWidth = 0;
        int startPixel = (int) font.getTextWidth(text.substring(0, start));
        for (int i = start; i < text.length(); i++) {
            int charWidth = (int) font.getTextWidth(String.valueOf(text.charAt(i)));
            visibleWidth += charWidth;
            if (startPixel + visibleWidth - scrollOffset > textAreaWidth) {
                end = i;
                break;
            }
        }
        return text.substring(start, end);
    }

    /**
     * Returns the pixel offset within the text area at which to start drawing
     * the visible text (accounts for partial character scroll).
     */
    private int getVisibleTextStartX(int textAreaWidth) {
        if (text.isEmpty()) return 0;
        int accumulated = 0;
        for (int i = 0; i < text.length(); i++) {
            int charWidth = (int) font.getTextWidth(String.valueOf(text.charAt(i)));
            if (accumulated + charWidth > scrollOffset) {
                // This char is the first visible one; it may be partially scrolled
                return accumulated - scrollOffset;
            }
            accumulated += charWidth;
        }
        return 0;
    }

    // ---------------- INPUT ----------------
    public void onCharTyped(char c) {
        if (!active) return;
        if (c == '\b') {
            if (text.length() > 0) {
                text = text.substring(0, text.length() - 1);
            }
        } else if (c >= 32 && c <= 126) {
            // Enforce max character limit
            if (maxChars < 0 || text.length() < maxChars) {
                text += c;
            }
        }
    }

    // ---------------- HELPERS ----------------
    private boolean isMouseInside() {
        int mx = (int) gp.mouseL.getScreenX();
        int my = (int) gp.mouseL.getScreenY();
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    private boolean active = false;

    public void setActive(boolean active) {
        this.active = active;
    }

    // ---------------- GETTERS / SETTERS ----------------
    public String getText() { return text; }

    public void setText(String text) {
        // Clamp to maxChars if set
        if (maxChars >= 0 && text.length() > maxChars) {
            this.text = text.substring(0, maxChars);
        } else {
            this.text = text;
        }
        scrollOffset = 0;
    }

    public boolean isActive() { return active; }

    public int getMaxChars() { return maxChars; }

    public void setMaxChars(int maxChars) { this.maxChars = maxChars; }
}