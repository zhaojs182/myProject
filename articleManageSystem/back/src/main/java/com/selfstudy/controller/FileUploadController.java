package com.selfstudy.controller;


import com.selfstudy.common.Result;
import com.selfstudy.service.UserService;
import com.selfstudy.util.AliOssUtil;
import com.selfstudy.util.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.io.File;

//@CrossOrigin
@Controller
@Validated
@RequestMapping("/file")
public class FileUploadController {
    @Autowired
    UserService userService;

    @PostMapping("/upload")
    public void upload(MultipartFile file, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String originalFilename = file.getOriginalFilename();
//        System.out.println("这是要上传的本地文件的名字:aaaaa"+originalFilename);
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileLocalPath = "C:\\Users\\27959\\Desktop\\云测试图片\\" + filename;
        file.transferTo(new File(fileLocalPath));
//        String url = AliOssUtil.uploadFile(filename,file.getInputStream());
        String url = AliOssUtil.uploadFile(filename, fileLocalPath);
        System.out.println("这是要上传的url" + url);
        Map data = new HashMap();
        data.put("url", url);
        Result result = Result.ok(data);
        WebUtil.writeJson(resp, result);

    }
}
//    @PostMapping("/avatarUpload")
//    public void avatarUpload(MultipartFile avatar, HttpServletRequest req, HttpServletResponse resp) throws Exception {
////        System.out.println("这是要上传的文件"+avatar+"     222222222222222这是用户的id"+userId   );
////        User user=userService.getById(userId);
//        String originalFilename = avatar.getOriginalFilename();
//        System.out.println("这是要上传的本地文件的名字:aaaaa"+originalFilename);
//        String filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
//        String fileLocalPath = "C:\\Users\\27959\\Desktop\\云测试图片\\"+filename;
//        avatar.transferTo(new File(fileLocalPath));
////        String url = AliOssUtil.uploadFile(filename,file.getInputStream());
//        String url = AliOssUtil.uploadFile(filename,fileLocalPath);
////        user.setUserPic(url);
////        Boolean result1 = userService.updateById(user);
////        if(result1){
////            System.out.println("头像上传成功");
////        }else{
////            System.out.println("头像上传失败");
////        }
//        System.out.println("这是要上传的url"+url);
//        Map data = new HashMap();
//        data.put("url",url);
//        Result result = Result.ok(data);
//        WebUtil.writeJson(resp,result);
////        4fc9c162-0b1a-4f85-b2e4-242a7e895f99
//    }




