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
import { useParams } from "react-router-dom";
import { useNavigate } from 'react-router-dom';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';

// Firebase
import { storage } from "../../firebase/firebase";
import { ref, uploadBytes, getDownloadURL, deleteObject } from "firebase/storage";
import { Firebase_URL } from "../../api/EndPoint";

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

const handleRemoveImageFromList = (imageID, imageList, setImageList) => {
    setImageList(imageList.filter(data => data.id !== imageID));
}

// FUNCTION
// CALL API
const handleGetBikeById = async (id, setData, setDefaultCategory, setDefaultManufacturer, setDefaultColor, setImageList) => {
    await AxiosInstance.get(BikeAPI.getById + id, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        if (res.data.code === 1) {
            setData(res.data.data);
            setDefaultCategory({
                value: res.data.data.bikeCategoryId,
                label: res.data.data.bikeCategoryName,
            })
            setDefaultManufacturer({
                value: res.data.data.bikeManufacturerId,
                label: res.data.data.bikeManufacturerName,
            })
            setDefaultColor({
                value: res.data.data.bikeColorId,
                label: res.data.data.bikeColor,
            })
            setImageList(res.data.data.imageList)
        }
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
    });
};

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
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
    });
};

const handleDeleteImage = async (imageId, setAlert, setShowCloseButton, setIsImageDelete, setImageList, imageList) => {
    await AxiosInstance.post(BikeAPI.deteteImage + imageId, {}, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        setIsImageDelete(true);
        setShowCloseButton(true);
        if (res.data.code === 1) {
            showAlert(setAlert, res.data.message, true);
            var removedList = imageList.filter(item => item.id !== imageId);
            setImageList(removedList);
        } else {
            showAlert(setAlert, res.data.message, false);
        }
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
        showAlert(setAlert, error, false);
    });
};

