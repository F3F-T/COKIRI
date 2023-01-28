//https://enfanthoon.tistory.com/166
//https://swiperjs.com/react
import {Swiper, SwiperSlide} from 'swiper/react';
import SwiperCore, {Navigation, Pagination, Autoplay} from 'swiper';
import React from 'react';

import 'swiper/swiper.scss';
import '../../styles/scss/CustomSwipe.scss'


interface imageProps {
    imagesList: [{
        id: number,
        imgPath: string,
    }]
}

const CustomSwiper = (imageProps) => {

    // console.log(imageProps.imageList["id"])
    if (!imageProps.imageList) {
        return null;
    }

    SwiperCore.use([Navigation, Pagination, Autoplay]);

    return (
        <>
            <Swiper
                // style={}
                spaceBetween={50}
                slidesPerView={1}
                onSlideChange={() => console.log('slide change')}
                navigation
                pagination={{clickable: true}}
                // loop={true}
                // autoplay={true}
            >
                {imageProps.imageList.map((image, index) => (

                    ((image != null && image.includes("https://s3.ap-northeast-2.amazonaws.com")) ?
                            <SwiperSlide key={index}>
                                <img alt={"d"} key={image} className="image" src={image}></img>
                            </SwiperSlide>
                            :
                            <SwiperSlide key={index}>
                                <h6 key={index}>imgurl이 서버의 이미지가 아닙니다 <br/>(postman으로 업로드해서 발생하는 화면)<br/>"https://s3.ap-northeast-2.amazonaws.com"의
                                    경로여야함 </h6>
                            </SwiperSlide>
                )))}

            </Swiper>
        </>
    );
};
export default CustomSwiper;