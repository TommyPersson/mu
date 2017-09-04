
import * as React from "react"
import { Route } from "react-router"
import DashboardPageContainer from "./container/DashboardPageContainer"

export const root = "/dashboard"

export const routes = (
    <Route exact path={root} component={DashboardPageContainer} />
)