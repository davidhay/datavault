package org.datavaultplatform.common.model.dao;

import java.util.List;
import org.datavaultplatform.common.model.User;
 
public interface UserDAO extends BaseDAO<User> {

    List<User> search(String query);

    long count();
}
