import React, { useState, useEffect, Fragment } from "react";
import { AxiosInstance } from "../../api/AxiosClient";
import { Banner } from "../../components/Banner/Banner";
import { ListSwiper } from "../../components/Swiper/Swiper";
import { PublicAPI } from "../../api/EndPoint";
import CircularProgress from '@mui/material/CircularProgress';
import Cookies from 'universal-cookie';
import { PageLoad } from '../../components/Base/PageLoad';

// Redux
import { useDispatch } from "react-redux";
import { reduxAuthenticateAction } from "../../redux-store/redux/reduxAuthenticate.slice";

const handleGetListBike = async (categoryId, setListManual, setListAutomatic, setLoadingData) => {
    const body = {
        searchKey: null,
        categoryId: categoryId,
        page: 1,
        limit: 7,
        sortBy: "name",
        sortType: "ASC",
    };
    await AxiosInstance.post(PublicAPI.getBikePagination, body, {
        headers: {}
    })
        .then((res) => {
            var listBike = res.data.data.content.map((data) => {
                return {
                    id: data.id,
                    name: data.name,
                    bikeCategory: data.categoryName,
                    price: data.price,
                    filePath: data.imageList[0].filePath,
                    fileName: data.imageList[0].fileName,
                }
            })
            if (categoryId === 1) {
                setListAutomatic(listBike)
            } else {
                setListManual(listBike)
            }
            setLoadingData(false)
        })
        .catch((error) => {
            if (error && error.response) {
                console.log("Error: ", error);
            }
        });
}

function Home() {

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(true));
        setLoadingPage(false);
    }

    // USE STATE
    const [listManual, setListManual] = useState([]);
    const [listAutomatic, setListAutomatic] = useState([]);
    const [loadingData, setLoadingData] = useState(true);

    // USE EFFECT
    useEffect(() => {
        if (loadingData) {
            handleGetListBike(1, setListManual, setListAutomatic, setLoadingData);
            handleGetListBike(2, setListManual, setListAutomatic, setLoadingData);
        }
    }, [loadingData])

    return (
        !loadingData ?
            <Fragment>
                <Banner />
                <div className="container">
                    <h2 className="text-center">Manual Transmission Motorcycle</h2>
                    {loadingData ?
                        <div className="circular_progress">
                            <CircularProgress />
                        </div> :
                        <ListSwiper listBike={listManual} />
                    }
                    <h2 className="text-center">Automatic Transmission Motorcycle</h2>
                    {loadingData ?
                        <div className="circular_progress">
                            <CircularProgress />
                        </div> :
                        <ListSwiper listBike={listAutomatic} />
                    }
                </div>
            </Fragment>
            :
            <Fragment>
                <PageLoad />
            </Fragment>
    )
}

export default Home;