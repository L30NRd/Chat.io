package com.projetos.mongopoo;

import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MongoConnection.conectar();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
