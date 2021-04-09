package com.yangyun.generator;

import com.yangyun.generator.generators.MyMyBatisGenerator;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.BeanUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangyun
 * @Description: https://blog.csdn.net/qq_28898309/article/details/106812466?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase
 * @date 2020/5/26 18:16
 */
public class Main {

    private static String[] TABLES = {"material_hot_paper_recover"};

    private static String C = "_";

    public static void main(String[] args) throws  Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("mybatis-generator.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(is);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        scanTables(config);
        if(config.getContexts().get(0).getTableConfigurations().size() < 1){
            throw new RuntimeException("==============么得表信息================");
        }
        MyMyBatisGenerator myBatisGenerator = new MyMyBatisGenerator(config, callback, warnings, true, true);
        myBatisGenerator.generate(null);
    }

    public static void scanTables(Configuration config){
        List<Context> contexts = config.getContexts();

        TableConfiguration tableConfiguration = config.getContexts().get(0).getTableConfigurations().get(0);
        TableConfiguration tc = null;
        for (String s : TABLES){
            if (StringUtils.isBlank(s)){
                continue;
            }
            tc = new TableConfiguration(contexts.get(0));
            BeanUtils.copyProperties(tableConfiguration, tc);
            tc.setTableName(s);
            tc.setDomainObjectName(resolverDomainName(s));
            config.getContexts().get(0).getTableConfigurations().add(tc);
        }
        List<TableConfiguration> tableConfigurations = contexts.get(0).getTableConfigurations();
        // 因为为了不改动配置文件，所以移除里面配置的表信息，只使用代码中配置的需要生成实体类的表
        // 所以，表个数小于 1，将报错
        tableConfigurations.remove(0);
    }

    public static String resolverDomainName(String s){
        StringBuffer sb = new StringBuffer();
        for (String ss : s.split(C)){
            sb.append(com.yangyun.generator.utils.StringUtils.upperCase(ss));
        }
        return sb.toString();
    }
}
