package com.example.decathlon.heptathlon;

import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.deca.InvalidResultException;

public class Hep100MHurdles {

    private int score;
    private double A = 9.23076;
    private double B = 26.7;
    private double C = 1.835;
    CalcTrackAndField calc = new CalcTrackAndField();

    // Calculate the score based on time. GUI-safe and blocks negative values.
    public int calculateResult(double runningTime) throws InvalidResultException {

        // No negative values allowed
        if (runningTime < 0) {
            System.out.println("Negative result is not possible.");
            throw new InvalidResultException("Negative result is not possible.");
        }

        if (runningTime < 5) {
            System.out.println("Value too low");
            throw new InvalidResultException("Value too low");
        } else if (runningTime > 26.4) {
            System.out.println("Value too high");
            throw new InvalidResultException("Value too high");
        }
        score = calc.calculateTrack(A, B, C, runningTime);
        System.out.println("The result is " + score);
        return score;
    }
}
