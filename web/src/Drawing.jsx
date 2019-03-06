import React, { Component } from 'react';
import DateFormatter from './Date'

class Drawing extends Component {
    render() {
        const {name, contents} = this.props;
        return contents === null
            ? (<h1 className="center">{ name } Drawing is Unavailable</h1>)
            : (
                <div className="Drawing">
                    <h2>{ name }</h2>
                    <h3> { DateFormatter.format(contents.draw_date) }</h3>
                    <ul className="Balls">
                        { contents.balls.map( ch => <li key={ch.id} className={ ch.ball_type.abbr }>{ ch.value }</li> )  }
                    </ul>
                </div>
            );
    }
}
export default Drawing;