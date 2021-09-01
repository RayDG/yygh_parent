package com.dg.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dg.yygh.model.cmn.Dict;
import com.dg.yygh.model.hosp.HospitalSet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: DG
 * @Date: 2021/8/29 00:04
 * @Description:
 */
public interface DictService extends IService<Dict> {

    // 根据数据id查询子数据列表
    List<Dict> findChildData(Long id);

    // 导出数据字典数据
    void exportDictData(HttpServletResponse response);

    // 导入字典数据
    void importDictData(MultipartFile file);
}
