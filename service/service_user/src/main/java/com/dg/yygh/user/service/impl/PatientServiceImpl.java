package com.dg.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dg.yygh.cmn.client.DictFeignClient;
import com.dg.yygh.enums.DictEnum;
import com.dg.yygh.model.user.Patient;
import com.dg.yygh.user.mapper.PatientMapper;
import com.dg.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: DG
 * @Date: 2021/9/23 15:08
 * @Description:
 */
@Service
public class PatientServiceImpl extends
        ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private DictFeignClient dictFeignClient;

    // 获取就诊人列表
    @Override
    public List<Patient> findAllByUserId(Long userId) {
        // 根据userId查询所有就诊人信息列表
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId);
        List<Patient> patientList = baseMapper.selectList(wrapper);
        // 通过远程调用，得到编码对应的具体内容，查询数据字典表内容
        patientList.stream().forEach(item -> {
            // 其他参数封装
            this.packagePatient(item);
        });
        return patientList;
    }

    // 封装Patient对象的其他参数
    private Patient packagePatient(Patient patient) {
        // 根据证件编号，获取证件类型具体值
        String certificatesTypeString = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),
                patient.getCertificatesType());
        //联系人证件类型
        String contactsCertificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsCertificatesType());
        //省
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        //市
        String cityString = dictFeignClient.getName(patient.getCityCode());
        //区
        String districtString = dictFeignClient.getName(patient.getDistrictCode());

        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
        return patient;
    }

    @Override
    public Patient getPatientById(Long id) {
        return this.packagePatient(baseMapper.selectById(id));
    }
}
