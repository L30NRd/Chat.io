package com.projetos.mongopoo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UsuarioFrame extends JFrame {

    private final boolean isAdmin;
    private JTextArea areaChat;
    private JTextField campoMensagem;
    private JButton btnEnviar;

    private JList<UsuarioInfo> listaUsuarios;
    private DefaultListModel<UsuarioInfo> listaModel;

    private JButton btnEditar;
    private JButton btnDestruir;

    private final LoginController controller = new LoginController();
    private final String usuarioAtual;

    public UsuarioFrame(String usuarioAtual) {
        this.usuarioAtual = usuarioAtual;
        this.isAdmin = usuarioAtual.equals("admin");

        setTitle("Painel do Usuário");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        listaModel = new DefaultListModel<>();
        listaUsuarios = new JList<>(listaModel);
        listaUsuarios.setCellRenderer(new UsuarioRenderer()); // Custom renderer
        JScrollPane scrollUsuarios = new JScrollPane(listaUsuarios);
        carregarUsuarios();

        areaChat = new JTextArea();
        areaChat.setEditable(false);
        JScrollPane scrollChat = new JScrollPane(areaChat);

        campoMensagem = new JTextField();
        btnEnviar = new JButton("Enviar");

        JPanel painelMensagem = new JPanel(new BorderLayout(5, 5));
        painelMensagem.add(campoMensagem, BorderLayout.CENTER);
        painelMensagem.add(btnEnviar, BorderLayout.EAST);

        JPanel painelChat = new JPanel(new BorderLayout(10, 10));
        painelChat.add(scrollChat, BorderLayout.CENTER);
        painelChat.add(painelMensagem, BorderLayout.SOUTH);

        btnEditar = new JButton("Editar Conta");
        btnDestruir = new JButton("Destruir Conta");

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 10, 10));
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnDestruir);

        JPanel painelDireita = new JPanel(new BorderLayout(10, 10));
        painelDireita.add(painelChat, BorderLayout.CENTER);
        painelDireita.add(painelBotoes, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollUsuarios, painelDireita);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.3);
        add(splitPane);

        btnEditar.addActionListener(this::editarConta);
        btnDestruir.addActionListener(this::destruirConta);
        btnEnviar.addActionListener(e -> {
            String texto = campoMensagem.getText().trim();
            if (!texto.isEmpty()) {
                controller.enviarMensagem(usuarioAtual, texto);
                campoMensagem.setText("");
                carregarMensagens();
            }
        });

        carregarMensagens();
        setVisible(true);
    }

    private void carregarUsuarios() {
        List<UsuarioInfo> usuarios = controller.getInformacoesUsuarios(); // lista com username e email
        listaModel.clear();
        for (UsuarioInfo user : usuarios) {
            listaModel.addElement(user);
        }
    }

    private void carregarMensagens() {
        List<String> mensagens = controller.getMensagens();
        areaChat.setText("");
        for (String msg : mensagens) {
            areaChat.append(msg + "\n");
        }
    }

    private void editarConta(ActionEvent e) {
        UsuarioInfo selecionado = listaUsuarios.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.");
            return;
        }

        if (!isAdmin && !selecionado.username.equals(usuarioAtual)) {
            JOptionPane.showMessageDialog(this, "Você só pode editar sua própria conta.");
            return;
        }

        if (isAdmin && selecionado.username.equals("admin")) {
            JOptionPane.showMessageDialog(this, "O admin não pode editar sua própria conta.");
            return;
        }

        EditarContaDialog dialog = new EditarContaDialog(this, controller, selecionado.username);
        dialog.setVisible(true);
        carregarUsuarios(); // Atualiza a lista após edição
    }


    private void destruirConta(ActionEvent e) {
        UsuarioInfo selecionado = listaUsuarios.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.");
            return;
        }

        if (!isAdmin && !selecionado.username.equals(usuarioAtual)) {
            JOptionPane.showMessageDialog(this, "Você só pode excluir sua própria conta.");
            return;
        }

        if (isAdmin && selecionado.username.equals("admin")) {
            JOptionPane.showMessageDialog(this, "O admin não pode excluir sua própria conta.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir a conta de " + selecionado.username + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.removerUsuario(selecionado.username)) {
                JOptionPane.showMessageDialog(this, "Conta removida com sucesso.");
                if (selecionado.username.equals(usuarioAtual)) dispose(); // se for o próprio usuário
                carregarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover conta.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Renderer customizado para exibir nome + email com linha separadora
    private static class UsuarioRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = new JLabel();
            if (value instanceof UsuarioInfo usuario) {
                label.setText("<html><b>" + usuario.username + "</b><br><small>" + usuario.email + "</small></html>");
            } else {
                label.setText(value.toString());
            }
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            label.setOpaque(true);
            label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return label;
        }
    }
}
