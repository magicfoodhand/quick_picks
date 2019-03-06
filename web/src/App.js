import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Link } from 'react-router-dom'
import { ApolloClient, HttpLink, InMemoryCache } from 'apollo-boost';
import { ApolloProvider} from 'react-apollo';
import createBrowserHistory from 'history/createBrowserHistory'

import Home from './Home'
import DrawingType from './DrawingType'
import DrawingTypes from './DrawingTypes'
import QuickPicks from './QuickPicks'

import './App.css'
import './application.css'

const client = new ApolloClient({
    link: new HttpLink({uri: 'https://fast-lowlands-95206.herokuapp.com/graphql'}),
    cache: new InMemoryCache(),
});

const history = createBrowserHistory();

export default class App extends Component {
    render() {
        return (
            <ApolloProvider client={client}>
                <Router history={history}>
                    <div className="App">
                        <Link to="/" className="link-to-home" title="Home">
                            <h1 className="logo">QP</h1>
                        </Link>
                        <div>
                            <nav>
                                <Link to="/random_draw" title="QuickPicks">Random Draw</Link>
                                <Link to="/lotteries" title="Lotteries">Lotteries</Link>
                            </nav>
                            <Route exact path="/" component={Home}/>
                            <Route exact path="/random_draw" component={QuickPicks}/>
                            <Route exact path="/lotteries" component={DrawingTypes}/>
                            <Route path="/lotteries/:abbr" component={DrawingType}/>
                        </div>
                    </div>
                </Router>
            </ApolloProvider>
        );
    }
}


document.addEventListener('DOMContentLoaded', () => {
    ReactDOM.render(
        <App/>,
        document.body.appendChild(document.createElement('div')),
    )
});
