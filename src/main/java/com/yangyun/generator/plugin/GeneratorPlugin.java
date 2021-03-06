package com.yangyun.generator.plugin;

import com.yangyun.generator.constant.AnnotationEnum;
import com.yangyun.generator.constant.ConstantPool;
import com.yangyun.generator.constant.GroupClass;
import com.yangyun.generator.constant.MethodEnum;
import com.yangyun.generator.utils.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @author yangyun
 * @Description:
 * @date 2020/10/13 9:42
 */
public class GeneratorPlugin extends BaseGeneratorPlugin {

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        baseRecordType = introspectedTable.getBaseRecordType();
        modelName = baseRecordType.substring(baseRecordType.lastIndexOf(".") + 1);
        model = new FullyQualifiedJavaType(baseRecordType);

        materialServiceName = materialServiceServicePackage + ConstantPool.POINT + servicePreffix + modelName + serviceSuffix;
        materialServiceImplName = materialServiceServiceImplPackage + "." + modelName + serviceSuffix+"Impl";
        msdmServiceName = msdmServicePackage + ConstantPool.POINT + servicePreffix + modelName + serviceSuffix;
        msdmServiceImplName = msdmServiceImplPackage + "." + modelName + serviceSuffix+"Impl";

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

    /**
     * ????????????: 
     * @param introspectedTable??? ?????????
     * @param controllerName??? ??????
     * @param type?????????????????????
     * Return: org.mybatis.generator.api.GeneratedJavaFile
     * Author: yun.Yang
     * Date: 2020/10/13 16:34
     */
    private GeneratedJavaFile generateMaterialServiceController(IntrospectedTable introspectedTable, String controllerName, Boolean type) {
        FullyQualifiedJavaType controller = new FullyQualifiedJavaType(controllerName);
        TopLevelClass clazz = new TopLevelClass(controller);
        // ??????????????? public
        clazz.setVisibility(JavaVisibility.PUBLIC);

        // ??????@RestController???@Api???@RequestMapping ??????
        addAnnotation(clazz, introspectedTable.getRemarks(), type);

        // ?????? save ??????
        addMethod(clazz, MethodEnum.SAVE);
        addMethod(clazz, MethodEnum.DELETE);
        addMethod(clazz, MethodEnum.UPDATE);
        addMethod(clazz, MethodEnum.PAGE);
        addMethod(clazz, MethodEnum.DETAIL);

        GeneratedJavaFile gjf2 = new GeneratedJavaFile(clazz, targetProject, context.getJavaFormatter());
        return gjf2;
    }

    /**
     * ????????????: ??????@RestController???@Api???@RequestMapping ??????
     * @param clazz???
     *  @param tableRemark???
     * Return: void
     * Author: yun.Yang
     * Date: 2020/10/13 16:21
     */
    private void addAnnotation(TopLevelClass clazz, String tableRemark, Boolean type){
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

        //??????controller????????????model??????????????????
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
    }

    private void addMethod (TopLevelClass clazz, MethodEnum methodEnum){
        //?????? save ?????????
        Method method = new Method(methodEnum.getEnName());

        // @ApiOperation
        StringBuilder sb = new StringBuilder(AnnotationEnum.APIOPERATION.getAnnotationName());
        sb.append(ConstantPool.LEFT_BRACKET).append(ConstantPool.VALUE).append(ConstantPool.EQUALS_SYMBOL).append(ConstantPool.QUOTATION)
                .append(methodEnum.getCnName()).append(ConstantPool.QUOTATION).append(ConstantPool.SYMBOL).append(ConstantPool.NOTES).append(ConstantPool.EQUALS_SYMBOL)
                .append(ConstantPool.QUOTATION).append(methodEnum.getCnName()).append(ConstantPool.QUOTATION).append(ConstantPool.RIGHT_BRACKET);
        method.addAnnotation(sb.toString());
        clazz.addImportedType(AnnotationEnum.APIOPERATION.getAnnotationReference());

        //????????????
        addMapping(method, clazz, methodEnum);

        setReturnType(method, clazz, methodEnum);

        //????????????????????????
        method.addBodyLine("return success(null);");
        //?????????
        method.setVisibility(JavaVisibility.PUBLIC);
        clazz.addMethod(method);
    }

    /**
     * ????????????: ??????????????????
     * @param method???????????????
     * @param clazz??? ??????
     * @param methodEnum??? 
     * Return: void
     * Author: yun.Yang
     * Date: 2020/10/14 9:57
     */
    private void addMapping(Method method, TopLevelClass clazz, MethodEnum methodEnum){
        AnnotationEnum annotationEnum = "detail".equals(methodEnum.getEnName()) || "page".equals(methodEnum.getEnName()) ? AnnotationEnum.GETMAPPING : AnnotationEnum.POSTMAPPING;
        StringBuilder sb = new StringBuilder(annotationEnum.getAnnotationName());
        clazz.addImportedType(annotationEnum.getAnnotationReference());
        sb.append(ConstantPool.LEFT_BRACKET).append(ConstantPool.QUOTATION).append(ConstantPool.UPRIGHT_SLASH).append(methodEnum.getEnName()).append(ConstantPool.QUOTATION).append(ConstantPool.RIGHT_BRACKET);
        method.addAnnotation(sb.toString());
    }

    /**
     * ????????????: ??????????????????
     * @param method???
     * @param clazz???
     * @param methodEnum???
     * Return: void
     * Author: yun.Yang
     * Date: 2020/10/14 10:49
     */
    private void setReturnType (Method method, TopLevelClass clazz, MethodEnum methodEnum){

        StringBuilder sb = new StringBuilder(GroupClass.MATERIAL_RESULT.getClassName());
        sb.append("<");
        switch (methodEnum.getEnName()){
            case "page":
                sb.append(GroupClass.PAGE.getClassName());
                sb.append("<?????????>");
                clazz.addImportedType(GroupClass.PAGE.getClassReference());
                break;
            case "detail":
                sb.append("?????????");
                break;
            default:
                sb.append(GroupClass.BOOLEAN.getClassName());
                break;
        }
        sb.append(">");
        FullyQualifiedJavaType methodReturnType = new FullyQualifiedJavaType(sb.toString());
        clazz.addImportedType(GroupClass.MATERIAL_RESULT.getClassReference());
        method.setReturnType(methodReturnType);
    }
}
