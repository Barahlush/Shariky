package com.shariky.objects.BonusBalls;

import com.shariky.objects.Ball;

import static com.shariky.objects.Ball.Colors.blue;
import static com.shariky.objects.Ball.Colors.gray;
import static com.shariky.objects.Ball.Colors.green;
import static com.shariky.objects.Ball.Types.kill;

/**
 * Created by User on 03.02.2017.
 */

public class BallKill extends Ball {
    private static boolean[] killed_colors = {false, false, false};

    public static Colors getKilledColor() {
        if (killed_colors[0])
            return green;
        if (killed_colors[1])
            return gray;
        if (killed_colors[2])
            return blue;
        return green;
    }

    public BallKill(Ball ball) {
        super(ball);
        this.type = kill;
    }

    public static boolean isKilled() {
        return killed_colors[0] || killed_colors[1] || killed_colors[2];
    }

    public static boolean isKilled(Colors color) {
        switch (color) {
            case green:
                return killed_colors[0];
            case gray:
                return killed_colors[1];
            case blue:
                return killed_colors[2];
        }
        return false;
    }

    public static void setKilled(Colors color) {
        switch (color) {
            case green:
                killed_colors[0] = true;
                break;
            case gray:
                killed_colors[1] = true;
                break;
            case blue:
                killed_colors[2] = true;
                break;
        }
    }

    public static void resetKilled() {
        killed_colors[0] = false;
        killed_colors[1] = false;
        killed_colors[2] = false;
    }


}
