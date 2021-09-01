package com.dg.easyexcel;

import com.alibaba.excel.EasyExcel;

/**
 * @Author: DG
 * @Date: 2021/8/31 10:06
 * @Description:
 */
public class ReadTest {

    public static void main(String[] args) {
        String fileName = "D:\\easyExcelTest\\01.xlsx";

        EasyExcel.read(fileName, UserData.class, new ExcelListener()).sheet().doRead();
    }

}
