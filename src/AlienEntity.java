/**
 * Created by benas on 17.11.6.
 */
public class AlienEntity extends Entity {
    // greitis horizontaliai
    private double moveSpeed = 75;
    private double speedUpPercentage = 1.02;


    public AlienEntity(Game pGame, String spriteRef, double x, double y) {
        super(pGame, spriteRef, x, y);
    // pradeda judeti i kaire kaip originalioi versijoi
        this.dx = -this.moveSpeed;
    }
    // didinam greiti
    public void speedUp() {
        this.setXSpeed(this.dx * this.speedUpPercentage);
    }

    @Override
    public void move(long delta) {
        // kai pasiekiama kaire riba ekrano keiciam judejimo krypti
        if (dx < 0 && this.x < 10) {
            game.updateLogic();
        }
        // kai pasiekiama desine riba
        if (dx > 0 && this.x > 750) {
            game.updateLogic();
        }

        super.move(delta);
    }

    public void collidedWith(Entity other) {
    }

    public void doLogic() {
        // leidziame alienus i ekrano apacia
        this.dx = -this.dx;
        this.y += 10;
        // jei apacioi zaidejas mirsta
        if (y > 570) {
            game.notifyPlayerDeath();
        }
    }
}