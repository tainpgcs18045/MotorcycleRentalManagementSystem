import React, { Fragment, useState, useEffect, useRef } from "react";

// Library
import { AxiosInstance } from "../../api/AxiosClient";
import { Formik, Form } from "formik";
import { DropzoneArea } from "material-ui-dropzone";
import { BikeSchema } from "../../validation";
import Cookies from 'universal-cookie';
import LinearProgress from '@mui/material/LinearProgress';
import Box from '@mui/material/Box';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { useNavigate } from 'react-router-dom';

// Firebase
import { storage } from "../../firebase/firebase";
import { ref, uploadBytes, getDownloadURL, deleteObject } from "firebase/storage";

// Component
import { AlertMessage } from "../../components/Modal/AlertMessage";
import { TextFieldCustom } from "../../components/Form/TextFieldCustom";
import { SelectField } from "../../components/Form/SelectField";
import { BikeAPI, CategoryAPI, ColorAPI, ManufacturerAPI } from "../../api/EndPoint";
import { GenerateRandomString } from "../../function/RandomString";
import { PageLoad } from '../../components/Base/PageLoad';
import { Popup } from '../../components/Modal/Popup';

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

// FUNCTION
// CALL API
const handleGetCategoryList = async (setListCategory) => {
    const body = {
        searchKey: null,
        page: 1,
        limit: 100,
        sortBy: "name",
        sortType: "ASC"
    };
    await AxiosInstance.post(CategoryAPI.getPagination, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        var listCategory = res.data.data.content.map((data) => {
            return {
                value: data.id,
                label: data.name,
                key: data.id,
            }
        })
        setListCategory(listCategory)
    })
        .catch((error) => {
            if (error && error.response) {
                console.log("Error: ", error);
            }
        });
};

const handleGetColorList = async (setListColor) => {
    const body = {
        searchKey: null,
        page: 1,
        limit: 100,
        sortBy: "name",
        sortType: "ASC"
    };
    await AxiosInstance.post(ColorAPI.getPagination, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        var listColor = res.data.data.content.map((data) => {
            return {
                value: data.id,
                label: data.name,
                key: data.id,
            }
        })
        setListColor(listColor)
    })
        .catch((error) => {
            if (error && error.response) {
                console.log("Error: ", error);
            }
        });
};

const handleGetManufacturerList = async (setListManufacturer) => {
    const body = {
        searchKey: null,
        page: 1,
        limit: 100,
        sortBy: "name",
        sortType: "ASC"
    };
    await AxiosInstance.post(ManufacturerAPI.getPagination, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        var listManufacturer = res.data.data.content.map((data) => {
            return {
                value: data.id,
                label: data.name,
                key: data.id,
            }
        })
        setListManufacturer(listManufacturer)
    })
        .catch((error) => {
            if (error && error.response) {
                console.log("Error: ", error);
            }
        });
};

