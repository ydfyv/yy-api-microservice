package com.yy.yyapibackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.Data;

/**
 * 接口调用信息表
 *
 * @author 阿狸
 */
@TableName(value = "interface_invoke_info")
@Data
public class InterfaceInvokeInfo implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 调用用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 调用状态 0-正常，1-异常
     */
    private Integer status;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}