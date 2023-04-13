import React, { Fragment, useEffect, useState } from 'react';

// Library
import Cookies from 'universal-cookie';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { useNavigate } from 'react-router-dom';
import dayjs from 'dayjs';

// Source
// API
import { AxiosInstance } from "../../api/AxiosClient";
import { OrderAPI } from '../../api/EndPoint';

//Component
import { TableOrderList } from '../../components/Table/TableOrderList';
import SortBarOrder from "../../components/Navbar/SortBarOrder";
import { PaginationCustom } from '../../components/Table/Pagination';
import { PageLoad } from '../../components/Base/PageLoad';
import { GetFormattedCurrency } from '../../function/CurrencyFormat';
import { GetFormattedDatetTime } from '../../function/DateTimeFormat';

// Redux
import { useSelector, useDispatch } from "react-redux";
import { reduxAction } from "../../redux-store/redux/redux.slice";
import { reduxPaginationAction } from '../../redux-store/redux/reduxPagination.slice';
import { reduxAuthenticateAction } from "../../redux-store/redux/reduxAuthenticate.slice";


const cookies = new Cookies();

const SortByExpected = [
    { value: "id", label: "Sort by ID", key: "1" },
    { value: "expectedStartDate", label: "Sort by expected start date", key: "2" },
    { value: "expectedEndDate", label: "Sort by expected end date", key: "3" },
    { value: "totalAmount", label: "Sort by total amount", key: "4" }
];

const SortByActual = [
    { value: "id", label: "Sort by ID", key: "1" },
    { value: "actualStartDate", label: "Sort by actual start date", key: "2" },
    { value: "actualEndDate", label: "Sort by actual end date", key: "3" },
    { value: "totalAmount", label: "Sort by total amount", key: "4" }
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
    reduxPagination,
    startDate,
    endDate,
) => {
    const body = {
        searchKey: reduxFilter.reduxSearchKey,
        page: reduxPagination.reduxPage,
        limit: reduxPagination.reduxRowsPerPage,
        sortBy: reduxFilter.reduxSortBy,
        sortType: reduxFilter.reduxSortType,
        status: reduxFilter.reduxSortByStatus,
        startDate: startDate,
        endDate: endDate
    };
    await AxiosInstance.post(OrderAPI.getPagination, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        var listData = res.data.data.content.map((data) => {
            var expectedStartDate = dayjs(data.expectedStartDate)
            var expectedEndDate = dayjs(data.expectedEndDate)
            var totalHours = expectedEndDate.diff(expectedStartDate, 'hour');
            if (data.status === "CLOSED") {
                return {
                    id: data.id,
                    actualStartDate: data.actualStartDate === null ? "N/A" : GetFormattedDatetTime(data.actualStartDate),
                    actualEndDate: data.actualEndDate === null ? "N/A" : GetFormattedDatetTime(data.actualEndDate),
                    totalHours: totalHours,
                    bikeNumber: data.bikeNumber,
                    totalAmount: GetFormattedCurrency(data.totalAmount),
                    status: data.status
                }
            } else {
                return {
                    id: data.id,
                    expectedStartDate: GetFormattedDatetTime(data.expectedStartDate),
                    expectedEndDate: GetFormattedDatetTime(data.expectedEndDate),
                    totalHours: totalHours,
                    bikeNumber: data.bikeNumber,
                    totalAmount: GetFormattedCurrency(data.totalAmount),
                    status: data.status
                }
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

function ManageOrderList() {

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(false));
        dispatch(reduxAction.setSortType('DESC'));
        dispatch(reduxAction.setSortBy('id'));
        dispatch(reduxAction.setSortByStatus('PENDING'))
        setLoadingPage(false);
    }

    // USE STATE
    // Table variables
    const tableTitleList = [
        { name: 'ID', width: '5%' },
        { name: '*START DATE', width: '15%' },
        { name: '*END DATE', width: '15%' },
        { name: 'HIRING HOURS', width: '15%' },
        { name: 'TOTAL BIKES', width: '10%' },
        { name: 'TOTAL AMOUNT', width: '15%' },
        { name: 'STATUS', width: '10%' },
    ]

    // Redux - Filter form
    let reduxFilter = {
        reduxSearchKey: useSelector((state) => state.redux.searchKey),
        reduxSortBy: useSelector((state) => state.redux.sortBy),
        reduxSortType: useSelector((state) => state.redux.sortType),
        reduxSortByStatus: useSelector((state) => state.redux.status),
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

    // Search By Date
    var now = dayjs()
    const [startDate, setStartDate] = useState(now.startOf('year'));
    const [endDate, setEndDate] = useState(now.endOf('year'));


    // USE EFFECT
    // Table loading - page load
    useEffect(() => {
        if (loadingData === true) {
            handleGetDataPagination(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination, startDate, endDate);
        }
    }, [loadingData])

    // Table loading filter submit
    useEffect(() => {
        if (reduxIsSubmitting === true) {
            if (reduxPagination.reduxPage === 1) {
                handleGetDataPagination(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination, startDate, endDate);
            } else {
                dispatch(reduxPaginationAction.updatePage(1));
            }
            dispatch(reduxAction.setIsSubmitting({ isSubmitting: false }));
        }
    }, [reduxIsSubmitting])

    // Table loading pagination - change page
    useEffect(() => {
        handleGetDataPagination(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination, startDate, endDate);
    }, [reduxPagination.reduxPage])

    // Table loading pagination - change row per page -> call above useEffect
    useEffect(() => {
        if (reduxPagination.reduxPage === 1) {
            handleGetDataPagination(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination, startDate, endDate);
        } else {
            dispatch(reduxPaginationAction.updatePage(1));
        }
    }, [reduxPagination.reduxRowsPerPage])

    useEffect(() => {
        if (dataID !== 0) {
            navigate('/manage-order/order/' + dataID);
        }
    }, [dataID])


    // Table - Pagination
    let tablePagination;
    if (listData.length > 0) {
        tablePagination = <div className='table-pagination'>
            <TableOrderList
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
                    <h2 className="text-center">Management Order List</h2>
                    {reduxFilter.reduxSortByStatus === "CLOSED" ?
                        <SortBarOrder
                            SortBy={SortByActual}
                            startDate={startDate}
                            endDate={endDate}
                            setStartDate={setStartDate}
                            setEndDate={setEndDate}
                        />
                        :
                        <SortBarOrder
                            SortBy={SortByExpected}
                            startDate={startDate}
                            endDate={endDate}
                            setStartDate={setStartDate}
                            setEndDate={setEndDate}
                        />
                    }
                    <div className='table-header'>
                        <Row>
                            <Col lg={6} xs={6}><label style={{ fontSize: '36px' }}>Order List</label></Col>
                            <Col lg={6} xs={6}><button className="btn btn-primary" style={{ float: "right", marginTop: '10px' }}
                                onClick={() => navigate('/manage-order/cart-create')} >Create Order</button></Col>
                        </Row>
                    </div>
                    <div className='table-note'>
                        <label className='form-label' style={{ color: "red", fontStyle: "italic" }}>* CLOSED order will show ACTUAL DATE - OTHER STATUS order will show EXPECTED DATE</label>
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
export default ManageOrderList;