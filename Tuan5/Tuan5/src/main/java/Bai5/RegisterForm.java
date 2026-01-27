package Bai5;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class RegisterForm extends JFrame {

    private final JTextField txtId = new JTextField(20);
    private final JTextField txtName = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtPhone = new JTextField(20);

    private final JTextArea txtAddress = new JTextArea(3, 20);

    private final JPasswordField txtPass = new JPasswordField(20);
    private final JPasswordField txtConfirm = new JPasswordField(20);

    // Ngày sinh không bắt buộc - nhập yyyy-MM-dd để dễ parse
    private final JTextField txtDob = new JTextField(20);

    private final JRadioButton rdNam = new JRadioButton("Nam");
    private final JRadioButton rdNu = new JRadioButton("Nữ");
    private final JRadioButton rdKhac = new JRadioButton("Khác");

    private final JCheckBox chkTerms = new JCheckBox("Tôi đồng ý với các điều khoản dịch vụ *");

    private final JButton btnRegister = new JButton("Đăng ký");
    private final JButton btnReset = new JButton("Nhập lại");

    private final JTextArea txtErrors = new JTextArea(7, 35);

    private final CustomerDao dao = new CustomerDao();
    private final CustomerValidator validator = new CustomerValidator(dao);

    public RegisterForm() {
        setTitle("ĐĂNG KÝ TÀI KHOẢN KHÁCH HÀNG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        txtErrors.setEditable(false);
        txtErrors.setLineWrap(true);
        txtErrors.setWrapStyleWord(true);

        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(rdNam);
        genderGroup.add(rdNu);
        genderGroup.add(rdKhac);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        int row = 0;
        addRow(form, g, row++, "Mã Khách Hàng *", txtId, "6-10 ký tự, chỉ chữ và số");
        addRow(form, g, row++, "Họ và Tên *", txtName, "VD: Đào Thị Thu Huyền");
        addRow(form, g, row++, "Email *", txtEmail, "VD: nguyenvana@email.com");
        addRow(form, g, row++, "Số điện thoại *", txtPhone, "Bắt đầu bằng 0, 10-12 số");

        // Address
        g.gridx = 0; g.gridy = row; g.weightx = 0;
        form.add(new JLabel("Địa chỉ *"), g);
        g.gridx = 1; g.weightx = 1;
        form.add(new JScrollPane(txtAddress), g);
        row++;

        addRow(form, g, row++, "Mật khẩu *", txtPass, "Tối thiểu 8 ký tự");
        addRow(form, g, row++, "Xác nhận Mật khẩu *", txtConfirm, "Phải khớp mật khẩu");
        addRow(form, g, row++, "Ngày sinh", txtDob, "yyyy-MM-dd (không bắt buộc, đủ 18 tuổi)");

        // Gender
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.add(rdNam);
        genderPanel.add(rdNu);
        genderPanel.add(rdKhac);

        g.gridx = 0; g.gridy = row; g.weightx = 0;
        form.add(new JLabel("Giới tính"), g);
        g.gridx = 1; g.weightx = 1;
        form.add(genderPanel, g);
        row++;

        // Terms
        g.gridx = 1; g.gridy = row; g.weightx = 1;
        form.add(chkTerms, g);
        row++;

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttons.add(btnRegister);
        buttons.add(btnReset);

        g.gridx = 1; g.gridy = row;
        form.add(buttons, g);
        row++;

        // Errors
        g.gridx = 0; g.gridy = row; g.gridwidth = 2;
        form.add(new JLabel("Thông báo lỗi:"), g);
        row++;

        g.gridx = 0; g.gridy = row; g.gridwidth = 2;
        form.add(new JScrollPane(txtErrors), g);

        add(form);
        pack();
        setLocationRelativeTo(null);

        btnRegister.addActionListener(e -> onRegister());
        btnReset.addActionListener(e -> onReset(genderGroup));
    }

    private void addRow(JPanel panel, GridBagConstraints g, int row, String label, JComponent field, String hint) {
        g.gridx = 0; g.gridy = row; g.gridwidth = 1; g.weightx = 0;
        panel.add(new JLabel(label), g);

        JPanel wrapper = new JPanel(new BorderLayout(0, 2));
        wrapper.add(field, BorderLayout.NORTH);

        JLabel lbHint = new JLabel(hint);
        lbHint.setFont(lbHint.getFont().deriveFont(11f));
        lbHint.setForeground(Color.GRAY);
        wrapper.add(lbHint, BorderLayout.SOUTH);

        g.gridx = 1; g.gridy = row; g.weightx = 1;
        panel.add(wrapper, g);
    }

    private void onRegister() {
        Customer c = new Customer();
        c.customerId = txtId.getText();
        c.fullName = txtName.getText();
        c.email = txtEmail.getText();
        c.phone = txtPhone.getText();
        c.address = txtAddress.getText();
        c.password = new String(txtPass.getPassword());
        c.confirmPassword = new String(txtConfirm.getPassword());
        c.acceptedTerms = chkTerms.isSelected();

        // gender optional
        if (rdNam.isSelected()) c.gender = "Nam";
        else if (rdNu.isSelected()) c.gender = "Nữ";
        else if (rdKhac.isSelected()) c.gender = "Khác";
        else c.gender = null;

        // dob optional
        String dobText = txtDob.getText().trim();
        if (!dobText.isEmpty()) {
            try {
                c.dob = LocalDate.parse(dobText); // yyyy-MM-dd
            } catch (Exception ex) {
                txtErrors.setText("Ngày sinh không đúng định dạng. Vui lòng nhập yyyy-MM-dd.");
                return;
            }
        } else {
            c.dob = null;
        }

        // validate
        var errors = validator.validate(c);
        if (!errors.isEmpty()) {
            txtErrors.setText(String.join("\n", errors));
            return;
        }

        // insert DB
        try {
            String hash = PasswordUtil.sha256(c.password);
            dao.insert(c, hash);
            txtErrors.setText("");
            JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi lưu CSDL: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onReset(ButtonGroup genderGroup) {
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtPass.setText("");
        txtConfirm.setText("");
        txtDob.setText("");
        genderGroup.clearSelection();
        chkTerms.setSelected(false);
        txtErrors.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterForm().setVisible(true));
    }
}
