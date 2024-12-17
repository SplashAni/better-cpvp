package splash.dev.ui.hud;

import net.minecraft.client.gui.DrawContext;

public abstract class HudElement {
    public int x, y;
    int width, height;
    float scale;
    boolean dragging, visible;

    public HudElement() {
        dragging = false;
        visible = true;
        scale = 1.0f;
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }


    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void toggle() {
        this.visible = !visible;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}