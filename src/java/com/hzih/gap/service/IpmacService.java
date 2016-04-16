package com.hzih.gap.service;

import cn.collin.commons.domain.PageResult;
import com.hzih.gap.domain.Ipmac;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-7
 * Time: 上午9:52
 * To change this template use File | Settings | File Templates.
 */
public interface IpmacService {
    void bond(Ipmac ipmac);
    String getBondIpmac(int start,int limit);
    void update(Ipmac ipmac);
    void delete(Ipmac ipmac);
}
