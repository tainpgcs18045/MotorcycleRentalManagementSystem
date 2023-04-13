import React, { useState, useEffect, useRef, Fragment } from "react";
import FormCheck from 'react-bootstrap/FormCheck'
import { Formik, Form } from "formik";

export const FilterSide = ({
    listColor,
    listManufacturer
}) => {
    return (
        <Fragment>
            <div className="pt-3 pb-3 px-3">
                <Formik
                    // initialValues={initialValues}
                    onSubmit={(values) => {
                        console.log(values)
                    }}>
                    {({
                        isSubmitting,
                        handleChange,
                        handleBlur,
                        handleSubmit,
                        values,
                        errors,
                        touched,
                        setFieldValue,
                    }) => (
                        <Form>
                            <div key={"Select-Manufacturer"} className="form-group mb-3">
                                <label className='form-label'>Manufacturer</label>
                                {listManufacturer.map((data) => {
                                    return (
                                        <FormCheck
                                            type={"checkbox"}
                                            id={`${data.value}`}
                                            label={`${data.label}`}
                                            key={`${data.key}`}
                                        />
                                    )
                                })}
                            </div>
                            <div key={"Select-Color"} className="form-group mb-3">
                                <label className='form-label'>Color</label>
                                {listColor.map((data) => {
                                    return (
                                        <FormCheck
                                            type={"checkbox"}
                                            id={`${data.value}`}
                                            label={`${data.label}`}
                                            key={`${data.key}`}
                                        />
                                    )
                                })}
                            </div>
                            <div key={"Select-Status"} className="form-group mb-3">
                                <label className='form-label'>Status</label>
                                <FormCheck
                                    type={"checkbox"}
                                    id={"enable"}
                                    label={"Enable"}
                                    key={"enable"}
                                />
                                <FormCheck
                                    type={"checkbox"}
                                    id={"disable"}
                                    label={"Disable"}
                                    key={"disable"}
                                />
                            </div>
                            <button type="submit" className="btn btn-dark btn-md mt-3">
                                Filter
                            </button>
                        </Form>
                    )}
                </Formik>
            </div>
        </Fragment>
    )
}