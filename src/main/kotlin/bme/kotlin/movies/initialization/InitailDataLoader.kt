package bme.kotlin.movies.initialization

import bme.kotlin.movies.persistence.*
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
class BlogConfiguration {

    @Bean
    fun databaseInitializer(userRepository: UserRepository,
                            roleRepository: RoleRepository,
                            eventRepository: EventRepository,
                            movieRepository: MovieRepository
    ) = ApplicationRunner {

        val allRoles = roleRepository.findAll();
        if (allRoles.filter { it.authority == "ROLE_USER" }.count() > 0) {
            return@ApplicationRunner
        }

        val userRole = roleRepository.save(Role("ROLE_USER"))
        val b = userRepository.save(User("b", "Ben", "Dover", "b", listOf(userRole)))
        val d = userRepository.save(User("d", "Dixie", "Normus", "d", listOf(userRole)))
        val j = userRepository.save(User("j", "Jenny", "Talia", "j", listOf(userRole)))
        val k = userRepository.save(User("k", "Kriss", "P. Bacon", "k", listOf(userRole)))
        val l = userRepository.save(User("l", "Liz", "Bien", "l", listOf(userRole)))
        val m = userRepository.save(User("m", "Mike", "Ok", "m", listOf(userRole)))

        val event = eventRepository.save(Event(
                title = "",
                date = LocalDate.now().atStartOfDay().plusDays(5),
                organiser = b,
                participants = mutableListOf(d, j, k, l, m)
        ))

        movieRepository.save(MovieOption(
                title = "Pirates of the Caribbean: The Curse of the Black Pearl",
                event = event,
                voters = mutableSetOf(b, d, j, k, l)
        ))
        movieRepository.save(MovieOption(
                title = "The Lord of the Rings: The Fellowship of the Ring",
                event = event,
                voters = mutableSetOf(b, d, j, k, l, m)
        ))
        movieRepository.save(MovieOption(
                title = "Star Wars: A New Hope",
                event = event,
                voters = mutableSetOf(m, d, j, k)
        ))
        movieRepository.save(MovieOption(
                title = "The Martian",
                event = event,
                voters = mutableSetOf(b, d, j, m)
        ))

        eventRepository.save(Event(
                title = "",
                date = LocalDate.now().atStartOfDay().plusDays(6),
                organiser = b,
                participants = mutableListOf(j, l)
        ))
        eventRepository.save(Event(
                title = "",
                date = LocalDate.now().atStartOfDay().plusDays(6),
                organiser = k,
                participants = mutableListOf(b, l)
        ))

    }
}