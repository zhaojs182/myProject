package com.selfstudy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.selfstudy.common.Result;
import com.selfstudy.common.ResultCodeEnum;
import com.selfstudy.pojo.User;
import com.selfstudy.service.UserService;
import com.selfstudy.util.JwtUtils;
import com.selfstudy.util.SmsUtil;
import com.selfstudy.util.VerifyCodeUtils;
import com.selfstudy.util.WebUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("/login")
    public void login(HttpServletRequest req, HttpServletResponse resp, @Pattern(regexp = "^\\S{3,16}$") String username,
                      @Pattern(regexp = "^\\S{5,16}$") String password) throws ServletException, IOException{
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user1 = userService.getOne(queryWrapper);
        if(user1.getPassword().equals(password)&&user1.getState()==1){
//            System.out.println("这是user1.getId()："+user1.getId());
//            System.out.println("这是user1.getUsername()："+user1.getUsername());
            String token = JwtUtils.setJwt(user1.getId(), user1.getUsername());
            System.out.println("这是token："+token);
            Map data = new HashMap();
            data.put("token",token);
            Result result = Result.ok(data);
            WebUtil.writeJson(resp,result);
        }else{
            Result result = Result.build(null, ResultCodeEnum.USERNAEM_ERROR);
            WebUtil.writeJson(resp,result);
        }
    }

    @RequestMapping("/getPhonenumCaptcha")
    public void getPhonenumCaptcha(@RequestBody Map<String, String> requestData, HttpSession session, HttpServletResponse resp) throws Exception {
        String phonenum = requestData.get("phonenum");
        System.out.println("这是发出的手机号1111111111111111111111111phonenum:" + phonenum);
        // 获取所有匹配的 Redis 键
        Set<String> keys = redisTemplate.keys("user:*"); // 获取所有以 "user:" 开头的键
        if (keys != null) {
            for (String redisKey : keys) {
                // 获取 Hash 数据
                Map<Object, Object> userMap = redisTemplate.opsForHash().entries(redisKey);

                // 检查 phonenum 字段是否匹配
                if (userMap.containsKey("phonenum") && phonenum.equals(userMap.get("phonenum"))) {
                    System.out.println("找到了User with phonenum: " + userMap.get("phonenum"));
                    // 你可以在这里打印出其他字段
                    System.out.println("User details: " + userMap);
                    return;
                }
            }
        }
        // 验证码发送
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phonenum", phonenum);
        User user=userService.getOne(queryWrapper);
        if (user != null) {
            // 使用 Redis Hash 存储 User 对象
            String redisKey = "user:" + user.getId(); // Redis key 为 user:id 格式
            // 或者一次性保存所有字段
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("nickname", user.getNickname());
            userMap.put("email", user.getEmail());
            userMap.put("userPic", user.getUserPic());
            userMap.put("createTime", user.getCreateTime());
            userMap.put("updateTime", user.getUpdateTime());
            userMap.put("phonenum", user.getPhonenum());

            // 一次性存储所有字段到 Redis Hash
            redisTemplate.opsForHash().putAll(redisKey, userMap);

            // 设置该 Redis key 的过期时间为 10 分钟
            redisTemplate.expire(redisKey, 10, TimeUnit.MINUTES);

            System.out.println("User saved to Redis with key: " + redisKey);
        } else {
            System.out.println("User not found.");
        }
        Random random = new Random();
        int captcha = 1000 + random.nextInt(9000);  // 生成范围在1000到9999之间的随机数
        // 保存验证码到 Redis（这里你可以使用验证码保存的时间）
        redisTemplate.opsForValue().set(phonenum, String.valueOf(captcha),300, TimeUnit.SECONDS);
        SmsUtil smsUtil = new SmsUtil();
        smsUtil.sendSms(phonenum,String.valueOf(captcha));
        // 构造返回结果
        Result result = Result.ok(null);
        WebUtil.writeJson(resp, result);
    }


    @RequestMapping("/register")
    public void register(HttpServletRequest req, HttpServletResponse resp, @Pattern(regexp = "^\\S{3,16}$") String username,
                         @Pattern(regexp = "^\\S{5,16}$") String password, String phonenum) throws ServletException, IOException{
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setPhonenum(phonenum);
        user.setState(1);
        user.setRole(0);
        Boolean flag=userService.save(user);
        Result result = Result.ok(null);

        if(!flag){
            result=Result.build(null, ResultCodeEnum.USERNAME_USED);
        }
        WebUtil.writeJson(resp,result);
    }
    @RequestMapping("/captcha")
    public void captcha(HttpSession session, HttpServletResponse resp) throws Exception {
        // 生成验证码
        String captcha = VerifyCodeUtils.generateVerifyCode(4);
        // 将验证码文本存储到 session 中
        session.setAttribute("captcha", captcha);

        // 设置响应类型为 JSON（因为我们要返回 Base64 字符串）
        resp.setContentType("application/json");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        // 生成验证码图片
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(120, 40, byteArrayOutputStream, captcha);

        // 将图片转为 Base64 编码字符串
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes); // 将字节数据转换为 Base64 字符串

        // 返回 JSON 格式的数据
        Map<String, String> data = new HashMap<>();
        data.put("captchanum", captcha);
        data.put("captchaImage", base64Image);  // 保存 Base64 编码的验证码图像
        Result result = Result.ok(data);
        // 将结果返回给前端
        WebUtil.writeJson(resp, result);
    }
    @RequestMapping("/updataAvatar")
    public void updataAvatar( @RequestHeader(value = "Authorization", required = false) String authorization,@RequestParam("imgUrl")String imgUrl,
                              HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("这是11111111111111111111imgurl：" + imgUrl);
        String token = authorization;
        Claims claims = JwtUtils.parseJwt(token);
        Integer id = claims.get("id", Integer.class);
        User user = userService.getById(id);
        user.setUserPic(imgUrl);
        userService.updateById(user);
        Result result = Result.ok(null);
        WebUtil.writeJson(resp, result);
    }
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void update(@RequestBody User user, HttpServletResponse resp) throws ServletException, IOException {
        // 打印用户的 ID 来确认接收到的数据
        System.out.println("这是11111111111111111111user的id：" + user.getId());

        // 使用用户的 ID 查询并更新
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", user.getId());
            Boolean flag = userService.updateById(user);

            Result result = flag ? Result.ok(null) : Result.build(null, ResultCodeEnum.UPDATA_ERROR);
            WebUtil.writeJson(resp, result);

    }
    @RequestMapping("/getUserInfo")
    public void getUserInfo(@RequestHeader(value = "Authorization", required = false) String authorization,HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = authorization;
        Claims claims = JwtUtils.parseJwt(token);
        Integer id = claims.get("id", Integer.class);
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id",id);
        User user=userService.getById(id);
        System.out.println(user);
        Map data = new HashMap();
        data.put("user",user);
        Result result = Result.ok(data);
        WebUtil.writeJson(resp,result);
    }



}
