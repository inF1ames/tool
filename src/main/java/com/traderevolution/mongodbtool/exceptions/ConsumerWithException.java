/*
 * Copyright TraderEvolution LTD. © 2018-2020. All rights reserved.
 */

package com.traderevolution.mongodbtool.exceptions;

public interface ConsumerWithException<T, EX extends Exception> {
  void accept(T t) throws EX;
}
