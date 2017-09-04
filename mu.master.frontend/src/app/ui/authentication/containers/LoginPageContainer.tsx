

import * as React from "react"
import { connect, Dispatch } from "react-redux"
import LoginPage, { CallbackProps, StateProps } from "../components/LoginPage"
import { Actions, State } from "app/domain/authentication"


function mapStateToProps(state: State): StateProps {
    return {
        isLoggingIn: state.isLoggingIn
    }
}

function mapDispatchToProps(dispatch: Dispatch<any>): CallbackProps {
    return {
        performLogin: (email: string, password: string) => {
            dispatch(Actions.performLogin(email, password))
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginPage)