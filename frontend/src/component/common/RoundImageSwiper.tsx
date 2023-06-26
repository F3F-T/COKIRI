//https://enfanthoon.tistory.com/166
//https://swiperjs.com/react
import { Swiper, SwiperSlide } from 'swiper/react';
import SwiperCore, { Autoplay, Navigation, Pagination } from 'swiper';
import React from 'react';

import 'swiper/swiper.scss';
import '../../styles/scss/RoundImageSwiper.scss';
import styles from '../../styles/trade/PostContainer.module.css';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { Rootstate } from '../../index';
import { storeCategory } from '../../store/categoryReducer';


interface Image {
  imageList: string;
}


const RoundImageSwiper = (imageProps) => {
  const store = useSelector((state: Rootstate) => state);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  console.log(imageProps);


  SwiperCore.use([Navigation, Pagination, Autoplay]);

  const onClickPost = (post) => {
    console.log(post);
    console.log(post.id);
    navigate(`/post/${post.id}`);
  };

  const imageClick = (index) => {
    let category = '도서';
    console.log(index);
    if (index === 0) {
      category = '도서';
    } else if (index === 1) {
      category = '식품';
    } else if (index === 2) {
      category = '티켓 / 교환권';
    } else if (index === 3) {
      category = '의류';
    } else if (index === 4) {
      category = '서비스';
    } else if (index === 5) {
      category = '유아동';
    } else if (index === 6) {
      category = '운동용품';
    } else if (index === 7) {
      category = '가구';
    }


    dispatch(storeCategory(category));
  };

  if (!imageProps.imageList) {
    return null;
  }

  return (
    <>
      <Swiper
        // style={}

        spaceBetween={10}
        slidesPerGroup={5}
        loopFillGroupWithBlank={true}
        slidesPerView={5}
        onSlideChange={() => console.log('slide change')}
        effect={'fade'}
        pagination={{ clickable: true }}
        // loop={true}
        // autoplay={true}
      >
        <div className={styles.postContainer}>
          {
            imageProps.imageList.map((image, index) => (
              <SwiperSlide>
                <div className={styles.imageDiv}>
                  <img onClick={() => {
                    imageClick(index);
                  }} className={styles.img} src={image} />
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