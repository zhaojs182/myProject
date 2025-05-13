//package com.selfstudy.test;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.selfstudy.mapper.ArticleMapper;
//import com.selfstudy.mapper.UserMapper;
//import com.selfstudy.pojo.Article;
//import com.selfstudy.pojo.User;
//import com.selfstudy.service.ArticleService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
//@SpringBootTest
//public class RedisTemplateTest {
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private ArticleMapper articleMapper;
//    @Autowired
//    private ArticleService articleService;
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate1;
//    @Test
//    void testRedis123() {
//        Set<String> keys = redisTemplate1.keys("article_list*"); // 查找所有以 article_list 开头的键
//        if (keys != null) {
//            redisTemplate1.delete(keys); // 删除这些键
//        }
//    }
//    @Test
//    void tsetRedis() {
//
//        List<Article>articleList=articleService.getArticleList();
//
//
//        System.out.println(articleList);
//
//    }
////    @Test
////    void testHashQueryByPhonenum() {
////        // 假设我们查询的 Redis key 为 "user:{id}"
////        String redisKey = "user:*"; // 假设我们不知道具体的 id，要查询所有
////        Map<Object, Object> userMap = redisTemplate.opsForHash().entries(redisKey); // 获取所有字段
////
////        // 根据 phonenum 查找整个 user 数据
////        userMap.forEach((key, value) -> {
////            if ("phonenum".equals(key) && value.equals("18258005708")) { // 替换 desiredPhoneNumber 为要查找的 phonenum
////                System.out.println("找到了User found with phonenum: " + value);
////                // 根据找到的 phonenum，你可以进一步查询其他字段
////            }
////        });
////    }
//
//    @Test
//    void testHashQueryByPhonenum() {
//        String phonenum = "18258005708";
//        // 获取所有匹配的 Redis 键
//        Set<String> keys = redisTemplate.keys("user:*"); // 获取所有以 "user:" 开头的键
//        if (keys != null) {
//            for (String redisKey : keys) {
//                // 获取 Hash 数据
//                Map<Object, Object> userMap = redisTemplate.opsForHash().entries(redisKey);
//
//                // 检查 phonenum 字段是否匹配
//                if (userMap.containsKey("phonenum") && phonenum.equals(userMap.get("phonenum"))) {
//                    System.out.println("找到了User with phonenum: " + userMap.get("phonenum"));
//                    // 你可以在这里打印出其他字段
//                    System.out.println("User details: " + userMap);
//                }
//            }
//        } else {
//            System.out.println("No user keys found.");
//        }
//    }
//    @Test
//    void testHash1() {
//        // 查询数据库获取 User 对象
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", 8);
//        User user = userMapper.selectOne(queryWrapper);
//        System.out.println("User from DB: " + user);
////        user.setEmail("123456@qq.com");
//        if (user != null) {
//            // 使用 Redis Hash 存储 User 对象
//            String redisKey = "user:" + user.getId(); // Redis key 为 user:id 格式
//            // 或者一次性保存所有字段
//            Map<String, Object> userMap = new HashMap<>();
//            userMap.put("id", user.getId());
//            userMap.put("username", user.getUsername());
//            userMap.put("nickname", user.getNickname());
//            userMap.put("email", "123123@qq.com");
//            userMap.put("userPic", user.getUserPic());
//            userMap.put("createTime", user.getCreateTime());
//            userMap.put("updateTime", user.getUpdateTime());
//            userMap.put("phonenum", user.getPhonenum());
//
//            // 一次性存储所有字段到 Redis Hash
//            redisTemplate.opsForHash().putAll(redisKey, userMap);
//
//            redisTemplate.expire(redisKey, 10,TimeUnit.SECONDS);
//
////            System.out.println("User saved to Redis with user: " + user);
//        } else {
//            System.out.println("User not found.");
//        }
//    }
//
//
//    @Test
//    void testString() {
//        QueryWrapper<User>queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("id",7);
//        User user=new User();
//        user=userMapper.selectOne(queryWrapper);
//        System.out.println(user);
//        // 写入一条String数据
////        redisTemplate.opsForValue().set("name", "王一", 60, TimeUnit.SECONDS);
////        //      获取string数据
////        Object name=redisTemplate.opsForValue().get("name");
////        System.out.println("name = " + name);
//    }
//    @Test
//    void testHash(){
//        stringRedisTemplate.opsForHash().put("user2", "name2", "王一2");
//        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("user2");
//        System.out.println(entries);
//    }
//}
//
//
