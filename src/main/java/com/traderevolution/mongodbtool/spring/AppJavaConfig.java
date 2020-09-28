/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.spring;

import com.traderevolution.mongodbtool.view.StageManager;
import java.io.IOException;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AppJavaConfig {

  @Autowired
  private SpringFXMLLoader springFXMLLoader;

  @Bean
  @Lazy(value = true)
  public StageManager stageManager(Stage stage) throws IOException {
    return new StageManager(springFXMLLoader, stage);
  }
}
