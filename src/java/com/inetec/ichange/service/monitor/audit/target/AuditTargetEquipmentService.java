package com.inetec.ichange.service.monitor.audit.target;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.service.monitor.audit.util.EquipmentLogDataBase;
import com.inetec.ichange.service.monitor.databean.AuditInfo;
import com.inetec.ichange.service.monitor.databean.UdpHeader;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class AuditTargetEquipmentService extends Thread{
    private static final Logger logger = Logger.getLogger(AuditTargetEquipmentService.class);
    private List<Long> list;
    private long packSize;
    private long time;
    private Map<Long, byte[]> map;
    private boolean run = false;
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
        packSize = 0;
        time = 0;
        list = new ArrayList<Long>();
        map = new HashMap<Long, byte[]>();
    }

    public void receive() {
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
        if(buffs.size()>0){
            logger.info("处理设备审计文件...");
            byte[] bytes = buffs.toByteArray();
            buffs.reset();
            new EquipmentLogDataBase().putIntoDatabase(bytes);
        }
    }

    private void makeProcess(){
        AuditInfo auditInfo = pollQuery();
        if(auditInfo!=null){
            String header = auditInfo.getHeader();
            byte[] buf = auditInfo.getByteArray();
            int len = UdpHeader.getSize(header);
            long fileSize = UdpHeader.getFileSize(header);
            long id = UdpHeader.getId(header);
            long time = UdpHeader.getTime(header);
            process(time,buf,fileSize,id,len);
        }
    }

    /**
     *
     * @param time
     * @param buf         每包内容
     * @param fileSize   文件大小
     * @param id          每包编号
     * @param len         每包大小
     */
    public void process(long time, byte[] buf, long fileSize, long id, int len){
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
                logger.warn("源端发送的设备审计文件数据和目标端收到的数据存在不一致,存在丢包");
                return;
            }
        }

        if(this.packSize == fileSize){
            this.packSize = 0;
            receive();
        }
    }


    public void run() {
        run = true;
        while (run){
//            setTempAudit(pollQuery());
            makeProcess();
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

    public boolean isRunning() {
        return run;
    }

    public void close() {
        run = false;
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
