package edu.siren.gui;

import java.io.IOException;

import edu.siren.core.geom.Rectangle;
import edu.siren.renderer.Font;

public class Text extends Element {
    class TextState {
        public String text;
        public Font font;
        public int fontScaling;
        public Rectangle padding = new Rectangle(0, 0, 0, 0);
    };
    
    public TextState textState;
    
    public Text(String text, int scaling, String font) throws IOException {
        textState = new TextState();
        textState.font = new Font(font);
        textState.fontScaling = scaling;
        textState.text = text;
        state.name = "Text";
    }

    public Text(String text, int scaling) throws IOException {
        this(text, scaling, "res/tests/fonts/proggy.png");
    }
    
    public Text(String text) throws IOException {
        this(text, 1);
    }
    
    public void padding(float tb, float lr) {
        xywh(x() + textState.padding.width / 2.0f,
             y() + textState.padding.height / 2.0f,
             w() - textState.padding.width,
             h() - textState.padding.height);

        textState.padding.width = lr;
        textState.padding.height = tb;

        xywh(x() - textState.padding.width / 2.0f,
             y() - textState.padding.height / 2.0f,
             w() + textState.padding.width,
             h() + textState.padding.height);
    }
    
    @Override
	public boolean boundsCheck(float mx, float my) {
        float lineHeight = textState.font.lineHeight();
        float offsetY = h() - lineHeight;

        // Factor in offset and bounds
        float x = realX() - textState.padding.width / 2.0f;
        float y = realY() - offsetY + textState.padding.height / 2.0f;

        // Check the current node
        float t = y + h(), b = y;
        float l = x, r = x + w();
        
        // If we're not in the bounding box, then just fail
        return mx > l && mx < r && my > b && my < t;
    }
    
    @Override
	public void drawBackground(float x, float y) {
        // Only draw the background if it is specified
        if (this.state.background != null) {
            float lineHeight = textState.font.lineHeight();
            float offsetY = h() - lineHeight;
            this.state.background.draw(x - textState.padding.width / 2.0f, 
                                       y - offsetY + textState.padding.height / 2.0f);
        }
    }

    
    @Override
	public void draw() {
        super.draw();

        if (hidden())
            return;
        
        textState.font.print(textState.text, textState.fontScaling, 
                             realX(), realY());
        
        Rectangle rect = textState.font.bounds;
        dimensions(rect.width + textState.padding.width,
             rect.height + textState.padding.height);
    }

    public void fontScaling(int scaling) {
        textState.fontScaling = scaling;
        textState.font.invalidate();
    }
    
    public int fontScaling() {
        return textState.fontScaling;
    }
    
    public void text(String textual) {
        textState.text = textual;
        textState.font.invalidate();
    }
    
    public void fontColor(float r, float g, float b) {
        textState.font.color(r, g, b, 1.0f);
    }
    
    public void hoverColor(final float r, final float g, final float b) {
        this.onMouseEnter(new ElementEvent() {
            public boolean event(Element e) {
                try {
                    Text text = (Text) e;
                    text.background("res/game/gui/black.png");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return false;
            }
        });
        
        this.onMouseExit(new ElementEvent() {
             public boolean event(Element e) {
                Text text = (Text) e;
                text.state.background = null;
                return false;
            }
        });
    }
}
