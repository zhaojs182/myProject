package com.selfstudy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.selfstudy.pojo.Tag;
import com.selfstudy.service.TagService;
import com.selfstudy.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author 27959
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2024-12-27 22:06:46
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




