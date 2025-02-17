package com.easycom.Service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easycom.Mapper.EmailCodeMapper;
import com.easycom.Mapper.UserInfoMapper;
import com.easycom.Utils.DefaultParam;
import com.easycom.config.AppConfig;
import com.easycom.entity.DTO.SysSettingDTO;
import com.easycom.entity.PO.EmailCode;
import com.easycom.Service.IEmailCodeService;
import com.easycom.entity.PO.UserInfo;
import com.easycom.entity.VO.Result;
import com.easycom.entity.enums.CheckCodeTypeEnum;
import com.easycom.entity.enums.EmailCodeStatusEnum;
import com.easycom.exception.UserException;
import com.easycom.redis.RedisComponent;
import com.easycom.redis.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 邮箱验证码表 服务实现类
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-16
 */
@Service
@Slf4j
public class EmailCodeServiceImpl extends ServiceImpl<EmailCodeMapper, EmailCode> implements IEmailCodeService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private EmailCodeMapper emailCodeMapper;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result sendCode(String email, String checkCode, String checkCodeKey, Integer type) {
        try{
            if(!RedisUtils.hasKey(DefaultParam.REDIS_KEY_CHECK_CODE_EMAIL+checkCodeKey)){
                return Result.fail("验证码已经过期，请重新获取！");
            }
            if(!checkCode.equalsIgnoreCase(
                    (String) RedisUtils.get(DefaultParam.REDIS_KEY_CHECK_CODE_EMAIL+checkCodeKey)
            )){
                return Result.fail("验证码不正确！");
            }

            if(type.equals(CheckCodeTypeEnum.REGISTER.getType())||
                    type.equals(CheckCodeTypeEnum.CHECK_EMAIL.getType())){
                UserInfo userInfo = userInfoMapper.selectByEmail(email);
                if(userInfo!=null){
                    return Result.fail("该邮箱已经被绑定！");
                }
            }
            String code = RandomUtil.randomNumbers(5);

            //发送验证码
            sendEmailCode(email,code);

            //每次发送验证码之前，都要将已经发送的验证码全部置为已使用！
            emailCodeMapper.disableEmailCode(email);

            EmailCode emailCode = new EmailCode();
            emailCode.setCode(code);
            emailCode.setEmail(email);
            emailCode.setStatus(EmailCodeStatusEnum.USED.getStatus());
            emailCode.setCreateTime(LocalDateTime.now());
            emailCodeMapper.insert(emailCode);
            return Result.ok();
        }finally {
            RedisUtils.del(DefaultParam.REDIS_KEY_CHECK_CODE+checkCodeKey);
        }
    }

    public void checkCode(String email,String code){
        EmailCode emailCode = emailCodeMapper.selectOne(
                new LambdaQueryWrapper<EmailCode>()
                        .eq(EmailCode::getCode, code)
                        .eq(EmailCode::getEmail, email)
        );
        if (emailCode==null) {
            throw new UserException("邮箱验证码不正确！");
        }

        //剩余有效时间
        Long validTime = System.currentTimeMillis() - LocalDateTimeUtil.toEpochMilli(emailCode.getCreateTime());
        if(emailCode.getStatus() == 1 || validTime > DefaultParam.MINUTES_15){
            throw new UserException("邮箱验证码已经过期！");
        }
        emailCodeMapper.disableEmailCode(email);
    }

    /**
     *  发送邮箱验证码
     * @param toEmail 发给谁？
     * @param code      验证码
     *
     */
    private void sendEmailCode(String toEmail,String code){
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            //谁发送?
            helper.setFrom(appConfig.getSendUserName());
            helper.setTo(toEmail);

            SysSettingDTO sysSettingDTO = redisComponent.getSysSettingDTO();
            helper.setSubject(sysSettingDTO.getRegisterMailTitle());
            helper.setText(String.format(sysSettingDTO.getRegisterEmailContent(),code));
            helper.setSentDate(new Date());
            javaMailSender.send(message);

        }catch (Exception e){
            log.info("邮件发送失败！{}",e.getMessage());
            throw new UserException("邮件发送失败");
        }
    }
}
