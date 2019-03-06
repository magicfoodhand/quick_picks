import React, { Component } from 'react';
import Numbers from './Numbers'

class QuickPicksButton extends Component {
    constructor(props) {
        super(props);
        this.state = {numbers: []};

        // This binding is necessary to make `this` work in the callback
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        this.setState(prevState => ( Numbers.next() ));
    }

    render() {
        return (
            <div>
                <button className="logo" onClick={e => this.handleClick(e)}>Draw</button>
                <Numbers value = { this.state.numbers }/>
            </div>
        );
    }
}
export default QuickPicksButton;