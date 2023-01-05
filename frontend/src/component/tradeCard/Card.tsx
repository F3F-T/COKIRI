import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/card/cards.module.scss";
import profileImg from "../../img/profileImg.png";
import classNames from "classnames/bind";
import myImage from "../../img/cokkiriLogo.png";
import PostContainer from "../trade/PostContainer";
import transfer from "../../img/transfer.png"
import tradeEx from "../../img/tradeEx.jpeg"


//classNames로 styles를 bind해서 styles에 쉽게 접근하고 css 조건문을 쉽게 달수있게 돕는 API
const ck = classNames.bind(styles)

//물물교환에 쓸 css와 내 정보에 쓸 css를 구분하기 위해 사용  유니온 뭐시기였어 이게
type cardTypes = "forTrade" | "forMypage"


//props에서 받을 카드 속 컨탠츠들
interface props{
    className?: cardTypes; //옵셔널로 준 이유가 뭐라 그랬지
    postTitle?: string;
    postContent?: string;
    like?: number;
    comment?: number;
    wishCategory? : string; //나중에 enum사용해서 다시 해보던가 할듯 없는 카테고리 못들어오게 막아야지
}

const TradeCard = (props1:props)=>{
    return(
        <>
            <div className={styles.postItem}>
                <img className={styles.postImage} src = {tradeEx}/>
                <p className={styles.postTitle}>{props1.postTitle}</p>
                <p className={styles.postContent}>{props1.postContent}</p>
                <div className={styles.detail}>
                    <p className={styles.like}>좋아요 {props1.like}개</p>
                    <div className={styles.detail2}>
                        <img className={styles.tradeImage} src = {transfer}/>
                        <p className={styles.like}>{props1.wishCategory}</p>
                    </div>
                </div>
            </div>

        </>
    )
}
const MypageCard = (props1:props)=>{
    return(
        <>
            <div className={styles.postItem}>
                <img className={styles.postImage} src = {tradeEx}/>
                <p className={styles.postTitle}>{props1.postTitle}</p>
                <div className={styles.detail}>
                    <p className={styles.like}>좋아요 {props1.like}개</p>
                    <div className={styles.detail2}>
                        <img className={styles.tradeImage} src = {transfer}/>
                        <p className={styles.like}>{props1.wishCategory}</p>
                    </div>
                </div>
            </div>

        </>
    )
}


const Card = (props1: props)=>{
    return(
        <>
            {props1.className === "forTrade" &&
                <div className={ ck(props1.className)}>
                    <TradeCard postTitle={props1.postTitle} postContent={props1.postContent} like={props1.like} comment={props1.comment} wishCategory={props1.wishCategory}/>
                </div>}
            {props1.className === "forMypage" &&
                <div className={ ck(props1.className)}>
                    <MypageCard postTitle={props1.postTitle} like={props1.like} comment={props1.comment} wishCategory={props1.wishCategory}/>
                </div>}

        </>
    )
}
export default Card;