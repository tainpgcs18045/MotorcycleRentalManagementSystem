import React from "react";
import { CircularProgress } from '@mui/material';

export const PageLoad = () => {
    return (
        <div className='text-center' style={{ padding: '25vh 0' }}>
            <CircularProgress size={200} />
            <h3 style={{ marginTop: '1vh' }}>Loading...</h3>
        </div>
    )
}