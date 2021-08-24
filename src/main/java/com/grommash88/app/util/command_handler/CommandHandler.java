package com.grommash88.app.util.command_handler;

import com.grommash88.app.repository.RepositoryImpl;
import com.grommash88.app.util.logger.AppLogger;
import com.grommash88.app.util.logger.Messages;
import com.mongodb.client.AggregateIterable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import org.bson.Document;

public class CommandHandler {

  private static Commands command;
  private static String description;


  public static boolean executeAndContinue(String commandDescription,
      RepositoryImpl mongoRepository)
      throws Exception {

    if (isCommandDefined(commandDescription)) {

      switch (command) {
        case ADD_MARKET:
          if (!description.isBlank()) {
            mongoRepository.addMarket(description);
          } else {
            AppLogger.logMessage(Messages.INVALID_COMMAND.getMsg());
          }
          return true;
        case DELETE_MARKET:
          if (!description.isBlank()) {
            mongoRepository.deleteMarket(description);
          } else {
            AppLogger.logMessage(Messages.INVALID_COMMAND.getMsg());
          }
          return true;
        case ADD_PRODUCT:

          if (isCorrectDescription(description)) {

            if (!description.isBlank()) {
              mongoRepository.addProduct(description);
            } else {
              AppLogger.logMessage(Messages.INVALID_COMMAND.getMsg());
            }
            return true;
          } else {

            throw new IllegalArgumentException(String.format(
                Messages.INCORRECT_PRICE_FORMAT_MSG.getMsg(), description.split("\\s")[1]));
          }
        case DELETE_PRODUCT:

          if (!description.isBlank()) {
            mongoRepository.deleteProduct(description);
          } else {
            AppLogger.logMessage(Messages.INVALID_COMMAND.getMsg());
          }
          return true;
        case PUT_PRODUCT_UP_FOR_SALE_IN_A_MARKET:

          if (!description.isBlank()) {
            mongoRepository.putProductUpForSaleInAMarket(description);
          } else {
            AppLogger.logMessage(Messages.INVALID_COMMAND.getMsg());
          }
          return true;
        case REPORT:

          AppLogger.logMessage(Messages.INVALID_TARGET_PRICE.getMsg());
          BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
          double targetPrice = Double.parseDouble(reader.readLine().trim());

          AggregateIterable<Document> stat;
          if (description.isBlank()) {

            stat = mongoRepository.getStatisticsOfAllMarkets(targetPrice);

          } else {

            stat = mongoRepository.getStatisticsOfMarketProducts(description, targetPrice);
          }
          AppLogger.showStatistic(stat, targetPrice);
          return true;
        default:
          mongoRepository.close();
          return false;
      }
    } else {
      AppLogger.logMessage(Messages.INVALID_COMMAND.getMsg());
      return true;
    }
  }

  private static boolean isCommandDefined(String commandDescription) {

    String[] commandAndDescription = commandDescription.split("\\s", 2);
    command = Stream.of(Commands.values())
        .filter(cmd -> cmd.getCmd().equals(commandAndDescription[0]))
        .findFirst().orElse(null);

    if (command != null) {

      description = commandAndDescription.length == 2 ? commandAndDescription[1] : "";
      return true;
    } else {
      return false;
    }
  }

  private static boolean isCorrectDescription(String description) {

    return description.matches("\\D+\\s\\d+\\.?\\d*");
  }
}
