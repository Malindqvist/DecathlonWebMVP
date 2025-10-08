package com.example.decathlon.deca;

import com.example.decathlon.common.CalcTrackAndField;

public class DecaPoleVault {

    private int score;
    private double A = 0.2797;
    private double B = 100;
    private double C = 1.35;
    CalcTrackAndField calc = new CalcTrackAndField();

    // Calculate the score based on height (cm). GUI-safe and blocks negative values.
    public int calculateResult(double distance) throws InvalidResultException {

        // No negative values allowed
        if (distance < 0) {
            System.out.println("Negative result is not possible.");
            throw new InvalidResultException("Negative result is not possible.");
        }

        if (distance < 2) {
            System.out.println("Value too low");
            throw new InvalidResultException("Value too low");
        } else if (distance > 1000) {
            System.out.println("Value too high");
            throw new InvalidResultException("Value too high");
        }
        score = calc.calculateField(A, B, C, distance);
        System.out.println("The result is: " + score);
        return score;
    }
}
