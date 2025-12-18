package vn.vpbanks.bo.api.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import vn.vpbanks.bo.api.utils.StringUtils;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class: AuthenticationConfig
 * Description: Cau hinh authen, author
 *
 * @author quyetnv
 * @date 10/21/2025
 */
@Configuration
@ConfigurationProperties("auth-config")
@Getter
@Setter
public class AuthenticationConfig {
    //off: Khong check
    //back: Check jwt theo role
    //internal: check basicAuth theo authUsers duoc khai bao
    private String authMode;
    //URL se khong check auth (VD: health)
    private List<String> ignoreAuthList;
    //Danh sach user basic auth (Call service 2 service) (Key la userName)
    private Map<String, AuthUser> authUsers;
    //Tu user/pass cua basic auth, luu tren inmem thanh token de validate nhanh hon (Key la token, value la usename)
    private Map<String, String> basicAuthMaps = new HashMap<>();

    @PostConstruct
    private void init() {
        if (authUsers != null && authUsers.size() > 0) {
            authUsers.entrySet().forEach(u -> {
                var encoding = Base64.getEncoder().encodeToString((u.getKey() + ":" + u.getValue().getPass()).getBytes());
                var basicAuth = "Basic " + encoding;
                u.getValue().setBasicAuth(basicAuth);
                basicAuthMaps.put(basicAuth, u.getKey());
            });
        }
    }

    public AuthMode getAuthModeEnum(){
        if (StringUtils.isNullOrEmpty(authMode)) return null;
        if ("off".equals(authMode)) return AuthMode.OFF;
        if ("back".equals(authMode)) return AuthMode.BACK;
        if ("internal".equals(authMode)) return AuthMode.INTERNAL;
        return null;
    }

    public enum AuthMode {
        OFF, BACK, INTERNAL
    }

    @Getter
    @Setter
    public static class AuthUser {
        //pass doc tu config de generate ra BasicAuth luu vao inmem
        private String pass;
        //danh sach urls khong duoc phep truy cap (Se check danh sach bi chan truoc,  neu bi chan se bao 403)
        private List<String> disallowUrls;
        //danh sach urls duoc phep truy cap
        // - Neu danh sach nay null, tuc la duoc phep truy cap moi tai nguyen ngoai tru disallowUrls
        // - Neu danh sach nay co gia tri => Chi duoc phep truy cap trong cac urls nay
        private List<String> allowUrls;
        //basic auth duoc generate tu user/pass
        private String basicAuth;
    }
}
