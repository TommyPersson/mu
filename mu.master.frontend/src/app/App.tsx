
import * as React from "react"
import { Redirect, Switch } from "react-router"

import { routes as LoginRoutes, root as LoginRoot } from "./ui/authentication/routes"
import { routes as DashboardRoutes, root as DashboardRoot } from "./ui/dashboard/routes"

export interface StateProps {
    isLoggedIn: boolean
    isLoaded: boolean
}

export interface CallbackProps {
    loadStoredAuthToken: () => void
    logOut: () => void
}

export type Props = StateProps & CallbackProps

export default class App extends React.Component<Props, {}> {

    componentDidMount() {
        if (!this.props.isLoaded) {
            this.props.loadStoredAuthToken()
        }
    }

    componentDidUpdate() {
        if (!this.props.isLoaded) {
            this.props.loadStoredAuthToken()
        }
    }

    render() {
        if (!this.props.isLoaded) {
            return (<p>Loading ...</p>)
        }

        return this.props.isLoggedIn ? (
            <Switch>
                {DashboardRoutes}
                <Redirect to={DashboardRoot} push />
            </Switch>
        ) : (
            <Switch>
                {LoginRoutes}
                <Redirect to={LoginRoot} push />
            </Switch>
        )
    }
}