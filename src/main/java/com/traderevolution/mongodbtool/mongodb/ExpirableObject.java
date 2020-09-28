/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.mongodb;

public interface ExpirableObject {

  void expireObject();

  boolean isExpiredObject();

}
