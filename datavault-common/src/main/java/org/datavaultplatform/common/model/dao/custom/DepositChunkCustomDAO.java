package org.datavaultplatform.common.model.dao.custom;

import java.util.List;
import org.datavaultplatform.common.model.DepositChunk;

public interface DepositChunkCustomDAO {
  List<DepositChunk> list(String sort);
}
