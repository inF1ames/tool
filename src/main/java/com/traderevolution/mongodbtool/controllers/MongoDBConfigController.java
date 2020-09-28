/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.controllers;

import com.traderevolution.mongodbtool.Context;
import com.traderevolution.mongodbtool.Main;
import com.traderevolution.mongodbtool.view.FxmlView;
import com.traderevolution.mongodbtool.view.StageManager;
import java.util.Collections;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class MongoDBConfigController implements FxmlController {

  private final StageManager stageManager;

  @Autowired
  private Context context;
  @FXML
  private TextField pathField;
  @FXML
  private TextField sizeField;
  @FXML
  private Button nextButton;
  @FXML
  private Label statusLabel;

  @Autowired
  @Lazy
  public MongoDBConfigController(StageManager stageManager) {
    this.stageManager = stageManager;
  }

  @Override
  public void initialize() {
    setUpValidation(pathField);
    setUpValidation(sizeField);
    sizeField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        sizeField.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });
  }

  @FXML
  public void next(ActionEvent event) {
      Platform.runLater(() -> {
        if (Strings.isEmpty(pathField.getText()) || !pathField.getText().startsWith("mongodb://")) {
          statusLabel.setText("Wrong connection string");
        } else if (Strings.isEmpty(sizeField.getText())) {
          statusLabel.setText("Storage size can't be empty");
        } else {
          context.setPath(pathField.getText());
          context.setSize(Long.parseLong("100"));
          stageManager.switchScene(FxmlView.CHOOSE_FILES);
          Main.mainStage.getScene().setCursor(Cursor.DEFAULT);
        }
      });
  }

  private void setUpValidation(final javafx.scene.control.TextField tf) {
    tf.focusedProperty().addListener((observable, oldValue, newValue) -> validate(tf));
  }

  private void validate(TextField tf) {
    ObservableList<String> styleClass = tf.getStyleClass();
    if (Strings.isEmpty(tf.getText())) {
      if (!styleClass.contains("error")) {
        styleClass.add("error");
      }
    } else {
      styleClass.removeAll(Collections.singleton("error"));
    }
  }
}
