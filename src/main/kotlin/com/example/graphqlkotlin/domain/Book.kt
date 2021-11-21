package com.example.graphqlkotlin.domain

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Entity
class Book(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column
    val title: String,
    @Column
    val authorId: UUID,
)

interface BookRepository : JpaRepository<Book, UUID> {
    fun findByAuthorIdIn(authorIds: List<UUID>): List<Book>
    fun findByAuthorId(authorId: UUID): List<Book>
}

@Service
@Transactional
class BookService(private val bookRepository: BookRepository) {
    fun getBooks(authorIds: List<UUID>): List<Book> = bookRepository.findByAuthorIdIn(authorIds)
    fun getBooks(authorId: UUID): List<Book> = bookRepository.findByAuthorId(authorId)
}
