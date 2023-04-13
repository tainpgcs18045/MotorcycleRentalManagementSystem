/** Store setup using configureStore method from redux-toolkit */
import { configureStore } from "@reduxjs/toolkit";

/** Where to import slice reducers */
import reduxSlice from "./redux/redux.slice";
import reduxPaginationSlice from "./redux/reduxPagination.slice";
import reduxAuthenticateSlice from "./redux/reduxAuthenticate.slice";

const store = configureStore({
    reducer: {
        redux: reduxSlice.reducer,
        reduxPagination: reduxPaginationSlice.reducer,
        reduxAuthenticate: reduxAuthenticateSlice.reducer
    },
});

export default store;
