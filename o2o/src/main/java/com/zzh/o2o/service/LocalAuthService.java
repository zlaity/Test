package com.zzh.o2o.service;

import com.zzh.o2o.entity.LocalAuth;

public interface LocalAuthService {

	LocalAuth queryLocalAuthByUserNameAndPwd(String userName, String password);
}
