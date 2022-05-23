package org.datavaultplatform.common.model.dao;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseDAO<T> extends AbstractDAO<T,String>{
}

