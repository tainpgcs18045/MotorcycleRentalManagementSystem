import React, { useState, useEffect, useRef, Fragment } from "react";
import { Formik, Form } from "formik";
import { SortSelect } from "../Form/SortSelect";
import { SearchField } from "../Form/SearchField";
import { SortType } from "../Form/SelectItem";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { useDispatch } from "react-redux";
import { reduxAction } from "../../redux-store/redux/redux.slice";

// Library - date time
import TextField from '@mui/material/TextField';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers';

const initialValues = {
    searchKey: ""
};


const SortBarMaintain = (props) => {

    const { startDate, setStartDate, endDate, setEndDate, SortBy } = props;

    const dispatch = useDispatch();
    return (
        <Fragment>
            <Row className="sort-bar mb-3">
                <Col lg={3} xs={12}>
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DatePicker
                            value={startDate}
                            onChange={(newValue) => {
                                setStartDate(newValue);
                            }}
                            renderInput={({ inputRef, inputProps, InputProps }) => (
                                <div className="form-group mb-3">
                                    <label className='form-label' htmlFor={inputProps.name}>
                                        Date From
                                    </label>
                                    <div style={{ display: 'inline' }}>
                                        <input className='form-control shadow-none'
                                            autoComplete='off'
                                            ref={inputRef} {...inputProps}
                                        />
                                        {InputProps?.endAdornment}
                                    </div>
                                </div>
                            )}
                        />
                    </LocalizationProvider>
                </Col>
                <Col lg={3} xs={12}>
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DatePicker
                            value={endDate}
                            onChange={(newValue) => {
                                setEndDate(newValue);
                            }}
                            renderInput={({ inputRef, inputProps, InputProps }) => (
                                <div className="form-group mb-3">
                                    <label className='form-label' htmlFor={inputProps.name}>
                                        Date To
                                    </label>
                                    <div style={{ display: 'inline' }}>
                                        <input className='form-control shadow-none'
                                            autoComplete='off'
                                            ref={inputRef} {...inputProps}
                                        />
                                        {InputProps?.endAdornment}
                                    </div>
                                </div>
                            )}
                        />
                    </LocalizationProvider>
                </Col>
                <Col lg={3} xs={12}>
                    <SortSelect
                        label={'Sort By'}
                        name={'sortBy'}
                        options={SortBy}
                        defaultValue={{ label: "Sort by ID", value: "id" }}
                        onChange={(value) => {
                            dispatch(reduxAction.setSortBy(value.value))
                        }}
                    />
                </Col>
                <Col lg={3} xs={12}>
                    <SortSelect
                        label={'Sort Type'}
                        name={'sortType'}
                        options={SortType}
                        defaultValue={{ label: "Newest to Oldest", value: "DESC" }}
                        onChange={(value) => {
                            dispatch(reduxAction.setSortType(value.value))
                        }}
                    />
                </Col>
            </Row>
            <Row className="sort-bar mb-3">
                <Col lg={3} xs={12}></Col>
                <Col lg={6} xs={12}>
                    <Formik
                        initialValues={initialValues}
                        onSubmit={(values) => {
                            dispatch(reduxAction.searchBike({ searchKey: values.searchKey }));
                            dispatch(reduxAction.setIsSubmitting({ isSubmitting: true }));
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
                            <Form className="d-flex flex-column">
                                <Row>
                                    <Col lg={9} xs={12}>
                                        <SearchField
                                            label={"Search"}
                                            name={"searchKey"}
                                            type={"text"}
                                            placeholder={"Search by id, type, cost, description"}
                                        />
                                    </Col>
                                    <Col lg={3} xs={12}>
                                        <button type="submit" className="btn btn-dark btn-md" style={{ width: '100%', marginTop: '2rem' }}>
                                            Submit
                                        </button>
                                    </Col>
                                </Row>
                            </Form>
                        )}
                    </Formik>
                </Col>
                <Col lg={3} xs={12}></Col>
            </Row>
        </Fragment >
    )
}

export default SortBarMaintain;

