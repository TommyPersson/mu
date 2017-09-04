import { Dispatch } from "react-redux"
import { MuApi } from "utils/MuApi/MuApi"
import Team from "./models/Team"

export const loadTeamsData = () =>
    async (dispatch: Dispatch<any>) => {
        dispatch(LOAD_TEAMS__STARTED())

        const api = new MuApi()
        try {
            const token = await api.getTeamsData()
            dispatch(LOAD_TEAMS__SUCCESSFUL(token))
        } catch (error) {
            dispatch(LOAD_TEAMS__FAILED())
        }
    }

export const LOAD_TEAMS__STARTED = () => ({ type: "LOAD_TEAMS__STARTED" })
export const LOAD_TEAMS__SUCCESSFUL = (teams: Team[]) => ({ type: "LOAD_TEAMS__SUCCESSFUL", teams: teams })
export const LOAD_TEAMS__FAILED = () => ({ type: "LOAD_TEAMS__FAILED" })

