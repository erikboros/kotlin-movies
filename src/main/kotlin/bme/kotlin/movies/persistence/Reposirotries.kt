package bme.kotlin.movies.persistence

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByLogin(login: String): User?
}

interface RoleRepository : CrudRepository<Role, Long>

interface MovieRepository : CrudRepository<MovieOption, Long>{
    fun findAllByEvent_Id(event_id: Long) : List<MovieOption>
}

interface EventRepository : CrudRepository<Event, Long> {

    @Query("select distinct EVENT.ID ID, TITLE, ORGANISER_ID, DATE " +
            "FROM EVENT " +
            "JOIN PARTICIPANT_EVENT PE on EVENT.ID = PE.EVENT_ID " +
            "join USER on EVENT.ORGANISER_ID = USER.ID OR PE.USER_ID=USER_ID " +
            "where USER.LOGIN = :userLogin",
            nativeQuery = true)
    fun getAllByUserLogin(userLogin: String): List<Event>

    @Query("select EVENT.ID ID, TITLE, ORGANISER_ID, DATE " +
            "FROM EVENT " +
            "join USER on EVENT.ORGANISER_ID = USER.ID " +
            "where USER.LOGIN = :userLogin",
            nativeQuery = true)
    fun getAllByOrgLogin(userLogin: String): List<Event>

    @Query("select EVENT.ID ID, TITLE, ORGANISER_ID, DATE " +
            "FROM EVENT " +
            "JOIN PARTICIPANT_EVENT PE on EVENT.ID = PE.EVENT_ID " +
            "join USER on PE.USER_ID = USER.ID " +
            "where USER.LOGIN = :userLogin",
            nativeQuery = true)
    fun getAllByPartLogin(userLogin: String): List<Event>
}