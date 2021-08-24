package com.grommash88.app.repository.parent;

import com.mongodb.client.AggregateIterable;
import org.bson.Document;

public interface Repository {

  void addMarket(String marketName);

  void deleteMarket(String marketName);

  void addProduct(String productDescription);

  void deleteProduct(String productName);

  void putProductUpForSaleInAMarket(String description);

  AggregateIterable<Document> getStatisticsOfMarketProducts(String marketName, double targetPrice);

  AggregateIterable<Document>  getStatisticsOfAllMarkets(double targetPrice);
}