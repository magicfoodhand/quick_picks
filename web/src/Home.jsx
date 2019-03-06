import React from 'react';
import gql from 'graphql-tag';
import { graphql } from 'react-apollo';
import Drawing from './Drawing';
import Fragments from './Fragments';

const HomeList = ({ data: {loading, error, powerball, megaball }}) => {
    if (loading) {
        return <p>Loading ...</p>;
    }
    if (error) {
        return <p>{error.message}</p>;
    }
    return <div className="Home">
        <h1 className="center drawings-title">Latest Drawings</h1>
        <Drawing name = "Power Ball" contents = { powerball }/>
        <Drawing name = "Mega Millions" contents = { megaball }/>
    </div>;
};

const HomeQuery = gql(`
    `+Fragments.AllDrawings()+`
    query {  
      powerball: latestDrawing(abbr: "power_ball") {
            ...AllDrawings
      }
      megaball: latestDrawing(abbr: "mega_millions") {
            ...AllDrawings
      }
    }
 `);

const Home = graphql(HomeQuery)(HomeList)

export default Home;