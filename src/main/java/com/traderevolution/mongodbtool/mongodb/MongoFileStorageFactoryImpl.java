/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.mongodb;

import com.traderevolution.mongodbtool.Context;
import com.traderevolution.mongodbtool.exceptions.ConsumerWithException;
import com.traderevolution.trading.Logger;
import com.traderevolution.trading.util.files.storage.FileStorageInitializationFailedException;
import com.traderevolution.trading.util.files.storage.app.EAppStorage;
import com.traderevolution.trading.util.files.storage.core.ChangableFileStorageFactory;
import com.traderevolution.trading.util.files.storage.core.FilePath;
import com.traderevolution.trading.util.files.storage.core.FileStorage;
import com.traderevolution.trading.util.files.storage.core.FileStorageBuilder;
import com.traderevolution.trading.util.files.storage.core.FileStorageBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoFileStorageFactoryImpl implements ChangableFileStorageFactory {

  private final Context context;

  @Autowired
  public MongoFileStorageFactoryImpl(Context context) {
    this.context = context;
  }

  private volatile ChangableFileStorage mongoFileStorage;

  public Optional<FileStorage> getMongoFileStorage() {
    if (mongoFileStorage == null || mongoFileStorage.isExpiredObject()) {
      mongoFileStorage = createMongoFileStorage();
    }
    return mongoFileStorage == null
        ? Optional.empty()
        : Optional.ofNullable(mongoFileStorage.getStorage());
  }

  private ChangableFileStorage createMongoFileStorage() {
    final String uri = context.getPath();
    final long size =
        BigDecimal.valueOf(context.getSize())
            .multiply(new BigDecimal(1024 * 1024))
            .longValue(); // convert from mb to bytes
    try {
      FileStorageBuilder fsb = FileStorageBuilderFactory.newInstance().newFileStorageBuilder();
      ChangableFileStorage mongoStorage =
          new ChangableFileStorage(fsb.newStorage(EAppStorage.FILE_STORAGE.name(), uri), size);
      return mongoStorage;
    } catch (FileStorageInitializationFailedException e) {
      Logger.error(Logger.FILE_STORAGE, "Mongo file storage failed to initialize", e);
    }
    return null;
  }

  public void save(FilePath path, InputStream is) throws IOException {
    final ConsumerWithException<FileStorage, IOException> result =
        storage -> storage.insert(path, is);
    if (getMongoFileStorage().isPresent()) {
      result.accept(getMongoFileStorage().get());
    }
  }

  @Override
  public Optional<FileStorage> getStorage(EAppStorage storage) {
    switch (storage) {
      case FILE_STORAGE:
        return getMongoFileStorage();
      default:
        return Optional.empty();
    }
  }

  @Override
  public void reInit() {
    if (mongoFileStorage != null) {
      mongoFileStorage.expireObject();
    }
    mongoFileStorage = createMongoFileStorage();
  }
}
