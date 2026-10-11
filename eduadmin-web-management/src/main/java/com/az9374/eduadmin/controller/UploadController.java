package com.az9374.eduadmin.controller;

import com.az9374.eduadmin.pojo.Result;
import com.az9374.eduadmin.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    /**
     * 文件本地磁盘存储
     * */
/*    @RequestMapping("/upload")
    public Result Upload(String name,Integer age,
                         MultipartFile file) throws IOException {
        log.info("上传文件：{},{},{}",name,age,file);
//        文件名--重名问题
        String flName =  UUID.randomUUID().toString();
//        文件名--后缀问题
        String fileName= file.getOriginalFilename();
        String flNameLast=fileName.substring(fileName.lastIndexOf("."));
//        保存文件
//        file.transferTo(new File("D:\\IdeaCode\\uploadFiles\\"+fileName));
        file.transferTo(new File( "D:\\IdeaCode\\uploadFiles\\"+flName+flNameLast));
        return Result.success();
    }*/

    /**
     * 文件-阿里云OOS存储
     * */
    @RequestMapping("/upload")
    public Result Upload(MultipartFile file) throws Exception {
        log.info("上传文件：{}",file);
        String url= aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
//        返回图片路径给前端
        return Result.success(url);
    }


}
