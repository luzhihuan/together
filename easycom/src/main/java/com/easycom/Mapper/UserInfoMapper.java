package com.easycom.Mapper;

import com.easycom.entity.PO.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-09
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo selectByEmail(@Param("email") String email);

    UserInfo selectByUserIdAndStudentId(@Param("userId") String userId,@Param("studentId") String studentId);

    List<String> selectStudentIdsByClassId(String classId);

    List<String> selectStudentIdsBySpecId(String classId,String specId);

    List<String> selectStudentIdsByAcademyId(String classId,String specId,String academyId);
}
