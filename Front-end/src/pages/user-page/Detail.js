import React, { useState, useEffect, Fragment } from "react";
import { useParams } from "react-router-dom";
import { AxiosInstance } from "../../api/AxiosClient";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import CircularProgress from '@mui/material/CircularProgress';
import { Firebase_URL, PublicAPI } from "../../api/EndPoint";
import ImageGallery from "react-image-gallery";
import Badge from 'react-bootstrap/Badge';
import { PageLoad } from "../../components/Base/PageLoad";
import { GetFormattedCurrency } from "../../function/CurrencyFormat";
import { ListSwiper } from "../../components/Swiper/Swiper";
import Grid3x3Icon from '@mui/icons-material/Grid3x3';
import ColorLensIcon from '@mui/icons-material/ColorLens';
import FactoryIcon from '@mui/icons-material/Factory';
import RuleIcon from '@mui/icons-material/Rule';
import { TableBikeDetail } from "../../components/Table/TableBikeDetail";

// Redux
import { useDispatch } from "react-redux";
import { reduxAuthenticateAction } from "../../redux-store/redux/reduxAuthenticate.slice";


const handleBikeDetail = async (id, setBikeDetail, setListImage, setLoadingData, setListAutomatic, Firebase_URL) => {
    await AxiosInstance.get(PublicAPI.getBikeDetail + id, {
        headers: {}
    }).then((res) => {
        var bikeDetail = {
            name: res.data.data.name,
            bikeNo: res.data.data.bikeNo,
            bikeCategoryName: res.data.data.bikeCategoryName,
            bikeColor: res.data.data.bikeColor,
            bikeManufacturerName: res.data.data.bikeManufacturerName,
            status: res.data.data.status,
            price: res.data.data.price
        }
        var listImage = res.data.data.imageList
        var listLinkImage = []
        listImage.forEach(e => {
            var image = {
                original: Firebase_URL + e.filePath,
                thumbnail: Firebase_URL + e.filePath,
                originalHeight: '400px',
                originalWidth: '400px',
                originalClass: 'image-item-origin'
            }
            listLinkImage.push(image)
        });
        var listBike = res.data.data.listBike.map((data) => {
            return {
                id: data.id,
                name: data.name,
                bikeCategory: data.categoryName,
                price: data.price,
                filePath: data.imageList[0].filePath,
                fileName: data.imageList[0].fileName,
            }
        })
        setListAutomatic(listBike);
        setListImage(listLinkImage);
        setBikeDetail(bikeDetail);
        setLoadingData(false);
    }).catch((error) => {
        if (error && error.response) {
            console.log("Error: ", error);
        }
    });
}

const Detail = props => {

    // Show Public Navigation
    const dispatch = useDispatch();
    const [loadingPage, setLoadingPage] = useState(true);
    if (loadingPage === true) {
        dispatch(reduxAuthenticateAction.updateIsShowPublicNavBar(true));
        setLoadingPage(false);
    }

    let { id } = useParams()
    const [loadingData, setLoadingData] = useState(true);
    const [listImage, setListImage] = useState([]);
    const [bikeDetail, setBikeDetail] = useState({});
    const [listAutomatic, setListAutomatic] = useState([]);

    useEffect(() => {
        if (loadingData) {
            handleBikeDetail(id, setBikeDetail, setListImage, setLoadingData, setListAutomatic, Firebase_URL);
        }
    }, [loadingData])

    return (
        !loadingData ?
            <Fragment>
                <div className="container">
                    {loadingData ?
                        <div className="circular_progress circular_progress_detail">
                            <CircularProgress />
                        </div>
                        :
                        <div>
                            <Row style={{ marginBottom: "3%" }}>
                                <Col lg={6} xs={12}>
                                    <ImageGallery
                                        showPlayButton={false}
                                        thumbnailPosition={"right"}
                                        items={listImage}
                                        useBrowserFullscreen={false}
                                    />
                                </Col>
                                <Col lg={6} xs={12} className="public">
                                    <div className="detail-header">
                                        <h2 className="bikeName">{bikeDetail.name}</h2>
                                        <Badge className="bikeCategory">{bikeDetail.bikeCategoryName}</Badge>
                                    </div>
                                    <div className="detail-body">
                                        <Row>
                                            <Col lg={6} xs={6}><Grid3x3Icon className='body-icon' /><label className="body body-title">Bike No</label></Col>
                                            <Col lg={6} xs={6}><ColorLensIcon className='body-icon' /><label className="body body-title">Bike Color</label></Col>
                                            <Col lg={6} xs={6}><label className="body body-info">{bikeDetail.bikeNo}</label></Col>
                                            <Col lg={6} xs={6}><label className="body body-info">{bikeDetail.bikeColor}</label></Col>
                                            <Col lg={6} xs={6}><FactoryIcon className='body-icon' /><label className="body body-title">Bike Manufacturer</label></Col>
                                            <Col lg={6} xs={6}><RuleIcon className='body-icon' /><label className="body body-title">Status</label></Col>
                                            <Col lg={6} xs={6}><label className="body body-info">{bikeDetail.bikeManufacturerName}</label></Col>
                                            <Col lg={6} xs={6}>{bikeDetail.status === "AVAILABLE" ?
                                                <label className="body body-info" style={{ color: 'green' }}>{bikeDetail.status}</label> :
                                                <label className="body body-info" style={{ color: 'red' }}>{bikeDetail.status}</label>}
                                            </Col>
                                        </Row>
                                    </div>
                                    <div className="detail-footer">
                                        <h3 className="bikePrice">{GetFormattedCurrency(bikeDetail.price)}</h3>
                                    </div>
                                </Col>
                            </Row>
                            <div style={{ marginBottom: "3%" }}>
                                <label style={{ fontSize: '24px', marginBottom: '8px' }}>Rent Detail Table</label>
                                <p style={{ marginBottom: '16px' }}><span style={{ fontWeight: 600, color: '#ff4444' }}>Important note: </span>If you rent a motorcycle beyond the specified time, we will charge an extra fee as follows:</p>
                                <TableBikeDetail bikePrice={bikeDetail.price} />
                            </div>
                            <Row>
                                <Col lg={12} xs={12}>
                                    <h2 className="text-center">Relation Bike</h2>
                                    {loadingData ?
                                        <div className="circular_progress">
                                            <CircularProgress />
                                        </div> :
                                        <ListSwiper listBike={listAutomatic} />
                                    }
                                </Col>
                            </Row>
                        </div>
                    }
                </div>
            </Fragment>
            :
            <Fragment>
                <PageLoad />
            </Fragment>
    )
}

export default Detail;