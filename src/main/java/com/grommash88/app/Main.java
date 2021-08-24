package com.grommash88.app;

import com.grommash88.app.repository.RepositoryImpl;
import com.grommash88.app.util.logger.AppLogger;
import com.grommash88.app.util.command_handler.CommandHandler;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

  public static void main(String[] args) {

    RepositoryImpl mongoRepository = new RepositoryImpl();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      try {

        AppLogger.logMessage("Enter command:");
        String command = reader.readLine().trim();
        if (!CommandHandler.executeAndContinue(command, mongoRepository)) {
          break;
        }
      } catch (Exception e) {

        AppLogger.logException(e);
      }
    }
  }
}