const handleSubmit = async (
    formData,
    fileUpload,
    setAlert,
    setIsSubmitting,
    setFileUpload,
    setLoading,
    setShowPopup
) => {
    const body = {
        name: formData.bikeName,
        bikeManualId: formData.bikeManualId,
        bikeNo: formData.bikeNo,
        bikeCategoryId: formData.bikeCategory,
        bikeColorId: formData.bikeColor,
        bikeManufacturerId: formData.bikeManufacturer,
        status: formData.status,
        hiredNumber: formData.hiredNumber,
        files: fileUpload,
    };
    await AxiosInstance.post(BikeAPI.update + formData.id, body, {
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


function ManageBikeUpdate() {

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(false));
        setLoadingPage(false);
    }

    // GET ID FROM URL
    let { id } = useParams()

    // Render page
    const navigate = useNavigate();

    // INITIALIZE USE STATE
    // VARIABLE
    // PAGE LOADING
    const [loadingData, setLoadingData] = useState(true);

    // VARIABLE
    // UPDATE BIKE
    const [isClicking, setIsClicking] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [imageUpload, setImageUpload] = useState([]);
    const [fileUpload, setFileUpload] = useState([]);
    const [formData, setFormData] = useState({});
    const [listCategory, setListCategory] = useState([]);
    const [listColor, setListColor] = useState([]);
    const [listManufacturer, setListManufacturer] = useState([]);
    const [data, setData] = useState({});
    const [imageList, setImageList] = useState([]);

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
    const [showCloseButton, setShowCloseButton] = useState(false);
    const [isImageDelete, setIsImageDelete] = useState(false);
    const [imageID, setImageID] = useState(0);

    // Modal fullscreen
    const [openModal, setOpenModal] = useState(false);
    const [imageFullScreen, setImageFullScreen] = useState("");

    const handleClickOpenModal = () => {
        setOpenModal(true);
    };

    const onCloseModal = () => {
        setOpenModal(false);
    };

    // Formik variables
    const initialValues = {
        bikeName: "",
        bikeManualId: "",
        bikeNo: "",
        bikeCategory: 0,
        bikeColor: 0,
        bikeManufacturer: 0,
        files: [{}],
    };
    const [defaultCategory, setDefaultCategory] = useState({});
    const [defaultManufacturer, setDefaultManufacturer] = useState({});
    const [defaultColor, setDefaultColor] = useState({});

    // IMAGE HANDLING
    // USE EFFECT
    // UPLOAD IMAGE TO FIREBASE
    useEffect(() => {
        // CASE 1: NO IMAGE
        if (isClicking && imageUpload.length === 0) {
            handleSubmit(formData, fileUpload, setAlert, setIsSubmitting, setFileUpload, setLoading, setShowPopup);
        }

        // CASE 2: EXCEED LIMIT IMAGE NUMBER
        else if (isClicking && imageList.length + imageUpload.length > 4) {
            setShowPopup(true);
            showAlert(setAlert, "Exceeded maximum image number (Max: 4 - Current: " + imageList.length + " - New: " + imageUpload.length + ")", false);
            setLoading(false);
        }

        // CASE 3: NO EXCEED LIMIT IMAGE NUMBER
        else if (isClicking) {
            var index = imageList.length;
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

    // USE EFFECT
    // LOADING BAR - UPLOAD IMAGE
    useEffect(() => {
        return () => {
            clearTimeout(timer.current);
        };
    }, []);

    // USE EFFECT
    // PAGE LOADING
    useEffect(() => {
        if (loadingData) {
            handleGetCategoryList(setListCategory);
            handleGetColorList(setListColor);
            handleGetManufacturerList(setListManufacturer)
            handleGetBikeById(id, setData, setDefaultCategory, setDefaultManufacturer, setDefaultColor, setImageList);
            setLoadingData(false)
        }
    }, [loadingData])

    // USE EFFECT
    // SHOW CONFIRMATION DELETE IMAGE POPUP
    useEffect(() => {
        if (isImageDelete === true && imageID !== 0) {
            setShowPopup(true);
        }
    }, [isImageDelete, imageID])


    // Update initialValues
    if (Object.keys(data).length !== 0) {
        initialValues.bikeManualId = data.bikeManualId;
        initialValues.bikeName = data.name;
        initialValues.bikeNo = data.bikeNo;
        initialValues.bikeCategory = data.bikeCategoryId;
        initialValues.bikeManufacturer = data.bikeManufacturerId;
        initialValues.bikeColor = data.bikeColorId;
    }

    let popup;
    if (isImageDelete === true) {
        popup = <Popup showPopup={showPopup} setShowPopup={setShowPopup}
            child={
                showCloseButton ?
                    < Fragment >
                        <AlertMessage
                            isShow={alert.alertShow}
                            message={alert.alertMessage}
                            status={alert.alertStatus}
                        />
                        <div className="popup-button">
                            <button className="btn btn-secondary btn-cancel-view"
                                onClick={() => {
                                    setShowPopup(false);
                                    setShowCloseButton(false);
                                    setAlert({ alertShow: false });
                                    setImageID(0);
                                    setIsImageDelete(false);
                                    handleRemoveImageFromList(imageID, imageList, setImageList);
                                }}>Close</button>
                        </div>
                    </ Fragment>
                    :
                    <Fragment>
                        <div className='popup-message text-center mb-3'>
                            <label>Do you really want to delete this image?</label>
                            <p>This process cannot be undone</p>
                        </div>
                        <div className="popup-button">
                            <button className="btn btn-secondary btn-cancel"
                                onClick={() => {
                                    setShowPopup(false);
                                    setImageID(0);
                                    setIsImageDelete(false);
                                }}>Cancel</button>
                            <button className="btn btn-danger btn-action"
                                onClick={() => handleDeleteImage(
                                    imageID,
                                    setAlert,
                                    setShowCloseButton,
                                    setIsImageDelete,
                                    setImageList,
                                    imageList)
                                }>DELETE</button>
                        </div>
                    </Fragment >
            }
        />
    } else {
        popup = <Popup showPopup={showPopup} setShowPopup={setShowPopup}
            child={
                <Fragment>
                    <AlertMessage
                        isShow={alert.alertShow}
                        message={alert.alertMessage}
                        status={alert.alertStatus}
                    />
                    <div className="popup-button">
                        {alert.alertStatus === "success" ?
                            <button className="btn btn-secondary btn-cancel-view"
                                onClick={() => {
                                    setShowPopup(false);
                                    navigate('/manage-bike/bike/' + data.id);
                                }}>Close</button>
                            :
                            <button className="btn btn-secondary btn-cancel-view"
                                onClick={() => {
                                    setShowPopup(false);
                                }}>Close</button>}
                    </div>
                </Fragment >
            }
        />
    }

    return (
        !loadingData ?
            <Fragment>
                {popup}
                <div className="container">
                    <h2 className="text-center">UPDATE BIKE</h2>
                    {loading && (
                        <Box sx={{ width: '100%' }}>
                            <LinearProgress />
                        </Box>
                    )}
                    <Formik
                        enableReinitialize
                        initialValues={initialValues}
                        validationSchema={BikeSchema}
                        onSubmit={(values) => {
                            setAlert({
                                alertShow: false,
                                alertStatus: "success",
                            })
                            setFormData({
                                id: data.id,
                                status: data.status,
                                hiredNumber: data.hiredNumber,
                                bikeName: values.bikeName,
                                bikeManualId: values.bikeManualId,
                                bikeNo: values.bikeNo,
                                bikeCategory: values.bikeCategory,
                                bikeColor: values.bikeColor,
                                bikeManufacturer: values.bikeManufacturer,
                                files: [{}]
                            })
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
                                <Row className="mb-3">
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
                                            value={defaultCategory}
                                            options={listCategory}
                                            placeholder={"Choose bike category"}
                                            onChange={(selectOption) => {
                                                setDefaultCategory(selectOption)
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
                                            value={defaultManufacturer}
                                            options={listManufacturer}
                                            placeholder={"Choose bike manufacturer"}
                                            onChange={(selectOption) => {
                                                setDefaultManufacturer(selectOption);
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
                                            value={defaultColor}
                                            options={listColor}
                                            placeholder={"Choose bike color"}
                                            onChange={(selectOption) => {
                                                setDefaultColor(selectOption);
                                                setFieldValue("bikeColor", selectOption.value);
                                            }}
                                            onBlur={() => {
                                                handleBlur({ target: { name: "bikeColor" } });
                                            }}
                                        />
                                    </Col>
                                    <label className='form-label'>Bike Images</label>
                                    {Object.keys(imageList).length !== 0 ?
                                        imageList.map((value, index) => {
                                            return (
                                                <Col key={index} xs={12} sm={6} md={4} lg={3}>
                                                    <div className="card-item">
                                                        <img src={Firebase_URL + value.filePath} alt={value.fileName} onClick={() => { handleClickOpenModal(); setImageFullScreen(value.filePath) }} />
                                                        <IconButton
                                                            className="btn-remove"
                                                            aria-label="close"
                                                            onClick={() => { setIsImageDelete(true); setImageID(value.id); }}
                                                        >
                                                            <CloseIcon />
                                                        </IconButton>
                                                    </div>
                                                </Col>
                                            )
                                        })
                                        :
                                        <div></div>
                                    }
                                </Row>
                                <Row className="mb-3">
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
                                </Row>
                            </Form>
                        )}
                    </Formik>
                </div>
                <Dialog
                    fullWidth={true}
                    maxWidth={'md'}
                    open={openModal}
                    onClose={onCloseModal}
                >
                    <DialogTitle style={{ alignSelf: 'end' }}>
                        <IconButton
                            aria-label="close"
                            onClick={onCloseModal}
                        >
                            <CloseIcon />
                        </IconButton>
                    </DialogTitle>
                    <DialogContent>
                        <img src={Firebase_URL + imageFullScreen} alt='image-fullscreen' />
                    </DialogContent>
                </Dialog>
            </Fragment >
            :
            <Fragment>
                <PageLoad />
            </Fragment>
    )
}

export default ManageBikeUpdate;