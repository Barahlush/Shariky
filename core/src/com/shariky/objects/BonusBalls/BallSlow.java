package com.shariky.objects.BonusBalls;

import com.shariky.objects.Ball;

import java.util.HashMap;

import static com.shariky.objects.Ball.Colors.blue;
import static com.shariky.objects.Ball.Colors.gray;
import static com.shariky.objects.Ball.Colors.green;
import static com.shariky.objects.Ball.Types.slow;

/**
 * Created by User on 03.02.2017.
 */

public class BallSlow extends Ball {

    public BallSlow() {
        super();
        this.type = slow;
        slow_time = new HashMap<Ball.Colors, Long>();
        slow_time.put(green, 0L);
        slow_time.put(gray, 0L);
        slow_time.put(blue, 0L);

    }

    public static HashMap<Ball.Colors, Long> getSlowTime() {
        return slow_time;
    }

    public static void setSlowTime(long time, Ball.Colors color) {
        slow_time.put(color, time);
    }

    private static HashMap<Ball.Colors, Long> slow_time;


    public static long slow_period = 7000000000L;

    public BallSlow(Ball.Colors color, int x, int y, int speed) {
        super(color, x, y, speed);
        this.type = slow;
        slow_time = new HashMap<Ball.Colors, Long>();
        slow_time.put(green, 0L);
        slow_time.put(gray, 0L);
        slow_time.put(blue, 0L);
    }

    public BallSlow(Ball ball) {
        super(ball);
        this.type = slow;
        slow_time = new HashMap<Ball.Colors, Long>();
        slow_time.put(green, 0L);
        slow_time.put(gray, 0L);
        slow_time.put(blue, 0L);
    }

    public static void updateSlowTime(long cur_time) {
        if (cur_time - slow_time.get(green) >= slow_period) {
            slow_time.put(green, 0L);
        }

        if (cur_time - slow_time.get(gray) >= slow_period) {
            slow_time.put(gray, 0L);
        }

        if (cur_time - slow_time.get(blue) >= slow_period) {
            slow_time.put(blue, 0L);
        }
    }

}
