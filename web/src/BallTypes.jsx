import React from 'react';
import gql from 'graphql-tag';
import { graphql } from 'react-apollo';
import { Link } from 'react-router-dom'

const BallTypesList = ({ data: {loading, error, ball_types }}) => {
    if (loading) {
        return <p>Loading ...</p>;
    }
    if (error) {
        return <p>{error.message}</p>;
    }
    return <div className="BallTypes">
        <h3>Ball Types</h3>
        <ul>
            { ball_types.map( ch => <li key={ch.abbr}><Link to={'/ball_types/'+ch.abbr}>{ch.name}</Link></li> ) }
        </ul>
    </div>;
};

const ballTypesQuery = gql`
    query {  
      ball_types {
        name, abbr
      }
    }
 `;

const BallTypes = graphql(ballTypesQuery)(BallTypesList)

export default BallTypes;