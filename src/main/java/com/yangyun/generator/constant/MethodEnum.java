package com.yangyun.generator.constant;

import lombok.Getter;

/**
 * @author yangyun
 * @Description:
 * @date 2020/10/14 9:36
 */
@Getter
public enum MethodEnum {
    SAVE("save", "新增"),
    DELETE("delete", "删除"),
    UPDATE("update", "修改"),
    PAGE("page", "列表查询"),
    DETAIL("detail", "详情查看"),
    ;

    private String enName;
    private String cnName;

    MethodEnum (String enName, String cnName){
        this.enName = enName;
        this.cnName = cnName;
    }
}
