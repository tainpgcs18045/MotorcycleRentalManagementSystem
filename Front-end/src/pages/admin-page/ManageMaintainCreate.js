import React, { Fragment, useState, useEffect, useRef } from "react";

// Library
import { AxiosInstance } from "../../api/AxiosClient";
import { Formik, Form } from 'formik';
import { MaintainSchema } from "../../validation";
import Cookies from 'universal-cookie';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { useNavigate } from 'react-router-dom';
import { Radio, RadioGroup, FormControlLabel } from "@mui/material";

// Library - date time
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DateTimePicker } from '@mui/x-date-pickers';
import dayjs from 'dayjs';


// Component
import { AlertMessage } from "../../components/Modal/AlertMessage";
import { TextFieldCustom } from "../../components/Form/TextFieldCustom";
import { TextAreaCustom } from "../../components/Form/TextAreaCustom";
import { MaintainAPI } from "../../api/EndPoint";
import { PageLoad } from '../../components/Base/PageLoad';
import { Popup } from '../../components/Modal/Popup';
import { GetFormattedCurrency, ParseCurrencyToNumber, InputNumber } from "../../function/CurrencyFormat";

// Redux
import { useDispatch } from "react-redux";
import { reduxAuthenticateAction } from "../../redux-store/redux/reduxAuthenticate.slice";

const cookies = new Cookies();

// FUNCTION
// INTERNAL PAGE
const showAlert = (setAlert, message, isSuccess) => {
    if (isSuccess) {
        setAlert({
            alertShow: true,
            alertStatus: "success",
            alertMessage: message
        })
    } else {
        setAlert({
            alertShow: true,
            alertStatus: "error",
            alertMessage: message
        })
    }
}

