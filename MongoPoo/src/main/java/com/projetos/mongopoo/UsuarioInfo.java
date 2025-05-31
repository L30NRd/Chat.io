package com.projetos.mongopoo;

public class UsuarioInfo {
    public final String username;
    public final String email;

    public UsuarioInfo(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @Override
    public String toString() {
        return username + " - " + email;
    }
}
