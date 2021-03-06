package com.data.mapper;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author marvin 2021/9/11
 */
@Mapper
public interface ConfigAgentMapper {

    /**
     * 查询
     */
    @Select("select a.lvl lv,a.lvl_name lvname,a.min,a.max,a.rebate rata  from config_agent a where a.tid = #{tid} order by a.lvl asc")
    List<Map> getConfigAgentList(@Param("tid") Integer tid);

    @Select("select time from data_register d left join sys_tenant t on d.channel = t.t_id where t.tenant = #{tid} and uid = #{agentId}")
    Long selectAgent(@Param("tid") Integer tid, @Param("agentId") String agentId);

    @Update("update data_register set agent_id = #{agentId},agent_time = #{agentTime} where uid = #{uid} and time>#{time}")
    Integer setAgentId(@Param("agentId") String agentId, @Param("uid") String uid, @Param("time") Long time, @Param("agentTime") Long agentTime);

    @Select("SELECT d.uid," +
            "d.sub_ratio sub_water," +
            "d.other_ratio team_water," +
            "d.total_income commission_all," +
            "d.total_income today_income," +
            "d.team_num teamNum " +
            "FROM agent_commission_day d "+
            "WHERE d.agent_id = #{uid} and d.day=#{day} limit #{beginNum},#{limit}")
    List<Map> selectSubinfo(@Param("beginNum") int beginNum, @Param("limit") Integer limit, @Param("uid") String uid, @Param("day") String day);

    @Select("select count(1) from agent_commission_day where agent_id = #{uid} and day=#{day}")
    Integer selectSubinfoCount(@Param("uid") String uid, @Param("day") String day);

    @Select("select case_income out_golds,DATE_FORMAT(create_time,'%Y-%m-%d %H:%i::%s') out_time from agent_case_income " +
            "where uid = #{uid} order by create_time desc limit #{beginNum},#{limit}")
    List<Map> getWithdrawHistory(@Param("beginNum") int beginNum, @Param("limit") int limit, @Param("uid") Long uid);

    @Select("select count(1) from agent_case_income where uid = #{uid}")
    Integer getWithdrawHistoryCount(@Param("uid") Long uid);

    @Select("SELECT d.uid," +
            "r.agent_id pid," +
            "d.team_num team_num_with_new," +
            "d.sub_num first_proxy_num_with_new," +
            "c.total_income commission_all," +
            "(c.wait_income - d.cash_income)*10000 commission_pre_all," +
            "d.total_income todayRate " +
            "FROM data_register r " +
            "LEFT JOIN agent_commission_day d " +
            "ON r.uid = d.uid " +
            "LEFT JOIN agent_commission c " +
            "ON d.uid = c.uid " +
            "WHERE r.uid = #{uid} AND d.day = #{day}")
    Map getInfo(@Param("uid") String uid, @Param("day") String day);

    @Select("select promotion_domain from config_agent where tid=#{tid} limit 0,1")
    String getSpreatUrl(@Param("tid") Integer tid);

    @Select("SELECT day date,sub_ratio+other_ratio teamwater," +
            "sub_ratio subwater,other_ratio next_water,total_income count_rebate FROM agent_commission_day " +
            "WHERE uid = #{uid} order by day desc limit #{beginNum},#{limit} ")
    List<Map> getIncome(@Param("beginNum") int beginNum, @Param("limit") int limit, @Param("uid") Long uid);

    @Select("select count(*) from agent_commission_day where uid = #{uid}")
    Integer getIncomeCount(Long uid);

    @Select("select c.wait_income-d.cash_income from agent_commission c left join agent_commission_day d " +
            "ON d.uid = c.uid where c.uid = #{uid} and d.day = #{day} ")
    BigDecimal getWaitIncom(@Param("uid") String uid, @Param("day") String day);

    @Insert("insert into agent_case_income (uid,case_income,create_time) values (#{uid},#{cash},sysdate())")
    void saveWithdarw(@Param("uid") String uid, @Param("cash") BigDecimal cash);

    @Update("update agent_commission_day set cash_income = cash_income+#{cash},wait_income = wait_income-#{cash} where uid = #{uid} and day = #{day} ")
    void updateWaitIncome(@Param("uid") String uid, @Param("cash") BigDecimal cash, @Param("day") String day);

    @Select("SELECT begin_time act_begin_time,end_time act_end_time,id act_id,title act_name,IF(`type`=1,122,123) act_type,'true' open_state,'true' show_icon," +
            "IF(`type`=1,'',content) bg_url,IF(`type`=2,'',content) act_desc,sort sort_index FROM sys_propaganda WHERE state = '2' and tid = #{tid} " +
            "and (addressee_type=1 or (addressee_type=2 and addressee like concat('%',#{channel},'%')) " +
            "or (addressee_type=3 and (addressee like concat('%',#{uid},'%')) or addressee LIKE CONCAT('%',(select agent_id from data_register where uid = #{uid} AND agent_id !='0'),'%')))")
    List<Map> getActList(@Param("tid") Integer tid,@Param("channel")String channel,@Param("uid")String uid);

    @Select("select activity_type from config_activity where activity_begin<=sysdate() AND sysdate()<=activity_end and tid = #{tid} " +
            "and (channel_list = '*' or channel_list like CONCAT('%',#{channel},'%')) order by sort ")
    List<Integer> getActivitys(@Param("tid") Integer tid,@Param("channel") String channel);

    @Select("select count(1) from config_month_recharge where status = '1' and tid = #{tid}")
    int getMonthConfig(@Param("tid") Integer tid);

    @Select("SELECT promotion_domain FROM config_agent where tid = #{tid} limit 0,1")
    String getBeifen(@Param("tid") Integer tid);

    @Select("SELECT uid,SUM(recharge_amount)*10000 recharge_money " +
            "FROM config_recharge_order " +
            "WHERE payment_status = '1' " +
            "AND UNIX_TIMESTAMP(finish_time)>=#{start} " +
            "AND #{endTime}>=UNIX_TIMESTAMP(finish_time) " +
            "AND uid IN (SELECT uid FROM data_register WHERE agent_id = #{uid}) " +
            "GROUP BY uid HAVING SUM(recharge_amount)*10000>#{line} ")
    List getRecharge(@Param("uid") String uid,@Param("start") Long start,@Param("endTime") Long endTime,@Param("line") Long line);
}
