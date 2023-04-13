import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    page: 1,
    rowsPerPage: 5
};

const reduxPaginationSlice = createSlice({
    name: "reduxPagination",
    initialState: initialState,
    reducers: {
        updatePage(state, action) {
            state.page = action.payload;
        },
        updateRowsPerPage(state, action) {
            state.rowsPerPage = action.payload;
        },
    }
});

export const reduxPaginationAction = reduxPaginationSlice.actions;

export default reduxPaginationSlice;