package com.yangyun.generator.constant;

import lombok.Getter;

/**
 * @author yangyun
 * @Description:
 * @date 2020/5/27 10:09
 */
@Getter
public enum AnnotationEnum {

    DATA("@Data","lombok.Data"),
    APIMODELPROPERTY("@ApiModelProperty","io.swagger.annotations.ApiModelProperty"),
    NOTNULL("@NotNull","javax.validation.constraints.NotNull"),
    MIN("@Min","javax.validation.constraints.Min"),
    MAX("@Max","javax.validation.constraints.Max"),
    SIZE("@Size","javax.validation.constraints.Max"),
    LENGTH("@Length", "org.hibernate.validator.constraints.Length"),
    RESTCONTROLLER("@RestController", "org.springframework.web.bind.annotation.RestController"),
    API("@Api", "io.swagger.annotations.Api"),
    REQUESTMAPPING("@RequestMapping", "org.springframework.web.bind.annotation.RequestMapping"),
    ;


    private String annotationName;
    private String annotationReference;

    AnnotationEnum(String annotationName, String annotationReference){
        this.annotationName = annotationName;
        this.annotationReference = annotationReference;
    }
}
