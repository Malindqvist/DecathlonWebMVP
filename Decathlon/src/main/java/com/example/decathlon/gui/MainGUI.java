package com.example.decathlon.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import com.example.decathlon.deca.*; // contains Deca100M, Deca400M, ... and InvalidResultException
import com.example.decathlon.common.InputName;

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
        frame.setSize(500, 450);

        JPanel panel = new JPanel(new GridLayout(7, 1));

        // Input for competitor's name
        nameField = new JTextField(20);
        panel.add(new JLabel("Enter Competitor's Name:"));
        panel.add(nameField);

        // Dropdown for selecting a discipline
        String[] disciplines = {
                "100m (s)", "Long Jump (cm)", "Shot Put (m)", "High Jump (cm)",
                "400m (s)", "110m Hurdles (s)", "Discus Throw (m)", "Pole Vault (cm)",
                "Javelin Throw (m)", "1500m (s)"
        };
        disciplineBox = new JComboBox<>(disciplines);
        panel.add(new JLabel("Select Discipline:"));
        panel.add(disciplineBox);

        // Input for result
        resultField = new JTextField(10);
        panel.add(new JLabel("Result:"));
        panel.add(resultField);

        // Button to calculate and display result
        JButton calculateButton = new JButton("Calculate Score");
        calculateButton.addActionListener(new CalculateButtonListener());
        panel.add(calculateButton);

        // Output area
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
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
