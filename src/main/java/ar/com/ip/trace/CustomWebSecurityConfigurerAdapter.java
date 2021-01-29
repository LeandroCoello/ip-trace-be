package ar.com.ip.trace;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	
    	
    	httpSecurity.antMatcher("/**")
    	.cors()
    	.and()
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().csrf().disable();
    }
    

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/*/ping","/_ah/*","/bulk*");
    }

}
