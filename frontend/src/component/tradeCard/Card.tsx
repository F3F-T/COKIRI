import React from 'react';
import styles from '../../styles/card/cards.module.scss';
import classNames from 'classnames/bind';
import transfer from '../../img/transfer.png';
import tradeEx from '../../img/tradeEx.jpeg';
import { AiOutlineHeart } from 'react-icons/ai';
import { IoPaperPlaneOutline } from 'react-icons/io5';
import { FaExchangeAlt } from 'react-icons/fa';


//classNames로 styles를 bind해서 styles에 쉽게 접근하고 css 조건문을 쉽게 달수있게 돕는 API
const ck = classNames.bind(styles);

//물물교환에 쓸 css와 내 정보에 쓸 css를 구분하기 위해 사용  유니온 뭐시기였어 이게
type cardTypes = 'forTrade' | 'forMypage'


//props에서 받을 카드 속 컨탠츠들
interface props {
  className?: cardTypes; //옵셔널로 준 이유가 뭐라 그랬지
  postTitle?: string;
  postContent?: string;
  like?: number;
  comment?: number;
  wishCategory?: string; //나중에 enum사용해서 다시 해보던가 할듯 없는 카테고리 못들어오게 막아야지
  onClick?: any;
  messageRoomCount?: number;
  thumbnail?: string;
  price?: number;
}


const TradeCard = (props1: props) => {

  let changeImgSize = props1.thumbnail;
  return (
    <>
      <div className={styles.postItem}>
        {
          ((props1.thumbnail != null && props1.thumbnail.includes('https://s3.ap-northeast-2.amazonaws.com')) ?
              <img className={styles.postImage} src={props1.thumbnail} /> :
              <img className={styles.postImage} src={tradeEx} />
          )
        }
        {/*<img className={styles.postImage} src={props1.thumbnail}/>*/}
        {/*<img className={styles.postImage} src={tradeEx}/>*/}
        <p className={styles.postTitle}>{props1.postTitle}</p>
        <p className={styles.postPrice}>{props1.price}원</p>
        <p className={styles.postContent}>{props1.postContent}</p>
        <div className={styles.detail}>
          <div className={styles.heart}>
            <AiOutlineHeart className={styles.likeImg} />
            <span className={styles.like}>{props1.like}</span>
            <IoPaperPlaneOutline className={styles.commentImg} />
            <span className={styles.commmentNum}>{props1.messageRoomCount}</span>
          </div>
          <div className={styles.detail2}>
            {/*<FaExchangeAlt className={styles.tradeImage} />*/}
            {/*<RiExchangeBoxLine className={styles.tradeImage} />*/}
            <FaExchangeAlt className={styles.tradeImage} />
            <span className={styles.tradestring}>{props1.wishCategory}</span>
          </div>
        </div>
      </div>

    </>
  );
};
const MypageCard = (props1: props) => {
  return (
    <>
      <div className={styles.postItem}>
        {
          ((props1.thumbnail != null && props1.thumbnail.includes('https://s3.ap-northeast-2.amazonaws.com')) ?
              <img className={styles.postImage} src={props1.thumbnail} /> :
              <img className={styles.postImage} src={tradeEx} />
          )
        }
        <p className={styles.postTitle}>{props1.postTitle}</p>
        <div className={styles.detail}>
          <p className={styles.like}>좋아요 {props1.like}개</p>
          <div className={styles.detail2}>
            <img className={styles.tradeImage} src={transfer} />
            <p className={styles.like}>{props1.wishCategory}</p>
          </div>
        </div>
      </div>

    </>
  );
};

const Card = (props1: props) => {
  return (
    <>
      {props1.className === 'forTrade' &&
        <div className={ck(props1.className)} onClick={props1.onClick}>
          <TradeCard postTitle={props1.postTitle} postContent={props1.postContent} like={props1.like}
                     messageRoomCount={props1.messageRoomCount}
                     comment={props1.comment} wishCategory={props1.wishCategory} thumbnail={props1.thumbnail}
                     price={props1.price} />
        </div>}
      {props1.className === 'forMypage' &&
        <div className={ck(props1.className)} onClick={props1.onClick}>
          <MypageCard postTitle={props1.postTitle} postContent={props1.postContent} like={props1.like}
                      messageRoomCount={props1.messageRoomCount}
                      comment={props1.comment} wishCategory={props1.wishCategory} thumbnail={props1.thumbnail} />
        </div>}

    </>
  );
};
export default Card;