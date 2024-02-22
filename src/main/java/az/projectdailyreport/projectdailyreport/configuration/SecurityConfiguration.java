package az.projectdailyreport.projectdailyreport.configuration;

import az.projectdailyreport.projectdailyreport.model.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CorsConfig corsConfig;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("api/v1/auth/**").permitAll()
                                .requestMatchers(permitSwagger).permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/auth/refresh-token").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD","EMPLOYEE")
                                .requestMatchers(HttpMethod.POST,"/users/forget-password-otp").permitAll()
                                .requestMatchers(HttpMethod.POST,"/users/forget-password-email").permitAll()
                                .requestMatchers(HttpMethod.POST,"/users/confirm-password").permitAll()
                                .requestMatchers(HttpMethod.GET,"/users/profile").permitAll()
                                .requestMatchers(HttpMethod.GET,"/users/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD")
                                .requestMatchers(HttpMethod.POST,"/users/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/users/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/users/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/teams/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD")
                                .requestMatchers(HttpMethod.POST,"/api/teams/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/teams/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/teams/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/project/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD","EMPLOYEE")
                                .requestMatchers(HttpMethod.POST,"/api/project/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/project/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/project/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/report/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD","EMPLOYEE")
                                .requestMatchers(HttpMethod.POST,"/api/report/{reportId}/").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD","EMPLOYEE")
                                .requestMatchers(HttpMethod.GET,"/api/report/user/reports").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.GET,"/api/report/admin/filtir").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD")
                                .requestMatchers(HttpMethod.GET,"/api/report/export-excel").hasAnyAuthority("SUPER_ADMIN", "ADMIN","HEAD")
                                .requestMatchers(HttpMethod.POST,"/api/report/**").hasAnyAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.PUT,"/api/report/**").hasAnyAuthority("EMPLOYEE")
                                .anyRequest().authenticated());
        httpSecurity.authenticationProvider(authenticationProvider);
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

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
