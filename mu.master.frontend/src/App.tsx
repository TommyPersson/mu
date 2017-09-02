import * as React from "react"

import "./App.css"
import { Redirect, Route, Switch } from "react-router"
import * as jwt_decode from "jwt-decode"

const logo = require("./logo.svg")

interface State {
    jwtToken: string | null
    teams: Team[] | null
}

interface JwtTokenClaims {
    sub: string,
    email: string,
    displayName: string
    exp: number
}

interface Team {
    id: string,
    name: string,
    members: {
        id: string,
        displayName: string
        email: string
    }[]
}

class App extends React.Component<{}, State> {

    private emailInput: HTMLInputElement | null
    private passwordInput: HTMLInputElement | null

    constructor(props: any) {
        super(props)
        const jwtToken = localStorage.getItem("jwtToken")

        if (jwtToken != null) {
            const decoded = jwt_decode<JwtTokenClaims>(jwtToken)
            console.log(decoded)
        }

        this.state = {
            jwtToken,
            teams: null,
        }
    }

    componentWillMount() {
        const jwtToken = this.state.jwtToken

        if (!jwtToken) return
        if (this.state.teams) return

        // noinspection JSIgnoredPromiseFromCall
        this.featchTeamData(jwtToken)
    }

    componentWillUpdate(nextProps: {}, nextState: State) {
        const jwtToken = nextState.jwtToken

        if (!jwtToken) return
        if (this.state.teams) return

        // noinspection JSIgnoredPromiseFromCall
        this.featchTeamData(jwtToken)
    }

    render() {
        const isLoggedIn = this.state.jwtToken != null

        const teamsUi = this.state.teams ? (
            <div>
                {this.state.teams.map(it => (
                    <div key={it.id}>
                        <h2>{it.name}</h2>
                        {it.members.map(it => (
                            <p>{it.displayName} - {it.email}</p>
                        ))}
                    </div>
                ))}
            </div>
        ) : null

        const routes = isLoggedIn ? (
            <Switch>
                <Route exact path="/hello" render={() => (<h1>Hello, Router!</h1>)}/>
                <Route exact path="/" render={() => (
                    <div>
                        <h1>Main</h1>
                        {teamsUi}
                        <input type="button" value="Log out" onClick={this.onLogOutClicked}/>
                    </div>
                )}/>
                <Redirect to="/" push/>
            </Switch>
        ) : (
            <Switch>
                <Route exact path="/login" render={() => (
                    <div>
                        <p>Email</p>
                        <input type="text" ref={r => this.emailInput = r}/>
                        <p>Password</p>
                        <input type="password" ref={r => this.passwordInput = r}/>
                        <br/>
                        <input type="submit" value="Log in" onClick={this.onLogInClicked}/>
                    </div>
                )}/>
                <Redirect to="/login" push/>
            </Switch>
        )

        return (
            <div className="App">
                <div className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h2>Welcome to React</h2>
                </div>
                {routes}
            </div>
        )
    }

    onLogOutClicked = () => {
        localStorage.removeItem("jwtToken")
        this.setState({
            jwtToken: null,
            teams: null
        })
    }

    onLogInClicked = async () => {
        const email = this.emailInput!.value
        const password =  this.passwordInput!.value

        const api = new MuApi() as IMuApi
        try {
            const token = await api.createAuthToken(email, password)
            console.log(`token = ${token}`)
            localStorage.setItem("jwtToken", token)
            this.setState({
                jwtToken: token
            })
        } catch (error) {
            console.error(`status: ${error}`)
        }
    }

    private async featchTeamData(jwtToken: string) {

        const response = await fetch(new Request("http://localhost:8080/graphql", {
            method: "POST",
            headers: {
                "Content-Type": "application/graphql",
                "Authorization": `Bearer ${jwtToken}`
            },
            body: `
                { 
                    teams { 
                        id
                        name
                        members {
                            id
                            displayName
                            email
                        }
                    }
                }
            `
        }))

        interface Result {
            data: { teams: Team[] }
        }

        const json = await response.json() as Result

        this.setState({
            teams: json.data.teams
        })
    }
}

export default App

interface IMuApi {
    createAuthToken(email: string, password: string): Promise<string>
}

class MuApi implements IMuApi {

    async createAuthToken(email: string, password: string): Promise<string> {
        const jsonData = await MuApi.postJson<any>(
            "http://localhost:8080/api/auth.token.create",
            { email, password })

        return jsonData.jwt
    }

    private static async postJson<T>(url: string, obj: any): Promise<T> {
        const response = await fetch(new Request(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
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