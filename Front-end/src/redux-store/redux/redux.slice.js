import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    searchKey: null,
    color: [],
    manufacturer: [],
    sortBy: "id",
    sortType: "DESC",
    isSubmitting: false,
    status: "PENDING",
    cartNumber: 0,
};

const reduxSlice = createSlice({
    name: "redux",
    initialState: initialState,
    reducers: {
        searchBike(state, action) {
            state.searchKey = action.payload.searchKey;
        },
        setSortBy(state, action) {
            state.sortBy = action.payload;
        },
        setSortType(state, action) {
            state.sortType = action.payload;
        },
        filterBike(state, action) {
            state.color = action.payload.color;
            state.manufacturer = action.payload.manufacturer;
        },
        setIsSubmitting(state, action) {
            state.isSubmitting = action.payload.isSubmitting;
        },
        setSortByStatus(state, action) {
            state.status = action.payload;
        },
        setCartNumber(state, action) {
            state.status = action.payload;
        }
    }
});

export const reduxAction = reduxSlice.actions;

export default reduxSlice;