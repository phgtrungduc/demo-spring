package vn.vpbanks.bo.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Class: OracleConfig
 * Description: Cau hinh HikariConnectionPool
 *
 * @author quyetnv
 * @date 10/21/2025
 */
@Configuration
public class OracleConfig {

    //db active
    @Bean(name = "activeDataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return new HikariDataSource();
    }

    //db standby
    @Bean(name = "standbyDataSource")
    @ConfigurationProperties("spring.datasource-standby")
    public DataSource dataSourceStandby() {
        return new HikariDataSource();
    }

    //db log
    @Bean(name = "loggerDataSource")
    @ConfigurationProperties("spring.datasource-logdb")
    public DataSource dataSourceLogger() {
        return new HikariDataSource();
    }
}
