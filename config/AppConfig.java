package vn.vpbanks.bo.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Class: AppConfig
 * Description: App config, doc tu file application.yaml
 *
 * @author quyetnv
 * @date 10/21/2025
 */
@Configuration
@ConfigurationProperties
@Getter
@Setter
public class AppConfig {
    //Key: ignoreLogRequestResponseList => Cac URL se khong ghi log request va log response
    private List<String> ignoreLogRequestResponseList;
    //Key: ignoreLogResponseList => Cac URL se khong ghi log response
    private List<String> ignoreLogResponseList;
    //Key: allowCorsList => Cac URL allow cors
    private List<String> allowCorsList;
}
