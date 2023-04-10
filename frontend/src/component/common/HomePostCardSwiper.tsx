//https://enfanthoon.tistory.com/166
//https://swiperjs.com/react
import { Swiper, SwiperSlide } from 'swiper/react';
import SwiperCore, { Autoplay, Navigation, Pagination } from 'swiper';
import React from 'react';

import 'swiper/swiper.scss';
import '../../styles/scss/HomePostCardSwipe.scss';
import styles from '../../styles/trade/PostContainer.module.css';
import Card from '../tradeCard/Card';
import { useNavigate } from 'react-router-dom';


interface postType {
  id?: number;
  title?: string;
  content?: string;
  tradeEachOther?: boolean;
  authorNickname?: string;
  wishCategory?: string;
  productCategory?: string;
  tradeStatus?: string;
  tagNames?: string[];
  scrapCount?: number;
  messageRoomCount?: number;
  thumbnail?: string;
}

interface postProps {
  postList: postType[];
}


const HomePostCardSwiper = (postProps) => {

  const navigate = useNavigate();

  // console.log(imageProps.imageList["id"])
  if (!postProps.postList) {
    return null;
  }

  console.log(postProps);

  SwiperCore.use([Navigation, Pagination, Autoplay]);

  const onClickPost = (post) => {
    console.log(post);
    console.log(post.id);
    navigate(`/post/${post.id}`);
  };

  return (
    <>
      <Swiper
        // style={}
        spaceBetween={30}
        slidesPerGroup={4}
        speed={1000}
        loopFillGroupWithBlank={true}
        slidesPerView={4}
        onSlideChange={() => console.log('slide change')}
        className={'homeSwiper'}
        navigation
        pagination={{ clickable: true }}
        autoplay={true}
        // loop={true}
      >
        <div className={styles.postContainer}>
          {

            postProps.postList.map((post, index) => (
              <SwiperSlide key={index}>
                <Card key={index} className={'forHome'} like={post.scrapCount} postTitle={post.title}
                      postContent={post.content} wishCategory={post.wishCategory}
                      messageRoomCount={post.messageRoomCount}
                      price={post.price}
                      onClick={() => {
                        onClickPost(post);
                      }} thumbnail={post.thumbnail} />
              </SwiperSlide>
            ))
          }
        </div>
      </Swiper>
    </>
  );
};
export default HomePostCardSwiper;