package com.yangyun.generator.plugin;

import com.yangyun.generator.plugin.BaseGeneratorPlugin;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;

import java.util.List;

/**
 * @author yangyun
 * @Description:
 * @date 2020/10/13 11:43
 */
public class ServiceGeneratorPlugin extends BaseGeneratorPlugin {

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        return null;
    }
}
