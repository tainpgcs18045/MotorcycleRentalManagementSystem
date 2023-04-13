import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    accessToken: null,
    isShowPublicNavBar: false
};

const reduxAuthenticateSlice = createSlice({
    name: "reduxAuthenticate",
    initialState: initialState,
    reducers: {
        updateToken(state, action) {
            state.accessToken = action.payload;
        },
        updateIsShowPublicNavBar(state, action) {
            state.isShowPublicNavBar = action.payload;
        }
    }
});

export const reduxAuthenticateAction = reduxAuthenticateSlice.actions;

export default reduxAuthenticateSlice;