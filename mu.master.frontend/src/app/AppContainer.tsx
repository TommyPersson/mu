
import { connect, Dispatch } from "react-redux"
import App from "./App"
import { AppState } from "./reducers"
import { Actions } from "./domain/authentication"

function mapStateToProps(state: AppState) {

    return {
        isLoggedIn: state.domain.authentication.isLoggedIn,
        isLoaded: state.domain.authentication.hasLoadedStoredAuthToken
    }
}

function mapDispatchToProps(dispatch: Dispatch<any>) {
    return {
        loadStoredAuthToken() {
            dispatch(Actions.loadStoredAuthToken())
        },

        logOut() {
            dispatch(Actions.logout())
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App)