package com.schoolwork.epsys.model.device;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * @TableName maintain_record
 */
@TableName(value ="maintain_record")
@Data
public class MaintainRecord implements Serializable {
    private Integer id;

    private Integer deviceId;

    private Object maintenanceType;

    private Date startTime;

    private Date endTime;

    private Integer operatorId;

    private String description;

    private Object status;

    private Integer approvalId;

    private Date approvalTime;

    private Integer miantainId;

    @Version
    private Integer version;

    private static final long serialVersionUID = 1L;
}
