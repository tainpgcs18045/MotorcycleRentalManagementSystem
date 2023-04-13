import React from "react";
import { ErrorMessage, useField } from "formik";
import Select from "react-select";

export const SelectField = ({ label, ...props }) => {
    const [field, meta] = useField(props);

    return (
        <div className="form-group mb-3">
            <label className='form-label' htmlFor={field.name}>
                {label}
            </label>
            <Select
                className={`shadow-none ${meta.touched && meta.error && "is-invalid"}`}
                {...props}
            />
            <ErrorMessage component='div' name={field.name} className='form-error text-danger' />
        </div>
    );
};