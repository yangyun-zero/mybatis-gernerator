package com.yangyun.generator.plugin;

import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @author yangyun
 * @Description:
 * @date 2020/10/13 11:44
 */
public abstract class BaseGeneratorPlugin extends PluginAdapter {

    protected String baseRecordType;
    protected String targetProject;
    protected String msdmControllerPackage;
    protected String materialServiceControllerPackage;
    protected String targetServiceImplPackage;
    protected String targetServicePackage;
    protected String serviceSuffix;
    protected String servicePreffix;
    protected String modelName;
    protected String serviceName;
    protected String serviceImplName;
    protected String superControllerClass;
    protected String superServiceImpl;
    protected String superServiceInterface;
    protected String msdmControllerName;
    protected String materialServiceControllerName;
    protected String controllerRequestValue;
    protected FullyQualifiedJavaType model;

    @Override
    public boolean validate(List<String> warnings) {
        targetProject = properties.getProperty("targetProject");
        msdmControllerPackage = properties.getProperty("msdmControllerPackage");
        materialServiceControllerPackage = properties.getProperty("materialServiceControllerPackage");
        targetServiceImplPackage = properties.getProperty("targetServiceImplPackage");
        targetServicePackage = properties.getProperty("targetServicePackage");
        servicePreffix = properties.getProperty("servicePreffix");
        servicePreffix = stringHasValue(servicePreffix) ? servicePreffix : "";
        serviceSuffix = properties.getProperty("serviceSuffix");
        serviceSuffix = stringHasValue(serviceSuffix) ? serviceSuffix : "";
        serviceImplName = properties.getProperty("serviceImplName");
        superControllerClass = properties.getProperty("superControllerClass");
        superServiceImpl = properties.getProperty("superServiceImpl");
        superServiceInterface = properties.getProperty("superServiceInterface");

        return true;
    }
}
