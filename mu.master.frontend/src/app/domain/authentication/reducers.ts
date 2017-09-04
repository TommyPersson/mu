
import { AnyAction } from "redux"

export interface State {
    isLoggingIn: boolean
    isLoggedIn: boolean
    authToken: string | null
    hasLoadedStoredAuthToken: boolean
}

const initialState: State = {
    isLoggingIn: false,
    isLoggedIn: false,
    authToken: null,
    hasLoadedStoredAuthToken: false
}

export const reducer = (state = initialState, action: AnyAction): any => {
    switch (action.type) {
        case "LOGIN__STARTED":
            return {
                isLoggingIn: true
            }
        case "LOGIN__SUCCESSFUL":
            return {
                isLoggingIn: false,
                isLoggedIn: true,
                authToken: action.token
            }
        case "LOGIN__FAILED":
            return {
                isLoggingIn: false,
                authToken: null
            }
        case "STORED_AUTH_TOKEN_LOADED":
            return {
                isLoggedIn: action.token !== null,
                authToken: action.token,
                hasLoadedStoredAuthToken: true
            }
        case "LOGGED_OUT": {
            return {
                isLoggedIn: false,
                authToken: null
            }
        }
        default:
            return state
    }
}