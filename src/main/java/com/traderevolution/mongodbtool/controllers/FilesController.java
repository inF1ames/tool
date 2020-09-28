/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.controllers;

import com.traderevolution.mongodbtool.Context;
import com.traderevolution.mongodbtool.Main;
import com.traderevolution.mongodbtool.controllers.model.FileModel;
import com.traderevolution.mongodbtool.view.FxmlView;
import com.traderevolution.mongodbtool.view.StageManager;
import java.io.File;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class FilesController implements FxmlController {

  private final StageManager stageManager;

  @Autowired
  private Context context;

  @FXML
  private TableView<FileModel> table;
  @FXML
  private Button chooseFileButton;

  private ObservableList<FileModel> files;

  @Autowired
  @Lazy
  public FilesController(StageManager stageManager) {
    this.stageManager = stageManager;
  }

  @Override
  public void initialize() {
    files = FXCollections.observableArrayList();
    TableColumn<FileModel, String> fileName = new TableColumn<>("Name");
    fileName.setCellValueFactory(new PropertyValueFactory<>("name"));
    TableColumn<FileModel, String> fileSize = new TableColumn<>("Size");
    fileSize.setCellValueFactory(new PropertyValueFactory<>("size"));
    TableColumn<FileModel, FileModel> remove = new TableColumn<>("Remove");
    remove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    remove.setCellFactory(param -> new TableCell<FileModel, FileModel>() {
      private final Button deleteButton = new Button("delete");

      @Override
      protected void updateItem(FileModel item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
          setGraphic(null);
          return;
        }
        deleteButton.setStyle("-fx-background-color: #666666");
        deleteButton.setTextFill(Paint.valueOf("#f5efef"));
        setAlignment(Pos.CENTER);
        setGraphic(deleteButton);
        deleteButton.setOnAction(event -> getTableView().getItems().remove(item));
      }
    });

    table.getColumns().setAll(fileName, fileSize, remove);

    table.setItems(files);
  }

  public void add(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose files");
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
    chooseFileButton.setOnAction(event -> {
      final List<File> files = fileChooser.showOpenMultipleDialog(Main.mainStage);
      if (files != null) {
        files.forEach(file -> {
          final FileModel model = new FileModel(file.getName(), file.length(), file);
          this.files.add(model);
          context.getToUpload().add(model);
        });
      }
    });

  }

  public void next(ActionEvent actionEvent) {
    stageManager.switchScene(FxmlView.PROCESS_UPLOAD);
  }

  public void back(ActionEvent actionEvent) {
    stageManager.switchScene(FxmlView.MONGO_DB_SETTINGS);
  }
}
