import React from "react";
import Select from "react-select";

export const SortSelect = ({ label, ...props }) => {
    return (
        <div className="form-group mb-3">
            <label className='form-label' htmlFor={props.name}>
                {label}
            </label>
            <Select
                className={`shadow-none`}
                {...props}
            />
        </div>
    );
}