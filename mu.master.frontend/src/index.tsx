import * as React from "react"
import * as ReactDOM from "react-dom"
import { createStore, applyMiddleware } from "redux"
import { Provider } from "react-redux"
import thunk from "redux-thunk"

import { BrowserRouter } from "react-router-dom"
import { appReducer } from "./app/reducers"
import AppContainer from "./app/AppContainer"
import { Route } from "react-router"

const store = createStore(appReducer, applyMiddleware(thunk))

ReactDOM.render(
    <Provider store={store}>
        <BrowserRouter>
            <Route path="/" component={AppContainer} />
        </BrowserRouter>
    </Provider>,
    document.getElementById("root") as HTMLElement
)
