package com.hzih.gap.web.action.audit;

import cn.collin.commons.utils.DateUtils;
import com.hzih.gap.domain.AuditReset;
import com.hzih.gap.service.AuditResetService;
import com.hzih.gap.service.LogService;
import com.hzih.gap.utils.Configuration;
import com.hzih.gap.utils.StringContext;
import com.hzih.gap.utils.StringUtils;
import com.hzih.gap.web.SessionUtils;
import com.hzih.gap.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-7-31
 * Time: 上午11:54
 * 业务手动重传
 */
public class AuditResetAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(AuditResetAction.class);
    private LogService logService;
    private AuditResetService auditResetService;
    private String id;
    private String fileSize;
    private int start;
    private int limit;
    private File uploadFile;
    private String uploadFileFileName;
    private String uploadFileContentType;

    public String select() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json =  null;
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String businessType = request.getParameter("businessType");
            String businessName = request.getParameter("businessName");
            String resetStatus = request.getParameter("resetStatus");
            Date startDate = StringUtils.isBlank(startDateStr) ? null : DateUtils.parse(startDateStr, "yyyy-MM-dd");
            Date endDate = StringUtils.isBlank(endDateStr) ? null : DateUtils.parse(endDateStr, "yyyy-MM-dd");
            if(StringUtils.isBlank(startDateStr)&&StringUtils.isBlank(endDateStr)
                    &&StringUtils.isBlank(businessName)&&StringUtils.isBlank(businessType)
                            &&StringUtils.isBlank(resetStatus)){
                resetStatus = "0";
            }
            json = auditResetService.select(start, limit,startDate,endDate,businessName,businessType,resetStatus);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "业务手动重传","用户读取业务手动重传信息成功 ");
        } catch (Exception e) {
            logger.error("业务手动重传", e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "业务手动重传","用户读取业务手动重传信息失败 ");
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String insert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            List<AuditReset> list = new ArrayList<AuditReset>();
            list = setList();
            list = auditResetService.update(list);
            auditResetService.insert(list);
            msg = "导入成功,点击[确定]返回列表!";
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "业务手动重传","用户导入业务手动重传信息成功 ");
        } catch (Exception e) {
            logger.error("业务手动重传", e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "业务手动重传","用户导入业务手动重传信息失败 ");
            msg = "导入失败"+e.getMessage();
        }
        String json =  "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String update() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            auditResetService.updateResetStatus(id,fileSize);
            msg = "重传成功,点击[确定]返回列表!";
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "业务手动重传","用户重传业务手动重传信息成功 ");
        } catch (Exception e) {
            logger.error("业务手动重传", e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "业务手动重传","用户重传业务手动重传信息失败 ");
            msg = "重传失败"+e.getMessage();
        }
        String json =  "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    private List<AuditReset> setList() throws Exception {
        FileUtils.copyFile(uploadFile,new File(StringContext.RESETFILE));
        Configuration config = new Configuration(StringContext.RESETFILE);
        List<AuditReset> list = config.getAuditResets();
        return list;
    }

    public String truncate() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String businessType = request.getParameter("businessType");
            String businessName = request.getParameter("businessName");
            String resetStatus = request.getParameter("resetStatus");
            auditResetService.truncate(startDate, endDate, businessName, businessType, resetStatus);
            msg = "清空成功,点击[确定]返回列表!";
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "业务手动重传","用户清空业务手动重传信息成功 ");
        } catch (Exception e) {
            logger.error("业务手动重传", e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "业务手动重传","用户清空业务手动重传信息失败 ");
            msg = "清空失败"+e.getMessage();
        }
        String json =  "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public AuditResetService getAuditResetService() {
        return auditResetService;
    }

    public void setAuditResetService(AuditResetService auditResetService) {
        this.auditResetService = auditResetService;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    public String getUploadFileContentType() {
        return uploadFileContentType;
    }

    public void setUploadFileContentType(String uploadFileContentType) {
        this.uploadFileContentType = uploadFileContentType;
    }
}
