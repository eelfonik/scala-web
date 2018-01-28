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
  module.hot.accept('./js/App', () => {
    render(
      <AppContainer>
        <App />
      </AppContainer>,
      document.getElementById('react-app')
    );
  });
}
