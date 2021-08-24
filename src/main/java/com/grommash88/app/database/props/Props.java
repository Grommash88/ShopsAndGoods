package com.grommash88.app.database.props;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
public enum Props {

  INSTANCE;

  private static final String PROPS_PATH = "src/main/resources/application.properties";

  private final String username;
  private final String password;
  private final String host;
  private final String port;
  private final String params;
  private final String dbName;
  private final String collectionShops;
  private final String collectionProducts;
  private final String mapPackage;

  Props() {
    Properties props = new Properties();

    try (InputStream in = new FileInputStream(PROPS_PATH)) {
      props.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.username = props.getProperty("mongodb.username");
    this.password = props.getProperty("mongodb.password");
    this.host = props.getProperty("mongodb.host");
    this.port = props.getProperty("mongodb.port");
    this.params = props.getProperty("mongodb.paramsAfterSlash");
    this.dbName = props.getProperty("mongodb.database");
    this.collectionShops = props.getProperty("mongodb.collectionShops");
    this.collectionProducts = props.getProperty("mongodb.collectionProducts");
    this.mapPackage = props.getProperty("morphia.mapPackage");
  }

  public String getMongoURI() {
    String credentials = (username.isBlank() || password.isBlank()) ?
        "" : String.format("%s:%s@", this.username, this.password);
    String portValue = this.port.isBlank() ?
        "" : String.format(":%s", this.port);
    String paramsValue = this.params.isBlank() ?
        "" : String.format("/%s", this.params);
    return String.format("mongodb://%s%s%s%s", credentials, host, portValue, paramsValue)
        .replaceAll("\\s+", "");
  }
}
