package org.datavaultplatform.common.model.dao;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

public class SQLAppender extends AppenderBase<ILoggingEvent> {

  private static final String QUERY_NAME = "QUERY_NAME";

  private static ThreadLocal<GroupedSQL> SQL_TL = ThreadLocal.withInitial(GroupedSQL::new);

  @Override
  protected void append(ILoggingEvent event) {
    GroupedSQL grouped = SQL_TL.get();
    String sql = event.getFormattedMessage();
    grouped.addSQL(sql);
  }

  @ToString
  public static class GroupedSQL {
    public GroupedSQL(String name){
      this.name = name;
    }
    public GroupedSQL(){
      this.name = null;
    }
    final String name;
    final List<String> queries= new ArrayList();

    GroupedSQL getForQuery(String name){
      if(name != null && this.name.equals(name)){
        return this;
      } else {
        return new GroupedSQL(name);
      }
    }
    void addSQL(String sql) {
      this.queries.add(sql);
    }
  }

  public static GroupedSQL getGroupedSQL() {
    return SQL_TL.get();
  }

  public static void setQueryName(String queryName){
    SQL_TL.set(new GroupedSQL(queryName));
  }

  public static void clearQueryName() {
    SQL_TL.set(new GroupedSQL(null));
  }
}
