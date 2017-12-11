/**
 * Created by benas on 17.11.6.
 */
public class ShotEntity extends Entity {
    private int shotSpeed = 300;

    // naudoju tam kad jei kulka panaudota, kad isvengti dvigubo nuzudimo
    private boolean used = false;


    public ShotEntity(Game pGame, String spriteRef, double x, double y) {
        super(pGame, spriteRef, x, y);
        // nustatom saudymo krypti i virsu
        this.dy = -shotSpeed;
    }

    @Override
    public void move(long delta) {
        super.move(delta);

        // jei kulka uzeina uz ekrano ribu pasalinam
        if (y < -100) {
            this.game.removeEntity(this);
        }
    }


    public void collidedWith(Entity other) {
        // ziurim kad vienu suviu nenuzudytu dvieju alienu
        if (this.used) {
            return;
        }
        if (other instanceof AlienEntity) {
            this.game.removeEntity(this);
            this.game.removeEntity(other);
            this.used = true;

            this.game.notifyAlienKilled();
        }
    }

    public void doLogic() {

    }
}