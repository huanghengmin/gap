package com.hzih.gap.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.gap.dao.IpmacDao;
import com.hzih.gap.domain.Ipmac;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-6
 * Time: 下午5:55
 * To change this template use File | Settings | File Templates.
 */
public class IpmacDaoImpl extends MyDaoSupport implements IpmacDao{
    @Override
    public void setEntityClass() {
        this.entityClass = Ipmac.class;
    }

    @Override
    public void bond(Ipmac ipmac) {
        String sql = "insert into ipmac (ip,mac,probe) value ('"+ipmac.getIp()+"','"+ipmac.getMac()+"',"+ipmac.getProbe()+")";
        System.out.println(sql);
        Session session = getSession();
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.executeUpdate();
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PageResult getBondIpmac(int start, int limit) {
        String hql = "from Ipmac";
        String countHql = "select count(*) " + hql;
        PageResult ps = this.findByPage(hql, countHql, 	start/limit+1, limit);
        return ps;
    }

    @Override
    public void update(Ipmac ipmac) {
        //To change body of implemented methods use File | Settings | File Templates.
        String sql = "update ipmac set ip='"+ipmac.getIp()+"',mac='"+ipmac.getMac()+"',probe="+ipmac.getProbe()+" where id="+ipmac.getId();
        Session session = getSession();
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.executeUpdate();
    }

    @Override
    public void delete(Ipmac ipmac) {
        //To change body of implemented methods use File | Settings | File Templates.
        String sql = "delete from ipmac where id="+ipmac.getId();
        Session session = getSession();
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.executeUpdate();
    }
}
