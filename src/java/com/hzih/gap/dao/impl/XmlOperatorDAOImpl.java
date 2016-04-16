package com.hzih.gap.dao.impl;

import com.hzih.gap.dao.XmlOperatorDAO;
import com.hzih.gap.entity.*;
import com.hzih.gap.utils.Configuration;
import com.hzih.gap.utils.StringContext;
import com.hzih.gap.utils.StringUtils;
import com.inetec.common.config.stp.ConfigParser;
import com.inetec.common.config.stp.nodes.*;
import com.inetec.common.config.stp.nodes.Channel;
import com.inetec.common.exception.Ex;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlOperatorDAOImpl implements XmlOperatorDAO{
//	private Logger log = Logger.getLogger(XmlOperatorDAOImpl.class);


    @Override
    public String[] readJdbcName(String typeXml) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Jdbc> getInternalJdbc() throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Jdbc> getExternalJdbc() throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Table> getInternalTypeTable(String appName, String type) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Table> getExternalTypeTable(String appName, String type) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveProxyType(TypeBase typeBase, TypeSafe typeSafe, TypeData typeData) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean deleteInternalTypeByName(String appName) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean deleteExternalTypeByName(String appName) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExistExternalType(String appName) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExistInternalType(String appName) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getExternalProxyIp(String appName) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getInternalProxyIp(String appName) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveExternalProxyIp(String appName, String ip) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveInternalProxyIp(String appName, String ip) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Jdbc getJdbcByName(String typeXml, String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Jdbc getExternalJdbcByName(String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Jdbc getInternalJdbcByName(String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveDBType(TypeBase typeBase, TypeDB typeDB) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveDBTypeTable(String type, TypeBase typeBase, TypeDB typeDB, TypeTable typeTable, Field[] tableFields) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateDBTypeTable(String type, TypeBase typeBase, TypeDB typeDB, TypeTable typeTable, Field[] tableFields) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> deleteJdbcByName(String[] jdbcNameArray, String typeXml) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveJdbc(Jdbc jdbc, Boolean privated) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateJdbc(Jdbc jdbc, Boolean privated) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getExternalSourceTableName(String appName) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getInternalSourceTableName(String appName) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getJdbcName(String typeXml, String appName) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveDBTargetName(String typeXml, String appName, String[] targetdbNames) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Field> getExternalTypeFields(String appName, String dbName, String tableName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Field> getInternalTypeFields(String appName, String tableName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveDBTypeMergeTable(TypeBase typeBase, TypeDB typeDB, TypeTable typeTable, String[] merge_table_names, Map<String, MergeField[]> mergeTable) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateDBTypeTargetApp(TypeBase typeBase) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateDBTypeSourceApp(TypeBase typeBase) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateDBTypeSourceData(TypeBase typeBase, TypeDB typeDB) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] readSourceTableNames(String typeXml, String appName) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] readTargetTableNames(String typeXml, String appName) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateDBTypeFields(TypeBase typeBase, String dbName, TypeTable typeTable, String[] fields, String[] is_pks) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] readThisTargetDBNames(String typeXml, String srcTableName, String appName) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] readThisTargetTableNames(String typeXml, String srcTableName, String targetDB, String appName) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Field> getThisTargetTableField(String typeXml, String appName, String sourceDBName, String sourceTableName, String targetDBName, String targetTableName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateDBTypeTargetFields(TypeBase typeBase, TypeTable typeTable, String[] fields, String[] is_pks) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, List<MergeField>> getThisMergeTableField(String typeXml, String appName, String tableName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean deleteTypeMergeTable(String typeXml, String appName, String[] tableNames) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean deleteTypeSourceTable(String typeXml, String appName, String dbName, String[] tableNames) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean deleteTypeSourceTableBackUp(String typeXml, String appName, String dbName, String tableName) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean deleteTypeSrcTable(String typeXml, String appName, String[] tableNames) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean deleteDBTypeFields(String typeXml, String appName, String dbName, String tableName, String[] fields) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Table readSourceTable(String typeXml, String appName, String dbName, String tableName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Field readSourceField(String typeXml, String appName, String dbName, String tableName, String fieldName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveTypeTargetDB(String typeXml, String appName, String srcTableName, String[] targetDBs) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteTypeTargetDB(String typeXml, String appName, String srcTableName, String[] targetDBs) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean deleteTypeTargetTable(String typeXml, String appName, String srcTableName, String targetDB, String[] targetTableNames) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveTypeTargetTableName(String typeXml, String appName, String srcTableName, String targetDB, String[] targetTableNames) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Table getThisTargetTable(String typeXml, String appName, String srcTableName, String targetDB, String targetTable) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] readTypeName(String typeXml, String appType) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isInternalTwowayApp(String appName) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExternalTwowayApp(String appName) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExistInternalTypeS(String appName) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExistExternalTypeS(String appName) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExistInternalTypeDescT(String appDesc) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExistExternalTypeDescS(String appDesc) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DataBase getDataBase(String typeXml, String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Integer, Map<Integer, Map<String, Object>>> readProxyType(String appType) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveFileType(TypeBase typeBase, TypeFile typeFile) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateFileType(TypeBase typeBase, TypeFile typeFile) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Type> getTypes(String xmlType, String appType) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Type> getTypes(String xmlType, String appType, boolean isAllow) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SourceFile getSourceFiles(String xmlType, String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TargetFile getTargetFiles(String xmlType, String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SocketChange getSocketChange(String xmlType, String pluginType, String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getSrcdbName(String xmlType, String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Type readInternalProxyType(String appName, String proxyType) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Type readExternalProxyType(String appName, String proxyType) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Type getInternalTypeByName(String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Type getExternalTypeByName(String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveInternalProxyBlackIpMac(String appName, IpMac[] ipMacs) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveExternalProxyBlackIpMac(String appName, IpMac[] ipMacs) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveInternalProxyWhiteIpMac(String appName, IpMac[] ipMacs) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveExternalProxyWhiteIpMac(String appName, IpMac[] ipMacs) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteInternalProxyBlackIpMac(String appName, IpMac[] ipMacs) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteExternalProxyBlackIpMac(String appName, IpMac[] ipMacs) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteInternalProxyWhiteIpMac(String appName, IpMac[] ipMacs) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteExternalProxyWhiteIpMac(String appName, IpMac[] ipMacs) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateInternalProxyBlackIpMac(String appName, IpMac ipMac, String oldUpdateIp) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateExternalProxyBlackIpMac(String appName, IpMac ipMac, String oldUpdateIp) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateInternalProxyWhiteIpMac(String appName, IpMac ipMac, String oldUpdateIp) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateExternalProxyWhiteIpMac(String appName, IpMac ipMac, String oldUpdateIp) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateDescription(String pathFile, String text) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateProxyTypeSafe(TypeBase typeBase, TypeSafe typeSafe) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateProxyType(TypeBase typeBase, TypeData typeData) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readRootDesc(String type, String fileName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTypeActive(String typeXml, String appName, boolean isActive) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateSecurityFile(TypeBase typeBase) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateSecurityDB(TypeBase typeBase) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateSecurityProxy(TypeBase typeBase, TypeSafe typeSafe) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] readTypeNameSingle(String plugin, String appType) throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Type> readTypes(String plugin) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Channel> readChannel() throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateChannel(Boolean privated, List<Channel> channels) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateChannelCount(Boolean privated, String count) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExistExternalChannelPort(int channelPort) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTypeAppSend(String appName, int status) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Type> readTypeNameForBusiness(String xmlType) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IChangeUtils getIChangeUtils() throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateChangeUtils(ChannelIChangeUtils channelIChangeUtils) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveRestartTime(String time) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getSNMPClient() throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getSysLogClient() throws Ex {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveSNMPClient(String ip) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveSysLogClient(String ip) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteSNMPClient(String ip) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteSysLogClient(String ip) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateSNMPClient(String snmpclient, String oldSNMPClient) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateSysLogClient(String syslogclient, String oldSysLogClient) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTypeAllow(String plugin, String appName, boolean allow) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateEntirelyDeleteTimeSyncToTrigger(TypeBase typeBase, TypeDB typeDB) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTriggerToEntirelyDeleteTimeSync(TypeBase typeBase, TypeDB typeDB) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateEntirelyDeleteTimeSyncToFlag(TypeBase typeBase, TypeDB typeDB) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateFlagToEntirelyDeleteTimeSync(TypeBase typeBase, TypeDB typeDB) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateFlagToTrigger(TypeBase typeBase, TypeDB typeDB) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTriggerToFlag(TypeBase typeBase, TypeDB typeDB) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateToTrigger(TypeBase typeBase, TypeDB typeDB, boolean operateDB) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTriggerTo(TypeBase typeBase, TypeDB typeDB, boolean operateDB) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List readSourceTables(String typeXml, String appName, String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateDBName(TypeBase typeBase, String dbName, String dbNameOld) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DataBase> getDataBases(String typeXml, String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteDataBase(String typeXml, String appName, String dbName) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Table> getTypeTables(String typeXml, String appName, String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateSourceTable(String typeXml, String appName, String dbName, Table table) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Field> getSourceFields(String typeXml, String appName, String dbName, String tableTame) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateDataBase(String typeXml, String appName, DataBase dataBase) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, List<String>> getTempTables(String xmlPath, String appType) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteSourceTable(String typeXml, String appName, String dbName, Table table) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTargetSourceDBName(String typeXml, String appName, String sourceDBNameOld, String sourceDBName) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getJdbcsByName(boolean privated, String jdbcName) throws Ex {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDataBaseDelete(String appName, String plugin) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Field getSourceFile(String typeXml, String appName, String dbName, String tableName, String fieldName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
