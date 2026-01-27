package Bai4;

import javax.swing.*;
import java.awt.*;

public class PaymentForm extends JFrame {

    private final JCheckBox chkMale = new JCheckBox("Male");
    private final JCheckBox chkFemale = new JCheckBox("Female");
    private final JCheckBox chkChild = new JCheckBox("Child (0 - 17 years)");

    private final JTextField txtAge = new JTextField(10);
    private final JTextField txtPayment = new JTextField(10);

    private final JButton btnCalculate = new JButton("Calculate");

    public PaymentForm() {
        setTitle("Calculate the Payment for the Patient");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        txtPayment.setEditable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: checkboxes
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(chkMale, gbc);
        gbc.gridx = 1;
        panel.add(chkFemale, gbc);
        gbc.gridx = 2;
        panel.add(chkChild, gbc);

        // Row 1: Age + button
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Age (Years)"), gbc);

        gbc.gridx = 1;
        panel.add(txtAge, gbc);

        gbc.gridx = 2;
        panel.add(btnCalculate, gbc);

        // Row 2: Payment
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Payment is"), gbc);

        gbc.gridx = 1;
        panel.add(txtPayment, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("euro €"), gbc);

        add(panel);
        pack();
        setLocationRelativeTo(null);

        chkMale.addActionListener(e -> {
            if (chkMale.isSelected()) { chkFemale.setSelected(false); chkChild.setSelected(false); }
        });
        chkFemale.addActionListener(e -> {
            if (chkFemale.isSelected()) { chkMale.setSelected(false); chkChild.setSelected(false); }
        });
        chkChild.addActionListener(e -> {
            if (chkChild.isSelected()) { chkMale.setSelected(false); chkFemale.setSelected(false); }
        });

        btnCalculate.addActionListener(e -> onCalculate());
    }

    private void onCalculate() {
        try {
            String sAge = txtAge.getText().trim();
            if (sAge.isEmpty()) throw new IllegalArgumentException("Age is required");

            int age = Integer.parseInt(sAge);

            PaymentCalculator.Type type = null;
            if (chkMale.isSelected()) type = PaymentCalculator.Type.MALE;
            if (chkFemale.isSelected()) type = PaymentCalculator.Type.FEMALE;
            if (chkChild.isSelected()) type = PaymentCalculator.Type.CHILD;

            int payment = PaymentCalculator.calculate(type, age);
            txtPayment.setText(String.valueOf(payment));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaymentForm().setVisible(true));
    }
}
