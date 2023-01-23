//https://enfanthoon.tistory.com/166
//https://swiperjs.com/react
import { Swiper, SwiperSlide } from 'swiper/react';
import SwiperCore, { Navigation, Pagination, Autoplay } from 'swiper';
import React from 'react';

import 'swiper/swiper.scss';
import '../../styles/common/CustomSwipe.scss'
// import 'swiper/components/navigation/navigation.scss';
// import 'swiper/components/pagination/pagination.scss';
import coatImg from "../../img/coat.png"
import styles from "../../styles/post/PostDetail.module.css";
import tradeEx from "../../img/tradeEx.jpeg";


interface imageProps{
    imagesList : [{
        id: number,
        imgPath : string,
    }]
}

const CustomSwiper = (imageProps) => {

    // console.log(imageProps.imageList["id"])
    if(!imageProps.imageList)
    {
        return null;
    }


    // imageProps.imageList.map((image, index) => {
    //     console.log(image);
    // });


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
                {imageProps.imageList.map((image, index) => (

                    ((image.imgPath != null && image.imgPath.includes("https://s3.ap-northeast-2.amazonaws.com")) ?
                        <>
                            <div key = {index}>
                                <SwiperSlide key={index}>
                                    <img className="image" src={image.imgPath}></img>
                                </SwiperSlide>
                            </div>
                        </>
                            :
                            <SwiperSlide key={index}>
                               <h6>imgurl이 서버의 이미지가 아닙니다 <br/>(postman으로 업로드해서 발생하는 화면)<br/>"https://s3.ap-northeast-2.amazonaws.com"의 경로여야함 </h6>
                            </SwiperSlide>
                    )

                    // <div key = {index}>
                    // <SwiperSlide key={index}>
                    // <img className="image" src={image.imgPath}></img>
                    // </SwiperSlide>
                    // </div>
                ))}
                {/*<SwiperSlide>*/}
                {/*    /!*<img src={imageProps.imageList[0].imgPath}></img>*!/*/}
                {/*</SwiperSlide>*/}
                {/*<SwiperSlide>Slide 2</SwiperSlide>*/}
                {/*<SwiperSlide>Slide 3</SwiperSlide>*/}
                {/*<SwiperSlide>Slide 4</SwiperSlide>*/}
                {/*<SwiperSlide>Slide 5</SwiperSlide>*/}
                {/*<SwiperSlide>Slide 6</SwiperSlide>*/}
                {/*<SwiperSlide>Slide 7</SwiperSlide>*/}
                {/*<SwiperSlide>Slide 8</SwiperSlide>*/}
            </Swiper>
        </>
    );
};
export default CustomSwiper;