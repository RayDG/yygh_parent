package com.dg.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.dg.yygh.cmn.mapper.DictMapper;
import com.dg.yygh.model.cmn.Dict;
import com.dg.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

import java.util.Dictionary;

/**
 * @Author: DG
 * @Date: 2021/8/31 10:55
 * @Description:
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
