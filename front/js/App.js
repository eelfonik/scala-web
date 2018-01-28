import React from 'react';
import axios from 'axios';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }
  componentDidMount() {
    axios.get('/data').then(res => {
      const data = res.data;
      this.setState({
        sunRise: data.sunInfo.sunRise,
        sunSet: data.sunInfo.sunSet
      });
    })
  }

  render() {
    return [
      <h1>Hello from react</h1>,
      <div>the sun rise at {this.state.sunRise ? `${this.state.sunRise}` : ''}</div>,
      <div>the sun set at {this.state.sunSet ? `${this.state.sunSet}` :  ''}</div>
    ]
  }
}

export default App;