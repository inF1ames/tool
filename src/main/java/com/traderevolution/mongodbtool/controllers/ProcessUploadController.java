/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.controllers;

import com.traderevolution.mongodbtool.Context;
import com.traderevolution.mongodbtool.Main;
import com.traderevolution.mongodbtool.controllers.model.ResultModel;
import com.traderevolution.mongodbtool.mongodb.FileSaver;
import com.traderevolution.mongodbtool.view.FxmlView;
import com.traderevolution.mongodbtool.view.StageManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Lazy
@Scope("prototype")
public class ProcessUploadController implements FxmlController {

  private final StageManager stageManager;
  @Autowired
  private FileSaver fileSaver;
  @Autowired
  private Context context;
  @FXML
  private TableView<ResultModel> table;
  @FXML
  private MenuButton menuButton;

  @Autowired
  @Lazy
  public ProcessUploadController(StageManager stageManager) {
    this.stageManager = stageManager;
  }

  @PostConstruct
  public void init() {
    Thread thread = new Thread(() -> fileSaver.save());
    thread.setDaemon(true);
    thread.start();
  }

  @Override
  public void initialize() {
    final MenuItem csv = new MenuItem("Export to CSV");
    csv.setOnAction(e -> {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
      String fileName = "log_" + LocalDateTime.now().format(formatter) + ".csv";
      Path log = Paths.get(fileName);
      try {
        Files.write(log,
            context.getUploadInfo().stream().map(ResultModel::toString)
                .collect(Collectors.toList()),
            StandardCharsets.UTF_8, StandardOpenOption.CREATE);
      } catch (IOException ex) {
        ex.printStackTrace();
      }

    });
    final MenuItem table = new MenuItem("Show in table");
    table.setOnAction(e -> {
      Stage dialog = new Stage();
      final TextArea textArea = new TextArea();
      textArea.appendText(
          context.getUploadInfo().stream().map(ResultModel::toString)
              .collect(Collectors.joining("\n")));
      textArea.setPrefSize(600, 600);
      dialog.setScene(new Scene(textArea));
      dialog.initOwner(Main.mainStage);
      dialog.initModality(Modality.APPLICATION_MODAL);
      dialog.show();
    });
    menuButton.getItems().addAll(csv, table);

    TableColumn<ResultModel, String> eventColumn = new TableColumn<>("Event");
    eventColumn.setCellValueFactory(new PropertyValueFactory<>("event"));

    TableColumn<ResultModel, String> nameColumn = new TableColumn<>("File name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

    TableColumn<ResultModel, String> dateTimeColumn = new TableColumn<>("Date time");
    dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

    this.table.getColumns().setAll(eventColumn, nameColumn, dateTimeColumn);

    this.table.setItems(context.getUploadInfo());
  }

  public void back(ActionEvent actionEvent) {
    context.getUploadInfo().clear();
    context.getToUpload().clear();
    stageManager.switchScene(FxmlView.CHOOSE_FILES);
  }

  public void newWizard(ActionEvent actionEvent) {
    context.clearContext();
    stageManager.switchScene(FxmlView.MONGO_DB_SETTINGS);
  }

}
