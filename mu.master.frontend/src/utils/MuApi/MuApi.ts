
import Team from "../../app/domain/teams/models/Team"

export interface IMuApi {
    createAuthToken(email: string, password: string): Promise<string>
    getTeamsData(): Promise<Team[]>
}

export class MuApi implements IMuApi {

    private masterBaseUrl = "http://localhost:8080" // TODO get from config
    private apiBaseUrl = `${this.masterBaseUrl}/api`

    async createAuthToken(email: string, password: string): Promise<string> {
        const jsonData = await this.call<any>("auth.token.create",{ email, password })

        return jsonData.jwt
    }

    async getTeamsData(): Promise<Team[]> {
        interface TeamQueryResult {
            teams: Team[]
        }

        const teamsQuery = `{
            teams {
                id
                name
                members {
                    id
                    displayName
                    email
                }
            }
        }`

        const response = await this.doGraphQlQuery<TeamQueryResult>(teamsQuery)

        return response.data!.teams
    }

    private async doGraphQlQuery<T>(query: string): Promise<GraphQLResponse<T>> {
        const authToken = localStorage.getItem("mu/authentication/authToken")

        const url = `${this.masterBaseUrl}/graphql`
        const response = await fetch(new Request(url, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${authToken}`,
                "Content-Type": "application/graphql"
            },
            body: query
        }))

        if (response.status !== 200) {
            throw { statusCode: response.status, statusText: response.statusText }
        } else {
            return await response.json()
        }
    }

    private async call<T>(action: string, obj: any): Promise<T> {
        const authToken = localStorage.getItem("mu/authentication/authToken")

        const authHeaders = authToken ? {
            "Authorization": `Bearer ${authToken}`
        } : {}

        const url = `${this.apiBaseUrl}/${action}`
        const response = await fetch(new Request(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                ...authHeaders
            },
            body: JSON.stringify(obj)
        }))

        if (response.status !== 200) {
            throw { statusCode: response.status, statusText: response.statusText }
        } else {
            return await response.json()
        }
    }

}

interface GraphQLResponse<T> {
    data: T | null
    errors?: any[] | null
}