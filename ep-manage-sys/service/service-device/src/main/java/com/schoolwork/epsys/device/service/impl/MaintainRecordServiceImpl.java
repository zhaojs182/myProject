package com.schoolwork.epsys.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schoolwork.epsys.model.device.MaintainRecord;
import com.schoolwork.epsys.device.mapper.MaintainRecordMapper;
import com.schoolwork.epsys.device.service.MaintainRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 27959
* @description 针对表【maintain_record】的数据库操作Service实现
* @createDate 2025-04-07 08:24:12
*/
@Service
public class MaintainRecordServiceImpl extends ServiceImpl<MaintainRecordMapper, MaintainRecord>
        implements MaintainRecordService {

    @Override
    @Transactional
    public boolean updateRecordWithLock(MaintainRecord record) {
        return this.updateById(record);  // 调用 MyBatis-Plus 提供的方法
    }
}





