import React, { Fragment, useEffect, useState } from 'react';

// Library
import Cookies from 'universal-cookie';

import { Formik, Form } from 'formik';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Grid3x3Icon from '@mui/icons-material/Grid3x3';
import CategoryIcon from '@mui/icons-material/Category';
import DateRangeIcon from '@mui/icons-material/DateRange';
import PersonIcon from '@mui/icons-material/Person';
import UpdateIcon from '@mui/icons-material/Update';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';

// Source
// API
import { AxiosInstance } from "../../api/AxiosClient";
import { CategoryAPI } from '../../api/EndPoint';

//Component
import { TableCRUD } from '../../components/Table/TableCRUD';
import SortBar from "../../components/Navbar/SortBar";
import { Popup } from '../../components/Modal/Popup';
import { TextFieldCustom } from '../../components/Form/TextFieldCustom';
import { AlertMessage } from '../../components/Modal/AlertMessage';
import { GetFormattedDate } from "../../function/DateFormat";
import { PaginationCustom } from '../../components/Table/Pagination';
import { PageLoad } from '../../components/Base/PageLoad';
import { GetFormattedCurrency, ParseCurrencyToNumber, InputNumber } from '../../function/CurrencyFormat';

// Redux
import { useSelector, useDispatch } from "react-redux";
import { reduxAction } from "../../redux-store/redux/redux.slice";
import { reduxPaginationAction } from '../../redux-store/redux/reduxPagination.slice';
import { reduxAuthenticateAction } from "../../redux-store/redux/reduxAuthenticate.slice";

const cookies = new Cookies();

const SortBy = [
    { value: "id", label: "Sort by ID", key: "1" },
    { value: "name", label: "Sort by name", key: "2" },
    { value: "price", label: "Sort by price", key: "3" },
];

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

const handleGetDataPagination = async (
    setListData,
    setLoadingData,
    setTotalPages,
    reduxFilter,
    reduxPagination
) => {
    const body = {
        searchKey: reduxFilter.reduxSearchKey,
        page: reduxPagination.reduxPage,
        limit: reduxPagination.reduxRowsPerPage,
        sortBy: reduxFilter.reduxSortBy,
        sortType: reduxFilter.reduxSortType
    };
    await AxiosInstance.post(CategoryAPI.getPagination, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        var listData = res.data.data.content.map((data) => {
            return {
                id: data.id,
                name: data.name,
                price: GetFormattedCurrency(data.price)
            }
        })
        setListData(listData)
        setTotalPages(res.data.data.totalPages)
        setTimeout(() => {
            setLoadingData(false)
        }, 500);
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
    });
};

const handleGetDataById = async (dataID, setLineItem) => {
    await AxiosInstance.get(CategoryAPI.getById + dataID, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        if (res.data.code === 1) {
            setLineItem(res.data.data);
        }
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
    });

}

const handleCreateData = async (
    values,
    setAlert,
    setLoadingData,
    setShowCloseButton
) => {
    const body = {
        name: values.name,
        price: ParseCurrencyToNumber(values.price),
    };
    await AxiosInstance.post(CategoryAPI.create, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        if (res.data.code === 1) {
            showAlert(setAlert, res.data.message, true);
            setLoadingData(true)
            setShowCloseButton(true);
        } else {
            showAlert(setAlert, res.data.message, false);
        }
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
        showAlert(setAlert, error, false);
    });

}

const handleUpdateData = async (
    values,
    dataID,
    setAlert,
    setLoadingData,
    setShowCloseButton
) => {
    const body = {
        name: values.name,
        price: ParseCurrencyToNumber(values.price),
    };
    await AxiosInstance.post(CategoryAPI.update + dataID, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        if (res.data.code === 1) {
            showAlert(setAlert, res.data.message, true);
            setLoadingData(true)
            setShowCloseButton(true);
        } else {
            showAlert(setAlert, res.data.message, false);
        }
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
        showAlert(setAlert, error, false);
    });

}

