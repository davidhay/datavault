package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.DepositChunk;

import java.util.List;

public interface DepositChunkDAO extends BaseDAO<DepositChunk> {
  List<DepositChunk> list(String sort);
}
