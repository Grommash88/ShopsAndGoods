package com.grommash88.app.util.logger;

import com.mongodb.client.AggregateIterable;
import java.util.Arrays;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.bson.Document;

public class AppLogger {

  private static final Marker EXCEPTION = MarkerManager.getMarker("EXCEPTION");
  private static final Marker CONSOLE = MarkerManager.getMarker("CONSOLE");
  private static final Logger LOGGER = LogManager.getRootLogger();

  public static void logException(Exception e) {

    LOGGER.error(EXCEPTION, Arrays.toString(e.getStackTrace()).concat(System.lineSeparator())
        .concat(e.getMessage()));
    LOGGER.info(CONSOLE, e.getMessage());
  }

  public static void logMessage(String message) {

    LOGGER.info(CONSOLE, message);
  }

  public static void showStatistic(AggregateIterable<Document> stat, double targetPrice) {

    Consumer<Document> printStatisticBlock = document ->
        AppLogger.logMessage(
            String.format(Messages.PRODUCT_STATISTICS_BY_MARKET_MSG.getMsg(),
                    document.getString("shopName")).concat(String.format(
                    Messages.COUNT_OF_PRODUCTS_IN_THE_MARKET_MSG.getMsg(), document
                        .getInteger("productsCount")))
                .concat(
                    String.format(Messages.AVERAGE_PRICE_OF_A_PRODUCTS_IN_THE_MARKET_MSG.getMsg(),
                        document.get("avgPrice")).concat(String.format(
                            Messages.THE_COUNT_OF_PRODUCTS_BELOW_THE_TARGET_PRICE_MSG.getMsg(),
                            targetPrice, document.getInteger("priceLessLimitCount"))
                        .concat(String.format(
                            Messages.THE_MOST_EXPENSIVE_AND_CHEAPEST_PRODUCTS_IN_THE_MARKET_MSG
                                .getMsg(),
                            document.get("maxPriceProductName"),
                            document.get("maxPrice"),
                            document.get("maxPriceProductName"),
                            document.get("minPrice"))))));

    stat.forEach(printStatisticBlock);
  }
}

