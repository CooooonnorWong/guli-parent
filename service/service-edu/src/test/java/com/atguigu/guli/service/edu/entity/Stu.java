package com.atguigu.guli.service.edu.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Connor
 * @date 2022/7/25
 */
@Data
public class Stu {
    //value代表写入到excel的表头内容   index代表写入到excel的索引序号，从0开始 默认-1，从左开始哪一列空着写到哪一列
    @ExcelProperty(value = "编号" , index = 8)
    private String id;
    @ExcelProperty(value = "姓名" )
    private String name;
    @ExcelIgnore //忽略当前属性
    private Integer age;
    @ExcelProperty(value = "身高" )
    @NumberFormat(value = "#.##") //格式化数字   .##    一个#表示保留小数点后一位
    private Double height;
    @ExcelProperty(value = "生日")
    @DateTimeFormat(value = "yyyy年MM月dd日")
    private Date birthday;
}
