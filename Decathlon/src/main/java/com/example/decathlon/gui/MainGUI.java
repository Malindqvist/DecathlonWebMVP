package com.example.decathlon.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import com.example.decathlon.deca.*; // contains Deca100M, Deca400M, ... and InvalidResultException
import com.example.decathlon.common.InputName;
import com.example.decathlon.heptathlon.*;

public class MainGUI {

    private JTextField nameField;
    private JTextField resultField;
    private JComboBox<String> disciplineBox;
    private JTextArea outputArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Track and Field Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Top area for inputs ===
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        nameField = new JTextField(20);
        inputPanel.add(new JLabel("Enter Competitor's Name:"));
        inputPanel.add(nameField);

        String[] categories = {"Decathlon", "Heptathlon"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        inputPanel.add(new JLabel("Select Category:"));
        inputPanel.add(categoryBox);

        String[] disciplines = {
                "100m (s)", "Long Jump (cm)", "Shot Put (m)", "High Jump (cm)",
                "400m (s)", "110m Hurdles (s)", "Discus Throw (m)", "Pole Vault (cm)",
                "Javelin Throw (m)", "1500m (s)"
        };
        disciplineBox = new JComboBox<>(disciplines);
        inputPanel.add(new JLabel("Select Discipline:"));
        inputPanel.add(disciplineBox);

        resultField = new JTextField(10);
        inputPanel.add(new JLabel("Result:"));
        inputPanel.add(resultField);

        JButton calculateButton = new JButton("Calculate Score");
        calculateButton.addActionListener(new CalculateButtonListener(categoryBox));

        // === Output area (bottom, much bigger) ===
        outputArea = new JTextArea(20, 60);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // === Add to main layout ===
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(calculateButton, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private class CalculateButtonListener implements ActionListener {
        private JComboBox<String> categoryBox;

        public CalculateButtonListener(JComboBox<String> categoryBox) {
            this.categoryBox = categoryBox;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String category = (String) categoryBox.getSelectedItem(); // <-- added line
            String discipline = (String) disciplineBox.getSelectedItem();
            String resultText = resultField.getText().trim();

            // Validate competitor name using InputName
            if (!InputName.isValidName(name)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Please enter a valid name (letters only).",
                        "Invalid Name",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                double result = Double.parseDouble(resultText);

                int score = 0;
                switch (discipline) {
                    case "100m (s)": {
                        Deca100M deca100M = new Deca100M();
                        score = deca100M.calculateResult(result);
                        break;
                    }
                    case "400m (s)": {
                        Deca400M deca400M = new Deca400M();
                        score = deca400M.calculateResult(result);
                        break;
                    }
                    case "1500m (s)": {
                        Deca1500M deca1500M = new Deca1500M();
                        score = deca1500M.calculateResult(result);
                        break;
                    }
                    case "110m Hurdles (s)": {
                        Deca110MHurdles deca110MHurdles = new Deca110MHurdles();
                        score = deca110MHurdles.calculateResult(result);
                        break;
                    }
                    case "Long Jump (cm)": {
                        DecaLongJump decaLongJump = new DecaLongJump();
                        score = decaLongJump.calculateResult(result);
                        break;
                    }
                    case "High Jump (cm)": {
                        DecaHighJump decaHighJump = new DecaHighJump();
                        score = decaHighJump.calculateResult(result);
                        break;
                    }
                    case "Pole Vault (cm)": {
                        DecaPoleVault decaPoleVault = new DecaPoleVault();
                        score = decaPoleVault.calculateResult(result);
                        break;
                    }
                    case "Discus Throw (m)": {
                        DecaDiscusThrow decaDiscusThrow = new DecaDiscusThrow();
                        score = decaDiscusThrow.calculateResult(result);
                        break;
                    }
                    case "Javelin Throw (m)": {
                        DecaJavelinThrow decaJavelinThrow = new DecaJavelinThrow();
                        score = decaJavelinThrow.calculateResult(result);
                        break;
                    }
                    case "Shot Put (m)": {
                        DecaShotPut decaShotPut = new DecaShotPut();
                        score = decaShotPut.calculateResult(result);
                        break;
                    }
                    case "Hep 100m Hurdles":    score = new Hep100MHurdles().calculateResult(result); break;
                    case "Hep High Jump":       score = new HeptHightJump().calculateResult(result); break;
                    case "Hep Shot Put":        score = new HeptShotPut().calculateResult(result); break;
                    case "Hep 200m":            score = new Hep200M().calculateResult(result); break;
                    case "Hep Long Jump":       score = new HeptLongJump().calculateResult(result); break;
                    case "Hep Javelin Throw":   score = new HeptJavelinThrow().calculateResult(result); break;
                    case "Hep 800m":            score = new Hep800M().calculateResult(result); break;

                    default:
                        // If no discipline is selected (should not happen)
                        JOptionPane.showMessageDialog(
                                null,
                                "Please select a discipline.",
                                "No Discipline Selected",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                }

                // Append the result to the output area
                outputArea.append("Competitor: " + name + "\n");
                outputArea.append("Category: " + category + "\n"); // <-- added line
                outputArea.append("Discipline: " + discipline + "\n");
                outputArea.append("Result: " + result + "\n");
                outputArea.append("Score: " + score + "\n\n");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Please enter a valid number for the result.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (InvalidResultException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Invalid Result",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
