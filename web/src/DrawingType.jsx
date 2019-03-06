import React from 'react';
import gql from 'graphql-tag';
import { graphql } from 'react-apollo';
import Drawing from './Drawing';
import Fragments from './Fragments';

const DrawingTypesList = ({ data: {loading, error, drawing_type }}) => {
    if (loading) {
        return <p>Loading ...</p>;
    }
    if (error) {
        return <p>{error.message}</p>;
    }
    return <div className="DrawingTypes">
        <h2 className="center">{ drawing_type.name }</h2>
        <div className="Results">
            { drawing_type.drawings.map( ch => <Drawing name="" contents = { ch }/>) }
        </div>
    </div>;
};

const drawingTypesQuery = gql(`
    `+Fragments.AllDrawings()+`
    query DrawingType($abbr: String!) {
      drawing_type(abbr: $abbr) {
        name
        drawings { 
          ...AllDrawings
        }
      }
    }
 `);

const DrawingType = graphql(drawingTypesQuery, {
    options: (ownProps) => ({
        variables: {
            abbr: window.location.href.substr(window.location.href.lastIndexOf('/') + 1)
        }
    })
})(DrawingTypesList);

export default DrawingType;
