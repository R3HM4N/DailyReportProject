package az.projectdailyreport.projectdailyreport.configuration;

import az.projectdailyreport.projectdailyreport.model.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("api/v1/auth/**").permitAll()

                                .requestMatchers(permitSwagger).permitAll()
                                .requestMatchers("/users/**").permitAll()
                                .requestMatchers("/api/teams/**").permitAll()
                                .requestMatchers("/api/project/**").permitAll()
                                .requestMatchers("/api/report/**").permitAll()
//                                .requestMatchers(HttpMethod.GET,"/users/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD")
//                                .requestMatchers(HttpMethod.POST,"/users/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
//                                .requestMatchers(HttpMethod.DELETE,"/users/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
//                                .requestMatchers(HttpMethod.PUT,"/users/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
//
//                                .requestMatchers(HttpMethod.GET,"/api/teams/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD")
//                                .requestMatchers(HttpMethod.POST,"/api/teams/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
//                                .requestMatchers(HttpMethod.DELETE,"/api/teams/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
//                                .requestMatchers(HttpMethod.PUT,"/api/teams/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
//                                .requestMatchers(HttpMethod.GET,"/api/project/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD")
//                                .requestMatchers(HttpMethod.POST,"/api/project/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
//                                .requestMatchers(HttpMethod.DELETE,"/api/project/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
//                                .requestMatchers(HttpMethod.PUT,"/api/project/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
//                                .requestMatchers(HttpMethod.GET,"/api/report/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD","USER")
//                                .requestMatchers(HttpMethod.GET,"/api/report/user").hasAuthority("USER")
//                                .requestMatchers(HttpMethod.POST,"/api/report/**").hasAnyAuthority("USER")
//                                .requestMatchers(HttpMethod.PUT,"/api/report/**").hasAnyAuthority("USER")
                                .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//
    public static String[] permitSwagger = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
}
