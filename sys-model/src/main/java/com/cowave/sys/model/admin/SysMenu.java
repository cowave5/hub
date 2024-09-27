/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.cowave.commons.framework.access.AccessUser;
import com.cowave.commons.framework.support.excel.StatusConverter;
import com.cowave.commons.framework.support.excel.YesNoConverter;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 菜单
 *
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
@EqualsAndHashCode(callSuper=false)
public class SysMenu extends AccessUser {

	/**
	 * 上级菜单id
	 */
	@ExcelProperty("上级菜单编号")
	private Long parentId;

	/**
	 * 菜单id
	 */
	@ExcelProperty("菜单编号")
	private Long menuId;

	/**
	 * 菜单名称
	 */
	@NotBlank(message = "{menu.notnull.name}")
	@ExcelProperty("菜单名称")
	private String menuName;

	/**
	 * 菜单状态
	 */
	@ExcelProperty(value = "菜单状态", converter = StatusConverter.class)
	private Integer menuStatus;

	/**
	 * 菜单排序
	 */
	@ExcelProperty("菜单排序")
	private Integer menuOrder;

	/**
	 * 路由地址
	 */
	@ColumnWidth(30)
	@ExcelProperty("菜单路径")
	private String menuPath;

	/**
	 * 路由参数
	 */
	@ExcelProperty("路由参数")
	private String menuParam;

	/**
	 * 菜单图标
	 */
	@ExcelProperty("菜单图标")
	private String menuIcon;

	/**
	 * 菜单类型
	 */
	@NotBlank(message = "{menu.notnull.type}")
	@ExcelProperty("菜单类型")
	private String menuType;

	/**
	 * 菜单权限符
	 */
	@ColumnWidth(30)
	@ExcelProperty("权限符")
	private String menuPermit;

	/**
	 * 组件路径
	 */
	@ColumnWidth(30)
	@ExcelProperty("组件路径")
	private String component;

	/**
	 * 是否内部链接
	 */
	@ExcelProperty(value = "是否内部链接", converter = YesNoConverter.class)
	private Integer isFrame;

	/**
	 * 是否缓存
	 */
	@ExcelProperty(value = "是否缓存", converter = YesNoConverter.class)
	private Integer isCache;

	/**
	 * 是否显示
	 */
	@ExcelProperty(value = "是否显示", converter = YesNoConverter.class)
	private Integer isVisible;

	/**
	 * 是否显示
	 */
	@ExcelProperty(value = "是否受保护的", converter = YesNoConverter.class)
	private Integer isProtected;

	/**
    * 是否只读
    */
	@ExcelProperty(value = "是否只读", converter = YesNoConverter.class)
   private Integer readOnly;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建部门
	 */
	private Long createDept;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新人
	 */
	private Long updateUser;

	/**
	 * 更新部门
	 */
	private Long updateDept;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/**
	 * 备注
	 */
	@ExcelProperty("备注")
	private String remark;

	/**
	 * 子菜单
	 */
	private List<SysMenu> children = new ArrayList<>();

	/**
	 * 是否为内链组件
	 */
	public boolean ifInnerLink() {
		return 1 == isFrame && StringUtils.startsWithAny(menuPath, "http://", "https://");
	}

	/**
	 * 是否为菜单内部跳转
	 */
	public boolean ifMenuFrame(){
		return parentId == 0L && "C".equals(menuType) && 1 == isFrame;
	}

	/**
	 * 是否为parent_view组件
	 */
	public boolean ifParentView() {
		return parentId != 0L && "M".equals(menuType);
	}

	/**
	 * 路由名称
	 */
	public String routeName() {
		String routerName = StringUtils.capitalize(menuPath);
		if (ifMenuFrame()) {
			routerName = "";
		}
		return routerName;
	}

	/**
	 * 路由地址
	 */
	public String routePath() {
		String routerPath = menuPath;
		// 内链打开外网方式
		if (parentId != 0L && ifInnerLink()) {
			routerPath = routerPath.replace("http://", "");
			routerPath = routerPath.replace("https://", "");
		}
		// 非外链并且是一级目录
		if (parentId == 0L && "M".equals(menuType) && 1 == isFrame) {
			routerPath = "/" + menuPath;
		} else if (ifMenuFrame()) {
			routerPath = "/";
		}
		return routerPath;
	}

	/**
	 * 路由组件
	 */
	public String routeComponent() {
		String routeComponent = "Layout";
		if (StringUtils.isNotEmpty(component) && !ifMenuFrame()) {
			routeComponent = component;
		} else if (StringUtils.isEmpty(component) && parentId != 0L && ifInnerLink()) {
			routeComponent = "InnerLink";
		} else if (StringUtils.isEmpty(component) && ifParentView()) {
			routeComponent = "ParentView";
		}
		return routeComponent;
	}
}
