/**
 * Created by benas on 17.11.6.
 */
import java.awt.Graphics;
import java.awt.Image;

public class Sprite {
    private Image image;


    public Sprite(Image pImage) {
        this.image = pImage;
    }


    public int getWidth() {
        return this.image.getWidth(null);
    }


    public int getHeight() {
        return this.image.getHeight(null);
    }


    public void draw(Graphics gSurface, int x, int y) {
        gSurface.drawImage(this.image, x, y, null);
    }
}
