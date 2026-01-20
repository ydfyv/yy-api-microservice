package com.yy.yyapibackend.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yy.yyapimodel.model.entity.InterfaceInfo;
import lombok.Data;

import java.util.Date;

@Data
public class InterfaceInfoVO {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 接口地址
     */
    private String path;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应参数
     */
    private String responseParams;

    /**
     * 状态（0-关闭，1-开启）
     */
    private String status;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 创建用户 id
     */
    private UserVO user;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    public static InterfaceInfoVO objToVo(InterfaceInfo interfaceInfo) {
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtil.copyProperties(interfaceInfo, interfaceInfoVO);
        return interfaceInfoVO;
    }
}
