import React, { Fragment, useEffect, useState } from 'react';

// Library
import Cookies from 'universal-cookie';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { useNavigate } from 'react-router-dom';

// Source
// API
import { AxiosInstance } from "../../api/AxiosClient";
import { BikeAPI } from '../../api/EndPoint';

//Component
import { TableBikeList } from '../../components/Table/TableBikeList';
import SortBar from "../../components/Navbar/SortBar";
import { PaginationCustom } from '../../components/Table/Pagination';
import { PageLoad } from '../../components/Base/PageLoad';

// Redux
import { useSelector, useDispatch } from "react-redux";
import { reduxAction } from "../../redux-store/redux/redux.slice";
import { reduxPaginationAction } from '../../redux-store/redux/reduxPagination.slice';
import { reduxAuthenticateAction } from "../../redux-store/redux/reduxAuthenticate.slice";


const cookies = new Cookies();

const SortBy = [
    { value: "id", label: "Sort by ID", key: "1" },
    { value: "name", label: "Sort by name", key: "2" },
    { value: "bikeManualId", label: "Sort by manual ID", key: "3" },
    { value: "hiredNumber", label: "Sort by hired number", key: "5" },
    { value: "bikeCategoryId", label: "Sort by category", key: "6" },
    { value: "bikeColorId", label: "Sort by color", key: "7" },
    { value: "bikeManufacturerId", label: "Sort by manufacturer", key: "8" },
    { value: "status", label: "Sort by status", key: "9" },
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
    await AxiosInstance.post(BikeAPI.getBikePagination, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        var listData = res.data.data.content.map((data) => {
            return {
                id: data.id,
                bikeManualId: data.bikeManualId,
                name: data.name,
                bikeCategoryName: data.bikeCategoryName,
                hiredNumber: data.hiredNumber,
                status: data.status
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

function ManageBikeList() {

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(false));
        dispatch(reduxAction.setSortType('DESC'));
        dispatch(reduxAction.setSortBy('id'));
        setLoadingPage(false);
    }

    // USE STATE
    // Table variables
    const tableTitleList = [
        { name: 'ID', width: '10%' },
        { name: 'MANUAL ID', width: '10%' },
        { name: 'NAME', width: '20%' },
        { name: 'CATEGORY', width: '20%' },
        { name: 'HIRED NUMBER', width: '15%' },
        { name: 'STATUS', width: '15%' }
    ]

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
    const [dataID, setDataID] = useState(0);


    // Render page
    const navigate = useNavigate();


    // USE EFFECT
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

    useEffect(() => {
        if (dataID !== 0) {
            navigate('/manage-bike/bike/' + dataID);
        }
    }, [dataID])


    // Table - Pagination
    let tablePagination;
    if (listData.length > 0) {
        tablePagination = <div className='table-pagination'>
            <TableBikeList
                tableTitleList={tableTitleList}
                listData={listData}
                setDataID={setDataID}
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
                    <h2 className="text-center">Management Bike List</h2>
                    <SortBar SortBy={SortBy} />
                    <div className='table-header'>
                        <Row>
                            <Col lg={6} xs={6}><label style={{ fontSize: '36px' }}>Bike List</label></Col>
                            <Col lg={6} xs={6}><button className="btn btn-primary" style={{ float: "right", marginTop: '10px', width: '5rem' }}
                                onClick={() => navigate('/manage-bike/bike-create')} >Create</button></Col>
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
export default ManageBikeList;