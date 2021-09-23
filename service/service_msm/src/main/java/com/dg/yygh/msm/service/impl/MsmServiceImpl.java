package com.dg.yygh.msm.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.dg.yygh.msm.service.MsmService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;

/**
 * @Author: DG
 * @Date: 2021/9/22 20:40
 * @Description:
 */
@Service
public class MsmServiceImpl implements MsmService {

    @Override
    public boolean send(String phone, String code) {
        // 整合容联云短信服务
        //生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = "8aaf07087bc82708017c01aea282104a"; // 需修改
        String accountToken = "c91d807d125c4fbea9efd56f4819d09e"; // 需修改
        //请使用管理控制台中已创建应用的APPID
        String appId = "8aaf07087bc82708017c01aea3961051";  // 需修改
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        // 用逗号隔开手机号，官方说最多写三个手机号，似乎能实现群发的效果，但是我没做测试
        // 发送到固定手机号
        String to = "15573527289";
        String templateId= "1"; // 测试模板写死是1
        String[] datas = {code,"2"}; // datas就是发过去短信中的模板，code就是服务端生成的验证码， 3表示几分钟内有效
        // 下面这些可选的都不要选，否则会出问题
        //String subAppend="1234";  //可选 扩展码，四位数字 0~9999
        //String reqId="fadfafas";  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
        //HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
        HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas/*,subAppend,reqId*/);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
            return true;
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
            return false;
        }
    }
}
