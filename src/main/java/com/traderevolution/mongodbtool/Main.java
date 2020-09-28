package com.traderevolution.mongodbtool;

import com.traderevolution.mongodbtool.view.FxmlView;
import com.traderevolution.mongodbtool.view.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class
})
public class Main extends Application {

  protected ConfigurableApplicationContext springContext;
  protected StageManager stageManager;
  public static Stage mainStage;

  public static void main(final String[] args) {
    Application.launch(args);
  }

  @Override
  public void init() throws Exception {
    springContext = bootstrapSpringApplicationContext();
  }

  @Override
  public void start(Stage stage) throws Exception {
    mainStage = stage;
    stageManager = springContext.getBean(StageManager.class, stage);
    displayInitialScene();
  }

  @Override
  public void stop() throws Exception {
    springContext.close();
  }

  protected void displayInitialScene() {
    stageManager.switchScene(FxmlView.MONGO_DB_SETTINGS);
  }


  private ConfigurableApplicationContext bootstrapSpringApplicationContext() {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
    String[] args = getParameters().getRaw().toArray(new String[0]);
    builder.headless(false);
    return builder.run(args);
  }
}
