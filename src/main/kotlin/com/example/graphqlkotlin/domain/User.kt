package com.example.graphqlkotlin.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@Entity
class User(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column
    val name: String,
    @Column
    val age: Int,
    @OneToMany
    @JoinColumn(name = "userId")
    val books: MutableList<Book> = mutableListOf(),
)

interface UserRepository : JpaRepository<User, UUID>

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository,
) {
    fun save(user: User, books: List<Book>) {
        userRepository.save(user)

        if (user.name == "테스트") {
            throw IllegalArgumentException()
        }

        bookRepository.saveAll(books)
    }
    fun getUsers(): List<User> = userRepository.findAll()
    fun getUser(id: UUID): User = userRepository.findById(id).orElseThrow()
}
