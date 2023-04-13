import React, { useState, useEffect, Fragment } from "react";

// Library
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Badge from 'react-bootstrap/Badge';
import CircularProgress from '@mui/material/CircularProgress';
import { Link } from "react-router-dom";

// Fire base
import { AxiosInstance } from "../../api/AxiosClient";
import SortBar from "../../components/Navbar/SortBar";
import { Firebase_URL, PublicAPI } from "../../api/EndPoint";

// Redux
import { useSelector, useDispatch } from "react-redux";
import { reduxAction } from "../../redux-store/redux/redux.slice";
import { reduxPaginationAction } from '../../redux-store/redux/reduxPagination.slice';
import { reduxAuthenticateAction } from "../../redux-store/redux/reduxAuthenticate.slice";

//Component
import { PageLoad } from "../../components/Base/PageLoad";
import { PaginationCustom } from "../../components/Table/Pagination";
import { GetFormattedCurrency } from "../../function/CurrencyFormat";

const SortBy = [
    { value: "name", label: "Sort by name", key: "1" },
    { value: "color", label: "Sort by color", key: "2" },
    { value: "manufacturer", label: "Sort by manufacturer", key: "3" },
];


// FUNCTION
// CALL API
const handleGetListBike = async (
    categoryId,
    setListData,
    setLoadingData,
    setTotalPages,
    reduxFilter,
    reduxPagination
) => {
    const body = {
        searchKey: reduxFilter.reduxSearchKey,
        categoryId: categoryId,
        page: reduxPagination.reduxPage,
        limit: reduxPagination.reduxRowsPerPage,
        sortBy: reduxFilter.reduxSortBy,
        sortType: reduxFilter.reduxSortType
    };
    await AxiosInstance.post(PublicAPI.getBikePagination, body, {
        headers: {}
    }).then((res) => {
        var listData = res.data.data.content.map((data) => {
            return {
                id: data.id,
                name: data.name,
                bikeCategory: data.bikeCategoryName,
                price: data.price,
                filePath: data.imageList[0].filePath,
                fileName: data.imageList[0].fileName,
            }
        })
        setListData(listData)
        setTotalPages(res.data.data.totalPages)
        setTimeout(() => {
            setLoadingData(false)
        }, 500);
    })
        .catch((error) => {
            if (error && error.response) {
                console.log("Error: ", error);
            }
        });
}


// FUNCTION
// INTERNAL
const callGetListBikeByCategoryId = (categoryId, setCategoryId, setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination) => {
    if (categoryId === 1) {
        setCategoryId(1);
        handleGetListBike(categoryId, setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
    } else if (categoryId === 2) {
        setCategoryId(2);
        handleGetListBike(categoryId, setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
    } else {
        setCategoryId(null);
        handleGetListBike(null, setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
    }
}

const List = props => {

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(true));
        dispatch(reduxAction.setSortType('DESC'));
        dispatch(reduxAction.setSortBy('id'));
        setLoadingPage(false);
    }

    // USESTATE
    // LIST DATA
    const [listData, setListData] = useState([]);
    const [loadingData, setLoadingData] = useState(true);
    const [categoryId, setCategoryId] = useState(null);


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
        reduxRowsPerPage: useSelector((state) => state.reduxPagination.rowsPerPage) + 3
    }

    // USE EFFECT
    // Table loading pagination - change page
    useEffect(() => {
        setLoadingData(true)
        if (reduxPagination.reduxPage !== 1) {
            handleGetListBike(categoryId, setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
        } else {
            callGetListBikeByCategoryId(props.category, setCategoryId, setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
        }
    }, [reduxPagination.reduxPage])

    // USE EFFECT
    // Page Loading
    useEffect(() => {
        setLoadingData(true)
        dispatch(reduxPaginationAction.updatePage(1));
        callGetListBikeByCategoryId(props.category, setCategoryId, setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
    }, [props.category])

    // USE EFFECT
    // Table loading filter submit
    useEffect(() => {
        if (reduxIsSubmitting === true) {
            console.log("SUBMIT")
            if (reduxPagination.reduxPage === 1) {
                handleGetListBike(categoryId, setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
            } else {
                dispatch(reduxPaginationAction.updatePage(1));
            }
            dispatch(reduxAction.setIsSubmitting({ isSubmitting: false }));
        }
    }, [reduxIsSubmitting])

    return (
        !loadingData ?
            <Fragment>
                <div className="container">
                    <Row>
                        <Col lg={12}>

                            <h2 className="text-center">List Motorcycle</h2>
                            <SortBar SortBy={SortBy} />
                            {loadingData ?
                                <div className="circular_progress">
                                    <CircularProgress />
                                </div> :
                                <Row>
                                    {listData.map((data, index) => {
                                        return (
                                            <Col key={index} className="column" xs={12} sm={6} md={4} lg={3}>
                                                <Link className="card-item" to={`/bike/${data.id}`}>
                                                    <img src={Firebase_URL + data.filePath} alt={data.fileName} />
                                                    <label className="bikeName">{data.name}</label>
                                                    <div className="bikeTag">
                                                        <Badge bg="success">{data.bikeCategory}</Badge>
                                                    </div>
                                                    <p className="bikePrice">Price: <span>{GetFormattedCurrency(data.price)}</span></p>
                                                </Link>
                                            </Col>
                                        )
                                    })}
                                </Row>
                            }
                            <PaginationCustom
                                totalPages={totalPages}
                                isShowRowPerPage={false}
                            />
                        </Col>
                    </Row>
                </div>
            </Fragment>
            :
            <Fragment>
                <PageLoad />
            </Fragment>
    )
}

export default List;