package com.schoolwork.epsys.device.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.schoolwork.epsys.common.Result;
import com.schoolwork.epsys.device.service.DeviceinstanceService;
import com.schoolwork.epsys.device.service.MaintainRecordService;
import com.schoolwork.epsys.message.client.MessageFeignClient;
import com.schoolwork.epsys.model.device.Deviceinstance;
import com.schoolwork.epsys.model.search.DeviceInstanceDoc;
import com.schoolwork.epsys.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deviceInstance")
public class DeviceInstanceInfo {

    @Autowired
    private DeviceinstanceService deviceinstanceService;

    @Autowired
    private MaintainRecordService maintainRecordService;

    @Autowired
    private MessageFeignClient messageFeignClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/updateDeviceInstanceStatus")
    public void updateDeviceInstanceStatus(HttpServletRequest req, HttpServletResponse resp,
                                           @RequestParam(value = "deviceId") Integer id,
                                           @RequestParam(value = "status") String status) {
        System.out.println("id=" + id);
        System.out.println("status=" + status);
        Deviceinstance deviceinstance =deviceinstanceService.getOne(new QueryWrapper<Deviceinstance>().eq("id", id));
        deviceinstance.setStatus(status);
        boolean isUpdated = deviceinstanceService.updateById(deviceinstance);
        if (isUpdated) {
            Result result = Result.ok("设备状态更新成功");
            WebUtil.writeJson(resp, result);
        } else {
            Result result = Result.error("设备状态更新失败");
            WebUtil.writeJson(resp, result);
        }
    }



    @RequestMapping("/getDeviceInstanceListById")
    public List<Deviceinstance> getDeviceInstanceListById(HttpServletRequest req, HttpServletResponse resp,
                                                          @RequestBody List<Integer> deviceInstanceIds) {
        if (deviceInstanceIds == null || deviceInstanceIds.isEmpty()) {
            return null;
        }
        List<Deviceinstance>deviceInstanceList = deviceinstanceService.listByIds(deviceInstanceIds);
        System.out.println("deviceInstanceList="+deviceInstanceList);
        return deviceInstanceList;
    }



    @RequestMapping("/getBriefInfoList")
    public void getBriefInfoList(HttpServletRequest req, HttpServletResponse resp,
                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "3") int pageSize,
                                 @RequestParam(value = "id", required = false) Integer id) {
        System.out.println("查了查了...................................");
        System.out.println("pageNum="+pageNum);
        System.out.println("pageSize="+pageSize);
        System.out.println("id="+id);
        Page<Deviceinstance> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Deviceinstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("model_id", id);
        // 查询数据库并应用分页和条件
        List<Deviceinstance> list = deviceinstanceService.page(page, queryWrapper).getRecords();
        // 封装分页数据
        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("items", list);
        // 返回结果
        Result result = Result.ok(data);
        WebUtil.writeJson(resp, result);
    }

    @RequestMapping("/getTrueDetailInfoList/{id}")  // 使用 {id} 占位符
    public void getDetailInfoList(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable Integer id  // 使用 @PathVariable 接收路径参数
    ) {
        QueryWrapper<Deviceinstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Deviceinstance deviceinstance = deviceinstanceService.getOne(queryWrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("deviceinstance", deviceinstance);
        Result result = Result.ok(data);
        WebUtil.writeJson(resp, result);
    }

@RequestMapping("/deviceTrueDeleteService/{id}")
public void deviceTrueDeleteService(HttpServletRequest req, HttpServletResponse resp, @PathVariable Integer id) {
    System.out.println("这是删除的参数" + id);
    boolean isDeleted = deviceinstanceService.removeById(id);
    Integer maintainRecordId= id;
    if (isDeleted) {
        // 发送设备删除消息到 RabbitMQ
        rabbitTemplate.convertAndSend(
                "device_instance_exchange", // 交换机名称
                "deviceInstance.decrease", // routing key
                maintainRecordId                          // 删除时只发 id 即可
        );

        Result result = Result.ok("设备删除成功");
        WebUtil.writeJson(resp, result);
    } else {
        Result result = Result.error("设备删除失败");
        WebUtil.writeJson(resp, result);
    }
}

    @RequestMapping("/updateDeviceInstance")
    public void updateDeviceInstance(HttpServletRequest req, HttpServletResponse resp, @RequestBody Deviceinstance deviceinstance) {
        System.out.println("deviceinstance=" + deviceinstance);
        deviceinstance.setCreateAt(new Date());

        Integer id = deviceinstance.getId();
        if (id == null) {
            // 1. 保存设备
            deviceinstance.setStatus("闲置");
            boolean isCreated = deviceinstanceService.save(deviceinstance);
            if (isCreated) {

                rabbitTemplate.convertAndSend(
                        "device_instance_exchange",
                        "deviceInstance.increase",
                        deviceinstance
                );
                System.out.println("设备信息添加成功");
                Result result = Result.ok("设备信息添加成功");
                WebUtil.writeJson(resp, result);
                return;
            } else {
                Result result = Result.error("设备信息添加失败");
                WebUtil.writeJson(resp, result);
                return;
            }
        }
        // 更新逻辑
        boolean isUpdated = deviceinstanceService.updateById(deviceinstance);
        DeviceInstanceDoc deviceInstanceDoc = new DeviceInstanceDoc();
        deviceInstanceDoc.setId(deviceinstance.getId());
        deviceInstanceDoc.setModelId(deviceinstance.getModelId());
        deviceInstanceDoc.setSerialNumber(deviceinstance.getSerialNumber());
        deviceInstanceDoc.setStatus(deviceinstance.getStatus().toString());
        deviceInstanceDoc.setCreateAt(deviceinstance.getCreateAt());
        deviceInstanceDoc.setLocation(deviceinstance.getLocation());
        if (isUpdated) {
            rabbitTemplate.convertAndSend(
                    "device.instance.exchange",
                    "device.instance.increase",
                    deviceInstanceDoc
            );

            Result result = Result.ok("设备信息更新成功");
            WebUtil.writeJson(resp, result);
        } else {
            Result result = Result.error("设备信息更新失败");
            WebUtil.writeJson(resp, result);
        }
    }






}
