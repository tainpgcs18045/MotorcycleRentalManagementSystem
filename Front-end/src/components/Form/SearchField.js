import React from "react";
import { useField } from "formik";

export const SearchField = ({ label, ...props }) => {
    const [field, meta] = useField(props);

    return (
        <div className="form-group search-field mb-3">
            <label className='form-label' htmlFor={props.name}>
                {label}
            </label>
            <input
                className={`form-control shadow-none ${meta.touched && meta.error && "is-invalid"}`}
                {...field}
                {...props}
                autoComplete='off'
            />
        </div>
    );
};