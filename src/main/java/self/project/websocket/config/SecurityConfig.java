package self.project.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth // TODO() configure
                        .requestMatchers("/**")
                        .permitAll())
                .formLogin(login -> login // TODO() configure
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/auth/hello", true)
                        .failureUrl("/auth/login?error"))
                .httpBasic(Customizer.withDefaults()); // TODO() check if needed
        // .authenticationProvider() TODO() configure
        // .userDetailsService() TODO() configure
//                .logout(out -> out // TODO() configure
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/auth/login"));
        return http.build();
    }
}
