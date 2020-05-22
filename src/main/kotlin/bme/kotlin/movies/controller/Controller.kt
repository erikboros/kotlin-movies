package bme.kotlin.movies.controller

import bme.kotlin.movies.persistence.EventRepository
import bme.kotlin.movies.persistence.User
import bme.kotlin.movies.persistence.UserRepository
import bme.kotlin.movies.service.EventService
import bme.kotlin.movies.service.MovieService
import bme.kotlin.movies.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Controller
class Controller(private val userRepository: UserRepository,
                 private val userService: UserService,
                 private val eventService: EventService,
                 private val eventRepository: EventRepository) {

    @GetMapping("/new-event")
    fun newEvent(model: Model): String {
        model["title"] = "New event"
        model["users"] = userRepository.findAll()
        return "new-event"
    }

    @GetMapping("/event/{eventId}")
    fun event(@PathVariable eventId: Long, model: Model, principal: Principal): String {
        model["event"] = eventRepository.findById(eventId).get()
        model["userId"] = userRepository.findByLogin(principal.name)?.id
                ?: throw IllegalStateException("User not found.")
        return "event"
    }

    @GetMapping("/")
    fun eventList(model: Model, principal: Principal): String {
        model["myEvents"] = eventService.getEventsByOrganiser(principal.name)
        model["otherEvents"] = eventService.getEventsByParticipant(principal.name)
        return "event-list"
    }
}

@RestController
class RestController(private val userService: UserService,
                     private val userRepository: UserRepository,
                     private val movieService: MovieService,
                     private val eventService: EventService) {

    @GetMapping("/users")
    fun getUsers(principal: Principal): List<UserListItemDTO> = userService.getAllOtherUsersDTO(principal.name)

    @PostMapping("/new-event")
    fun createNewEvent(@RequestBody event: NewEventDTO, principal: Principal): ResponseEntity<String> {
        val createdEvent = eventService.createNewEvent(event, principal.name)
        return ResponseEntity("{\"id\":%d}".format(createdEvent.id), HttpStatus.OK) //redirectview
    }

    @PostMapping("/new-movie/{eventId}")
    fun createNewMovieOption(@PathVariable eventId: Long, @RequestBody movie: NewMovieDTO, principal: Principal): ResponseEntity<String> {
        eventService.addNewMovie(eventId, movie, principal.name)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/event/{eventId}/movies")
    fun getMoviesForEvent(@PathVariable eventId: Long, principal: Principal): List<MovieListItemDTO> {
        val user = userRepository.findByLogin(principal.name) ?: throw IllegalStateException("User not found.")
        return fetchMoviesForEvent(user, eventId)
    }

    @GetMapping("/event/{eventId}/vote-movie/{movieId}")
    fun attachVoter(@PathVariable eventId: Long, @PathVariable movieId: Long, principal: Principal): List<MovieListItemDTO> {
        val user = userRepository.findByLogin(principal.name) ?: throw IllegalStateException("User not found.")
        movieService.attachVoter(movieId, user)
        return fetchMoviesForEvent(user, eventId)
    }

    @GetMapping("/event/{eventId}/detach-movie/{movieId}")
    fun detachVoter(@PathVariable eventId: Long, @PathVariable movieId: Long, principal: Principal): List<MovieListItemDTO> {
        val user = userRepository.findByLogin(principal.name) ?: throw IllegalStateException("User not found.")
        movieService.detachVoter(movieId, user)
        return fetchMoviesForEvent(user, eventId)
    }

    private fun fetchMoviesForEvent(user: User, eventId: Long): List<MovieListItemDTO> {
        return eventService.getMoviesDTO(eventId).onEach {
            if (it.voters.map { user -> user.id }.contains(user.id)) {
                it.iVoted = true
            }
        }
    }

}
