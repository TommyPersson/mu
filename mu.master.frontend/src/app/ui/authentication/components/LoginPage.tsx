
import * as React from "react"
import * as L from "react-layout-components"

export interface StateProps {
    isLoggingIn: boolean
}

export interface CallbackProps {
    performLogin: (email: string, password: string) => void
}

type Props = StateProps & CallbackProps

export default class LoginPage extends React.Component<Props, {}> {

    private emailInput: HTMLInputElement | null
    private passwordInput: HTMLInputElement | null

    render() {

        const spinner = this.props.isLoggingIn ? (
            <p>Logging in ...</p>
        ) : null

        return (
            <L.VBox>
                <p>Email</p>
                <input type="text" ref={r => this.emailInput = r}/>
                <p>Password</p>
                <input type="password" ref={r => this.passwordInput = r}/>
                <br/>
                <input type="submit" value="Log in" onClick={this.onLogInClicked}/>
                {spinner}
            </L.VBox>
        )
    }

    private onLogInClicked = () => {
        if (!this.emailInput || !this.passwordInput) {
            return
        }

        const email = this.emailInput.value
        const password = this.passwordInput.value

        this.props.performLogin(email, password)
    }
}