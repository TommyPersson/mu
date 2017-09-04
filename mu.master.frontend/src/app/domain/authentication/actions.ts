import { Dispatch } from "react-redux"
import JwtTokenClaims from "./models/JwtTokenClaims"
import * as jwt_decode from "jwt-decode"
import { MuApi } from "../../../utils/MuApi/MuApi"

const authTokenKey = "mu/authentication/authToken"

export const performLogin = (email: string, password: string) =>
    async (dispatch: Dispatch<any>) => {
        dispatch(LOGIN__STARTED())

        const api = new MuApi()
        try {
            const token = await api.createAuthToken(email, password)
            localStorage.setItem(authTokenKey, token)
            dispatch(LOGIN__SUCCESSFUL(token))
        } catch (error) {
            dispatch(LOGIN__FAILED())
        }
    }

export const LOGIN__STARTED = () => ({ type: "LOGIN__STARTED" })
export const LOGIN__SUCCESSFUL = (token: string) => ({ type: "LOGIN__SUCCESSFUL", token: token })
export const LOGIN__FAILED = () => ({ type: "LOGIN__FAILED" })


export const loadStoredAuthToken = () => {
    return async (dispatch: Dispatch<any>) => {
        console.log("loading stored token")
        const token = localStorage.getItem(authTokenKey)

        if (token !== null) {
            const claims = jwt_decode<JwtTokenClaims>(token)
            if (claims.exp < Date.now()) {
                console.log("stored token loaded")
                dispatch(STORED_AUTH_TOKEN_LOADED(token))
                return
            }
            console.log("stored token too old, discarding")
            localStorage.removeItem(authTokenKey)
        }

        console.log("no stored token found")
        dispatch(STORED_AUTH_TOKEN_LOADED(null))
    }
}

export const STORED_AUTH_TOKEN_LOADED = (token: string | null) => ({
    type: "STORED_AUTH_TOKEN_LOADED",
    token: token
})

export const logout = () => {
    return async (dispatch: Dispatch<any>) => {
        localStorage.removeItem(authTokenKey)
        dispatch(LOGGED_OUT())
    }
}

export const LOGGED_OUT = () => ({ type: "LOGGED_OUT" })