package com.hzih.gap.service.impl;

import com.hzih.gap.dao.XmlOperatorDAO;
import com.hzih.gap.dao.impl.XmlOperatorDAOImpl;
import com.hzih.gap.service.DataBaseService;
import com.hzih.gap.utils.StringContext;
import com.hzih.gap.utils.StringUtils;
import com.inetec.common.config.stp.nodes.*;
import com.inetec.common.exception.Ex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseServiceImpl implements DataBaseService {
	private XmlOperatorDAO xmlOperatorDAO = new XmlOperatorDAOImpl();


    @Override
    public String readInternalDB(Integer start, Integer limit, String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readExternalDB(Integer start, Integer limit, String dbName, String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readTableField(Integer start, Integer limit, String typeXml, String tableName, String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readTableFieldExist(Integer start, Integer limit, String typeXml, String appName, String tableName, String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readInternalTargetDB(Integer start, Integer limit, String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readExternalTargetDB(Integer start, Integer limit, String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readTargetTableField(String typeXml, String tableName, String dbName, String[] dests) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readInternalDBKeyValue(String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readExternalDBKeyValue(String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readDBFieldKeyValue(String typeXml, String tableName, String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readDBSurceFieldKeyValue(String typeXml, String tableName, String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String checkTempTable(String typeXml, String tempTable, String dbName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String operateDBUpdateApp(String typeXml, String appName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void operateDBRemoveApp(String typeXml, String appName) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void operateDBInsertApp(String typeXml, String appName) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void operateDBUpdateApp(String typeXml) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void operateDBRemoveApp(String typeXml) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void operateDBInsertApp(String typeXml) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
