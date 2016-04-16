package com.hzih.gap.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.gap.dao.IpmacDao;
import com.hzih.gap.domain.Ipmac;
import com.hzih.gap.service.IpmacService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-7
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */
public class IpmacServiceImpl implements IpmacService {
    private IpmacDao ipmacDao;
    @Override
    public void bond(Ipmac ipmac) {
        //To change body of implemented methods use File | Settings | File Templates.
        ipmacDao.bond(ipmac);
    }

    @Override
    public String getBondIpmac(int start, int limit) {
        PageResult pageResult = ipmacDao.getBondIpmac(start,limit);
        List<Ipmac> ipmacList =  pageResult.getResults();
        int total = pageResult.getAllResultsAmount();
        String json = "{success:true,total:"+total + ",rows:[";
        for (Ipmac ipmac : ipmacList) {
            json += "{id:"+ipmac.getId()+",ip:'"+ipmac.getIp()+"',mac:'"+ipmac.getMac()+
                    "',probe:'"+ipmac.getProbe()+
                    "'},";
        }
        json += "]}";
        return json;
    }

    @Override
    public void update(Ipmac ipmac) {
        //To change body of implemented methods use File | Settings | File Templates.
        ipmacDao.update(ipmac);
    }

    @Override
    public void delete(Ipmac ipmac) {
        //To change body of implemented methods use File | Settings | File Templates.
        ipmacDao.delete(ipmac);
    }

    public IpmacDao getIpmacDao() {
        return ipmacDao;
    }

    public void setIpmacDao(IpmacDao ipmacDao) {
        this.ipmacDao = ipmacDao;
    }
}
