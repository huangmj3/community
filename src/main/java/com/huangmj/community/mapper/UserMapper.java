package com.huangmj.community.mapper;

import com.huangmj.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    //添加参数注解
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    //通过Id查询得到user
    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);
}
