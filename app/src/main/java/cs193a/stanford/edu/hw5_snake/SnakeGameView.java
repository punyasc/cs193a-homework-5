package cs193a.stanford.edu.hw5_snake;

import android.content.*;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import java.util.*;
import stanford.androidlib.graphics.*;
import stanford.androidlib.util.*;

public class SnakeGameView extends GCanvas {

    GSprite snakeHead;
    GSprite snakeBody;
    GLabel label;
    int orientation;
    float increment;
    Vector<GSprite> bodyVec;
    GSprite food;
    int points;
    boolean isDone;

    public SnakeGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        bodyVec = new Vector<GSprite>();
        orientation = 1;
        snakeHead = new GSprite(loadScaledBitmap(R.drawable.snakehead, 4));
        increment = snakeHead.getWidth();
        snakeHead.setCollisionMargin(10);
        add(snakeHead, 10 + 2*increment, 10);
        snakeBody = new GSprite(loadScaledBitmap(R.drawable.snakebody, 4));
        add(snakeBody, 10 + increment, 10);
        bodyVec.add(snakeBody);
        label = new GLabel("Score: 0", 10, 10);
        label.setFont(Typeface.MONOSPACE, Typeface.BOLD, 40f);
        add(label);
        food = new GSprite(loadScaledBitmap(R.drawable.food, 9));
        add(food);
        newFood();

    }

    public void startNewGame() {
        if (!isDone) {
            animate(5);
        }
    }

    public Bitmap loadScaledBitmap(int id, int factor) {
        Bitmap image = BitmapFactory.decodeResource(getResources(), id);
        image = Bitmap.createScaledBitmap(image, image.getWidth()/factor, image.getHeight()/factor, true);
        return image;
    }

    public void newFood() {
        RandomGenerator randy = RandomGenerator.getInstance();

        int upperLimit = Math.round(getWidth()/snakeHead.getWidth()) - 1;
        float x = 10 + increment * randy.nextInt(0, upperLimit);
        float y = 10 + increment * randy.nextInt(0, upperLimit
        );
        food.setLocation(x, y);
    }

    @Override
    public void onAnimateTick() {
        super.onAnimateTick();
        label.sendToFront();

        if (snakeHead.getX() >= getWidth() - 40 || snakeHead.getX() <= 10 || snakeHead.getY() <= 0 || snakeHead.getY() >= getHeight() - 10) {
            label.setText("GAME OVER!");
            label.setFontSize(80f);
            animationPause();
            isDone = true;
        }

        String formatted = Float.toString(increment);

        float initialX = snakeHead.getX();
        float initialY = snakeHead.getY();

        if (orientation == 0) {
            snakeHead.setY(snakeHead.getY() - increment);
        }
        if (orientation == 1) {
            snakeHead.setX(snakeHead.getX() + increment);
        }
        if (orientation == 2) {
            snakeHead.setY(snakeHead.getY() + increment);
        }
        if (orientation == 3) {
            snakeHead.setX(snakeHead.getX() - increment);
        }

        float goToX;
        float goToY;
        float fromX;
        float fromY;
        if (bodyVec.size()>1) {
           for (int i = bodyVec.size() - 1; i > 0; i--) {
               if (bodyVec.get(i).collidesWith(snakeHead)) {
                   label.setText("GAME OVER!");
                   label.setFontSize(80f);
                   animationPause();
                   isDone = true;
               }
                bodyVec.get(i).setLocation(bodyVec.get(i-1).getX(), bodyVec.get(i-1).getY());
            }
        }

        bodyVec.get(0).setLocation(initialX, initialY);

        if (food.collidesWith(snakeHead)) {
            newFood();
            addToTail();
            points++;
            label.setText("Score: " + points);
        }

    }

    public void startGame() {

    }

    public int getScore() {
        return points;
    }

    public void turnLeft() {
        if (orientation > 0) {
            orientation--;
        } else {
            orientation = 3;
        }
    }

    public void turnRight() {
        if (orientation < 3) {
            orientation++;
        } else {
            orientation = 0;
        }
    }

    public void addToTail() {
        GSprite newTail = new GSprite(loadScaledBitmap(R.drawable.snakebody, 4));
        add(newTail, bodyVec.get(bodyVec.size() - 1).getX(), bodyVec.get(bodyVec.size() - 1).getY());
        bodyVec.add(newTail);
    }
}
