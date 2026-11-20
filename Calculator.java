import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Calculator {
    int boardWidth = 360;
    int boardHeight = 640;

    Color customLightGray = new Color(212, 212, 210);
    Color customDarkGray = new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customOrange = new Color(255, 149, 0);
    Color customScientificGray = new Color(55, 55, 55);

    String[] buttonValues = {
            "sin", "cos", "tan", "log",
            "x²", "xʸ", "|x|", "⌫",
            "AC", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "√", "="
    };
    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "+/-", "%"};
    String[] scientificSymbols = {"sin", "cos", "tan", "log", "x²", "|x|", "⌫"};

    JFrame frame = new JFrame("Scientific Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    String A = "0";
    String operator = null;
    String B = null;

    Calculator() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 64));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);
        displayLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(7, 4));
        buttonsPanel.setBackground(customBlack);
        frame.add(buttonsPanel);

        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = new JButton();
            String buttonValue = buttonValues[i];
            button.setFont(new Font("Arial", Font.PLAIN, 24));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setBorder(new LineBorder(customBlack));

            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(customLightGray);
                button.setForeground(customBlack);
            } else if (Arrays.asList(rightSymbols).contains(buttonValue) || buttonValue.equals("xʸ")) {
                button.setBackground(customOrange);
                button.setForeground(Color.white);
            } else if (Arrays.asList(scientificSymbols).contains(buttonValue)) {
                button.setBackground(customScientificGray);
                button.setForeground(Color.white);
            } else {
                button.setBackground(customDarkGray);
                button.setForeground(Color.white);
            }
            buttonsPanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    String buttonValue = button.getText();
                    String currentText = displayLabel.getText();

                    if (currentText.equals("Error") && !buttonValue.equals("AC")) {
                        currentText = "0";
                        displayLabel.setText("0");
                    }

                    if (Arrays.asList(rightSymbols).contains(buttonValue) || buttonValue.equals("xʸ")) {
                        if (buttonValue.equals("=")) {
                            if (A != null && operator != null) {
                                B = currentText;
                                double numA = Double.parseDouble(A);
                                double numB = Double.parseDouble(B);

                                if (operator.equals("+")) {
                                    updateDisplay(removeZeroDecimal(numA + numB));
                                } else if (operator.equals("-")) {
                                    updateDisplay(removeZeroDecimal(numA - numB));
                                } else if (operator.equals("×")) {
                                    updateDisplay(removeZeroDecimal(numA * numB));
                                } else if (operator.equals("÷")) {
                                    if (numB == 0) {
                                        updateDisplay("Error");
                                    } else {
                                        updateDisplay(removeZeroDecimal(numA / numB));
                                    }
                                } else if (operator.equals("xʸ")) {
                                    updateDisplay(removeZeroDecimal(Math.pow(numA, numB)));
                                }
                                clearAll();
                            }
                        } else {
                            if (operator == null) {
                                A = currentText;
                                updateDisplay("0");
                                B = "0";
                            }
                            operator = buttonValue;
                        }
                    } else if (Arrays.asList(topSymbols).contains(buttonValue)) {
                        if (buttonValue.equals("AC")) {
                            clearAll();
                            updateDisplay("0");
                        } else if (buttonValue.equals("+/-")) {
                            double numDisplay = Double.parseDouble(currentText);
                            numDisplay *= -1;
                            updateDisplay(removeZeroDecimal(numDisplay));
                        } else if (buttonValue.equals("%")) {
                            double numDisplay = Double.parseDouble(currentText);
                            numDisplay /= 100;
                            updateDisplay(removeZeroDecimal(numDisplay));
                        }
                    } else if (Arrays.asList(scientificSymbols).contains(buttonValue) || buttonValue.equals("√")) {
                        if (buttonValue.equals("⌫")) {
                            if (currentText.length() > 1) {
                                String shortened = currentText.substring(0, currentText.length() - 1);
                                updateDisplay(shortened.equals("-") ? "0" : shortened);
                            } else {
                                updateDisplay("0");
                            }
                            return;
                        }

                        double numDisplay = Double.parseDouble(currentText);
                        double result = 0;

                        switch (buttonValue) {
                            case "√":
                                if (numDisplay < 0) {
                                    updateDisplay("Error");
                                    clearAll();
                                    return;
                                }
                                result = Math.sqrt(numDisplay);
                                break;
                            case "sin":
                                result = Math.sin(Math.toRadians(numDisplay));
                                break;
                            case "cos":
                                result = Math.cos(Math.toRadians(numDisplay));
                                break;
                            case "tan":
                                if (Math.abs(numDisplay % 180) == 90) {
                                    updateDisplay("Error");
                                    clearAll();
                                    return;
                                }
                                result = Math.tan(Math.toRadians(numDisplay));
                                break;
                            case "log":
                                if (numDisplay <= 0) {
                                    updateDisplay("Error");
                                    clearAll();
                                    return;
                                }
                                result = Math.log10(numDisplay);
                                break;
                            case "x²":
                                result = Math.pow(numDisplay, 2);
                                break;
                            case "|x|":
                                result = Math.abs(numDisplay);
                                break;
                        }
                        result = Math.round(result * 1e10) / 1e10;
                        updateDisplay(removeZeroDecimal(result));
                    } else {
                        if (buttonValue.equals(".")) {
                            if (!currentText.contains(".")) {
                                updateDisplay(currentText + ".");
                            }
                        } else if ("0123456789".contains(buttonValue)) {
                            if (currentText.equals("0")) {
                                updateDisplay(buttonValue);
                            } else {
                                updateDisplay(currentText + buttonValue);
                            }
                        }
                    }
                }
            });
        }
        frame.setVisible(true);
    }

    void updateDisplay(String text) {
        if (text.length() > 11) {
            displayLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        } else if (text.length() > 7) {
            displayLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        } else {
            displayLabel.setFont(new Font("Arial", Font.PLAIN, 64));
        }
        displayLabel.setText(text);
    }

    void clearAll() {
        A = "0";
        operator = null;
        B = null;
    }

    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0 && !Double.isInfinite(numDisplay)) {
            return Long.toString((long) numDisplay);
        }
        return Double.toString(numDisplay);
    }
}