const handleSubmit = async (formData, fileUpload, setAlert, setIsSubmitting, setFileUpload, setLoading, setShowPopup) => {

    const body = {
        name: formData.bikeName,
        bikeManualId: formData.bikeManualId,
        bikeNo: formData.bikeNo,
        bikeCategoryId: formData.bikeCategory,
        bikeColorId: formData.bikeColor,
        bikeManufacturerId: formData.bikeManufacturer,
        files: fileUpload,
    };
    await AxiosInstance.post(BikeAPI.create, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` },
    })
        .then((res) => {
            setIsSubmitting(false)
            setShowPopup(true)
            if (res.data.code === 1) {
                showAlert(setAlert, res.data.message, true)
                setFileUpload([]);
                setLoading(false);
            } else {
                showAlert(setAlert, res.data.message, false)
                fileUpload.forEach((data) => {
                    const imageRef = ref(storage, `bike-image/${data.fileName}`);
                    deleteObject(imageRef).then(() => {
                        // File deleted successfully
                    }).catch((error) => {
                        // Uh-oh, an error occurred!
                    });
                })
                setFileUpload([]);
                setLoading(false);
            }
        })
        .catch((error) => {
            showAlert(setAlert, error, false)
            setLoading(false);
        });
};

function ManageBikeCreate() {

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(false));
        setLoadingPage(false);
    }

    // Render page
    const navigate = useNavigate();

    // initialValues
    const initialValues = {
        bikeName: "",
        bikeManualId: "",
        bikeNo: "",
        bikeCategory: 0,
        bikeColor: 0,
        bikeManufacturer: 0,
        files: [{}],
    };

    // INITIALIZE USE STATE
    // VARIABLE
    // PAGE LOADING
    const [loadingData, setLoadingData] = useState(true);

    // VARIABLE
    // CREATE BIKE
    const [isClicking, setIsClicking] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [imageUpload, setImageUpload] = useState([]);
    const [fileUpload, setFileUpload] = useState([]);
    const [formData, setFormData] = useState({});
    const [listCategory, setListCategory] = useState([]);
    const [listColor, setListColor] = useState([]);
    const [listManufacturer, setListManufacturer] = useState([]);

    // VARIABLE
    // ALERT MESSAGE
    const [alert, setAlert] = useState({
        alertShow: false,
        alertStatus: "success",
        alertMessage: "",
    })

    // VARIABLE
    // LOADING BAR
    const [loading, setLoading] = useState(false);
    const timer = useRef();

    // VARIABLE
    // POPUP
    const [showPopup, setShowPopup] = useState(false);


    // IMAGE HANDLING
    // USE EFFECT
    // UPLOAD IMAGE TO FIREBASE
    useEffect(() => {
        if (isClicking && imageUpload.length === 0) {
            handleSubmit(formData, fileUpload, setAlert, setIsSubmitting, setFileUpload, setLoading, setShowPopup);
        }
        else if (isClicking) {
            var index = 0;
            imageUpload.forEach((data) => {
                index++;
                let randomString = GenerateRandomString(10);
                let fileName = `bike-image/${index}-${data.name}-${randomString}`;
                let imageRef = ref(storage, fileName);
                uploadBytes(imageRef, data).then(() => {
                    getDownloadURL(imageRef).then((url) => {
                        setFileUpload(prevState => {
                            return [...prevState, {
                                fileName: fileName.replace("bike-image/", ""),
                                filePath: url
                                    .replace(
                                        "https://firebasestorage.googleapis.com/v0/b/bike-hiring-management-d7a01.appspot.com/o/bike-image%2F",
                                        ""
                                    )
                            }];
                        })
                    });
                });
            });
            setIsSubmitting(true);
        }
        setIsClicking(false);
    }, [isClicking, imageUpload]);

    // USE EFFECT
    // HANDLING SUBMIT FORM
    useEffect(() => {
        if (isSubmitting && imageUpload.length === fileUpload.length) {
            handleSubmit(formData, fileUpload, setAlert, setIsSubmitting, setFileUpload, setLoading, setShowPopup);
        }
    }, [isSubmitting, fileUpload])

    // FUNCTION
    // UPLOAD IMAGE
    const handleFileUpload = (event) => {
        let values = event.target.value
        values.forEach((data) => {
            setImageUpload(prevState => {
                return [...prevState, event.target.value[values.indexOf(data)]];
            });
        })
    };

    // FUNCTION
    // REMOVE IMAGE
    const handleFileRemove = (file) => {
        setImageUpload(prevState => {
            let currentData = prevState;
            currentData.forEach((data) => {
                if (data.path === file.path) {
                    let index = currentData.indexOf(data);
                    currentData.splice(index, 1);
                }
            });
            return currentData;
        });
    };


    // LOADING BAR
    // USE EFFECT
    useEffect(() => {
        return () => {
            clearTimeout(timer.current);
        };
    }, []);

    useEffect(() => {
        if (loadingData) {
            handleGetCategoryList(setListCategory);
            handleGetColorList(setListColor);
            handleGetManufacturerList(setListManufacturer)
            setLoadingData(false)
        }
    }, [loadingData])




    return (
        !loadingData ?
            <Fragment>
                <Popup showPopup={showPopup} setShowPopup={setShowPopup}
                    child={
                        <Fragment>
                            <AlertMessage
                                isShow={alert.alertShow}
                                message={alert.alertMessage}
                                status={alert.alertStatus}
                            />
                            <div className="popup-button">
                                {alert.alertStatus === "success" ?
                                    <button className="btn btn-secondary btn-cancel"
                                        onClick={() => {
                                            setShowPopup(false);
                                            navigate('/manage-bike/bike-list');

                                        }}>Close</button>
                                    :
                                    <button className="btn btn-secondary btn-cancel"
                                        onClick={() => {
                                            setShowPopup(false);
                                        }}>Close</button>}
                            </div>
                        </Fragment >
                    }
                />
                <div className="container">
                    <h2 className="text-center">CREATE NEW BIKE</h2>
                    {loading && (
                        <Box sx={{ width: '100%' }}>
                            <LinearProgress />
                        </Box>
                    )}
                    <Formik
                        initialValues={initialValues}
                        validationSchema={BikeSchema}
                        onSubmit={(values) => {
                            setAlert({
                                alertShow: false,
                                alertStatus: "success",
                            })
                            setFormData(values)
                            setIsClicking(true);
                            setLoading(true);
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
                                    <Col xs={12} lg={6}>
                                        <TextFieldCustom
                                            label={"Bike Manual Id"}
                                            name={"bikeManualId"}
                                            type={"text"}
                                            placeholder={"Enter the bike manual id"}
                                        />
                                    </Col>
                                    <Col xs={12} lg={6}>
                                        <TextFieldCustom
                                            label={"Bike Name"}
                                            name={"bikeName"}
                                            type={"text"}
                                            placeholder={"Enter the bike name"}
                                        />
                                    </Col>
                                    <Col xs={12} lg={6}>
                                        <TextFieldCustom
                                            label={"Bike No"}
                                            name={"bikeNo"}
                                            type={"text"}
                                            placeholder={"Enter your bike number"}
                                        />
                                    </Col>
                                    <Col xs={12} lg={6}>
                                        <SelectField
                                            label={"Bike Category"}
                                            name={"bikeCategory"}
                                            options={listCategory}
                                            placeholder={"Choose bike category"}
                                            onChange={(selectOption) => {
                                                setFieldValue("bikeCategory", selectOption.value);
                                            }}
                                            onBlur={() => {
                                                handleBlur({ target: { name: "bikeCategory" } });
                                            }}
                                        />
                                    </Col>
                                    <Col xs={12} lg={6}>
                                        <SelectField
                                            label={"Bike Manufacturer"}
                                            name={"bikeManufacturer"}
                                            options={listManufacturer}
                                            placeholder={"Choose bike manufacturer"}
                                            onChange={(selectOption) => {
                                                setFieldValue("bikeManufacturer", selectOption.value);
                                            }}
                                            onBlur={() => {
                                                handleBlur({ target: { name: "bikeManufacturer" } });
                                            }}
                                        />
                                    </Col>
                                    <Col xs={12} lg={6}>
                                        <SelectField
                                            label={"Bike Color"}
                                            name={"bikeColor"}
                                            options={listColor}
                                            placeholder={"Choose bike color"}
                                            onChange={(selectOption) => {
                                                setFieldValue("bikeColor", selectOption.value);
                                            }}
                                            onBlur={() => {
                                                handleBlur({ target: { name: "bikeColor" } });
                                            }}
                                        />
                                    </Col>
                                </Row>
                                <label className='form-label'>Bike Images (Max 4)</label>
                                <label className='form-label' style={{ color: "red", fontStyle: "italic" }}>*Note: First image will be avatar of the bike</label>
                                <DropzoneArea
                                    acceptedFiles={[
                                        ".png,.jpg,.jpeg",
                                    ]}
                                    showPreviews={true}
                                    maxFileSize={10000000}
                                    fullWidth={true}
                                    dropzoneText='Drop files to attach or browse'
                                    filesLimit={4}
                                    showFileNamesInPreview={true}
                                    showPreviewsInDropzone={false}
                                    showAlerts={false}
                                    name='file'
                                    id='bikeImage'
                                    onDelete={(file) => {
                                        handleFileRemove(file);
                                    }}
                                    onDrop={(dropFiles) => {
                                        let event = {
                                            target: {
                                                name: "files",
                                                value: dropFiles,
                                            },
                                        };
                                        handleChange(event);
                                        handleFileUpload(event);
                                    }}
                                />
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

export default ManageBikeCreate;