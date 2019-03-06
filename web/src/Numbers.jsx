import React, { Component } from 'react';

class Numbers extends Component {
    static _instance;
    constructor(props) {
        super(props);
        console.log(props)
        this.state = { numbers: props.value }
        Numbers._instance = this
    }

    static newNumbers() {
        return Numbers.newNumber(5, 60).concat(Numbers.newNumber(1, 60));
    }

    /**
     * Generate an array of unique elements.
     * needs to be able to contain duplicates later, see example.
     * @param num
     * @param max
     * @returns {Array}
     * ex:
     *  powerball => [3]
     *  newNumber => [1, 2, 3, 4, 5]
     *  result = newNumber + powerball
     *  result should be => [1, 2, 3, 4, 5, 3]
     */
    static newNumber(num, max) {
        let results = new Set();
        if(max > 0 && num > 0) {
            while(results.size < num)
                results.add(Math.floor(Math.random() * max) + 1);
        }
        return Array.from(results);
    }

    static next() {
        Numbers._instance.setState ({ numbers:[Numbers.newNumbers(), Numbers.newNumbers(), Numbers.newNumbers(), Numbers.newNumbers(), Numbers.newNumbers()]})
    }

    render() {
        var i = 0;
        var j = 0;
        return this.state.numbers !== null ? (
            <div className="Numbers">
                <ul>
                    { this.state.numbers.map( ch => <li key={ i++ }><ul>{ ch.map( h => <li key={ j++ }><div>{ h }</div></li> ) }</ul></li> ) }
                </ul>
            </div>
        ) : <h3>Generate Quick Picks</h3>;
    }
}
export default Numbers;