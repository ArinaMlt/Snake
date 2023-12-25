import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.ImageObserver;

import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    // задержка, чем выше тем медленнее игра
    static final int DELAY = 75;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 4;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean runing = false;
    Timer timer;
    Random random;

    ImageIcon appleIcon = new ImageIcon("C:\\Users\\Марина\\Desktop\\ARINA\\JAVA\\Game\\Snake\\image\\apple.png");
    Image appleImage = appleIcon.getImage();

    boolean isPaused = false;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(213, 213, 213));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(new MyMouseAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        runing = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (runing) {
            // Lines
            g.setColor(Color.white);
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            // Aples
            // g.setColor(Color.blue);
            // g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            // g.drawImage(appleImage, appleX, appleY, UNIT_SIZE, UNIT_SIZE, (ImageObserver)
            // this);
            g.drawImage(appleImage, appleX, appleY, UNIT_SIZE, UNIT_SIZE, (ImageObserver) this);

            // Body of snake змеи
            /*
             * for (int i = 0; i < bodyParts; i++) {
             * if (i == 0) {
             * g.setColor(Color.green);
             * g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
             * } else {
             * g.setColor(new Color(45, 180, 0));
             * // g.setColor(new
             * // Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
             * g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
             * }
             * }
             */

            // другое тело змеи
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(0, 100, 10));
                    // g.setColor(new Color(26,60,64)); // Зеленый цвет для головы
                } else {
                    //  градиент для тела змеи
                    int red = (45 + i * 5) % 256; // Интенсивность красного цвета увеличивается с каждым сегментом тела
                    g.setColor(new Color(red, 100, 0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                //  цвет обводки 
                g.setColor(new Color(0, 100, 10));

                //  контур квадрата
                g.drawRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Score text
            g.setColor(new Color(45, 64, 89));
            g.setFont(new Font("Pixelade", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());

            if (isPaused) {
                pause(g);
            } else {

            }
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // check if body collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                runing = false;
            }
        }

        // check if head touches left border
        if (x[0] < 0) {
            runing = false;
        }

        // check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            runing = false;
        }

        // check if head touches top border
        if (y[0] < 0) {
            runing = false;
        }

        // check if heades touched bottom border
        if (y[0] > SCREEN_HEIGHT) {
            runing = false;
        }

        if (!runing || isPaused) {
            timer.stop();
        }
    }

    public void pause(Graphics g) {
        // Score
        g.setColor(new Color(247, 56, 89));
        g.setFont(new Font("Pixelade", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize());
        // Game Over text
        // g.setColor(Color.black);
        g.setColor(new Color(247, 56, 89));
        g.setFont(new Font("Pixelade", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Pause", (SCREEN_WIDTH - metrics2.stringWidth("Pause")) / 2, SCREEN_HEIGHT / 2);
    }

    public void gameOver(Graphics g) {
        // Score
        g.setColor(new Color(215, 35, 35));
        g.setFont(new Font("Pixelade", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize());
        // Game Over text
        g.setColor(new Color(215, 35, 35));
        g.setFont(new Font("Pixelade", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        // Кнопка начала сначала
        g.setColor(new Color(45, 64, 89));
        g.setFont(new Font("Pixelade", Font.BOLD, 30));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Click to start over", (SCREEN_WIDTH - metrics3.stringWidth("Click to start over")) / 2,
                SCREEN_HEIGHT / 2 + 100);
    }

    public void restartGame() {
        x[0] = 0;
        y[0] = 0;
        bodyParts = 4;
        applesEaten = 0;
        runing = true;
        direction = 'R';
        startGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (runing) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_A:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (!isPaused) {
                        isPaused = true;
                    } else {
                        isPaused = false;
                        timer.start();
                    }
                    break;
            }
        }

    }

    public class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!runing) {
                runing = true;
                restartGame();
            }
        }

    }

}
