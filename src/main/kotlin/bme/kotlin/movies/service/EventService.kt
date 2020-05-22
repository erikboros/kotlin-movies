package bme.kotlin.movies.service

import bme.kotlin.movies.controller.MovieListItemDTO
import bme.kotlin.movies.controller.NewEventDTO
import bme.kotlin.movies.controller.NewMovieDTO
import bme.kotlin.movies.controller.UserListItemDTO
import bme.kotlin.movies.persistence.*
import org.springframework.stereotype.Service
import java.lang.IllegalStateException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class EventService(private val userRepository: UserRepository,
                   private val eventRepository: EventRepository,
                   private val movieRepository: MovieRepository) {

    companion object {
        const val NEW_EVENT_DATE_FORMAT: String = "yyyy-MM-dd"
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(NEW_EVENT_DATE_FORMAT);
    }

    fun createNewEvent(newEvent: NewEventDTO, organiserLogin: String): Event {
        val org = userRepository.findByLogin(organiserLogin) ?: throw IllegalStateException("Cannot find user")
        val participantIds = newEvent.participants.map(UserListItemDTO::id).toList()
        val participants = userRepository.findAllById(participantIds).toMutableList()

        return eventRepository.save(Event(
                title = "",
                date = LocalDate.parse(newEvent.date, dateFormatter).atStartOfDay(),
                organiser = org,
                participants = participants
        ))
    }

    fun getEventsByUser(login: String): List<Event> = eventRepository.getAllByUserLogin(login)

    fun getEventsByOrganiser(login: String): List<Event> = eventRepository.getAllByOrgLogin(login)

    fun getEventsByParticipant(login: String): List<Event> = eventRepository.getAllByPartLogin(login)

    fun addNewMovie(eventId: Long, movie: NewMovieDTO, name: String): MovieOption {
        val user = userRepository.findByLogin(name) ?: throw IllegalStateException("Cannot find user")
        val event = eventRepository.findById(eventId).get()
        return movieRepository.save(MovieOption(movie.title, event, mutableSetOf(user)))
    }

    /**
     * Marks the most voted movie, if there is only one with the max amount of votes
     * */
    fun getMoviesDTO(eventId: Long): List<MovieListItemDTO> {
        val movieDTOs = movieRepository.findAllByEvent_Id(eventId).map { MovieListItemDTO(it) }
        if (movieDTOs.size < 2 ) {
            return movieDTOs
        } else {
            val maxMap = movieDTOs.groupBy { it.voters.size }.maxBy { it.key }
            if (maxMap?.key != 0 && maxMap?.value?.size == 1) {
                movieDTOs.find { it.voters.size == maxMap.key }?.apply { maxVotes = true }
            }
            return movieDTOs.sortedBy { it.title }
        }
    }

}