const handleDeleteData = async (
    dataID,
    setAlert,
    setLoadingData,
    setShowCloseButton
) => {
    await AxiosInstance.post(CategoryAPI.delete + dataID, {}, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        if (res.data.code === 1) {
            showAlert(setAlert, res.data.message, true);
            setLoadingData(true)
        } else {
            showAlert(setAlert, res.data.message, false);
        }
        setShowCloseButton(true);
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
        showAlert(setAlert, error, false);
    });

}

function ManageBikeCategory() {

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(false));
        dispatch(reduxAction.setSortType('DESC'));
        dispatch(reduxAction.setSortBy('id'));
        setLoadingPage(false);
    }

    // Table variables
    const tableTitleList = [
        { name: 'ID', width: '10%' },
        { name: 'NAME', width: '20%' },
        { name: 'PRICE', width: '20%' },
    ]

    // Formik variables
    const initialValues = {
        name: "",
        price: 0,
    };

    // Redux - Filter form
    let reduxFilter = {
        reduxSearchKey: useSelector((state) => state.redux.searchKey),
        reduxSortBy: useSelector((state) => state.redux.sortBy),
        reduxSortType: useSelector((state) => state.redux.sortType),
    }
    const reduxIsSubmitting = useSelector((state) => state.redux.isSubmitting);

    // Redux - Pagination
    const [totalPages, setTotalPages] = useState(1);
    let reduxPagination = {
        reduxPage: useSelector((state) => state.reduxPagination.page),
        reduxRowsPerPage: useSelector((state) => state.reduxPagination.rowsPerPage)
    }

    // Table useState
    const [loadingData, setLoadingData] = useState(true);
    const [listData, setListData] = useState([]);

    // Popup useState
    const [dataID, setDataID] = useState(0);
    const [lineItem, setLineItem] = useState({});
    const [showPopup, setShowPopup] = useState(false);
    const [titlePopup, setTitlePopup] = useState("");
    const [showCloseButton, setShowCloseButton] = useState(false);
    const [isDelete, setIsDelete] = useState(false);
    const [isUpdate, setIsUpdate] = useState(false);
    const [alert, setAlert] = useState({
        alertShow: false,
        alertStatus: "success",
        alertMessage: "",
    })

    // useEffect
    // Table loading - page load
    useEffect(() => {
        if (loadingData === true) {
            handleGetDataPagination(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
        }
    }, [loadingData])

    // Table loading filter submit
    useEffect(() => {
        if (reduxIsSubmitting === true) {
            if (reduxPagination.reduxPage === 1) {
                handleGetDataPagination(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
            } else {
                dispatch(reduxPaginationAction.updatePage(1));
            }
            dispatch(reduxAction.setIsSubmitting({ isSubmitting: false }));
        }
    }, [reduxIsSubmitting])

    // Table loading pagination - change page
    useEffect(() => {
        handleGetDataPagination(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
    }, [reduxPagination.reduxPage])


    // Table loading pagination - change row per page -> call above useEffect
    useEffect(() => {
        if (reduxPagination.reduxPage === 1) {
            handleGetDataPagination(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
        } else {
            dispatch(reduxPaginationAction.updatePage(1));
        }
    }, [reduxPagination.reduxRowsPerPage])

    // Trigger Get Data by ID API
    useEffect(() => {
        if (isDelete === false && dataID !== 0) {
            handleGetDataById(dataID, setLineItem);
        }
    }, [isDelete, dataID])


    // Update initialValues
    if (isUpdate === true && Object.keys(lineItem).length !== 0) {
        initialValues.name = lineItem.name;
        initialValues.price = GetFormattedCurrency(lineItem.price);
    }

    // Popup Interface
    let popupTitle;
    if (titlePopup === "Create") {
        popupTitle = <Popup showPopup={showPopup} title={"Create"} child={
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
                                dispatch(reduxPaginationAction.updatePage(1));
                            }}>Close</button>
                    </div>
                </ Fragment>
                :
                <Fragment>
                    <AlertMessage
                        isShow={alert.alertShow}
                        message={alert.alertMessage}
                        status={alert.alertStatus}
                    />
                    <Formik
                        initialValues={initialValues}
                        onSubmit={(values) => {
                            handleCreateData(
                                values,
                                setAlert,
                                setLoadingData,
                                setShowCloseButton
                            );
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
                                <TextFieldCustom
                                    label={"Name"}
                                    name={"name"}
                                    type={"text"}
                                    placeholder={"Enter the category name"}
                                />
                                <TextFieldCustom
                                    label={"Price"}
                                    name={"price"}
                                    type={"number"}
                                    onWheel={(e) => e.target.blur()}
                                    placeholder={"Enter the category price"}
                                    onChange={(event) => {
                                        let value = event.target.value;
                                        let decimalValue = ParseCurrencyToNumber(InputNumber(value))
                                        setFieldValue("price", GetFormattedCurrency(decimalValue));
                                    }}
                                />
                                <div className="popup-button">
                                    <button className="btn btn-secondary btn-cancel"
                                        onClick={() => {
                                            setShowPopup(false);
                                            setAlert({ alertShow: false })
                                        }}>Cancel</button>
                                    <button className="btn btn-primary btn-action" type="submit">{titlePopup}</button>
                                </div>
                            </Form>
                        )}
                    </Formik>
                </Fragment>
        } />
    } else if (titlePopup === "Update") {
        popupTitle = <Popup showPopup={showPopup} setShowPopup={setShowPopup} title={"Update"} child={
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
                                setDataID(0);
                                setIsUpdate(false)
                            }}>Close</button>
                    </div>
                </ Fragment>
                :
                <Fragment>
                    <AlertMessage
                        isShow={alert.alertShow}
                        message={alert.alertMessage}
                        status={alert.alertStatus}
                    />
                    <Formik
                        enableReinitialize
                        initialValues={initialValues}
                        onSubmit={(values) => {
                            handleUpdateData(
                                values,
                                dataID,
                                setAlert,
                                setLoadingData,
                                setShowCloseButton
                            );
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
                                <TextFieldCustom
                                    label={"Name"}
                                    name={"name"}
                                    type={"text"}
                                    placeholder={"Enter the category name"}
                                    disabled={true}
                                />
                                <TextFieldCustom
                                    label={"Price"}
                                    name={"price"}
                                    type={"text"}
                                    onWheel={(e) => e.target.blur()}
                                    placeholder={"Enter the category price"}
                                    onChange={(event) => {
                                        let value = event.target.value;
                                        let decimalValue = ParseCurrencyToNumber(InputNumber(value))
                                        setFieldValue("price", GetFormattedCurrency(decimalValue));
                                    }}
                                />
                                <div className="popup-button">
                                    <button className="btn btn-secondary btn-cancel"
                                        onClick={() => {
                                            setShowPopup(false);
                                            setAlert({ alertShow: false });
                                            setDataID(0); setIsUpdate(false)
                                        }}>Cancel</button>
                                    <button className="btn btn-primary btn-action" type="submit">{titlePopup}</button>
                                </div>
                            </Form>
                        )}
                    </Formik>
                </Fragment>
        } />
    } else if (titlePopup === "View") {
        popupTitle = <Popup showPopup={showPopup} setShowPopup={setShowPopup} title={"View"} child={
            <Fragment>
                <div className='popup-view-container'>
                    <div className="popup-view-body">
                        <Row>
                            <Col lg={6} xs={6}><Grid3x3Icon className='body-icon' /><label className="body-title">Category Id</label></Col>
                            <Col lg={6} xs={6}><CategoryIcon className='body-icon' /><label className="body-title">Category Name</label></Col>
                            <Col lg={6} xs={6}><p className='body-detail'>{lineItem.id}</p></Col>
                            <Col lg={6} xs={6}><p className='body-detail'>{lineItem.name}</p></Col>
                            <Col lg={6} xs={6}><DateRangeIcon className='body-icon' /><label className="body-title">Create Date</label></Col>
                            <Col lg={6} xs={6}><PersonIcon className='body-icon' /><label className="body-title">Create User</label></Col>
                            <Col lg={6} xs={6}><p className='body-detail'>{GetFormattedDate(lineItem.createdDate)}</p></Col>
                            <Col lg={6} xs={6}><p className='body-detail'>{lineItem.createdUser}</p></Col>
                            <Col lg={6} xs={6}><UpdateIcon className='body-icon' /><label className="body-title">Modified Date</label></Col>
                            <Col lg={6} xs={6}><ManageAccountsIcon className='body-icon' /><label className="body-title">Modified User</label></Col>
                            <Col lg={6} xs={6}><p className='body-detail'>{lineItem.modifiedDate === null ? "N/A" : GetFormattedDate(lineItem.modifiedDate)}</p></Col>
                            <Col lg={6} xs={6}><p className='body-detail'>{lineItem.modifiedUser === null ? "N/A" : lineItem.modifiedUser}</p></Col>
                            <Col lg={12} xs={12}><AttachMoneyIcon className='body-icon' /><label className="body-title">Price</label></Col>
                            <Col lg={12} xs={12}><p className='body-detail'>{GetFormattedCurrency(lineItem.price)}</p></Col>
                        </Row>
                    </div>
                    <div className="popup-view-footer">
                        <div className="popup-button">
                            <button className="btn btn-secondary btn-cancel-view"
                                onClick={() => {
                                    setShowPopup(false);
                                    setDataID(0)
                                }}>Cancel</button>
                        </div>
                    </div>
                </div>
            </Fragment >
        } />
    } else if (titlePopup === "Delete") {
        popupTitle = <Popup showPopup={showPopup} setShowPopup={setShowPopup} title={"Delete ID " + dataID} child={
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
                                setDataID(0);
                                setIsDelete(false)
                            }}>Close</button>
                    </div>
                </ Fragment>
                :
                <Fragment>
                    <div className='popup-message text-center mb-3'>
                        <label>Do you really want to delete this record?</label>
                        <p>This process cannot be undone</p>
                    </div>
                    <div className="popup-button">
                        <button className="btn btn-secondary btn-cancel"
                            onClick={() => {
                                setShowPopup(false);
                                setDataID(0);
                                setIsDelete(false)
                            }}>Cancel</button>
                        <button className="btn btn-danger btn-action"
                            onClick={() => handleDeleteData(
                                dataID,
                                setAlert,
                                setLoadingData,
                                setShowCloseButton
                            )}>{titlePopup}</button>
                    </div>
                </Fragment >
        } />
    }


    // Table - Pagination
    let tablePagination;
    if (listData.length > 0) {
        tablePagination = <div className='table-pagination'>
            <TableCRUD
                tableTitleList={tableTitleList}
                listData={listData}
                setShowPopup={setShowPopup}
                setTitlePopup={setTitlePopup}
                setDataID={setDataID}
                setIsDelete={setIsDelete}
                setIsUpdate={setIsUpdate}
                isShowDeleteBtn={false}
            />
            <PaginationCustom
                totalPages={totalPages}
            />
        </div>
    } else {
        tablePagination = <div className='text-center'>
            <label style={{ fontSize: '36px' }}>No data found</label>
        </div>
    }

    return (
        !loadingData ?
            <Fragment>
                <div className='container'>
                    {popupTitle}
                    <h2 className="text-center">Management Bike Category</h2>
                    <SortBar SortBy={SortBy} />
                    <div className='table-header'>
                        <Row>
                            <Col lg={6} xs={6}><label style={{ fontSize: '36px' }}>Bike Category List</label></Col>
                            {/* <Col lg={6} xs={6}><button className="btn btn-primary" style={{ float: "right", marginTop: '10px', width: '5rem' }} onClick={() => { setShowPopup(true); setTitlePopup("Create") }}>Create</button></Col> */}
                        </Row>
                    </div>
                    {tablePagination}
                </div>
            </Fragment>
            :
            <Fragment>
                <PageLoad />
            </Fragment>
    )
}

export default ManageBikeCategory;