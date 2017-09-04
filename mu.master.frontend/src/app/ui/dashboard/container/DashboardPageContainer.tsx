

import { connect, Dispatch } from "react-redux"
import DashboardPage, { CallbackProps, StateProps } from "../components/DashboardPage"
import { AppState } from "app"
import { Actions as TeamActions } from "app/domain/teams"
import { Actions as AuthActions } from "app/domain/authentication"

function mapStateToProps(state: AppState): StateProps {
    const teamState = state.domain.teams

    return {
        teams: teamState.teams,
        isLoadingTeams: teamState.isLoadingTeams
    }
}

function mapDispatchToProps(dispatch: Dispatch<any>): CallbackProps {
    return {
        loadTeamsData() {
            dispatch(TeamActions.loadTeamsData())
        },

        logOut() {
            dispatch(AuthActions.logout())
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(DashboardPage)