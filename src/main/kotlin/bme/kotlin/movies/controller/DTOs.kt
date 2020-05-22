package bme.kotlin.movies.controller

import bme.kotlin.movies.persistence.Event
import bme.kotlin.movies.persistence.MovieOption
import bme.kotlin.movies.persistence.User
import java.time.LocalDateTime


class UserListItemDTO(val firstname: String, val lastname: String, val id: Long?) {
    constructor (user: User) : this(lastname = user.lastname, firstname = user.firstname, id = user.id)
}

class EventListItemDTO(var title: String, var date: LocalDateTime, val id: Long?) {
    constructor (event: Event) : this(title = event.title, date = event.date, id = event.id)
}

class NewEventDTO(val date: String, val participants: List<UserListItemDTO>)

class NewMovieDTO(val title: String)

class MovieListItemDTO(val title: String, val voters: List<UserListItemDTO>, val id: Long?) {
    constructor (movie: MovieOption) : this(title = movie.title, voters = movie.voters.map { UserListItemDTO(it) }, id = movie.id)

    var iVoted: Boolean = false
    var maxVotes: Boolean = false
}

