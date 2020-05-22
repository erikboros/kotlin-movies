package bme.kotlin.movies.service

import bme.kotlin.movies.controller.UserListItemDTO
import bme.kotlin.movies.persistence.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService (private val userRepository: UserRepository) {

    fun getAllOtherUsersDTO(loginName: String) : List<UserListItemDTO> =
            userRepository.findAll()
                .filter { it.login != loginName }
                .map{ UserListItemDTO(it) }
                .toList()

}