package com.fzq.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
// @TableName(value = "user") // 对应数据库表名
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 4090018450386287406L;

	@TableId(type = IdType.AUTO) // 声明主键自增
	private Long id;

	private String username;

	private String userAccount;

	private String avatarUrl;

	private Integer gender;

	private Integer userRole;

	private String userPassword;

	private String phone;

	private String email;

	private Integer userStatus;

	private Date createTime;

	private Date updateTime;

	@TableLogic
	private Integer isDelete;

}