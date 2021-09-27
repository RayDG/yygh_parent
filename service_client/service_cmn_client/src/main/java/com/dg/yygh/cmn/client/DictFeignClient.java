package com.dg.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: DG
 * @Date: 2021/9/2 14:40
 * @Description:
 */
@FeignClient(name = "service-cmn")
@Repository
public interface DictFeignClient {
    // 根据 dictcode 和 value 查询 dictName
    @GetMapping(value = "/admin/cmn/dict/getName/{dictCode}/{value}")
    String getName(@PathVariable("dictCode") String dictCode,
                   @PathVariable("value") String value);

    // 根据value查询dictName
    @GetMapping(value = "/admin/cmn/dict/getName/{value}")
    String getName(@PathVariable("value") String value);
}
