package VIEW;

import DAO.CheckEmailDAO;
import DTO.UsuarioDTO;
import VIEW.frLoginVIEW;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.sql.ResultSet;

public class frmCadastroUsuarioVIEW extends JFrame {
    private JTextField nomeField, emailField, enderecoField;
    private JFormattedTextField telefoneField, cpfField, dataNascimentoField;
    private JPasswordField senhaField, confirmSenhaField;
    private JComboBox<String> generoComboBox;
    private JLabel feedbackLabel;
    private JCheckBox termosCheckBox;

    public frmCadastroUsuarioVIEW() {
        setTitle("Cadastro de Pessoa");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Cadastro de Pessoa");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        
        mainPanel.add(createFieldPanel("Nome:", nomeField = new JTextField()));
        mainPanel.add(createFieldPanel("Email:", emailField = new JTextField()));
        mainPanel.add(createPasswordPanel("Senha:", senhaField = new JPasswordField()));
        mainPanel.add(createPasswordPanel("Confirmar Senha:", confirmSenhaField = new JPasswordField()));
        mainPanel.add(createFormattedFieldPanel("Telefone:", telefoneField = createFormattedField("###########")));
        mainPanel.add(createFieldPanel("Endereço:", enderecoField = new JTextField()));
        mainPanel.add(createFormattedFieldPanel("CPF:", cpfField = createFormattedField("###########")));
        mainPanel.add(createFormattedFieldPanel("Data de Nascimento:", dataNascimentoField = createFormattedField("####-##-##")));
        mainPanel.add(createComboBoxPanel("Gênero:", generoComboBox = new JComboBox<>(new String[]{"M", "F", "Outro"})));

        termosCheckBox = new JCheckBox("Li e aceito os Termos de Serviço");
        termosCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(termosCheckBox);

        feedbackLabel = new JLabel();
        feedbackLabel.setForeground(Color.RED);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(feedbackLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.setBackground(new Color(0, 123, 255));
        cadastrarButton.setForeground(Color.WHITE);
        cadastrarButton.setFocusPainted(false);
        cadastrarButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        cadastrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = "3";
                String tipo = "Pessoa";

                if (!new String(senhaField.getPassword()).equals(new String(confirmSenhaField.getPassword()))) {
                    feedbackLabel.setText("As Senhas precisam ser iguais");
                    feedbackLabel.setForeground(Color.RED);
                    return;
                }

                if (!termosCheckBox.isSelected()) {
                    feedbackLabel.setText("Você precisa aceitar os Termos de Serviço.");
                    feedbackLabel.setForeground(Color.RED);
                    return;
                }

                if (nomeField.getText().isEmpty() || emailField.getText().isEmpty() || new String(senhaField.getPassword()).isEmpty() ||
                    telefoneField.getText().trim().isEmpty() || enderecoField.getText().isEmpty() || cpfField.getText().trim().isEmpty() ||
                    dataNascimentoField.getText().trim().isEmpty() || generoComboBox.getSelectedItem() == null) {
                    feedbackLabel.setText("Todos os campos devem ser preenchidos.");
                    feedbackLabel.setForeground(Color.RED);
                    return;
                }

                boolean cadastroSucesso = cadastrarPessoa(id, nomeField.getText(), emailField.getText(), new String(senhaField.getPassword()), telefoneField.getText(), enderecoField.getText(), cpfField.getText(), dataNascimentoField.getText(), generoComboBox.getSelectedItem().toString(), tipo);
                if (cadastroSucesso) {
                    feedbackLabel.setText("Usuário cadastrado com sucesso!");
                    feedbackLabel.setForeground(Color.GREEN);
                } else {
                    feedbackLabel.setText("Erro ao cadastrar usuário.");
                    feedbackLabel.setForeground(Color.RED);
                }
            }
        });
        buttonPanel.add(cadastrarButton);
        
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(40, 167, 69));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frLoginVIEW objfrloginview = new frLoginVIEW();
                objfrloginview.setVisible(true);
                objfrloginview.setTitle("Login");
                dispose();
            }
        });
        buttonPanel.add(loginButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFieldPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 30));
        textField.setPreferredSize(new Dimension(300, 30));
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPasswordPanel(String labelText, JPasswordField passwordField) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 30));
        passwordField.setPreferredSize(new Dimension(300, 30));
        panel.add(label, BorderLayout.WEST);
        panel.add(passwordField, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormattedFieldPanel(String labelText, JFormattedTextField formattedField) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 30));
        formattedField.setPreferredSize(new Dimension(300, 30));
        panel.add(label, BorderLayout.WEST);
        panel.add(formattedField, BorderLayout.CENTER);
        return panel;
    }

    private JFormattedTextField createFormattedField(String pattern) {
        try {
            MaskFormatter formatter = new MaskFormatter(pattern);
            formatter.setPlaceholderCharacter('_');
            return new JFormattedTextField(formatter);
        } catch (ParseException e) {
            e.printStackTrace();
            return new JFormattedTextField();
        }
    }

    private JPanel createComboBoxPanel(String labelText, JComboBox<String> comboBox) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 30));
        comboBox.setPreferredSize(new Dimension(300, 30));
        panel.add(label, BorderLayout.WEST);
        panel.add(comboBox, BorderLayout.CENTER);
        return panel;
    }

