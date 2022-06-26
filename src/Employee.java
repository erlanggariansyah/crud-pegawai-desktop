import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JLabel email;
    private JLabel password;
    private JTextField txtFname;
    private JTextField txtEmail;
    private JTextField txtPassword;
    private JButton save;
    private JTable table1;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton cariButton;
    private JTextField searchField;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Data Pegawai");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement preparedStatement;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdemployee", "root", "");
            System.out.println("Success!");
        } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
        }
    }

    void table_load() {
        try {
            preparedStatement = con.prepareStatement("SELECT * FROM pegawai");
            ResultSet resultSet = preparedStatement.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee() {
        connect();
        table_load();
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fullname, email, password;

                fullname = txtFname.getText();
                email = txtEmail.getText();
                password = txtPassword.getText();

                try {
                    preparedStatement = con.prepareStatement("INSERT INTO pegawai (fullname, email, password) VALUES (?,?,?)");
                    preparedStatement.setString(1, fullname);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, password);

                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Pegawai berhasil ditambahkan.");

                    txtFname.setText("");
                    txtEmail.setText("");
                    txtPassword.setText("");
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

                table_load();
            }
        });
        cariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = searchField.getText();
                    preparedStatement = con.prepareStatement("SELECT * FROM pegawai WHERE id = ?");
                    preparedStatement.setString(1, id);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next() == true) {
                        String fullname = resultSet.getString(2);
                        String email = resultSet.getString(3);
                        String password = resultSet.getString(4);

                        txtFname.setText(fullname);
                        txtEmail.setText(email);
                        txtPassword.setText(password);
                    } else {
                        txtFname.setText("");
                        txtEmail.setText("");
                        txtPassword.setText("");

                        JOptionPane.showMessageDialog(null, "Pegawai tidak ditemukan.");
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

                table_load();
            }
        });
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = searchField.getText();

                try {
                    preparedStatement = con.prepareStatement("DELETE FROM pegawai WHERE id = ?");
                    preparedStatement.setString(1, id);

                    int status = preparedStatement.executeUpdate();

                    if (status == 1) {
                        JOptionPane.showMessageDialog(null, "Pegawai berhasil dihapus.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Pegawai tidak ditemukan.");
                    }
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Pegawai tidak ditemukan.");
                    exception.printStackTrace();
                }

                table_load();
            }
        });
        ubahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = searchField.getText();
                if (id.equalsIgnoreCase("")) {
                    JOptionPane.showMessageDialog(null, "Pegawai tidak ditemukan");
                }

                String fullname, email, password;

                fullname = txtFname.getText();
                email = txtEmail.getText();
                password = txtPassword.getText();

                try {
                    preparedStatement = con.prepareStatement("UPDATE pegawai SET fullname = ?, email = ?, password = ? WHERE id = ?");
                    preparedStatement.setString(1, fullname);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, password);
                    preparedStatement.setString(4, id);

                    int status = preparedStatement.executeUpdate();

                    if (status == 1) {
                        JOptionPane.showMessageDialog(null, "Pegawai berhasil diedit.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Pegawai tidak ditemukan.");
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

                table_load();
            }
        });
    }
}
