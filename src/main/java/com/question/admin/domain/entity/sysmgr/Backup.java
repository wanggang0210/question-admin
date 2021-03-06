package com.question.admin.domain.entity.sysmgr;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import com.question.admin.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * DB备份表
 * </p>
 *
 * @author zvc
 * @since 2019-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("st_backup")
public class Backup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Date backupDate;

    private String backupName;

    private String backupPath;

    private String dbName;

    private Long fileSize;

    private String remark;

    private Long runtime;

    private Integer status;

    /**
     * 有效标志
     */
    private String ynFlag;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String editor;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 修改时间
     */
    private Date modifiedTime;


}
