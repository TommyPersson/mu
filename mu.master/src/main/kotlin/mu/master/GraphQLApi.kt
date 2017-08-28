package mu.master

import graphql.ExecutionResult
import graphql.GraphQL
import graphql.GraphQLError
import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema.newSchema
import graphql.schema.GraphQLTypeReference
import mu.master.identity.application.api.UserDTO
import mu.master.teams_and_users.application.api.TeamDTO
import mu.master.teams_and_users.application.api.UserDTO as TeamUserDTO
import mu.master.teams_and_users.domain.TeamId
import org.jetbrains.ktor.application.ApplicationCall
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.HttpMethod
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.request.contentType
import org.jetbrains.ktor.request.httpMethod
import org.jetbrains.ktor.request.receiveText
import org.jetbrains.ktor.response.respond

class MyGraphQL {
    private val userType = newObject()
            .name("User")
            .field(newFieldDefinition()
                    .name("id")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("displayName")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("email")
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
                    .name("admin")
                    .type(userType)
                    .dataFetcher {
                        val source = it.getSource<TeamDTO>()
                        val adminId = source.teamAdminId
                        val result = DI.Identity.identityView.userAccountsById[adminId]
                        result
                    })
            .field(newFieldDefinition()
                    .name("members")
                    .type(GraphQLList(userType))
                    .dataFetcher {
                        val source = it.getSource<TeamDTO>()
                        val userIds = DI.TeamsAndUsers.teamsView.teamsById[source.id]?.memberIds ?: emptySet()
                        val result = userIds.mapNotNull { DI.Identity.identityView.userAccountsById[it] }
                        result
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
                    .dataFetcher { DI.Identity.identityView.userAccountsById.values })
            .build()

    private val gqlSchema = newSchema()
            .query(rootQuery)
            .build()

    private val gql = GraphQL.newGraphQL(gqlSchema).build()

    fun execute(query: String): ExecutionResult = gql.execute(query)
}

private val graphQL = MyGraphQL()

data class GraphQLResponseDTO(
        val data: Any?,
        val errors: List<GraphQLError>
)

suspend fun callGraphQLApi(call: ApplicationCall) {
    if (call.request.httpMethod != HttpMethod.Post || call.request.contentType() != ContentType.parse("application/graphql")) {
        call.respond(HttpStatusCode.NotImplemented)
        return
    }

    val query = call.receiveText()
    val result = graphQL.execute(query)

    val response = GraphQLResponseDTO(result.getData(), result.errors)
    if (response.errors.any()) {
        call.response.status(HttpStatusCode.BadRequest)
    }

    call.respond(response)
}