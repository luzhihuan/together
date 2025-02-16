package com.easycom.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easycom.Mapper.EmailCodeMapper;
import com.easycom.entity.PO.EmailCode;
import com.easycom.Service.IEmailCodeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮箱验证码表 服务实现类
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-16
 */
@Service
public class EmailCodeServiceImpl extends ServiceImpl<EmailCodeMapper, EmailCode> implements IEmailCodeService {

}
