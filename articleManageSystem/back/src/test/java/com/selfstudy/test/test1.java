//package com.selfstudy.test;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.selfstudy.dto.CategoryFrequencyDTO;
//import com.selfstudy.dto.CategoryUsageDTO;
//import com.selfstudy.mapper.ArticleMapper;
//import com.selfstudy.mapper.CategoryMapper;
//import com.selfstudy.mapper.ChartMapper;
//import com.selfstudy.mapper.TagcategoryMapper;
//import com.selfstudy.pojo.*;
//import com.selfstudy.service.*;
//import com.selfstudy.util.CodeUtil;
//import com.selfstudy.util.VerifyCodeUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//public class test1 {
//    @Autowired
//    CategoryService categoryService;
//    @Autowired
//    ArticleMapper articleMapper;
//    @Autowired
//    ArticleService articleService;
//    @Autowired
//    ChartService chartService;
//    @Autowired
//    private ChartMapper chartMapper;
//    @Autowired
//    private TagcategoryService tagcategoryService;
//    @Autowired
//    private TagcategoryMapper tagcategoryMapper;
//    @Autowired
//    private TagService tagService;
//    @Autowired
//    PayorderService payorderService;
//    @Autowired
//    UserService userService;
//
//    @Test
//    public void test9(){
//        Payorder payorder=new Payorder();
//        payorder.setTraceNo(123);
//        payorderService.save(payorder);
//    }
//    @Test
//    public void test8(){
//        CodeUtil.code();
//    }
//    @Test
//    public void test7(){
//        Integer id = 7;
//        List<Tag> tags = tagService.list(new QueryWrapper<Tag>().eq("userid", id));
//        System.out.println("拿到这个用户的全部标签："+tags);
//    }
//    @Test
//    public void test6(){
//        Integer id=7;
//        QueryWrapper<Tagcategory>queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("create_user",id);
//        List<Tagcategory> list = tagcategoryMapper.selectTagcategory("gg");
//        System.out.println("这是category的list："+list);
//    }
//    @Test
//    public void test5(){
//        Integer id=7;
//        QueryWrapper<Tagcategory>queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("userid",id);
//        List<Tagcategory> list = tagcategoryService.list(queryWrapper);
//        System.out.println("这是tagcategory的list："+list);
//    }
//    @Test
//    public void test1()throws Exception{
//        String s= VerifyCodeUtils.generateVerifyCode(4);
//        System.out.println(s);
//        //写入图片
//        VerifyCodeUtils.outputImage(200,100,new File("E:\\IDEAwrokspacee\\bigevent\\src\\main\\java\\com\\selfstudy\\111.jpg"), s);
//
//    }
//    @Test
//    public void test4(){
//        List<Category> categories = categoryService.list();
//
//        // 获取每个类别的文章使用次数
//        List<CategoryFrequencyDTO> categoryFrequencyDTOS = chartMapper.getCategoryFrequency();
//
//        // 创建一个新的集合来存储 categoryName 和 articleCount
//        List<CategoryUsageDTO> categoryUsageDTOS = new ArrayList<>();
//
//        // 使用两个集合进行映射
//        for (CategoryFrequencyDTO categoryFrequencyDTO : categoryFrequencyDTOS) {
//            // 根据 categoryId 获取 categoryName
//            String categoryName = categories.stream()
//                    .filter(category -> category.getId().equals(categoryFrequencyDTO.getCategoryId()))
//                    .map(Category::getCategoryName)
//                    .findFirst()
//                    .orElse("未知类别"); // 如果找不到对应的类别名称，返回"未知类别"
//
//            // 将 categoryName 和 articleCount 封装到 CategoryUsageDTO 中
//            categoryUsageDTOS.add(new CategoryUsageDTO(categoryName, categoryFrequencyDTO.getArticleCount()));
//        }
//
//        // 返回给前端的数据
//        System.out.println(categoryUsageDTOS);
//
//    }
//    @Test
//    public void test3(){
//        Integer maxid=articleMapper.getNewsetId();
//        System.out.println(maxid);
//    }
//    @Test
//    public void test() {
//        Integer id=7;
//        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("create_user", id);
//        List<Category> list = categoryService.list(queryWrapper);
//        System.out.println(list);
//
//    }
//    @Test
//    public void test2() {
//        Integer pageNum=1;
//        Integer pageSize=3;
//        Integer categoryId=1;
//        Page<Article> page = new Page<>(pageNum, pageSize);
//        articleMapper.selectPage(page, new QueryWrapper<Article>().eq("category_id", categoryId));
//        List<Article> list = page.getRecords();
//        System.out.println("当前页："+page.getCurrent());
//        for (Article article : list) {
//            System.out.println("这是一个文章aaaaa：：：：："+article);
//        }
////        Page<User> page = new Page<>(1, 5);
////        userMapper.selectPage(page, null);
////        //获取分页数据
////        List<User> list = page.getRecords();
////        list.forEach(System.out::println);
////        System.out.println("当前页："+page.getCurrent());
////        System.out.println("每页显示的条数："+page.getSize());
////        System.out.println("总记录数："+page.getTotal());
////        System.out.println("总页数："+page.getPages());
////        System.out.println("是否有上一页："+page.hasPrevious());
////        System.out.println("是否有下一页："+page.hasNext());
//
//    }
//}
