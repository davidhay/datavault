package org.datavaultplatform.common.model.dao.custom;

import java.util.List;
import org.datavaultplatform.common.model.User;

public interface UserCustomDAO {

    List<User> search(String query);
}
