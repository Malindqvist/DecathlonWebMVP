package com.example.decathlon.deca;

import com.example.decathlon.common.CalcTrackAndField;

public class Deca1500M {

    private int score;
    private double A = 0.03768;
    private double B = 480;
    private double C = 18.5;
    CalcTrackAndField calc = new CalcTrackAndField();

    // Calculate the score based on time. GUI-safe and blocks negative values.
    public int calculateResult(double runningTime) throws InvalidResultException {

        // No negative values allowed
        if (runningTime < 0) {
            System.out.println("Negative result is not possible.");
            throw new InvalidResultException("Negative result is not possible.");
        }

        if (runningTime < 2) {
            System.out.println("Value too low");
            throw new InvalidResultException("Value too low");
        } else if (runningTime > 7) {
            System.out.println("Value too high");
            throw new InvalidResultException("Value too high");
        }
        score = calc.calculateTrack(A, B, C, runningTime);
        System.out.println("The result is: " + score);
        return score;
    }
}
