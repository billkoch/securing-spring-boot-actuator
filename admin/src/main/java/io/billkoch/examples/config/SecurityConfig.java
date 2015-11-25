package io.billkoch.examples.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig  extends GlobalAuthenticationConfigurerAdapter{
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
    protected static class Foo extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .authorizeRequests()
                    .antMatchers("/api/applications").permitAll()
                    .antMatchers("/**").hasRole("support")
                    .and()
                .csrf()
                    .ignoringAntMatchers("/api/applications/**")
                    .and()
                .httpBasic();
            // @formatter:on
        }
    }
}
