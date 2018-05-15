package com.tmall.service.impl;

import com.google.common.collect.Lists;
import com.tmall.service.IFileService;
import com.tmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * create by sintai
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService{
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        String fileName= file.getOriginalFilename();
        //扩展名
        //*.jpg;*.gif
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        //打散，得到唯一文件名
        String uploadFileName= UUID.randomUUID().toString().trim()+"."+fileExtensionName;
        logger.info("开始上传，上传的文件名：{}，上传路径：{}，新文件名:{}",fileName,path,uploadFileName);
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        //文件上传成功。。。
        // 将target文件上传到FTP文件服务器上
        try {
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
        } catch (IOException e) {
            logger.error("上传失败",e);
        }
        //删除upload
        targetFile.delete();//删除此抽象路径名表示的文件或目录--若目录下存在文件则不可删除
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }


}
