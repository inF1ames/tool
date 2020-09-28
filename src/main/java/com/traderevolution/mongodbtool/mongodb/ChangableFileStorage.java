/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.mongodb;

import com.traderevolution.trading.util.files.storage.StructuredFileStorageMongo;
import com.traderevolution.trading.util.files.storage.core.FileStorage;

public class ChangableFileStorage implements ExpirableObject {

  private boolean expired = false;
  private FileStorage storage;

  public ChangableFileStorage(final FileStorage storage, final long size) {
    this.storage = storage;
    if (storage instanceof StructuredFileStorageMongo) {
      ((StructuredFileStorageMongo) storage).setMaxSize(size);
    }
  }

  public FileStorage getStorage() {
    return storage;
  }

  @Override
  public void expireObject() {
    expired = true;
    if (storage instanceof StructuredFileStorageMongo) {
      ((StructuredFileStorageMongo) storage).close();
    }
  }

  @Override
  public boolean isExpiredObject() {
    return expired;
  }
}
