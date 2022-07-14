package com.sorsix.album_collector.security

import com.sorsix.album_collector.security.jwt.AuthEntryPointJwt
import com.sorsix.album_collector.security.jwt.AuthTokenFilter
import com.sorsix.album_collector.security.jwt.JwtUtils
import com.sorsix.album_collector.security.service.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
class WebSecurityConfig(
    val userDetailsService: UserDetailsServiceImpl,
    val unauthorizedHandler: AuthEntryPointJwt
) : WebSecurityConfigurerAdapter() {
    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter {
        //parametrive se components nesho e ebano so autowired
        return AuthTokenFilter(JwtUtils(), userDetailsService)
    }

    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity?) {
        http?.cors()?.and()?.csrf()?.disable()?.exceptionHandling()?.authenticationEntryPoint(unauthorizedHandler)
            ?.and()
            ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()?.authorizeRequests()
            ?.anyRequest()?.permitAll()//delete row
//            ?.antMatchers("/api/auth/**")?.permitAll()?.anyRequest()?.authenticated()
//        http?.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}