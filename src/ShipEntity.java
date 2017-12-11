/**
 * Created by benas on 17.11.6.
 */
public class ShipEntity extends Entity {

    public ShipEntity(Game pGame, String spriteRef, double x, double y) {
        super(pGame, spriteRef, x, y);
    }

    @Override
    public void move(long delta) {
        // ziuriu judejima jei juda i kaire kad judetu tik iki tam tikros vietos
        if (dx < 0 && this.x < 10) {
            return;
        }


        if (dx > 0 && this.x > 750) {
            return;
        }

        super.move(delta);
    }

    public void collidedWith(Entity other) {
        if (other instanceof AlienEntity) {
            game.notifyPlayerDeath();
        }
    }

    public void doLogic() {

    }
}