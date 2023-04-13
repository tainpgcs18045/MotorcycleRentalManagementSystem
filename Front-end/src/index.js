import React from "react";
// React 18
import { createRoot } from 'react-dom/client';
// React 17
// import ReactDOM from "react-dom";
import { Provider } from "react-redux";
import store from "./redux-store/index";
import App from "./App";

// React 18
const container = document.getElementById("root");
const root = createRoot(container); // createRoot(container!) if you use TypeScript
root.render(
	<React.StrictMode>
		<Provider store={store}>
			<App />
		</Provider>
	</React.StrictMode>
);

// React 17
// ReactDOM.render(
// 	<React.StrictMode>
// 		<Provider store={store}>
// 			<App />
// 		</Provider>
// 	</React.StrictMode>,
// 	document.getElementById("root")
// );
