package com.grommash88.app.util.logger;

public enum Messages {

  EXIT_MSG("EXIT from program."),

  MARKET_EXIST_MSG("The \"%s\" store already exists."),
  MARKET_NO_EXIST_MSG("The \"%s\" store does not exists."),
  PRODUCT_EXIST_MSG("Product \"%s\" already exist."),
  PRODUCT_NO_EXIST_MSG("Product \"%s\" does not exists."),
  INCORRECT_PRICE_FORMAT_MSG("\"%s\" invalid price format. Correct format 100 or 100.99"),

  PRODUCT_STATISTICS_BY_MARKET_MSG("Product statistics by market \"%s\":\n"),
  COUNT_OF_PRODUCTS_IN_THE_MARKET_MSG("%s items of products.\n"),
  AVERAGE_PRICE_OF_A_PRODUCTS_IN_THE_MARKET_MSG("Average price of a product: %s\n"),
  THE_COUNT_OF_PRODUCTS_BELOW_THE_TARGET_PRICE_MSG("The count of products below %s, %s items\n"),
  THE_MOST_EXPENSIVE_AND_CHEAPEST_PRODUCTS_IN_THE_MARKET_MSG(
      "The most expensive product: %s - %s.\nThe most cheapest product: %s - %s.\n"),

  INVALID_TARGET_PRICE("Invalid target price"),
  INVALID_COMMAND("Invalid command.\n-ADD_MARKET Девяточка\n-DELETE_MARKET Девяточка."
      .concat("\n-ADD_PRODUCT Вафли 54.\n-DELETE_PRODUCT Вафли.\n-PUT_PRODUCT Вафли Девяточка.")
      .concat("\n-REPORT Девяточка.\n-EXIT.")),
  MARKET_ADDED("Market \"%s\" has been successfully added to repository."),
  MARKET_DELETED("Market \"%s\" successfully deleted from repository."),
  PRODUCT_ADDED("Product \"%s\" has been successfully added to repository."),
  PRODUCT_DELETED("Product \"%s\" successfully deleted from repository."),
  THE_PRODUCT_LISTED_FOR_SALE(
      "Product \"%s\" has been successfully put up for sale in the \"%s\" market.");

  private final String msg;

  Messages(String msg){
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}

