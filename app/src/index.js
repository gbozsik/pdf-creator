import React from 'react';
import axios from 'axios';
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import './index.css';
import App from './App';

axios.defaults.baseURL =  'http://localhost:8080/api'
// axios.defaults.headers.common['Authorization'] = 'AUTH TOKEN';
axios.defaults.headers.post['Content-Type'] = 'application/json';

const rootElement = document.getElementById("root");
const root = createRoot(rootElement);

root.render(
  <StrictMode>
    <App />
  </StrictMode>
);
