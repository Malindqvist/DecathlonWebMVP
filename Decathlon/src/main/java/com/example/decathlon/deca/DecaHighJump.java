package com.example.decathlon.deca;

import com.example.decathlon.common.CalcTrackAndField;

public class DecaHighJump {

    private int score;
    private double A = 0.8465;
    private double B = 75;
    private double C = 1.42;
    CalcTrackAndField calc = new CalcTrackAndField();

    // Calculate the score based on height (cm). GUI-safe and blocks negative values.
    public int calculateResult(double distance) throws InvalidResultException {

        // No negative values allowed
        if (distance < 0) {
            System.out.println("Negative result is not possible.");
            throw new InvalidResultException("Negative result is not possible.");
        }

        if (distance < 0) { // domain lower-limit check retained
            System.out.println("Value too low");
            throw new InvalidResultException("Value too low");
        } else if (distance > 100) {
            System.out.println("Value too high");
            throw new InvalidResultException("Value too high");
        }
        score = calc.calculateField(A, B, C, distance);
        System.out.println("The result is: " + score);
        return score;
    }
}
