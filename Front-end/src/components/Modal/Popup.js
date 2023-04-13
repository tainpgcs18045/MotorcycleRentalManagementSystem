import React from "react";
import { Modal } from '@mui/material';

export const Popup = (props) => {
    const { showPopup, child, title } = props;
    return (
        <Modal
            open={showPopup}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <div className="container">
                <div className="popup-container">
                    <h2 className="popup-title">{title}</h2>
                    <div className="popup-content">
                        {child}
                    </div>
                </div>
            </div>
        </Modal>
    )
}