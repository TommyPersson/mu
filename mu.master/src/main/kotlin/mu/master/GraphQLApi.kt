package mu.master

import graphql.ExecutionResult
import graphql.GraphQL
import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema.newSchema
import graphql.schema.GraphQLTypeReference
import mu.master.teams_and_users.application.api.TeamDTO
import mu.master.teams_and_users.application.api.UserDTO
import mu.master.teams_and_users.domain.TeamId
import org.jetbrains.ktor.application.ApplicationCall
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.response.respond

class MyGraphQL {
    private val userType = newObject()
            .name("User")
            .field(newFieldDefinition()
                    .name("id")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("memberOf")
                    .type(GraphQLList(GraphQLTypeReference("Team")))
                    .dataFetcher {
                        val userId = it.getSource<UserDTO>().id
                        val result = DI.TeamsAndUsers.teamsView.teams.filter { it.memberIds.contains(userId) }
                        result
                    })
            .build()

    private val teamType = newObject()
            .name("Team")
            .field(newFieldDefinition()
                    .name("id")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("name")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("members")
                    .type(GraphQLList(userType))
                    .dataFetcher {
                        val source = it.getSource<TeamDTO>()
                        DI.TeamsAndUsers.teamsView.usersByTeam[TeamId(source.id)] ?: emptySet<UserDTO>()
                    })
            .build()


    private val rootQuery = newObject()
            .name("TeamsQuery")
            .field(newFieldDefinition()
                    .name("teams")
                    .type(GraphQLList(teamType))
                    .dataFetcher { DI.TeamsAndUsers.teamsView.teams })
            .field(newFieldDefinition()
                    .name("users")
                    .type(GraphQLList(userType))
                    .dataFetcher { DI.TeamsAndUsers.teamsView.usersByTeam.values.flatten().distinct() })
            .build()

    private val gqlSchema = newSchema()
            .query(rootQuery)
            .build()

    private val gql = GraphQL.newGraphQL(gqlSchema).build()

    fun execute(query: String): ExecutionResult = gql.execute(query)
}

private val graphQL = MyGraphQL()

suspend fun callGraphQLApi(query: String, call: ApplicationCall) {
    val result = graphQL.execute(query)
    if (result.errors.any()) {
        result.errors.forEach {
            println(it)
        }
        call.response.status(HttpStatusCode.InternalServerError)
        call.respond("")
        return
    }

    val resultData = result.getData<Any>()
    if (resultData == null) {
        call.respond(Any())
        return
    }

    call.respond(resultData)
}