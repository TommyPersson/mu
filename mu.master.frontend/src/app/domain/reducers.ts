
import * as Authentication from "./authentication"
import * as Teams from "./teams"

import { combineReducers } from "redux"

export interface State {
    authentication: Authentication.State
    teams: Teams.State
}

export const Reducer = combineReducers({
    authentication: Authentication.Reducer,
    teams: Teams.Reducer
})