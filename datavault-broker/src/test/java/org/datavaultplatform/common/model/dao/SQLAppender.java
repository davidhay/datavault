package org.datavaultplatform.common.model.dao;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class SQLAppender extends AppenderBase<ILoggingEvent> {

  public static ThreadLocal<String> SQL_TL = new ThreadLocal<>();

  @Override
  protected void append(ILoggingEvent event) {
    String sql = event.getFormattedMessage();
    SQL_TL.set(sql);
  }

}
