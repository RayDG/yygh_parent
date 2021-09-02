package com.dg.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dg.yygh.cmn.listener.DictListener;
import com.dg.yygh.cmn.mapper.DictMapper;
import com.dg.yygh.cmn.service.DictService;
import com.dg.yygh.model.cmn.Dict;
import com.dg.yygh.model.hosp.HospitalSet;
import com.dg.yygh.vo.cmn.DictEeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * @Author: DG
 * @Date: 2021/8/29 00:04
 * @Description:
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    // 根据数据id查询子数据列表
    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        LambdaQueryWrapper<Dict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dict::getParentId, id);
        List<Dict> dictList = baseMapper.selectList(lambdaQueryWrapper);
        // 向list集合中每个dict对象设置hasChildren
        for (Dict dict : dictList) {
            boolean hasChildren = hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        }
        return dictList;
    }

    // 导出数据字典数据
    @Override
    public void exportDictData(HttpServletResponse response) {
        // 设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 查询数据
        List<Dict> dictList = baseMapper.selectList(null);
        List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());

        // Dict --> DictEeVo
        for (Dict dict : dictList) {
            DictEeVo dictVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictVo, DictEeVo.class);
            dictVoList.add(dictVo);
        }

        // 进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class)
                    .sheet("数据字典")
                    .doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 导入数据字典
    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper))
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 根据 dictcode 和 value 查询 dictName
    @Override
    public String getDictName(String dictCode, String value) {
        // dictCode为空
        if (StringUtils.isEmpty(dictCode)) {
            // 直接根据value查询
            LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Dict::getValue, value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else { // dictCode不为空，根据dictCode和value查询
            // 根据dictcode查询dict对象，得到 dict 的id值
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long parent_id = codeDict.getId();
            Dict findDict = baseMapper.selectOne(new LambdaQueryWrapper<Dict>()
                    .eq(Dict::getParentId, parent_id)
                    .eq(Dict::getValue, value));
            return findDict.getName();
        }
    }

    // 根据dictCode获取下级节点
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        // 根据dictcode获取对应id
        Dict dict = this.getDictByDictCode(dictCode);
        // 根据id获取子节点
        List<Dict> childData = this.findChildData(dict.getId());
        return childData;
    }

    private Dict getDictByDictCode(String dictCode) {
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dict::getDictCode, dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return codeDict;
    }

    // 判断id下面是否有子节点
    private boolean hasChildren(Long id) {
        LambdaQueryWrapper<Dict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dict::getParentId, id);
        Integer count = baseMapper.selectCount(lambdaQueryWrapper);
        return count > 0;
    }
}
