import React from 'react';
import gql from 'graphql-tag';
import { graphql } from 'react-apollo';

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
            { ball_types.map( ch => <li key={ch.abbr}>{ch.name}</li> ) }
        </ul>
    </div>;
};

const ballTypesQuery = gql`
    query {  
      ball_types {
        id, name, abbr, created_at, updated_at
      }
    }
 `;

const BallType = graphql(ballTypesQuery)(BallTypesList);

export default BallType;