private boolean cadastrarPessoa(String id, String nome, String email, String senha, String telefone, String endereco, String cpf, String dataNascimento, String genero, String tipo) {
    String url = "jdbc:mysql://localhost:3306/a3psc?user=root&password=testea3psc123!";
    String sql = "INSERT INTO usuario (id, nome, telefone, endereço, cpf, nascimento, genero) VALUES (?, ?, ?, ?, ?, ?, ?)";
    String sql1 = "INSERT INTO autentic (email, senha) VALUES (?, ?)";
    String sql2 = "SELECT id FROM autentic ORDER BY id DESC LIMIT 1;";
    
    try {
        UsuarioDTO objusuariodto = new UsuarioDTO();
        objusuariodto.setEmail_usuario(email);
        objusuariodto.setSenha_usuario(senha);

        CheckEmailDAO objcheckemaildao = new CheckEmailDAO();
        ResultSet rscheckemaildao = objcheckemaildao.autenticacaoUsuario(objusuariodto);

        if (rscheckemaildao.next()) {
            feedbackLabel.setText("Email já cadastrado. Tente outro email.");
            feedbackLabel.setForeground(Color.RED);
            JOptionPane.showMessageDialog(null, "Email já cadastrado. Tente outro email.");
            return false;
        } else {
            
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                try (Connection conn = DriverManager.getConnection(url);
                     PreparedStatement stmt = conn.prepareStatement(sql1)) {

                    stmt.setString(1, email);
                    stmt.setString(2, senha);
                    stmt.executeUpdate();
                    PreparedStatement stmt1 = conn.prepareStatement(sql2);
                    email = "aaaaaaaaaaaaaaaaa";
                    try (ResultSet rsid = stmt1.executeQuery()){
                        if (rsid.next()) {
                            id = rsid.getString("id");
                            try (Connection conn1 = DriverManager.getConnection(url);
                     PreparedStatement stmt2 = conn1.prepareStatement(sql)) {

                    stmt2.setString(1, id);
                    stmt2.setString(2, nome);
                    stmt2.setString(3, telefone);
                    stmt2.setString(4, endereco);
                    stmt2.setString(5, cpf);
                    stmt2.setString(6, dataNascimento);
                    stmt2.setString(7, genero);

                    stmt2.executeUpdate();
                    return true; // Retorna true para indicar sucesso na criação

                } catch (SQLException e) {
                    e.printStackTrace();
                    return false; // Retorna false para indicar falha na criação
                }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false; // Retorna false para indicar falha na criação
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false; // Retorna false para indicar falha na criação
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro no login: " + e.getMessage());
        return false; // Retorna false para indicar falha na criação
    }
        return false; // Retorna false para indicar falha na criação
}


    public static void main(String[] args) {
        frmCadastroUsuarioVIEW cadastroUsuario = new frmCadastroUsuarioVIEW();
        cadastroUsuario.setVisible(true);
    }
}
