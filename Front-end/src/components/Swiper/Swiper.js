import React from "react";
import { Link } from "react-router-dom";
import { Swiper, SwiperSlide } from "swiper/react"
import { Pagination, Navigation } from "swiper";
import { Firebase_URL } from "../../api/EndPoint";
import Badge from 'react-bootstrap/Badge';
import { GetFormattedCurrency } from "../../function/CurrencyFormat";

export const ListSwiper = ({ listBike, ...props }) => {
    return (
        <div className="listSwiper">
            <Swiper
                breakpoints={{
                    // when window width is >= 270px
                    270: {
                        slidesPerView: 1,
                    },
                    // when window width is >= 768px
                    768: {
                        slidesPerView: 2,
                    },
                    // when window width is >= 992px
                    992: {
                        slidesPerView: 3,
                    },
                    // when window width is >= 1200px
                    1200: {
                        slidesPerView: 4,
                    },
                }}
                slidesPerView={4}
                autoHeight={true}
                loop={true}
                pagination={{
                    clickable: true,
                }}
                navigation={true}
                modules={[Pagination, Navigation]}
                className="mySwiper"
            >
                {listBike.map((data) => {
                    return (
                        <SwiperSlide key={data.id}>
                            <Link className="card-item" to={`/bike/${data.id}`}>
                                <img src={Firebase_URL + data.filePath} alt={data.fileName} />
                                <label className="bikeName">{data.name}</label>
                                <div className="bikeTag">
                                    <Badge>{data.bikeCategory}</Badge>
                                </div>
                                <p className="bikePrice">Price: <span>{GetFormattedCurrency(data.price)}</span></p>
                            </Link>
                        </SwiperSlide>
                    )
                })}
            </Swiper>
        </div>
    )
}