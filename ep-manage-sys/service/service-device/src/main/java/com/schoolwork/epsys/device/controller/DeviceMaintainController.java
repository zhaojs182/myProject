package com.schoolwork.epsys.device.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.schoolwork.epsys.acl.client.AclFeignClient;
import com.schoolwork.epsys.common.Result;
import com.schoolwork.epsys.device.mapper.MaintainRecordMapper;
import com.schoolwork.epsys.device.service.MaintainRecordService;
import com.schoolwork.epsys.message.client.MessageFeignClient;
import com.schoolwork.epsys.model.device.Devicemodel;
import com.schoolwork.epsys.model.device.MaintainRecord;
import com.schoolwork.epsys.model.shared.UserNotification;
import com.schoolwork.epsys.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/deviceMaintain")
public class DeviceMaintainController {

    @Autowired
    private MaintainRecordService maintainRecordService;

    @Autowired
    private MaintainRecordMapper maintainRecordMapper;

    @Autowired
    MessageFeignClient messageFeignClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AclFeignClient aclFeignClient;

    @Autowired
    private RedissonClient redissonClient;



    @PostMapping("/getMaintainOrder")
    public void getMaintainOrder(HttpServletRequest req, HttpServletResponse resp, @RequestBody MaintainRecord maintainRecord) {
        System.out.println("这是传入的maintainRecord=" + maintainRecord);
        // 1. 构造锁 key：确保并发操作同一条数据时加锁
        String lockKey = "order:lock:" + maintainRecord.getId();
        RLock lock = redissonClient.getLock(lockKey);

        boolean isLocked = false; // 标记是否成功获取到锁
        Result result=null;

        try {
            // 2. 尝试加锁，最多等待 3 秒，加锁成功后最多占有锁 10 秒
            isLocked = lock.tryLock(3, 10, TimeUnit.SECONDS);

            if (isLocked) {
                // 3. 查询数据库记录，获取版本号等完整数据
                MaintainRecord dbRecord = maintainRecordService.getById(maintainRecord.getId());
                System.out.println("这是从数据库获取到的maintainRecord=" + dbRecord);
                if (dbRecord == null) {
                    result = Result.error("未找到对应的工单！");
                } else {
                    // 4. 构造更新对象，设置 version 和需要修改的字段
                    dbRecord.setStatus("维护中");
                    dbRecord.setMiantainId(maintainRecord.getMiantainId());
                    System.out.println("这是更新后的maintainRecord=" + dbRecord);
                    // 5. 调用更新逻辑
                    boolean isUpdated = maintainRecordService.updateRecordWithLock(dbRecord);
                    result = isUpdated ? Result.ok("获取订单成功！") : Result.error("获取订单失败！");
                }
            } else {
                result = Result.error("系统繁忙，请稍后再试！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("系统异常：" + e.getMessage());
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            WebUtil.writeJson(resp, result);
        }
    }




    @RequestMapping("/getMaintainRecord")
    public  void getMaintainRecord(HttpServletRequest req, HttpServletResponse resp,
                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "3") int pageSize) {
        // 创建分页对象
        Page<MaintainRecord> page = new Page<>(pageNum, pageSize);
        maintainRecordMapper.selectPage(page, null);
        List<MaintainRecord> records = page.getRecords();

        Map data = new HashMap();
        data.put("records", records);
        data.put("total", page.getTotal());
        Result result =Result.ok(data);
        WebUtil.writeJson(resp, result);

    }


    @RequestMapping("/getMaintainRecordById")
    public  void getMaintainRecordById(HttpServletRequest req, HttpServletResponse resp,
                                   @RequestParam(value = "id") int id) {
        MaintainRecord maintainRecord = maintainRecordMapper.selectById(id);
        Map data = new HashMap();
        data.put("record", maintainRecord);
        Result result =Result.ok(data);
        WebUtil.writeJson(resp, result);
    }

    @RequestMapping("/createMaintainRecord")
    public void createMaintainRecord(HttpServletRequest req, HttpServletResponse resp,
                                     @RequestBody MaintainRecord maintainRecord) {
        System.out.println("maintainRecord=" + maintainRecord);

        // 设置开始时间
        maintainRecord.setStartTime(new Date());

        // 保存维护记录
        boolean isCreated = maintainRecordService.save(maintainRecord);

        if (isCreated) {
            // 发送广播通知管理员
            try {
                System.out.println("发送广播通知管理员");
                messageFeignClient.sendToTopic4Maintain();  // Feign调用
                Result result = Result.ok("维护记录创建成功，消息已广播");
                WebUtil.writeJson(resp, result);
            } catch (Exception e) {
                e.printStackTrace();
                Result result = Result.ok("维护记录创建成功，但消息广播失败");
                WebUtil.writeJson(resp, result);
            }
        } else {
            Result result = Result.error("维护记录创建失败");
            WebUtil.writeJson(resp, result);
        }
    }

    @RequestMapping("/approvalMaintainRecord")
    public void approvalMaintainRecord(HttpServletRequest req, HttpServletResponse resp,
                                   @RequestBody MaintainRecord maintainRecord) {
    System.out.println("maintainRecord=" + maintainRecord);

    maintainRecord.setApprovalTime(new Date());
    Boolean isUpdated = maintainRecordService.updateById(maintainRecord);
    UserNotification userNotification = new UserNotification();
    System.out.println("这里开始调用acl的方法");
    String uname=aclFeignClient.getUsernameById(maintainRecord.getOperatorId());
    System.out.println("acl的方法调用完了");
    userNotification.setUsername(uname);
    userNotification.setNotification("您的报修单"+maintainRecord.getId()+"已被审批，审批结果为："+maintainRecord.getStatus());
    userNotification.setReceiverId(maintainRecord.getOperatorId());
    userNotification.setIsRead(0);
    userNotification.setCreateTime(new Date());
    if (isUpdated) {
        // 1. 通知单个用户（审批结果）
        rabbitTemplate.convertAndSend(
                "approval.direct",   // 交换机
                "user.notify",       // routingKey
                userNotification       // 通知用户
        );
        System.out.println("这里开始准备发送消息给维修员");
       if(maintainRecord.getStatus().equals("已通过")){
           System.out.println("这里开始发送消息给维修员");
           // 2. 通知所有维修员刷新
           rabbitTemplate.convertAndSend(
                   "approval.direct",
                   "repairman.refresh",
                   maintainRecord
           );
       }
        Result result = Result.ok("审批成功！");
        WebUtil.writeJson(resp, result);
    } else {
        Result result = Result.error("审批失败，可能已被他人审批，请刷新后重试！");
        WebUtil.writeJson(resp, result);
        }
    }


    // 获取当前用户发起的维护订单
    @RequestMapping("/getMyMaintainOrder")
    public void getMyMaintainOrder(HttpServletRequest req, HttpServletResponse resp,
                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "3") int pageSize,@RequestParam(value = "userId") String userId) {

        // 创建分页对象
        Page<MaintainRecord> page = new Page<>(pageNum, pageSize);
        QueryWrapper<MaintainRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operator_id", userId);
        maintainRecordMapper.selectPage(page, queryWrapper);
        List<MaintainRecord> records = page.getRecords();
        Map data = new HashMap();
        data.put("records", records);
        data.put("total", page.getTotal());
        Result result =Result.ok(data);
        WebUtil.writeJson(resp, result);

    }
    @RequestMapping("/getMyRepairOrder")
    public void getMyRepairOrder(HttpServletRequest req, HttpServletResponse resp,
                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "3") int pageSize,@RequestParam(value = "userId") String userId) {

        // 创建分页对象
        Page<MaintainRecord> page = new Page<>(pageNum, pageSize);
        QueryWrapper<MaintainRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("miantain_id", userId);
        maintainRecordMapper.selectPage(page, queryWrapper);
        List<MaintainRecord> records = page.getRecords();
        Map data = new HashMap();
        data.put("records", records);
        data.put("total", page.getTotal());
        Result result =Result.ok(data);
        WebUtil.writeJson(resp, result);
    }

    @RequestMapping("/updateMyRepairOrder")
    public void updateMyRepairOrder(HttpServletRequest req, HttpServletResponse resp,
                                   @RequestBody MaintainRecord maintainRecord) {
        System.out.println("maintainRecord=" + maintainRecord);
        maintainRecord.setEndTime(new Date());
        maintainRecord.setStatus("正常");
        Boolean isUpdated = maintainRecordService.updateById(maintainRecord);
        if (isUpdated) {
            Result result = Result.ok("更新成功！");
            WebUtil.writeJson(resp, result);
        } else {
            Result result = Result.error("更新失败！");
            WebUtil.writeJson(resp, result);
        }
    }
//    List<Integer> maintainRecordIds =null;



}
