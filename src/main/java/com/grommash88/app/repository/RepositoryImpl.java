package com.grommash88.app.repository;

import com.grommash88.app.database.MongoDB;
import com.grommash88.app.util.logger.Messages;
import com.grommash88.app.repository.parent.Repository;
import com.grommash88.app.util.logger.AppLogger;
import com.grommash88.app.util.Parser;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Arrays;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class RepositoryImpl implements Repository, AutoCloseable {

  private final MongoDB mongoDB;
  private final MongoCollection<Document> markets;
  private final MongoCollection<Document> products;

  private final int ONE = 1;
  private final int ZERO = 0;

  public RepositoryImpl() {
    this.mongoDB = MongoDB.getInstance();
    this.markets = mongoDB.getMarkets();
    this.products = mongoDB.getProducts();
  }

  @Override
  public void addMarket(String marketName) {

    if (findMarketByName(marketName).isEmpty()){

      markets.insertOne(Parser.parseMarketToDocument(Parser.parseStringToMarket(marketName)));
      AppLogger.logMessage(String.format(Messages.MARKET_ADDED.getMsg(), marketName));
    } else {

      throw new IllegalArgumentException(
          String.format(Messages.MARKET_EXIST_MSG.getMsg(), marketName));
    }
  }

  @Override
  public void deleteMarket(String marketName) {

    if (findMarketByName(marketName).isPresent()) {

      markets.findOneAndDelete(eq("name", marketName));
      AppLogger.logMessage(String.format(Messages.MARKET_DELETED.getMsg(), marketName));
    } else {

      throw new IllegalArgumentException(
          String.format(Messages.MARKET_NO_EXIST_MSG.getMsg(), marketName));
    }
  }

  @Override
  public void addProduct(String productDescription) {

    String productName = productDescription
        .substring(ZERO, productDescription.lastIndexOf(" "));
    if (findProductByName(productName).isEmpty()) {

      products.insertOne(Parser
          .parseProductToDocument(Parser.parseStringToProduct(productDescription)));
      AppLogger.logMessage(String.format(Messages.PRODUCT_ADDED.getMsg(), productName));
    } else {

      throw new IllegalArgumentException(
          String.format(Messages.PRODUCT_EXIST_MSG.getMsg(), productName));
    }
  }

  @Override
  public void deleteProduct(String productName) {

    if (findProductByName(productName).isPresent()) {

      products.findOneAndDelete(eq("name", productName));
      AppLogger.logMessage(String.format(Messages.PRODUCT_DELETED.getMsg(), productName));
    } else {

      throw new IllegalArgumentException(
          String.format(Messages.PRODUCT_NO_EXIST_MSG.getMsg(), productName));
    }
  }

  @Override
  public void putProductUpForSaleInAMarket(String description) {

    String[] data = description.split("\\s");
    String marketName = data[ONE].trim();
    String productName = data[ZERO].trim();

    Document addedProductDoc = findProductByName(productName)
        .orElseThrow(() -> new IllegalArgumentException(String
            .format(Messages.PRODUCT_NO_EXIST_MSG.getMsg(), productName)));

    Document marketDoc = findMarketByName(marketName)
        .orElseThrow(() -> new IllegalArgumentException(String
            .format(Messages.MARKET_NO_EXIST_MSG.getMsg(), marketName)));

    markets.updateOne(marketDoc,
        new Document("$addToSet", new Document("products", addedProductDoc)));
    AppLogger.logMessage(String.format(
        Messages.THE_PRODUCT_LISTED_FOR_SALE.getMsg(), productName, marketName));
  }

  @Override
  public AggregateIterable<Document> getStatisticsOfMarketProducts(
      String marketName, double targetPrice) {

    return markets.aggregate(Arrays.asList(
        new Document("$match", new Document("name", new Document("$eq", marketName))),
        new Document("$unwind", "$products"),
        new Document("$sort", new Document("products.price", ONE)),
        new Document("$group", new Document("_id", "$_id")
            .append("shopName", new Document("$first", "$name"))
            .append("productsCount", new Document("$sum", ONE))
            .append("avgPrice", new Document("$avg", "$products.price"))
            .append("priceLessLimitCount", new Document("$sum",
                new Document("$cond", new Document("if",
                    new Document("$lt", Arrays.asList("$products.price", targetPrice)))
                    .append("then", ONE)
                    .append("else", ZERO))))
            .append("minPriceProductName", new Document("$first","$products.name"))
            .append("minPrice", new Document("$first","$products.price"))
            .append("maxPriceProductName", new Document("$last", "$products.name"))
            .append("maxPrice", new Document("$last", "$products.price")))
    ));
  }

  @Override
  public AggregateIterable<Document> getStatisticsOfAllMarkets(double targetPrice) {

    return markets.aggregate(Arrays.asList(
        new Document("$match", new Document("name", new Document("$exists", true))),
        new Document("$unwind", "$products"),
        new Document("$sort", new Document("products.price", ONE)),
        new Document("$group", new Document("_id", "$_id")
            .append("shopName", new Document("$first", "$name"))
            .append("productsCount", new Document("$sum", ONE))
            .append("avgPrice", new Document("$avg", "$products.price"))
            .append("priceLessLimitCount", new Document("$sum",
                new Document("$cond", new Document("if",
                    new Document("$lt", Arrays.asList("$products.price", targetPrice)))
                    .append("then", ONE)
                    .append("else", ZERO))))
            .append("minPriceProductName", new Document("$first","$products.name"))
            .append("minPrice", new Document("$first","$products.price"))
            .append("maxPriceProductName", new Document("$last", "$products.name"))
            .append("maxPrice", new Document("$last", "$products.price")))
    ));
  }

  private Optional<Document> findMarketByName(String name) {

    return Optional.ofNullable(markets.find(eq("name", name)).first());
  }

  private Optional<Document> findProductByName(String name) {

    return Optional.ofNullable(products.find(eq("name", name)).first());
  }

  @Override
  public void close() throws Exception {
    mongoDB.close();
    AppLogger.logMessage(Messages.EXIT_MSG.getMsg());
  }
}
