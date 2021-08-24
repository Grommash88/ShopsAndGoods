package com.grommash88.app.database;


import com.grommash88.app.database.props.Props;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDB implements AutoCloseable {

  private static final Props PROPS = Props.INSTANCE;
  private static MongoDB instance;

  private final MongoClient mongoClient;
  private final MongoCollection<Document> markets;
  private final MongoCollection<Document> products;

  public static MongoDB getInstance() {
    if (instance == null) {
      instance = new MongoDB();
    }
    return instance;
  }

  private MongoDB() {

    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(new ConnectionString(PROPS.getMongoURI()))
        .retryWrites(true)
        .build();

    mongoClient = MongoClients.create(settings);

    MongoDatabase database = mongoClient.getDatabase(PROPS.getDbName());
    markets = database.getCollection(PROPS.getCollectionShops());
    products = database.getCollection(PROPS.getCollectionProducts());

  }

  public MongoCollection<Document> getMarkets() {

    return markets;
  }

  public MongoCollection<Document> getProducts() {

    return products;
  }

  @Override
  public void close() throws Exception {

    mongoClient.close();
  }

  private void dropCollections(MongoDatabase database) {
    database.getCollection(PROPS.getCollectionShops()).drop();
    database.getCollection(PROPS.getCollectionProducts()).drop();
  }
}
