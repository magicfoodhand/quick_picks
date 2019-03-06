class Fragments {
    static AllDrawings() {
        return `
        fragment AllDrawings on Drawing {
          multiplier
          jackpot
          drawDate
          drawingType {
            abbr
            name
          }
          balls {
            id
            ballType {
              abbr
            }
            value
          }
        }`;
    }
}

export default Fragments;