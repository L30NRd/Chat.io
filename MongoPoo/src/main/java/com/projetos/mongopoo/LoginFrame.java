package com.projetos.mongopoo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField txtUsernameLogin;
    private JPasswordField txtSenhaLogin;
    private JTextField txtUsernameRegister;
    private JTextField txtEmailRegister;
    private JPasswordField txtSenhaRegister;
    private JPasswordField txtConfirmSenhaRegister;

    private final LoginController controller = new LoginController();

    public LoginFrame() {
        setTitle("Sistema de Login e Registro");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- LOGIN ---
        JPanel panelLogin = new JPanel(new GridLayout(3, 2));
        panelLogin.add(new JLabel("Username:"));
        txtUsernameLogin = new JTextField();
        panelLogin.add(txtUsernameLogin);

        panelLogin.add(new JLabel("Senha:"));
        txtSenhaLogin = new JPasswordField();
        panelLogin.add(txtSenhaLogin);

        JButton btnLogin = new JButton("Entrar");
        panelLogin.add(btnLogin);

        // --- REGISTRO ---
        JPanel panelRegister = new JPanel(new GridLayout(5, 2));
        panelRegister.add(new JLabel("Username:"));
        txtUsernameRegister = new JTextField();
        panelRegister.add(txtUsernameRegister);

        panelRegister.add(new JLabel("Email:"));
        txtEmailRegister = new JTextField();
        panelRegister.add(txtEmailRegister);

        panelRegister.add(new JLabel("Senha:"));
        txtSenhaRegister = new JPasswordField();
        panelRegister.add(txtSenhaRegister);

        panelRegister.add(new JLabel("Confirmar Senha:"));
        txtConfirmSenhaRegister = new JPasswordField();
        panelRegister.add(txtConfirmSenhaRegister);

        JButton btnRegister = new JButton("Registrar");
        panelRegister.add(btnRegister);

        // --- TABS ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Login", panelLogin);
        tabbedPane.addTab("Registrar", panelRegister);
        add(tabbedPane, BorderLayout.CENTER);

        // --- AÇÕES ---

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsernameLogin.getText().trim();
                String senha = new String(txtSenhaLogin.getPassword()).trim();

                if (username.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (controller.verificarLogin(username, senha)) {
                    JOptionPane.showMessageDialog(null, "Login bem-sucedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    UsuarioFrame usuarioFrame = new UsuarioFrame(username);
                    usuarioFrame.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsernameRegister.getText().trim();
                String email = txtEmailRegister.getText().trim();
                String senha = new String(txtSenhaRegister.getPassword()).trim();
                String confirmarSenha = new String(txtConfirmSenhaRegister.getPassword()).trim();

                if (username.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!senha.equals(confirmarSenha)) {
                    JOptionPane.showMessageDialog(null, "As senhas não coincidem.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (controller.registrarUsuario(username, email, senha)) {
                    JOptionPane.showMessageDialog(null, "Registro bem-sucedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou email já cadastrados.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
