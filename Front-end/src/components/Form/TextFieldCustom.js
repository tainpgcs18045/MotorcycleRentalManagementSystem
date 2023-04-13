import React from "react";
import { ErrorMessage, useField } from "formik";

export const TextFieldCustom = ({ label, ...props }) => {
    const [field, meta] = useField(props);

    return (
        <div className="form-group mb-3">
            <label className='form-label' htmlFor={field.name}>
                {label}
            </label>
            <input
                className={`form-control shadow-none ${meta.touched && meta.error && "is-invalid"
                    }`}
                {...field}
                {...props}
                autoComplete='off'
            />
            <ErrorMessage component='div' name={field.name} className='form-error text-danger' />
        </div>
    );
};