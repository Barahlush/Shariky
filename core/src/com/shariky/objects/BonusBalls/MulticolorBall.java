package com.shariky.objects.BonusBalls;

import com.badlogic.gdx.math.MathUtils;
import com.shariky.objects.Ball;

import static com.shariky.objects.Ball.Colors.blue_gray;
import static com.shariky.objects.Ball.Colors.green_blue;
import static com.shariky.objects.Ball.Colors.green_gray;
import static com.shariky.objects.Ball.Colors.tricolor;
import static com.shariky.objects.Ball.Types.multicolor;

/**
 * Created by User on 03.02.2017.
 */

public class MulticolorBall extends Ball {
    public MulticolorBall(Ball ball) {
        super(ball);
        this.type = multicolor;
        this.color = ballColorMix();
    }

    public static Ball.Colors ballColorMix() {
        int color = MathUtils.random(0, 4);
        switch (color) {
            case 0:
                return green_blue;
            case 1:
                return green_gray;
            case 2:
                return blue_gray;
            case 3:
                return tricolor;
        }
        return ballColorMix();
    }


}
