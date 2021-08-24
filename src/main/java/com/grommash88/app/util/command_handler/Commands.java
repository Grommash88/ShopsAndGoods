package com.grommash88.app.util.command_handler;

public enum Commands {

  ADD_MARKET("ADD_MARKET"),
  DELETE_MARKET("DELETE_MARKET"),
  ADD_PRODUCT("ADD_PRODUCT"),
  DELETE_PRODUCT("DELETE_PRODUCT"),
  PUT_PRODUCT_UP_FOR_SALE_IN_A_MARKET("PUT_PRODUCT"),
  REPORT("REPORT"),
  EXIT("EXIT");

  private final String cmd;

  Commands(String cmd) {
    this.cmd = cmd;
  }

  public String getCmd() {
    return cmd;
  }
}
