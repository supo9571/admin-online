package com.data.mapper;

import com.manager.common.core.domain.model.PlayUser;
import com.manager.common.core.domain.model.UserExchange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author marvin 2021/8/20
 */
@Mapper
public interface PlayerMapper {

    List selectPlayer(PlayUser playUser);

    @Select("select curr,FROM_UNIXTIME(time) time from data_coins where uid = #{uid}")
    List<Map> selectPlayerCurr(@Param("uid") Long uid);

    Integer updatePlayer(PlayUser playUser);

    @Select("select phone from data_register where uid = #{uid}")
    String getPhone(@Param("uid") Long uid);

    @Select("select name,account from user_exchange where uid = #{uid} and type='0' ")
    Map getBankInfo(@Param("uid") Long uid);

    @Select("select name,account,origin_bank originBank from user_exchange where uid = #{uid} and type='1' ")
    Map getAlipayInfo(@Param("uid") Long uid);

    Integer updateBank(UserExchange userExchange);
}
