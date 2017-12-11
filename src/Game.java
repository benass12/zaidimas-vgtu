/**
 * Created by benas on 17.11.6.
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends Canvas {

    // Nustatom ekrano rezoliucija

    public static final int RES_X = 800;
    public static final int RES_Y = 600;
    public static final boolean DEBUG_ENABLED = false;

    private BufferStrategy bufferStrategy;
    private Entity ship;
    private boolean gameRunning;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean firePressed;
    private boolean waitingForKeyPress = true;
    private boolean logicUpdateRequired = false;
    private List<Entity> entities = new ArrayList<Entity>();
    private List<Entity> removeList = new ArrayList<Entity>();
    private int shipMoveSpeed = 300;
    private int alienCount;
    private long lastFire;
    private long firingInterval = 300;
    private String message = "";



    public Game() {
        JFrame container = new JFrame("Space Invaders ");

        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(RES_X, RES_Y));
        panel.setLayout(null);

        this.setBounds(0, 0, RES_X, RES_Y);
        panel.add(this);

        this.setIgnoreRepaint(true);

        this.addKeyListener(new KeyInputHandler());

        container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        // Grafikos metodas
        this.createBufferStrategy(2);
        this.bufferStrategy = this.getBufferStrategy();

        this.requestFocus();
    }


    private void initEntities() {
        this.ship = new ShipEntity(this, "sprites/ship.gif", 370, 550);
        entities.add(ship);

        this.alienCount = 0;
        for (int row = 0; row < 5; row++) {
            for (int x = 0; x < 12; x++) {
                Entity alien = new AlienEntity(this, "sprites/alien.gif", 100 + (x*50), (50) + row*30);

                entities.add(alien);
                this.alienCount++;
            }
        }
    }

    private void tryToFire () {
        long currentFiringInterval = System.currentTimeMillis() - this.lastFire;

        // jei bandom per greit spaudinet saudyma - nieko nedaro
        if (currentFiringInterval < this.firingInterval) {
            return;
        }

        this.lastFire = System.currentTimeMillis();

        ShotEntity shot = new ShotEntity(this, "sprites/shot.gif", this.ship.getXPosition() + 10, this.ship.getYPosition() - 30);
        this.entities.add(shot);
    }


    private void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();

        while (this.gameRunning) {
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // kai nusaunam aliena  ji pradanginam su juoda spalva
            Graphics2D graphSurface = (Graphics2D) this.bufferStrategy.getDrawGraphics();
            graphSurface.setColor(Color.BLACK);
            graphSurface.fillRect(0, 0, RES_X, RES_Y);

            if (!this.waitingForKeyPress) {
                for (Entity e : this.entities) {
                    e.move(delta);
                }
            }

            for (Entity e : this.entities) {
                e.draw(graphSurface);
            }

            for (Entity me : this.entities) {
                for (Entity him : this.entities) {
                    if (me == him) {
                        continue;
                    }
                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    }
                }
            }

            this.entities.removeAll(this.removeList);
            this.removeList.clear();

            if (this.logicUpdateRequired) {
                for (Entity entity : this.entities) {
                    entity.doLogic();
                }

                this.logicUpdateRequired = false;
            }

            if (this.waitingForKeyPress) {
                graphSurface.setColor(Color.white);
                graphSurface.drawString(this.message,(800 - graphSurface.getFontMetrics().stringWidth(this.message)) / 2,250);
                graphSurface.drawString("Paspauskite mygtuka ",(800 - graphSurface.getFontMetrics().stringWidth("Paspauskite mygtuka")) / 2,300);
            }

            graphSurface.dispose();
            this.bufferStrategy.show();

            this.ship.setXSpeed(0);

            if (this.rightPressed && !this.leftPressed) {
                this.ship.setXSpeed(this.shipMoveSpeed);
            }

            if (!this.rightPressed && this.leftPressed) {
                this.ship.setXSpeed(-this.shipMoveSpeed);
            }

            if (this.firePressed) {
                tryToFire();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null,
                        JOptionPane.ERROR_MESSAGE);

                if (DEBUG_ENABLED) {
                    e.printStackTrace();
                }

                System.exit(0);
            }
        }
    }


    public void startGame() {
        this.resetGame();
        this.gameRunning = true;
        this.gameLoop();
    }


    public void resetGame() {
        entities.clear();
        initEntities();

        leftPressed = false;
        rightPressed = false;
        firePressed = false;
    }


    public void removeEntity(Entity entity) {
        this.removeList.add(entity);
    }


    public void notifyPlayerDeath() {
        this.message = "MIREI :)!";
        this.waitingForKeyPress = true;
    }


    public void notifyPlayerWin() {
        this.message = "LAIMEJAI :)!";
        this.waitingForKeyPress = true;
    }


    public void notifyAlienKilled() {
        this.alienCount--;

        if (this.alienCount == 0) {
            this.notifyPlayerWin();
            return;
        }

        for (Entity entity : this.entities) {
            if (entity instanceof AlienEntity) {
                ((AlienEntity) entity).speedUp();
            }
        }
    }


    public void updateLogic() {
        this.logicUpdateRequired = true;
    }


    private class KeyInputHandler extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            if (waitingForKeyPress) {
                return;
            }

            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT: {
                    leftPressed = true;
                    break;
                }

                case KeyEvent.VK_RIGHT: {
                    rightPressed = true;
                    break;
                }

                case KeyEvent.VK_SPACE: {
                    firePressed = true;
                    break;
                }
            }
        }


        public void keyReleased(KeyEvent e) {
            if (waitingForKeyPress) {
                return;
            }

            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT: {
                    leftPressed = false;
                    break;
                }

                case KeyEvent.VK_RIGHT: {
                    rightPressed = false;
                    break;
                }

                case KeyEvent.VK_SPACE: {
                    firePressed = false;
                    break;
                }
            }
        }


        public void keyTyped(KeyEvent e) {
            if (waitingForKeyPress) {
                waitingForKeyPress = false;
                resetGame();
            }

            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }
}