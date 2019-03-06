import React from 'react';
import gql from 'graphql-tag';
import { graphql } from 'react-apollo';
import { Link } from 'react-router-dom'

const DrawingTypesList = ({ data: {loading, error, drawingTypes }}) => {
    if (loading) {
        return <p>Loading ...</p>;
    }
    if (error) {
        return <p>{error.message}</p>;
    }
    return <div className="DrawingTypes">
        <h3 className="center">Lotteries</h3>
        <ul>
            { drawingTypes.map( ch => <li key={ch.abbr}><Link to={'/lotteries/'+ch.abbr} title= {ch.name} >{ch.name}</Link></li> ) }
        </ul>
    </div>;
};

const drawingTypesQuery = gql`
    query {  
      drawingTypes {
        name, abbr
      }
    }
 `;

const DrawingTypes = graphql(drawingTypesQuery)(DrawingTypesList);

export default DrawingTypes;