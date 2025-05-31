package com.projetos.mongopoo;

import javax.swing.*;
import java.awt.*;

public class EditarContaDialog extends JDialog {

    public EditarContaDialog(JFrame parent, LoginController controller, String username) {
        super(parent, "Editar Conta", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 1, 5, 5));

        JTextField campoEmail = new JTextField();
        JPasswordField campoSenha = new JPasswordField();
        JButton btnSalvar = new JButton("Salvar");

        add(new JLabel("Novo Email:"));
        add(campoEmail);
        add(new JLabel("Nova Senha:"));
        add(campoSenha);
        add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            String novoEmail = campoEmail.getText().trim();
            String novaSenha = new String(campoSenha.getPassword()).trim();

            if (controller.atualizarUsuario(username, novoEmail, novaSenha)) {
                JOptionPane.showMessageDialog(this, "Conta atualizada com sucesso.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar conta.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
