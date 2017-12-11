/**
 * Created by benas on 17.11.6.
 */
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class SpriteStore {
    private static SpriteStore instance;

    private Map<String, Sprite> sprites;


    private SpriteStore() {
        this.sprites = new HashMap<String, Sprite>();
    }


    public static SpriteStore getInstance() {
        if (instance == null) {
            instance = new SpriteStore();
        }

        return instance;
    }


    public Sprite getSprite(String path) {
        if (this.sprites.get(path) != null) {
            return this.sprites.get(path);
        }

        BufferedImage sourceImage = null;
        try {
            URL url = this.getClass().getClassLoader().getResource(path);
            if (url == null) {
                JOptionPane.showMessageDialog(null,
                        JOptionPane.ERROR_MESSAGE);

                System.exit(0);
            }

            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    JOptionPane.ERROR_MESSAGE);

            if (Game.DEBUG_ENABLED) {
                e.printStackTrace();
            }

            System.exit(0);
        }

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);
        image.getGraphics().drawImage(sourceImage, 0, 0, null);

        Sprite sprite = new Sprite(image);
        this.sprites.put(path, sprite);

        return sprite;
    }
}