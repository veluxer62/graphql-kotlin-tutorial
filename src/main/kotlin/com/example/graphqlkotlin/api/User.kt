package com.example.graphqlkotlin.api

import com.example.graphqlkotlin.domain.Book
import com.example.graphqlkotlin.domain.User
import com.example.graphqlkotlin.domain.UserService
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.CompletableFuture

data class UserField(
    val id: ID,
    val name: String,
    private val age: Int,
) {
    fun age(): String {
        println("age 호출했습니다!")
        return "$age 세"
    }

    fun books(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<List<BookField>> {
        return dataFetchingEnvironment.getValueFromDataLoader("BooksDataLoader", id)
    }

    fun batchBooks(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<List<BookField>> {
        return dataFetchingEnvironment.getValueFromDataLoader("BooksBatchLoader", id)
    }
}

data class UserCreationInput(
    val name: String,
    val age: Int,
    val bookNames: List<String>
)

@Component
class UserQuery(private val userService: UserService) : Query {
    fun users(): List<UserField> = userService.getUsers()
        .map {
            UserField(
                id = ID(it.id.toString()),
                name = it.name,
                age = it.age,
            )
        }

    fun user(id: ID): UserField {
        val user = userService.getUser(UUID.fromString(id.value))
        return UserField(
            id = ID(user.id.toString()),
            name = user.name,
            age = user.age,
        )
    }
}

@Component
class UserMutation(private val userService: UserService) : Mutation {
    fun create(input: UserCreationInput): UserField {
        val user = User(name = input.name, age = input.age)
        val books = input.bookNames.map { name ->
            Book(title = name, authorId = user.id)
        }
        userService.save(user, books)
        return UserField(
            id = ID(user.id.toString()),
            name = user.name,
            age = user.age,
        )
    }
}
