package com.yangyun.generator.plugin;

import com.yangyun.generator.constant.AnnotationEnum;
import com.yangyun.generator.constant.ConstantPool;
import com.yangyun.generator.utils.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @author yangyun
 * @Description:
 * @date 2020/10/13 9:42
 */
public class ControllerGeneratorPlugin extends BaseGeneratorPlugin {

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        baseRecordType = introspectedTable.getBaseRecordType();
        modelName = baseRecordType.substring(baseRecordType.lastIndexOf(".") + 1);
        model = new FullyQualifiedJavaType(baseRecordType);
        serviceName = targetServicePackage + ConstantPool.POINT + servicePreffix + modelName + serviceSuffix;
        serviceImplName = targetServiceImplPackage + "." + modelName + serviceSuffix+"Impl";
        baseRecordType.substring(0,baseRecordType.lastIndexOf("."));
        msdmControllerName=msdmControllerPackage.concat(".").concat(modelName).concat("Controller");
        materialServiceControllerName=materialServiceControllerPackage.concat(".").concat(modelName).concat("Controller");
        List<GeneratedJavaFile> answer = new ArrayList<>();

        GeneratedJavaFile materialController = generateMaterialServiceController(introspectedTable, materialServiceControllerName, !ConstantPool.SUPER_INTEGERFACE);
        GeneratedJavaFile msdmController = generateMaterialServiceController(introspectedTable, msdmControllerName, ConstantPool.SUPER_INTEGERFACE);
        answer.add(materialController);
        answer.add(msdmController);
        return answer;
    }

    private GeneratedJavaFile generateMaterialServiceController(IntrospectedTable introspectedTable, String controllerName, Boolean type) {
        FullyQualifiedJavaType controller = new FullyQualifiedJavaType(controllerName);
        TopLevelClass clazz = new TopLevelClass(controller);
        // 类的描述符 public
        clazz.setVisibility(JavaVisibility.PUBLIC);

        addAnnotation(clazz, introspectedTable.getRemarks(), type);

        GeneratedJavaFile gjf2 = new GeneratedJavaFile(clazz, targetProject, context.getJavaFormatter());
        return gjf2;
    }

    private void addAnnotation(TopLevelClass clazz, String tableRemark, Boolean type){
        // 添加@RestController、@Api、@RequestMapping 注解
        clazz.addImportedType(new FullyQualifiedJavaType(AnnotationEnum.API.getAnnotationReference()));
        StringBuilder sb = new StringBuilder(AnnotationEnum.API.getAnnotationName());
        sb.append(ConstantPool.LEFT_BRACKET);
        sb.append(ConstantPool.TAG).append(ConstantPool.EQUALS_SYMBOL).append(ConstantPool.QUOTATION)
                .append(tableRemark).append(ConstantPool.QUOTATION);
        sb.append(ConstantPool.RIGHT_BRACKET);
        clazz.addAnnotation(sb.toString());

        clazz.addImportedType(new FullyQualifiedJavaType(AnnotationEnum.RESTCONTROLLER.getAnnotationReference()));
        clazz.addAnnotation(AnnotationEnum.RESTCONTROLLER.getAnnotationName());

        clazz.addImportedType(new FullyQualifiedJavaType(AnnotationEnum.REQUESTMAPPING.getAnnotationReference()));
        sb = new StringBuilder(AnnotationEnum.REQUESTMAPPING.getAnnotationName());
        sb.append(ConstantPool.LEFT_BRACKET);
        sb.append(ConstantPool.VALUE).append(ConstantPool.EQUALS_SYMBOL).append(ConstantPool.QUOTATION)
                .append(ConstantPool.UPRIGHT_SLASH).append(StringUtils.captureName(modelName)).append(ConstantPool.QUOTATION);
        sb.append(ConstantPool.RIGHT_BRACKET);
        clazz.addAnnotation(sb.toString());

        //引入controller的父类和model，并添加泛型
        if(stringHasValue(superControllerClass)) {
            clazz.addImportedType(superControllerClass);
            clazz.addImportedType(baseRecordType);
            FullyQualifiedJavaType superClass = new FullyQualifiedJavaType(superControllerClass);
            clazz.setSuperClass(superClass);

            if (type){
                FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(modelName + "FeignClient");
                clazz.addSuperInterface(superInterface);
            }
        }

        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceName);

    }
}
