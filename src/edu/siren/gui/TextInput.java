package edu.siren.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import edu.siren.core.geom.Rectangle;
import edu.siren.renderer.Font;

public class TextInput extends Element {
    class TextState {
        public String text;
        public Font font;
        public int fontScaling;
        public Rectangle padding = new Rectangle(0, 0, 0, 0);
        public boolean focused = false;
        public int maxLength = 24;
        public char nextChar;
    };
    
    public TextState textState;
    
    class TextInputEvents {
        public ArrayList<ElementEvent> keyPress, submit;
        public TextInputEvents() {
            keyPress = new ArrayList<ElementEvent>();
            submit = new ArrayList<ElementEvent>();
        }
    };
    
    public TextInputEvents textEvents = new TextInputEvents();
    
    public void onKeyPress(ElementEvent event) {
        textEvents.keyPress.add(event);
    }
    
    public void onSubmit(ElementEvent event) {
        textEvents.submit.add(event);
    }
    
    public TextInput() throws IOException {
        textState = new TextState();
        textState.font = new Font("res/tests/fonts/proggy.png");
        textState.fontScaling = 1;
        textState.text = "";
        state.name = "TextInput";
        maxLength(textState.maxLength);
    }
    
    @Override
	public void draw() {
        super.draw();

        if (hidden())
            return;
        
        textState.font.print(textState.text, textState.fontScaling, 
                             realX() + textState.padding.height / 2.0f, 
                             realY() + textState.padding.width / 2.0f);
    }
    
    public void maxLength(int chars) {
        textState.maxLength = chars;
        w((textState.font.squareW / textState.fontScaling) * textState.maxLength);
        h((textState.font.squareH / textState.fontScaling) * 1.5f);
        padding(textState.padding.height, textState.padding.width);
    }

    public void fontScaling(int scaling) {
        textState.fontScaling = scaling;
        textState.font.invalidate();
        h((textState.font.squareH / textState.fontScaling) * 1.5f);
        padding(textState.padding.height, textState.padding.width);
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
    
    public int fontScaling() {
        return textState.fontScaling;
    }
    
    public void text(String textual) {
        textState.text = textual;
        textState.font.invalidate();
    }
    
    @Override
    public boolean checkEvents(float mx, float my, float mdx, float mdy, 
                               boolean click)
    {
        boolean invalid = super.checkEvents(mx, my, mdx, mdy, click);
        if (invalid) return true;
        
        boolean inBounds = boundsCheck(mx, my);
        if (inBounds && click) {
            textState.focused = true;
        } else if (!inBounds && click) {
            textState.focused = false;
        } 
        
        if (!textState.focused)
            return false;
        
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_BACK) {
                    if (textState.text.length() > 0)
                        textState.text = textState.text.substring(0, textState.text.length() - 1);
                } else if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
                    for (ElementEvent event : textEvents.submit) {
                        event.event(this);
                    }
                } else if (Keyboard.getEventKey() == Keyboard.KEY_LSHIFT) {
                    // blah
                } else {
                    boolean draw = true;
                    textState.nextChar = Keyboard.getEventCharacter();
                    for (ElementEvent event : textEvents.keyPress) {
                        draw &= !event.event(this);
                    }
                    if (draw && textState.text.length() < textState.maxLength)
                        textState.text += textState.nextChar;
                }
                textState.font.invalidate();
            }
        }
        
        return false;
    }

    public String text() {
        return textState.text;
    }

    public void fontColor(float r, float g, float b) {
        textState.font.color(r, g, b, 1.0f);
    }
}