const handleSubmit = async (formikRef, date, setAlert, setShowPopup, setIsSubmitting) => {
    const body = {
        date: date,
        type: formikRef.current.values.type === "" ? null : formikRef.current.values.type,
        title: formikRef.current.values.title === "" ? null : formikRef.current.values.title,
        description: formikRef.current.values.description === "" ? null : formikRef.current.values.description,
        cost: ParseCurrencyToNumber(formikRef.current.values.cost) < 0 ? null : ParseCurrencyToNumber(formikRef.current.values.cost),
        stringListManualId: formikRef.current.values.stringListManualId === "" ? null : formikRef.current.values.stringListManualId,
    };
    await AxiosInstance.post(MaintainAPI.create, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` },
    }).then((res) => {
        if (res.data.code === 1) {
            showAlert(setAlert, res.data.message, true)
        } else {
            showAlert(setAlert, res.data.message, false)
        }
        setShowPopup(true)
        setIsSubmitting(false)
    }).catch((error) => {
        showAlert(setAlert, error, false)
    });
}


function ManageMaintainCreate() {

    var now = dayjs()

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(false));
        setLoadingPage(false);
    }

    // Render page
    const navigate = useNavigate();

    // VARIABLE
    // CART
    // TRIGGER
    const [isSubmitting, setIsSubmitting] = useState(false);

    const [date, setDate] = useState(now);
    const [type, setType] = useState("GENERAL");


    // VARIABLE
    // PAGE LOADING
    const [loadingData, setLoadingData] = useState(true);

    // VARIABLE
    // ALERT MESSAGE
    const [alert, setAlert] = useState({
        alertShow: false,
        alertStatus: "success",
        alertMessage: "",
    })

    // VARIABLE
    // POPUP
    const [showPopup, setShowPopup] = useState(false);


    // VARIABLE
    // FORMIK
    const formikRef = useRef(null);
    const initialValues = {
        type: "GENERAL",
        title: "",
        description: "",
        stringListManualId: "",
        cost: GetFormattedCurrency(0),
    }

    // USE EFFECT
    // PAGE LOADING
    useEffect(() => {
        if (loadingData === true) {
            setTimeout(() => {
                setLoadingData(false)
            }, 500);
        }
    }, [loadingData])

    // USE EFFECT
    // HANDLING SUBMIT FORM
    useEffect(() => {
        if (isSubmitting === true) {
            handleSubmit(formikRef, date, setAlert, setShowPopup, setIsSubmitting)
        }
    }, [isSubmitting])


    let popup = <Popup showPopup={showPopup} setShowPopup={setShowPopup}
        child={
            <Fragment>
                <AlertMessage
                    isShow={alert.alertShow}
                    message={alert.alertMessage}
                    status={alert.alertStatus}
                />
                {alert.alertStatus === "success" ?
                    <div className="popup-button">
                        <button className="btn btn-secondary btn-cancel-view"
                            onClick={() => {
                                navigate('/manage-maintenance/maintenance-list')
                            }}>Close</button>
                    </div>
                    :
                    <div className="popup-button">
                        <button className="btn btn-secondary btn-cancel-view"
                            onClick={() => {
                                setShowPopup(false);
                            }}>Close</button>
                    </div>
                }
            </Fragment >
        }
    />

    return (
        !loadingData ?
            <Fragment>
                {popup}
                <div className="container">
                    <h2 className="text-center">CREATE MAINTENANCE</h2>
                    <Formik
                        innerRef={formikRef}
                        enableReinitialize
                        initialValues={initialValues}
                        validationSchema={MaintainSchema}
                        onSubmit={(values) => {
                            setIsSubmitting(true);
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
                                <Row className="mb-3">
                                    <Col xs={12} lg={12}>
                                        <TextFieldCustom
                                            label={"Title"}
                                            name={"title"}
                                            type={"text"}
                                            placeholder={"Enter the title"}
                                        />
                                    </Col>

                                    <Row>
                                        <Col xs={12} lg={3}>
                                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                <DateTimePicker
                                                    label="Choose date"
                                                    value={date}
                                                    onChange={(newValue) => {
                                                        setDate(newValue);
                                                    }}
                                                    renderInput={({ inputRef, inputProps, InputProps }) => (
                                                        <div className="form-group mb-3">
                                                            <label className='form-label' htmlFor={inputProps.name}>
                                                                Choose date
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
                                    </Row>
                                    <Col xs={12} lg={2}>
                                        <label className='form-label'>Type</label>
                                        <RadioGroup
                                            aria-labelledby="demo-controlled-radio-buttons-group"
                                            name="type"
                                            value={type}
                                            onChange={(e, value) => {
                                                setType(value)
                                                setFieldValue("type", value);
                                            }}
                                        >
                                            <FormControlLabel value={"GENERAL"} control={<Radio />} label="GENERAL" />
                                            <FormControlLabel value={"BIKE"} control={<Radio />} label="BIKE" />
                                        </RadioGroup>
                                    </Col>
                                    {type === "BIKE" &&
                                        <Col xs={12} lg={12}>
                                            <TextAreaCustom
                                                label={"Bike Manual ID List"}
                                                name={"stringListManualId"}
                                                type={"text"}
                                                placeholder={"Enter the manual ID list"}
                                            />
                                        </Col>
                                    }
                                    <Col xs={12} lg={12}>
                                        <TextAreaCustom
                                            label={"Description"}
                                            name={"description"}
                                            type={"text"}
                                            placeholder={"Enter the description"}
                                        />
                                    </Col>
                                    <Col xs={12} lg={12}>
                                        <TextFieldCustom
                                            label={"Total cost"}
                                            name={"cost"}
                                            type={"text"}
                                            onWheel={(e) => e.target.blur()}
                                            placeholder={"Enter the cost"}
                                            onChange={(event) => {
                                                let value = event.target.value;
                                                let decimalValue = ParseCurrencyToNumber(InputNumber(value))
                                                setFieldValue("cost", GetFormattedCurrency(decimalValue));
                                            }}
                                        />
                                    </Col>
                                </Row>
                                <button type="submit" className="btn btn-dark btn-md mt-3">
                                    Submit
                                </button>
                            </Form>
                        )}
                    </Formik>
                </div>

            </Fragment >
            :
            <Fragment>
                <PageLoad />
            </Fragment>
    )
}

export default ManageMaintainCreate;