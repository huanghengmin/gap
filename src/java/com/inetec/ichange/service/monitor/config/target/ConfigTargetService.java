package com.inetec.ichange.service.monitor.config.target;

import cn.collin.commons.utils.DateUtils;
import com.inetec.common.config.stp.nodes.Type;
import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.databean.AuditInfo;
import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.monitor.syslog.SysLogSend;
import com.inetec.ichange.service.monitor.utils.Configuration;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-15
 * Time: 下午3:39
 * To change this template use File | Settings | File Templates.
 */
public class ConfigTargetService extends Thread{
    private static final Logger logger = Logger.getLogger(ConfigTargetService.class);
    private static final String ExternalXml = System.getProperty("ichange.home")+"/repository/external/config.xml";
    private static final String InternalXml = System.getProperty("ichange.home")+"/repository/config.xml";
    private static boolean run = false;
    List<Long> list;
    long packSize;
    long time;
    Map<Long,byte[]> map;
    public BlockingQueue<AuditInfo> query;
    private AuditInfo tempAudit;

    public AuditInfo getTempAudit() {
        return tempAudit;
    }

    public void setTempAudit(AuditInfo tempAudit) {
        this.tempAudit = tempAudit;
    }

    public void init() {
        query = new LinkedBlockingQueue<AuditInfo>();
        time = 0;
        packSize = 0;
        list = new ArrayList<Long>();
        map = new HashMap<Long, byte[]>();
    }
    /**
     *
     * @param time        同一个包的标识
     * @param buf         每包内容
     * @param fileSize   文件大小
     * @param id          每包编号
     * @param len         每包大小
     */
    public void process(long time,byte[] buf,long fileSize, long id, int len){
        if(this.time==0||this.time<time){   //新包开始
            map.clear();
            list.clear();
            this.packSize = 0;
            this.time = time;
            this.packSize += len;
            list.add(id);
            map.put(id,buf);
        } else if (this.time>0 && this.time == time) { //同一包继续
            long lastId = list.get(list.size()-1);//上一包的id值
            if(id - lastId == 1){ //是连续的包
                this.packSize += len;
                list.add(id);
                map.put(id,buf);
            } else {
                logger.warn("源端发送的配置文件数据和目标端收到的数据存在不一致,存在丢包");
                return;
            }
        }
        if(this.packSize == fileSize){
            this.packSize = 0;
            receive();
        }
    }

    public void receive(){
        ByteArrayOutputStream buffs = new ByteArrayOutputStream();
        Long[] arrays =  list.toArray(new Long[list.size()]);
        Arrays.sort(arrays);
        for (int i=0;i<arrays.length;i++) {
            try {
                byte[] buf = map.get(arrays[i]);
                buffs.write(buf);
                buffs.flush();
            } catch (IOException e) {
                logger.error("组包出错",e);
            }
        }
        map.clear();
        list.clear();
        packSize = 0;

        OutputStream out = null;
        if(buffs.size()>0){
            logger.info("处理系统源端发送的配置文件...");
            try {
                out = new FileOutputStream(new File(ExternalXml));
                out.write(buffs.toByteArray());
                buffs.reset();
                out.flush();
                out.close();
                Configuration config = new Configuration(ExternalXml);
                config.updatePrivated();
                List<Type> types = config.readTypes(Type.s_app_proxy);
                config = new Configuration(InternalXml);
                config.updateProxySocketPort(types);
                Service.sysLogSendService.sysLogSend(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ",目标端接收到配置文件","UTF-8");   //TODO 日志发出
            } catch (FileNotFoundException e) {
                logger.error("找不到文件"+ExternalXml);
            } catch (IOException e) {
                logger.error("解析xml出错",e);
            } catch (Ex ex) {
                logger.error("处理系统源端发送的配置文件错误",ex);
            }
        }
    }


    public void run() {
        run = true;
        while (run){
//            setTempAudit(pollQuery());
            makeProcess();
        }
    }

    private void makeProcess() {
        AuditInfo auditInfo = pollQuery();
        if(auditInfo!=null){
            String header = auditInfo.getHeader();
            byte[] buff = auditInfo.getByteArray();
            int len = UdpHeader.getSize(header);
            long fileSize = UdpHeader.getFileSize(header);
            long id = UdpHeader.getId(header);
            long time = UdpHeader.getTime(header);
            this.process(time,buff, fileSize, id, len);
        }
    }

    private byte[] readInputStream(InputStream isReceive) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 1024];   //buff用于存放循环读取的临时数据
        int rc = 0;
        while ((rc = isReceive.read(buff)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果
        return in_b;
    }

    public void close() {
        run = false;
    }

    public boolean isRunning() {
        return run;
    }

    public void addList(String header, ByteArrayInputStream in) {
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setHeader(header);
        auditInfo.setByteArrayInputStream(in);
        boolean isFull = query.offer(auditInfo);
        if(!isFull){
            logger.info("队列已满...");
        }
    }

    public AuditInfo pollQuery(){
        try {
            return query.take();
        } catch (InterruptedException e) {

        }
        return null;
    }

    public boolean isQueryEmpty(){
        return querySize()==0;
    }

    public int querySize() {
        return query.size();
    }
}
