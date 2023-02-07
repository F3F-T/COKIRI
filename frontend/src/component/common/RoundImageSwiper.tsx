//https://enfanthoon.tistory.com/166
//https://swiperjs.com/react
import {Swiper, SwiperSlide} from 'swiper/react';
import SwiperCore, {Navigation, Pagination, Autoplay} from 'swiper';
import React from 'react';

import 'swiper/swiper.scss';
import '../../styles/scss/RoundImageSwiper.scss'
import styles from "../../styles/trade/PostContainer.module.css";
import {useNavigate} from "react-router-dom";
import Card from "../tradeCard/Card";


interface Image{
    imageList : string;
}



const RoundImageSwiper = (imageProps) => {

    const navigate = useNavigate();

    console.log(imageProps)


    SwiperCore.use([Navigation, Pagination, Autoplay]);

    const onClickPost = (post) => {
        console.log(post)
        console.log(post.id)
        navigate(`/post/${post.id}`)
    }

    const imageClick = (index) => {
        console.log(index)
    }

    if(!imageProps.imageList)
    {
        return null;
    }

    return (
        <>
            <Swiper
                // style={}
                spaceBetween={10}
    slidesPerGroup={4}
    loopFillGroupWithBlank={true}
    slidesPerView={4}
    onSlideChange={() => console.log('slide change')}
    navigation
    pagination={{clickable: true}}
    // loop={true}
    // autoplay={true}
    >
    <div className={styles.postContainer}>
        {
                imageProps.imageList.map((image,index) => (
                    <SwiperSlide>
                        <div className={styles.imageDiv}>
                            <img onClick={() => {imageClick(index)}} className={styles.img} src = {image}/>
                        </div>
                    </SwiperSlide>
                ))

}
    </div>
    </Swiper>
    </>
);
};
export default RoundImageSwiper;