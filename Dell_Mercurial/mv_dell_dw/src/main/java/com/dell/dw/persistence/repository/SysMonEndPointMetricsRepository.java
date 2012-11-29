package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.SysMonEndPointMetrics;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/1/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SysMonEndPointMetricsRepository extends IdentifiableEntityRepository<Long, SysMonEndPointMetrics> {

      public Collection<SysMonEndPointMetrics> getEndPointMetrices(Long endpointId);
}
