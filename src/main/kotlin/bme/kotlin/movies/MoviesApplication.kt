package bme.kotlin.movies

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import javax.sql.DataSource

@SpringBootApplication
class MoviesApplication

fun main(args: Array<String>) {
	runApplication<MoviesApplication>(*args)
}

@EnableWebSecurity
class KotlinSecurityConfiguration(val dataSource: DataSource) : WebSecurityConfigurerAdapter() {

	override fun configure(http: HttpSecurity?) {

		http!!.csrf().disable()//for dev
		http.headers().frameOptions().disable()//for dev

		http.httpBasic().and()
				.authorizeRequests()
				.antMatchers("/h2-console/**").permitAll() //for dev
				.antMatchers("/api/*").authenticated()
				.antMatchers("/testView", "/felhasznalok").hasRole("USER")
				.anyRequest().authenticated()
				.and()
				.formLogin().permitAll() //.loginPage("/login")
				.and()
				.logout().permitAll();
	}

	override fun configure(auth: AuthenticationManagerBuilder?) {
		auth!!.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select login,password,enabled "
						+ "from User "
						+ "where login = ?")
				.authoritiesByUsernameQuery("SELECT user.login, authorities.authority "
						+ "from user "
						+ "join user_role on user.id = user_role.user_id "
						+ "join authorities on authorities.id = user_role.role_id "
						+ "where user.login = ?")
				.passwordEncoder(NoOpPasswordEncoder.getInstance())
	}
}


