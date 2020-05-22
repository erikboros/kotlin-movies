package bme.kotlin.movies.service

import bme.kotlin.movies.persistence.MovieOption
import bme.kotlin.movies.persistence.MovieRepository
import bme.kotlin.movies.persistence.User
import bme.kotlin.movies.persistence.UserRepository
import org.springframework.stereotype.Service

@Service
class MovieService(private val movieRepository: MovieRepository, private val userRepository: UserRepository) {

    fun attachVoter(movieId: Long, user: User) {
        val movie : MovieOption = movieRepository.findById(movieId).get()
        movie.voters.add(user)
        movieRepository.save(movie)
    }

    fun detachVoter(movieId: Long, user: User) {
        val movie : MovieOption = movieRepository.findById(movieId).get()
        movie.voters.remove(user)
        movieRepository.save(movie)
    }

}