package com.yangyun.generator.constant;

import lombok.Getter;

/**
 * @author yangyun
 * @Description:
 * @date 2020/8/18 11:50
 */
@Getter
public enum GroupClass {

    INSERT("Insert", "com.yl.materialservice.group.Insert"),
    UPDATE("Update", "com.yl.materialservice.group.Update"),
    MATERIAL_RESULT("Result", "com.yl.common.base.model.vo.Result"),
    BOOLEAN("Boolean", "java.lang.Boolean"),
    PAGE("Page", "com.baomidou.mybatisplus.extension.plugins.pagination.Page"),
    ;

    private String className;
    private String classReference;

    GroupClass(String className, String classReference){
        this.className = className;
        this.classReference = classReference;
    }
}
