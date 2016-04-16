package com.hzih.gap.service;

import com.hzih.gap.domain.AuditReset;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-7-31
 * Time: 上午11:57
 * To change this template use File | Settings | File Templates.
 */
public interface AuditResetService {

    public String select(int start,int limit,Date startDate, Date endDate,
                         String businessName,String businessType,String resetStatus) throws Exception;

    public void insert(List<AuditReset> auditResets) throws Exception;

    public void updateResetStatus(String id, String fileSize) throws Exception;

    public List<AuditReset> update(List<AuditReset> auditResets) throws Exception;

    public void truncate(String startDate, String endDate,
                         String businessName, String businessType, String resetStatus) throws Exception;
}
