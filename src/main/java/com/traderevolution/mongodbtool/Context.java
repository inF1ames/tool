/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool;

import com.traderevolution.mongodbtool.controllers.model.FileModel;
import com.traderevolution.mongodbtool.controllers.model.ResultModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

@Component
public class Context {

  private String path;
  private long size;
  private ObservableList<FileModel> toUpload = FXCollections.observableArrayList();
  private ObservableList<ResultModel> uploadInfo = FXCollections.observableArrayList();

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public ObservableList<FileModel> getToUpload() {
    return toUpload;
  }

  public void setToUpload(ObservableList<FileModel> toUpload) {
    this.toUpload = toUpload;
  }

  public ObservableList<ResultModel> getUploadInfo() {
    return uploadInfo;
  }

  public void setUploadInfo(ObservableList<ResultModel> uploadInfo) {
    this.uploadInfo = uploadInfo;
  }

  public void clearContext() {
    this.path = null;
    this.size = 0;
    this.toUpload.clear();
    this.uploadInfo.clear();
  }
}
