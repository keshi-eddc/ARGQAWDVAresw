package com.edmi.configs;

import com.edmi.utils.ExcelUtilForPOI;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class StateCodeConfig {

    private Logger logger = LoggerFactory.getLogger(StateCodeConfig.class);

    private static Map<String,String> stateCode;

    @Autowired
    private ExcelUtilForPOI excelUtilForPOI;

    @PostConstruct
    private void initStateCodeConfig() {
        logger.info("<---正在加载 国家代码 到全局变量--->");
        PropertiesConfiguration config = null;
        String filePath = "";
        try {
            config = new PropertiesConfiguration("path.properties" );
            filePath = config.getString("feixiaohao.stateCode");
        } catch (ConfigurationException e) {
            logger.info(e.getMessage());
        }
        List<String[]> list = excelUtilForPOI.getData(filePath);
        if(list != null&&list.size() != 0){
            stateCode = new HashMap<String,String>();
            for (int i = 0; i < list.size(); i++) {
                String[] strObject = list.get(i);
                if(i==0){
                   continue;
                }
                stateCode.put(strObject[1],strObject[4]);
            }
        }
        logger.info("<---加载 国家代码 到全局变量完毕--->");
    }
    public static Map<String,String> getStateCode(){
        return stateCode;
    }
}
