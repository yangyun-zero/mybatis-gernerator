package com.yangyun.generator.plugin;

import com.yangyun.generator.constant.AnnotationEnum;
import com.yangyun.generator.constant.GroupClass;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * @author yangyun
 * @Description:
 * @date 2020/5/27 10:20
 */
public class IngoreSetterAndGetterPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //添加domain的import
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.NoArgsConstructor");
        topLevelClass.addImportedType("lombok.AllArgsConstructor");
        topLevelClass.addImportedType("lombok.experimental.Accessors");
        topLevelClass.addImportedType("javax.validation.constraints.Size");
        topLevelClass.addImportedType(AnnotationEnum.LENGTH.getAnnotationReference());
//        topLevelClass.addImportedType("java.time.LocalDateTime");
        topLevelClass.addImportedType(GroupClass.INSERT.getClassReference());
        topLevelClass.addImportedType(GroupClass.UPDATE.getClassReference());

        //添加domain的注解
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");
        topLevelClass.addAnnotation("@Accessors(chain = true)");
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        appendApiModelProperty(field, introspectedColumn);
        return true;
    }

    private void appendApiModelProperty(Field field, IntrospectedColumn introspectedColumn){
        StringBuffer sb = new StringBuffer(AnnotationEnum.APIMODELPROPERTY.getAnnotationName());
        sb.append("(name = ").append("\"").append(field.getName()).append("\", value = \"")
                .append(introspectedColumn.getRemarks()).append("\")");
        field.addAnnotation(sb.toString());
    }
}
