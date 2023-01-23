//https://enfanthoon.tistory.com/166
//https://swiperjs.com/react
import { Swiper, SwiperSlide } from 'swiper/react';
import SwiperCore, { Navigation, Pagination, Autoplay } from 'swiper';
import React from 'react';

import 'swiper/swiper.scss';
import '../../styles/common/CustomSwipe.scss'
// import 'swiper/components/navigation/navigation.scss';
// import 'swiper/components/pagination/pagination.scss';

const CustomSwiper = () => {


    SwiperCore.use([Navigation, Pagination, Autoplay]);

    return (
        <>
            <Swiper
                // style={}
                spaceBetween={50}
                slidesPerView={1}
                onSlideChange={() => console.log('slide change')}
                navigation
                pagination={{ clickable: true }}
                // loop={true}
                // autoplay={true}
            >
                <SwiperSlide>Slide 1</SwiperSlide>
                <SwiperSlide>Slide 2</SwiperSlide>
                <SwiperSlide>Slide 3</SwiperSlide>
                <SwiperSlide>Slide 4</SwiperSlide>
                <SwiperSlide>Slide 5</SwiperSlide>
                <SwiperSlide>Slide 6</SwiperSlide>
                <SwiperSlide>Slide 7</SwiperSlide>
                <SwiperSlide>Slide 8</SwiperSlide>
            </Swiper>
        </>
    );
};
export default CustomSwiper;