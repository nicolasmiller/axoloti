package components.processing;

import axoloti.processing.PComponent;
import processing.core.PApplet;

public class PLabelComponent extends PComponent {
    private String text;
    
    public PLabelComponent(PApplet p) {
            super(p);
    }
        
    public void setText(String text) {
        this.text = text;
    }    
}