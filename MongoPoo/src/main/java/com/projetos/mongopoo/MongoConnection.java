package com.projetos.mongopoo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoConnection {

    private static final String DATABASE_NAME = "javaswing";
    private static final String COLLECTION_NAME = "usuarios";
    private static final String URI = "mongodb+srv://leo:SuspeitoV@javaswing.sfyetvl.mongodb.net/?retryWrites=true&w=majority&appName=javaswing";

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    public static void conectar() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(URI);
            database = mongoClient.getDatabase(DATABASE_NAME);
            collection = database.getCollection(COLLECTION_NAME);
            System.out.println("✅ Conectado ao MongoDB Atlas");
        }
    }

    public static MongoCollection<Document> getCollection() {
        conectar();
        return collection;
    }

    public static void fecharConexao() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            System.out.println("🔌 Conexão com MongoDB encerrada.");
        }
    }
    
    public static MongoDatabase getDatabase() {
        conectar();
        return database;
    }

}
