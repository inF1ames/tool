/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.mongodb;

import com.traderevolution.mongodbtool.Context;
import com.traderevolution.mongodbtool.controllers.model.FileModel;
import com.traderevolution.mongodbtool.controllers.model.ResultModel;
import com.traderevolution.trading.util.files.storage.FileValidator;
import com.traderevolution.trading.util.files.storage.core.FilePath;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class FileSaver {

  private final Context context;
  private final MongoFileStorageFactoryImpl factory;
  private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

  @Autowired
  @Lazy
  public FileSaver(Context context, MongoFileStorageFactoryImpl factory) {
    this.context = context;
    this.factory = factory;
  }

  public void save() {
    for (FileModel model : context.getToUpload()) {
      final String fileName = model.getName();
      try (FileInputStream is = new FileInputStream(model.getFile())) {
        if (FileValidator.isValidFileName(fileName)) {
          final FilePath path = new FilePath("javaFX" + FilePath.SEPARATOR + fileName);
          context.getUploadInfo()
              .add(new ResultModel("Attempting to write file", fileName, getDateTimeAsString()));
          factory.save(path, is);
          context.getUploadInfo()
              .add(
                  new ResultModel("Successfully written to file", fileName, getDateTimeAsString()));
        } else {
          context.getUploadInfo().add(
              new ResultModel("Skip uploading. Invalid file name", fileName,
                  getDateTimeAsString()));
        }
      } catch (IOException e) {
        context.getUploadInfo().add(
            new ResultModel("Skip uploading. " + e.getMessage(), fileName,
                getDateTimeAsString()));
      }
    }
  }

  private String getDateTimeAsString() {
    return format.format(new Date(System.currentTimeMillis()));
  }

}
