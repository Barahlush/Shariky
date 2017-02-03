package com.shariky.objects.BonusBalls;

import com.shariky.objects.Ball;

import static com.shariky.objects.Ball.Types.death;

/**
 * Created by User on 03.02.2017.
 */

public class DeathBall extends Ball {

    public DeathBall(Ball ball) {
        super(ball);
        this.type = death;
        this.color = Colors.death_ball;
    }
}
