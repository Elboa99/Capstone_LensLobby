package kennyboateng.Capstone_LensLobby.security;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Config {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JWTCheckFilter jwtCheckFilter) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return httpSecurity.build();
//    }
//
//
//    @Bean
//    PasswordEncoder getBCrypt() {
//        return new BCryptPasswordEncoder(11);

    }
}
