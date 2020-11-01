import React from 'react';
import { BrowserRouter as Router, Route, Link } from 'react-router-dom';

import OrientalismView from './orientalism';

export default class Root extends React.Component {
    render() {
        return (
            <div className="all">
                <Router>
                    <Route exact path="/" component={OrientalismView} />
                </Router>
            </div>
        );
    }
}
