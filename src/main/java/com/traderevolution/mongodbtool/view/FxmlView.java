/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.view;

import java.util.ResourceBundle;

public enum FxmlView {

  MONGO_DB_SETTINGS {
    @Override
    String getTitle() {
      return getStringFromResourceBundle("login.title");
    }

    @Override
    String getFxmlFile() {
      return "/fxml/mongoDBSettings.fxml";
    }
  }, CHOOSE_FILES {
    @Override
    String getTitle() {
      return getStringFromResourceBundle("main.app.title");
    }

    @Override
    String getFxmlFile() {
      return "/fxml/files.fxml";
    }
  }, PROCESS_UPLOAD {
    @Override
    String getTitle() {
      return getStringFromResourceBundle("main.app.title");
    }

    @Override
    String getFxmlFile() {
      return "/fxml/processUpload.fxml";
    }
  };

  abstract String getTitle();
  abstract String getFxmlFile();

  String getStringFromResourceBundle(String key){
    return ResourceBundle.getBundle("bundle").getString(key);
  }
}
