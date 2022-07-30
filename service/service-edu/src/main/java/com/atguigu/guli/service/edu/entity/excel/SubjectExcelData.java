package com.atguigu.guli.service.edu.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Connor
 * @date 2022/7/25
 */
@Data
public class SubjectExcelData {
    @ExcelProperty(value = "一级分类",index = 0)
    private String levelOneSubject;
    @ExcelProperty(value = "二级分类",index = 1)
    private String levelTwoSubject;
}
