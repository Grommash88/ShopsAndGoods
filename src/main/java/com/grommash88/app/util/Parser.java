package com.grommash88.app.util;

import com.grommash88.app.model.Market;
import com.grommash88.app.model.Product;
import java.util.HashSet;
import org.bson.Document;

public class Parser {

  private static final int ZERO = 0;

  public static Market parseStringToMarket(String marketName){
    return Market.builder()
        .name(marketName)
        .products(new HashSet<Product>())
        .build();
  }

  public static Document parseMarketToDocument(Market market){
    return new Document()
        .append("name", market.getName())
        .append("products", market.getProducts());
  }

  public static Product parseStringToProduct(String productDescription){
    return Product.builder()
        .name(productDescription.substring(ZERO, productDescription.lastIndexOf(" ")))
        .price(Integer.parseInt(productDescription.substring(productDescription.lastIndexOf(" ")).trim()))
        .build();
  }

  public static Document parseProductToDocument(Product product){
    return new Document()
        .append("name", product.getName())
        .append("price", product.getPrice());
  }
}

