import React, { Component } from 'react';
import QuickPicksButton from './QuickPicksButton'

class QuickPicks extends Component {
    render() {
        return (
                <div className="QuickPicks">
                    <h2 className="center">Quick Picks</h2>
                    <QuickPicksButton />
                </div>
            );
    }
}
export default QuickPicks;