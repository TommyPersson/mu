
import * as Domain from "./domain"
import { combineReducers } from "redux"

export interface AppState {
    domain: Domain.State
}

export const appReducer = combineReducers({
    domain: Domain.Reducer
})