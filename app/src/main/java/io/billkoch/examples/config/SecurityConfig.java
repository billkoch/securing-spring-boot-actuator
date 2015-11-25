package io.billkoch.examples.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends GlobalAuthenticationConfigurerAdapter{

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth
            .inMemoryAuthentication()
                .withUser("user").password("user").roles("user").and()
                .withUser("support").password("support").roles("user", "support");
        // @formatter:on
    }

    @Configuration
    @Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ManagementSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private ManagementServerProperties managementServerProperties;

        @Autowired
        private HealthEndpoint healthEndpoint;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .authorizeRequests()
                    .requestMatchers(request ->
                        managementServerProperties.getPort().equals(request.getLocalPort()) &&
                        (managementServerProperties.getContextPath() + "/" + healthEndpoint.getId()).equals(request.getRequestURI())
                    ).permitAll()
                    .antMatchers("/**").hasAnyRole("support")
                    .and()
                .httpBasic()
                .and()
                .csrf()
                    .ignoringAntMatchers("/jolokia/**");
            // @formatter:on
        }
    }

    @Configuration
    protected static class ApplicationSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private ServerProperties serverProperties;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .requestMatcher(request ->
                    serverProperties.getPort().equals(request.getLocalPort())
                ).authorizeRequests()
                    .anyRequest().hasRole("user")
                    .and()
                .formLogin();
            // @formatter:on
        }
    }
}
