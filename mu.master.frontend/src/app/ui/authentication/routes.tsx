import * as React from "react"
import { Route } from "react-router"
import LoginPageContainer from "./containers/LoginPageContainer"

export const root = "/login"

export const routes = (
    <Route exact path={root} component={LoginPageContainer} />
)
