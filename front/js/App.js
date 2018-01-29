import React from 'react';
import axios from 'axios';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      sunRise: '',
      sunSet: '',
      temp: 'Unknown',
      requests: 0
    };
  }
  componentDidMount() {
    axios.get('/api/data').then(res => {
      const data = res.data;
      this.setState({
        sunRise: data.sunInfo.sunRise,
        sunSet: data.sunInfo.sunSet,
        temp: data.weatherInfo,
        requests: data.requests
      });
    })
  }

  render() {
    return (
      <React.Fragment>
        <h1>Hello from react</h1>
        <h3>Shanghai</h3>
        <div>the sun rise at {`${this.state.sunRise}`}</div>
        <div>the sun set at {`${this.state.sunSet}`}</div>
        <div>Temperature is {`${this.state.temp}`}</div>
        <p>Requests count since server started: {`${this.state.requests}`}</p>
      </React.Fragment>
    )
  }
}

export default App;