package com.example.decathlon.heptathlon;

import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.deca.InvalidResultException;

public class Hep800M {

    private int score;
    private double A = 0.11193;
    private double B = 254;
    private double C = 1.88;
    CalcTrackAndField calc = new CalcTrackAndField();

    // Calculate the score based on time. GUI-safe and blocks negative values.
    public int calculateResult(double runningTime) throws InvalidResultException {

        // No negative values allowed
        if (runningTime < 0) {
            System.out.println("Negative result is not possible.");
            throw new InvalidResultException("Negative result is not possible.");
        }

        if (runningTime < 70) {
            System.out.println("Value too low");
            throw new InvalidResultException("Value too low");
        } else if (runningTime > 250.79) {
            System.out.println("Value too high");
            throw new InvalidResultException("Value too high");
        }
        score = calc.calculateTrack(A, B, C, runningTime);
        System.out.println("The result is " + score);
        return score;
    }
}
