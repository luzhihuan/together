package com.easycom.Service;

import com.easycom.entity.PO.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easycom.entity.VO.Result;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-09
 */
public interface IUserInfoService extends IService<UserInfo> {
    Result getCheckCode();

    Result login(UserInfo userInfo);
}
