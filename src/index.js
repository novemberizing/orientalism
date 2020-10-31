import 'bootstrap/dist/css/bootstrap.css';
import './custom.css';
import '@fortawesome/fontawesome-free/css/all.css';
import React from 'react';
import ReactDOM from 'react-dom';

import { library } from '@fortawesome/fontawesome-svg-core';
import { fab } from '@fortawesome/free-brands-svg-icons';
import { fas } from '@fortawesome/free-solid-svg-icons';

import Root from './root';

console.log('hello novemberizing orientalism');

library.add(fab);
library.add(fas);

ReactDOM.render(<Root />, document.getElementById('root'));


