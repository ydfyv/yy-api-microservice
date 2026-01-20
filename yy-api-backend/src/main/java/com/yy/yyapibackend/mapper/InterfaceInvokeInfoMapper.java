package com.yy.yyapibackend.mapper;

import com.yy.yyapibackend.model.entity.InterfaceInvokeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.yyapibackend.model.vo.InterfaceInvokeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 阿狸
 */
public interface InterfaceInvokeInfoMapper extends BaseMapper<InterfaceInvokeInfo> {
    List<InterfaceInvokeVO> getTopInvokeInterface(@Param("top") Integer top);
}