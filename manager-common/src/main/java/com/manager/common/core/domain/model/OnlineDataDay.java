package com.manager.common.core.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 实时在线数据
 * @author sieGuang 2021/10/21
 */
@Data
public class OnlineDataDay {

    @ApiModelProperty("日期")
    private String day;

    private String day1;
    private String day2;

    @ApiModelProperty("平台id")
    private String tid;

    @ApiModelProperty("用户人数")
    private String userCount;

    @ApiModelProperty("游戏id")
    private BigDecimal gameId;

    @ApiModelProperty("渠道")
    private BigDecimal channel;

    @ApiModelProperty("创建时间")
    private String createTime;

    // 最大值 和 平均值
    private String maxNum = "0";
    private String avgNum = "0";

}
