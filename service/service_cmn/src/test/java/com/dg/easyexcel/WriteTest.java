package com.dg.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: DG
 * @Date: 2021/8/31 09:51
 * @Description:
 */
public class WriteTest {

    public static void main(String[] args) {
        List<UserData> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            UserData userData = new UserData();
            userData.setUid(i);
            userData.setUsername("DG" + i);
            list.add(userData);
        }

        String fileName = "D:\\easyExcelTest\\01.xlsx";

        EasyExcel.write(fileName, UserData.class)
                .sheet("用户信息")
                .doWrite(list);

    }

}
