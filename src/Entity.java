/**
 * Created by benas on 17.11.6.
 */
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    protected Sprite sprite;
    protected Game game;
    protected Rectangle me = new Rectangle();
    protected Rectangle him = new Rectangle();


    public Entity(Game pGame, String spriteRef, double pX, double pY) {
        this.game = pGame;
        this.sprite = SpriteStore.getInstance().getSprite(spriteRef);
        this.x = pX;
        this.y = pY;
    }


    protected double getXPosition() {
        return x;
    }


    protected void setXPosition(double x) {
        this.x = x;
    }


    protected double getYPosition() {
        return y;
    }


    protected void setYPosition(double y) {
        this.y = y;
    }


    protected double getXSpeed() {
        return dx;
    }


    protected void setXSpeed(double dx) {
        this.dx = dx;
    }


    protected double getYSpeed() {
        return dy;
    }


    protected void setYSpeed(double dy) {
        this.dy = dy;
    }


    public void move(long delta) {
        // atnaujiman alieno buvimo vieta
        this.x += (delta * this.dx) / 1000;
        this.y += (delta * this.dy) / 1000;
    }


    public void draw(Graphics g) {
        this.sprite.draw(g, (int) this.x, (int) this.y);
    }


    public boolean collidesWith(Entity other) {
        //ziurima ar alienas nesusiduria su tuo kuris pirmas paliecia siena
        this.me.setBounds((int) this.x, (int) this.y, this.sprite.getWidth(), this.sprite.getHeight());
        this.him.setBounds((int) other.x, (int) other.y, other.sprite.getWidth(), other.sprite.getHeight());

        return me.intersects(him);
    }


    public abstract void collidedWith(Entity other);


    public abstract void doLogic();
}