package com.shariky.objects.BonusBalls;

import com.shariky.objects.Ball;

import static com.shariky.objects.Ball.Types.health;

/**
 * Created by User on 03.02.2017.
 */

public class HealthBall extends Ball {

    public HealthBall(Ball ball) {
        super(ball);
        this.type = health;
        this.color = Colors.health_ball;
    }
}
