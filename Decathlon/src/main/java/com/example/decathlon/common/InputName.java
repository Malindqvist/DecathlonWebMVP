package com.example.decathlon.common;

public class InputName {

    // Regex: only letters (all languages), spaces, apostrophes, dots, and dashes; must start with a letter
    private static final String NAME_REGEX = "^[\\p{L}][\\p{L}\\s.'-]*$";

    /**
     * Reusable validation for competitor names.
     * @param name the input string to validate
     * @return true if the name matches the regex rule
     */
    public static boolean isValidName(String name) {
        return name != null && name.matches(NAME_REGEX);
    }

    /*
     * Optional: old console input method (kept for completeness).
     * Uses the same validation logic. For GUI usage, this is not required.
     *
     * public String addCompetitor() {
     *     boolean active = true;
     *     String compName = "";
     *     Scanner sc = new Scanner(System.in);
     *     while (active) {
     *         System.out.println("Please enter the competitor's name:");
     *         compName = sc.nextLine();
     *         if (!isValidName(compName)) {
     *             System.out.println("Please enter a valid name (letters only).");
     *         } else {
     *             active = false;
     *         }
     *     }
     *     return compName;
     * }
     */
}
