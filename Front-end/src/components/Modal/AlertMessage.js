import React from 'react';
import { Alert } from '@mui/material';

export const AlertMessage = (props) => {
    const { status, message, isShow } = props;
    return (
        <div className='alert-message' style={{ marginBottom: "16px" }}>
            {isShow &&
                <Alert variant="outlined" severity={status} color={status}>
                    {message}
                </Alert>}
        </div>

    )
}