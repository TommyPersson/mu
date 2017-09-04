
import { AnyAction } from "redux"
import Team from "./models/Team"

export interface State {
    isLoadingTeams: boolean
    teams: Team[]
}

const initialState: State = {
    isLoadingTeams: false,
    teams: []
}

export const reducer = (state = initialState, action: AnyAction): Partial<State> => {
    switch (action.type) {
        case "LOAD_TEAMS__STARTED":
            return {
                isLoadingTeams: true
            }
        case "LOAD_TEAMS__SUCCESSFUL":
            return {
                isLoadingTeams: false,
                teams: action.teams
            }
        case "LOAD_TEAMS__FAILED":
            return {
                isLoadingTeams: false,
                teams: []
            }
        default:
            return state
    }
}