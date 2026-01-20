package com.yy.yyapibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.yyapibackend.common.ErrorCode;
import com.yy.yyapibackend.exception.BusinessException;
import com.yy.yyapibackend.model.entity.InterfaceInvokeInfo;
import com.yy.yyapibackend.model.vo.InterfaceInvokeVO;
import com.yy.yyapibackend.service.InterfaceInvokeInfoService;
import com.yy.yyapibackend.mapper.InterfaceInvokeInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 阿狸
 */
@Service
public class InterfaceInvokeInfoServiceImpl extends ServiceImpl<InterfaceInvokeInfoMapper, InterfaceInvokeInfo> implements InterfaceInvokeInfoService {

    @Override
    public boolean increaseInvokeCount(Long userId, Long interfaceInfoId) {
        InterfaceInvokeInfo interfaceInvokeInfo = lambdaQuery().eq(InterfaceInvokeInfo::getUserId, userId)
                .eq(InterfaceInvokeInfo::getInterfaceInfoId, interfaceInfoId)
                .gt(InterfaceInvokeInfo::getLeftNum, 0)
                .one();
        if (interfaceInvokeInfo == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口调用次数已耗光！");
        }
        return lambdaUpdate().eq(InterfaceInvokeInfo::getUserId, userId)
                .eq(InterfaceInvokeInfo::getInterfaceInfoId, interfaceInfoId)
                .setSql("totalNum = totalNum + 1, leftNum = leftNum - 1")
                .update();
    }

    @Override
    public List<InterfaceInvokeVO> getTopInvokeInterface(Integer top) {
        List<InterfaceInvokeVO> topInvokeInterface = getBaseMapper().getTopInvokeInterface(top);

        long sum = topInvokeInterface.stream().map(InterfaceInvokeVO::getInvokeCount).reduce(0, Integer::sum);

        if (sum == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "没有接口调用数据！");
        }

        return topInvokeInterface.stream().peek(interfaceInvokeVO -> interfaceInvokeVO.setProportion(Double.valueOf(String.format("%.2f", (double) interfaceInvokeVO.getInvokeCount() / sum * 100)))).collect(Collectors.toList());
    }
}




