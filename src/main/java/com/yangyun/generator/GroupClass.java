package com.yangyun.generator;

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
    ;

    private String className;
    private String classReference;

    GroupClass(String className, String classReference){
        this.className = className;
        this.classReference = classReference;
    }
}
