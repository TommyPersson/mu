
import * as React from "react"
import * as L from "react-layout-components"

import Team from "app/domain/teams/models/Team"

export interface StateProps {
    teams: Team[]
    isLoadingTeams: boolean
}

export interface CallbackProps {
    loadTeamsData: () => void
    logOut: () => void
}

export type Props = StateProps & CallbackProps

export default class DashboardPage extends React.Component<Props, {}>{

    componentDidMount() {
        this.props.loadTeamsData()
    }

    render() {
        if (this.props.isLoadingTeams) {
            return (<p>Loading teams</p>)
        }

        return (
            <L.VBox>
                <h1>Dashboard</h1>

                {this.props.teams.map(it => (
                    <L.VBox key={it.id}>
                        <h2>{it.name}</h2>
                        {it.members.map(it => (
                            <p key={it.id}>{it.displayName} - {it.email}</p>
                        ))}
                    </L.VBox>
                ))}

                <button onClick={this.props.logOut}>Log out</button>
            </L.VBox>
        )
    }
}