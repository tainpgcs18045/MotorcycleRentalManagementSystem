import React, { useState, useEffect, Fragment } from "react";

// Library
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
// import Badge from 'react-bootstrap/Badge';
import { Badge, Button, IconButton } from '@mui/material';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import CircularProgress from '@mui/material/CircularProgress';
import Cookies from 'universal-cookie';
import { useNavigate } from "react-router-dom";

// Fire base
import { AxiosInstance } from "../../api/AxiosClient";
import SortBar from "../../components/Navbar/SortBar";
import { Firebase_URL, BikeAPI, OrderAPI } from "../../api/EndPoint";

// Redux
import { useSelector, useDispatch } from "react-redux";
import { reduxAction } from "../../redux-store/redux/redux.slice";
import { reduxPaginationAction } from '../../redux-store/redux/reduxPagination.slice';
import { reduxAuthenticateAction } from "../../redux-store/redux/reduxAuthenticate.slice";

//Component
import { PageLoad } from "../../components/Base/PageLoad";
import { PaginationCustom } from "../../components/Table/Pagination";

const SortBy = [
    { value: "id", label: "Sort by ID", key: "1" },
    { value: "name", label: "Sort by name", key: "2" },
    { value: "bikeManualId", label: "Sort by bike manual ID", key: "3" },
    { value: "hiredNumber", label: "Sort by hired number", key: "4" },
    { value: "color", label: "Sort by color", key: "5" },
    { value: "manufacturer", label: "Sort by manufacturer", key: "6" }
];

const cookies = new Cookies();

// FUNCTION
// CALL API
const handleGetListBike = async (
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
        sortType: reduxFilter.reduxSortType,
        isInCart: true
    };
    await AxiosInstance.post(BikeAPI.getBikePagination, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        var listData = res.data.data.content.map((data) => {
            return {
                id: data.id,
                name: data.name,
                bikeManualId: data.bikeManualId,
                hiredNumber: data.hiredNumber,
                orderId: data.orderId,
                filePath: data.imageList[0].filePath,
                fileName: data.imageList[0].fileName,
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
}

const handleCreateCart = async (bikeId, setCarNumber) => {
    const body = {
        bikeId: bikeId
    };
    await AxiosInstance.post(OrderAPI.cartAddBike, body, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        if (res.data.code === 1) {
            if (res.data.data !== null) {
                setCarNumber(res.data.data);
            }
        }
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
    });
}

const handleBikeNumberInCart = async (setCarNumber) => {
    await AxiosInstance.get(OrderAPI.cartGetBikeNumber, {
        headers: { Authorization: `Bearer ${cookies.get('accessToken')}` }
    }).then((res) => {
        if (res.data.code === 1) {
            setCarNumber(res.data.data)
        }
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
    });
}

function CreateCart(props) {

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(false));
        setLoadingPage(false);
    }

    //Navigate
    const navigate = useNavigate();

    // USESTATE
    // LIST DATA
    const [listData, setListData] = useState([]);
    const [loadingData, setLoadingData] = useState(true);
    const [cartNumber, setCarNumber] = useState(0);

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

    // useEffect
    // Table loading - page load
    useEffect(() => {
        if (loadingData === true) {
            handleGetListBike(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
            handleBikeNumberInCart(setCarNumber);
        }
    }, [loadingData])

    // Table loading filter submit
    useEffect(() => {
        if (reduxIsSubmitting === true) {
            if (reduxPagination.reduxPage === 1) {
                handleGetListBike(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
            } else {
                dispatch(reduxPaginationAction.updatePage(1));
            }
            dispatch(reduxAction.setIsSubmitting({ isSubmitting: false }));
        }
    }, [reduxIsSubmitting])

    // Table loading pagination - change page
    useEffect(() => {
        handleGetListBike(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
    }, [reduxPagination.reduxPage])

    // Table loading pagination - change page
    useEffect(() => {
        handleGetListBike(setListData, setLoadingData, setTotalPages, reduxFilter, reduxPagination);
    }, [cartNumber])

    return (
        !loadingData ?
            <Fragment>
                <div className="container">
                    <Row>
                        <Col lg={12}>
                            <h2 className="text-center">Add Bike To Cart</h2>
                            <Row>
                                <Col lg={11} xs={11}>
                                    <SortBar SortBy={SortBy} />
                                </Col>
                                <Col lg={1} xs={1} style={{ alignSelf: 'center', textAlign: 'center' }}>
                                    <div className="view-cart">
                                        <IconButton aria-label="cart" onClick={() => navigate('/manage-order/order-create')}>
                                            <Badge badgeContent={cartNumber} color="secondary" max={999} showZero>
                                                <ShoppingCartIcon />
                                            </Badge>
                                        </IconButton>
                                    </div>
                                </Col>
                            </Row>
                            {loadingData ?
                                <div className="circular_progress">
                                    <CircularProgress />
                                </div> :
                                <Row>
                                    {listData.map((data, index) => {
                                        return (
                                            <Col key={index} className="column" xs={12} sm={6} md={4} lg={3} style={{ maxHeight: "30rem" }}>
                                                <div className="card-item">
                                                    <img src={Firebase_URL + data.filePath} alt={data.fileName} />
                                                    <label className="bikeName">{data.name}</label>
                                                    <p className="bikeManualId">Manual ID: <span>{data.bikeManualId}</span></p>
                                                    <Row>
                                                        <Col lg={6} xs={6} style={{ alignSelf: 'center' }}>
                                                            <p className="bikeHiredNumber">
                                                                <AddShoppingCartIcon /> <span>{data.hiredNumber}</span> times
                                                            </p>
                                                        </Col>
                                                        <Col lg={6} xs={6}>
                                                            {data.orderId === null ?
                                                                <Button variant="contained" onClick={() => handleCreateCart(data.id, setCarNumber)}>Add to Cart</Button>
                                                                :
                                                                <Button variant="contained" disabled>Add to Cart</Button>
                                                            }
                                                        </Col>
                                                    </Row>
                                                </div>
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

export default CreateCart;