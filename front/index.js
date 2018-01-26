import React from 'react';
import { render } from 'react-dom';
import { AppContainer } from 'react-hot-loader';

import App from './js/App';

render((
  <AppContainer>
    <App />
  </AppContainer>
), document.getElementById('react-app'));

if (module.hot) {
  module.hot.accept('./App', () => {
    const NextApp = require('./App').default;
    render(
      <AppContainer>
        <NextApp />
      </AppContainer>,
      document.getElementById('react-app')
    );
  });
}