package com.example.graphqlkotlin

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class GraphqlKotlinApplicationTests(
    @Autowired private val client: WebTestClient,
) {

    @Test
    fun testUserMutation() {
        val query = """
            mutation createUser(${"$"}input: UserCreationInput!) {
                create(input: ${"$"}input) { 
                    id
                    name
                    age 
                } 
            }
        """.trimIndent().replace("\n", "")

        val variables = """
            {
                "input": {
                    "name": "홍길동",
                    "age": 13,
                    "bookNames": [
                        "책1","책2","책3"
                    ]
                }
            }
        """.trimIndent()

        client
            .post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"query": "$query", "variables": $variables}""")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data.create.name").isEqualTo("홍길동")
            .jsonPath("$.data.create.age").isEqualTo("13 세")
    }

    @Test
    fun testUserQuery() {
        testUserMutation()

        val query = """
            query getUsers {
                users {
                    name
                    age
                    books {
                        title
                    }
                    batchBooks {
                        title
                    }
                }
            }
        """.trimIndent().replace("\n", "")

        client
            .post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"query": "$query"}""")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .json(
                """
                    {
                      "data": {
                        "users": [
                          {
                            "name": "홍길동",
                            "age": "13 세",
                            "books": [
                              { "title": "책1" },
                              { "title": "책2" },
                              { "title": "책3" }
                            ],
                            "batchBooks": [
                              { "title": "책1" },
                              { "title": "책2" },
                              { "title": "책3" }
                            ]
                          }
                        ]
                      }
                    }
                """.trimIndent()
            )
    }
}
