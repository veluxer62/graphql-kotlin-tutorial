package com.example.graphqlkotlin.api

import com.example.graphqlkotlin.domain.BookService
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.CompletableFuture

data class BookField(
    val id: ID,
    val title: String,
)

@Component
class BooksDataLoader(private val bookService: BookService) : KotlinDataLoader<ID, List<BookField>> {
    override val dataLoaderName: String = "BooksDataLoader"

    override fun getDataLoader(): DataLoader<ID, List<BookField>> = DataLoaderFactory.newDataLoader { ids ->
        CompletableFuture.supplyAsync {
            ids.map { id ->
                bookService.getBooks(UUID.fromString(id.value))
                    .map { BookField(ID(it.id.toString()), it.title) }
            }
        }
    }
}

@Component
class BooksBatchLoader(private val bookService: BookService) : KotlinDataLoader<ID, List<BookField>> {
    override val dataLoaderName: String = "BooksBatchLoader"

    override fun getDataLoader(): DataLoader<ID, List<BookField>> = DataLoaderFactory.newDataLoader { ids ->
        val books = bookService.getBooks(ids.map { UUID.fromString(it.value) })

        CompletableFuture.supplyAsync {
            ids.map { id ->
                books.filter { it.authorId == UUID.fromString(id.value) }
                    .map { BookField(ID(it.id.toString()), it.title) }
            }
        }
    }
}
