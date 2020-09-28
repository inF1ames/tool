/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.controllers.model;

public class ResultModel {

  private static final String CSV_SEPARATOR = ",";
  private String event;
  private String fileName;
  private String dateTime;

  public ResultModel(String event, String fileName, String dateTime) {
    this.event = event;
    this.fileName = fileName;
    this.dateTime = dateTime;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append(event)
        .append(CSV_SEPARATOR)
        .append(fileName)
        .append(CSV_SEPARATOR)
        .append(dateTime)
        .toString();
  }
}

