import * as React from "react"
import * as ReactDOM from "react-dom"
import App from "./App"
import "./index.css"
import { createStore, combineReducers } from "redux"
import { Provider } from "react-redux"

import { BrowserRouter } from "react-router-dom"

const reducer = (state = [], action: { type: string }) => {
    switch (action.type) {
        default:
            return state
    }
}

const rootReducer = combineReducers({ reducer })

const store = createStore(rootReducer)

ReactDOM.render(
    <Provider store={store}>
        <BrowserRouter>
            <App/>
        </BrowserRouter>
    </Provider>,
    document.getElementById("root") as HTMLElement
)
