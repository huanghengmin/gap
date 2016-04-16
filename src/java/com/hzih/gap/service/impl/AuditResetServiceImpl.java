package com.hzih.gap.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.gap.dao.AuditResetDao;
import com.hzih.gap.domain.AuditReset;
import com.hzih.gap.service.AuditResetService;
import com.hzih.gap.utils.StringUtils;
import org.apache.commons.httpclient.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-7-31
 * Time: 下午2:45
 * To change this template use File | Settings | File Templates.
 */
public class AuditResetServiceImpl implements AuditResetService {
    private AuditResetDao auditResetDao;

    public void setAuditResetDao(AuditResetDao auditResetDao) {
        this.auditResetDao = auditResetDao;
    }

    /**
     * 按条件分页查询,并返回json数据
     * @param start
     * @param limit
     * @param startDate
     * @param endDate
     * @param businessName
     * @param businessType
     * @param resetStatus
     * @return
     * @throws Exception
     */
    public String select(int start, int limit, Date startDate, Date endDate,
                         String businessName, String businessType, String resetStatus) throws Exception {
        PageResult pageResult = auditResetDao.pageList(start/limit+1,limit,startDate,endDate,
                businessName,businessType,resetStatus);
        int total = pageResult.getAllResultsAmount();
        List<AuditReset> list = pageResult.getResults();
        String json = "{success:true,total:"+total+",rows:[";
        for(AuditReset a : list){
            json += "{id:'"+a.getId()+"',businessName:'"+a.getBusinessName()+
                    "',fileName:'"+a.getFileName()+"',businessType:'"+a.getBusinessType()+
                    "',resetCount:"+a.getResetCount()+",resetStatus:"+a.getResetStatus()+
                    ",importTime:'"+ DateUtil.formatDate(a.getImportTime(),"yyyy-MM-dd HH:mm:ss")+"',fileSize:'"+a.getFileSize()+"'},";
        }
        json += "]}";
        return json;
    }

    /**
     * 批量插入
     * @param auditResets
     * @throws Exception
     */
    public void insert(List<AuditReset> auditResets) throws Exception {
         auditResetDao.insert(auditResets);
    }

    /**
     * 修改状态为需重传,实际值0
     *
     *
     * @param id    记录标识
     * @param fileSize
     * @throws Exception
     */
    public void updateResetStatus(String id, String fileSize) throws Exception {
        AuditReset old = (AuditReset) auditResetDao.getById(Long.valueOf(id));
        old.setResetStatus(1);
        old.setFileSize(fileSize);
        auditResetDao.update(old);
    }

    /**
     * 修改参数所对应的记录
     * @param auditResets
     * @return
     * @throws Exception
     */
    public List<AuditReset> update(List<AuditReset> auditResets) throws Exception {
        List<AuditReset> list = new ArrayList<AuditReset>();
        for(AuditReset a:auditResets){
            AuditReset old = auditResetDao.findByNameTypeFileName(a.getBusinessName(), a.getFileName(), a.getBusinessType());
            if(old!=null){
                boolean isNeedUpdated = old.getResetStatus()==1;
                if(isNeedUpdated){
                    old.setResetStatus(0);
                    old.setImportTime(new Date());
                    old.setResetCount(old.getResetCount() + 1);
                    old.setFileSize(a.getFileSize());
                    auditResetDao.update(old);
                }
                list.add(a);
            }
        }
        auditResets.removeAll(list);
        return auditResets;
    }

    /**
     * 根据条件删除,没有条件时清空表数据
     * @param startDate      开始时间
     * @param endDate         结束时间
     * @param businessName   业务名
     * @param businessType   业务类型
     * @param resetStatus    状态
     * @throws Exception
     */
    public void truncate(String startDate, String endDate,
                         String businessName, String businessType, String resetStatus) throws Exception {
        if(StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate) &&
                StringUtils.isBlank(businessType) && StringUtils.isBlank(businessName) &&
                    StringUtils.isBlank(resetStatus)) {
            auditResetDao.truncate();
        } else {
            auditResetDao.delete(startDate,endDate,businessName,businessType,resetStatus);
        }

    }
}
