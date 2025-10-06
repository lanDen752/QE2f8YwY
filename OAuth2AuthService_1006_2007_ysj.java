// 代码生成时间: 2025-10-06 20:07:56
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableAuthorizationServer
public class OAuth2AuthService {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2AuthService.class, args);
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        Map<String, ClientDetails> map = new HashMap<>();
        map.put("client-id", new ClientDetails(
                "client-id",
                "[SELECT SCOPE]", // Replace with actual scope
                "password", "authorization_code", "refresh_token", "client_credentials",
                Collections.emptySet()));
        return new InMemoryClientDetailsService(map);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                .antMatchers("/oauth/token").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(bearerTokenAuthenticationFilter(), BasicAuthenticationFilter.class);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("password")).roles("USER");
        }

        @Bean
        public BearerTokenExtractor bearerTokenExtractor() {
            return new BearerTokenExtractor();
        }

        @Bean
        public BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter() throws Exception {
            BearerTokenAuthenticationFilter filter = new BearerTokenAuthenticationFilter();
            filter.setAuthenticationManager(authenticationManager());
            filter.setBearerTokenExtractor(bearerTokenExtractor());
            return filter;
        }
    }

    // Configure Authorization Server
    @Bean
    public AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer() throws Exception {
        AuthorizationServerEndpointsConfigurer configurer = new AuthorizationServerEndpointsConfigurer();
        configurer
            .tokenStore(tokenStore())
            .authenticationManager(authenticationManager());
        return configurer;
    }

    // Configure Authorization Server Security
    @Bean
    public AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer() {
        AuthorizationServerSecurityConfigurer configurer = new AuthorizationServerSecurityConfigurer();
        configurer
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
        return configurer;
    }

    private AuthenticationManager authenticationManager() {
        try {
            return new SecurityConfig().authenticationManagerBean();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create authentication manager", e);
        }
    }
}

class ClientDetails {
    private String clientId;
    private String resourceIds;
    private String scope;
    private String[] grantTypes;
    private String[] authorities;
    private Set<String> redirectUris;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoApproveScopes;

    public ClientDetails(String clientId, String resourceIds, String scope, String[] grantTypes, String[] authorities, Set<String> redirectUris) {
        this.clientId = clientId;
        this.resourceIds = resourceIds;
        this.scope = scope;
        this.grantTypes = grantTypes;
        this.authorities = authorities;
        this.redirectUris = redirectUris;
    }

    // getters and setters
}