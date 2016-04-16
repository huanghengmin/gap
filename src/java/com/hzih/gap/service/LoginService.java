package com.hzih.gap.service;

import com.hzih.gap.domain.Account;

public interface LoginService {

	Account getAccountByNameAndPwd(String name, String pwd) ;

    Account getAccountByName(String name) ;
}
