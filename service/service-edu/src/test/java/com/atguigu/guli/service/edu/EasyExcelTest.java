package com.atguigu.guli.service.edu;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.atguigu.guli.service.edu.entity.Stu;
import com.atguigu.guli.service.edu.entity.excel.SubjectExcelData;
import com.atguigu.guli.service.edu.listener.SubjectDataListener;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Connor
 * @date 2022/7/25
 */
@SpringBootTest
public class EasyExcelTest {
    @Test
    public void testWrite(){
        EasyExcel.write("H:\\Test\\stu"+ ExcelTypeEnum.XLSX.getValue())
                .excelType(ExcelTypeEnum.XLSX)
                .head(Stu.class)
                .sheet(0)
                .doWrite(data());
    }

    private List<Stu> data() {
        ArrayList<Stu> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Stu stu = new Stu();
            stu.setAge(RandomUtils.nextInt(18,30));
            stu.setBirthday(new Date());
            stu.setHeight(RandomUtils.nextDouble(150 , 190));
            stu.setId(100000+i+"");
            stu.setName("fangfang"+i);
            list.add(stu);
        }
        return list;
    }
    
    @Test
    public void testRead(){
        EasyExcel.read("H:\\尚硅谷_220310班\\07_Es_分布式事务\\13-尚硅谷JavaEE技术之在线教育\\13-尚硅谷JavaEE技术之在线教育\\A资料\\静态资源\\excel\\课程分类列表.xls")
                .sheet(0)
                .head(SubjectExcelData.class)
                .registerReadListener(new SubjectDataListener())
                .doRead();
    }
}
