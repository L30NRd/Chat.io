package com.projetos.mongopoo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


public class LoginController {

    private final MongoCollection<Document> collection;

    public LoginController() {
        this.collection = MongoConnection.getCollection();
    }

    public boolean registrarUsuario(String username, String email, String senha) {
        Document usuarioExistente = collection.find(
                Filters.or(
                        Filters.eq("username", username),
                        Filters.eq("email", email)
                )).first();

        if (usuarioExistente != null) {
            return false;
        }

        Document novoUsuario = new Document("username", username)
                .append("email", email)
                .append("senha", senha);

        collection.insertOne(novoUsuario);
        return true;
    }

    public boolean verificarLogin(String username, String senha) {
        Document usuario = collection.find(Filters.eq("username", username)).first();
        return usuario != null && senha.equals(usuario.getString("senha"));
    }

    public boolean isAdmin(String username, String senha) {
        return "admin".equals(username) && "admin123".equals(senha);
    }

    public boolean removerUsuario(String username) {
        try {
            MongoCollection<Document> colecao = MongoConnection.getCollection();
            DeleteResult resultado = colecao.deleteOne(Filters.eq("username", username));
            return resultado.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getTodosUsuarios() {
        List<String> listaUsuarios = new ArrayList<>();
        try {
            FindIterable<Document> documentos = collection.find();
            for (Document doc : documentos) {
                String username = doc.getString("username");
                if (username != null) {
                    listaUsuarios.add(username);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }
    public void enviarMensagem(String remetente, String texto) {
        MongoCollection<Document> mensagens = MongoConnection.getDatabase().getCollection("mensagens");

        Document mensagem = new Document("remetente", remetente)
                .append("texto", texto)
                .append("timestamp", new Date());

        mensagens.insertOne(mensagem);
    }

    public List<String> getMensagens() {
        MongoCollection<Document> mensagens = MongoConnection.getDatabase().getCollection("mensagens");

        List<String> lista = new ArrayList<>();
        for (Document doc : mensagens.find().sort(Sorts.ascending("timestamp"))) {
            String remetente = doc.getString("remetente");
            String texto = doc.getString("texto");
            lista.add(remetente + ": " + texto);
        }
        return lista;
    }
    public boolean atualizarUsuario(String username, String novoEmail, String novaSenha) {
        try {
            Document atualizacao = new Document();
            if (novoEmail != null && !novoEmail.isEmpty()) {
                atualizacao.append("email", novoEmail);
            }
            if (novaSenha != null && !novaSenha.isEmpty()) {
                atualizacao.append("senha", novaSenha);
            }

            if (!atualizacao.isEmpty()) {
                collection.updateOne(Filters.eq("username", username), new Document("$set", atualizacao));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<UsuarioInfo> getInformacoesUsuarios() {
        List<UsuarioInfo> lista = new ArrayList<>();
        try {
            FindIterable<Document> documentos = collection.find();
            for (Document doc : documentos) {
                String username = doc.getString("username");
                String email = doc.getString("email");
                if (username != null && email != null) {
                    lista.add(new UsuarioInfo(username, email));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

}
