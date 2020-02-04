package com.hy.demo.mapper;

import com.hy.demo.model.SystemRole;
import com.hy.demo.model.SystemRoleExample;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SystemRoleMapper extends Mapper<SystemRole> {
    long countByExample(SystemRoleExample example);

    int deleteByExample(SystemRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SystemRole record);

    int insertSelective(SystemRole record);

    List<SystemRole> selectByExample(SystemRoleExample example);

    SystemRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SystemRole record, @Param("example") SystemRoleExample example);

    int updateByExample(@Param("record") SystemRole record, @Param("example") SystemRoleExample example);

    int updateByPrimaryKeySelective(SystemRole record);

    int updateByPrimaryKey(SystemRole record);
}