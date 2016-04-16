package com.hzih.gap.dao;

import cn.collin.commons.domain.PageResult;
import com.hzih.gap.domain.Ipmac;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-6
 * Time: 下午5:56
 * To change this template use File | Settings | File Templates.
 */
public interface IpmacDao {

    void bond(Ipmac ipmac);
    PageResult getBondIpmac(int start,int limit);
    void update(Ipmac ipmac);
    void delete(Ipmac ipmac);
}
