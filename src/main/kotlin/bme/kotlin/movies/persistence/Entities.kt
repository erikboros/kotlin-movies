package bme.kotlin.movies.persistence

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
class Event(
        var title: String,
        var date: LocalDateTime,
        @ManyToOne @JoinColumn(name = "organiser_id") var organiser: User,
        @ManyToMany(fetch = FetchType.LAZY, targetEntity = User::class)
        @JoinTable(name = "participant_event",
                joinColumns = [JoinColumn(name = "event_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
        var participants: MutableList<User> = mutableListOf(),
        @OneToMany(mappedBy = "event") var options: MutableList<MovieOption> = mutableListOf(),
        @Id @GeneratedValue var id: Long? = null) {

    fun getDateString() = date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    fun getDateTimeString() = date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
}

@Entity(name = "Movie")
class MovieOption(
        var title: String,
        @ManyToOne @JoinColumn(name = "event_id") var event: Event,
        @ManyToMany(fetch = FetchType.LAZY, targetEntity = User::class)
        @JoinTable(name = "movie_voter",
                joinColumns = [JoinColumn(name = "movie_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
        var voters: MutableSet<User> = mutableSetOf(),
        @Id @GeneratedValue var id: Long? = null)

@Entity
class User(
        var login: String,
        var firstname: String,
        var lastname: String,
        var password: String,
        @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role::class)
        @JoinTable(name = "user_role",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
        var roles: List<Role>,
        @OneToMany(mappedBy = "organiser") var ownEvents: MutableList<Event> = mutableListOf(),
        @ManyToMany(mappedBy = "participants", targetEntity = Event::class) var events: MutableList<Event> = mutableListOf(),
        @ManyToMany(mappedBy = "voters", targetEntity = MovieOption::class) var votes: MutableList<MovieOption> = mutableListOf(),
        var enabled: Int = 1,
        @Id @GeneratedValue var id: Long? = null)

@Entity(name = "Authorities")
class Role(
        var authority: String,
        @ManyToMany(mappedBy = "roles", targetEntity = User::class) var users: List<User> = mutableListOf(),
        @Id @GeneratedValue var id: Long? = null)