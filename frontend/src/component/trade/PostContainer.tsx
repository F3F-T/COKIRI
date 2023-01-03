import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/PostContainer.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../img/cokkiriLogo.png"
import Card from "../tradeCard/Card"
import Comments from "../comments/Comments";
import {storeCategory} from "../../store/categoryReducer";
import {useDispatch, useSelector} from "react-redux";
import {storePostDetail} from "../../store/postDetailReducer";
import TalkList from "../talk/TalkList";
import {Rootstate} from "../../index";
import tradeEx from "../../img/tradeEx.jpeg";
import transfer from "../../img/transfer.png";
import styled from "../../styles/card/cards.module.scss";


const PostContainer = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const onClickTotalTalkList = (key) => {
        return (event: React.MouseEvent) => {
            onClickPostDetail(key)
            event.preventDefault();
        }
    }
    const postDetail =[
        {
            keys: 1,
            postTitle : "21fw쿠어 MTR 발마칸 코트Msdfsdkbkhug",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },
        {
            keys: 2,
            postTitle : "코트",
            postContent : "남성의류",
            like : "1",
            comment : "4",
            category: "의류"
        },
        {
            keys: 3,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },
        {
            keys: 4,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },
        {
            keys: 5,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },        {
            keys: 6,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },        {
            keys: 7,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },        {
            keys: 8,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },
    ]
    const detail = useSelector((state : Rootstate)=>{return state.postDetailReducer})

    const onClickPostDetail = (i)  => {
        dispatch(storePostDetail(postDetail[i].postTitle))
        dispatch(storePostDetail(postDetail[i].like))
        dispatch(storePostDetail(postDetail[i].comment))
        dispatch(storePostDetail(postDetail[i].category))

        //확인하고 싶을때 : console.log(store.categoryReducer.category)
        //store : index.tsx에 있는 store
        //categoryReducer : store ->  reducer안에 선언한 categoryReducer, 이는 categoryReducer.ts를 참조한다.
        //category : categoryReducer.ts 안에 있는 categorySlice에 담긴 category이다. 이는 initialState : 안에 선언이 되어있다.
    }

        return (
        <div className={styles.postContainer}>

            {
                postDetail.map((SingleObject:object)=>(
                    <Card className={"forTrade"} postTitle={SingleObject["postTitle"]} postContent={SingleObject["postContent"]} like={SingleObject["like"]} comment={SingleObject["comment"]} category={SingleObject["category"]} />
                ))
            }
            <Card className={"forTrade"} postTitle={detail.postTitle} postContent={detail.postContent} like={detail.like} comment={detail.comment} category={detail.category} />
            {/*얘는 그냥 디폴트값*/}
            {/*{*/}
            {/*    postDetail.map((SingleObject:object,i)=>(*/}

            {/*        <div className={styled.postItem}>*/}
            {/*            <img className={styled.postImage} src = {tradeEx}/>*/}
            {/*            <p className={styled.postTitle}>{postDetail[i].postTitle}</p>*/}
            {/*            <p className={styled.postContent}>{SingleObject["postContent"]}</p>*/}
            {/*            <div className={styled.detail}>*/}
            {/*                <p className={styled.like}>좋아요 {SingleObject["like"]}개</p>*/}
            {/*                <div className={styled.detail2}>*/}
            {/*                    <img className={styled.tradeImage} src = {transfer}/>*/}
            {/*                    <p className={styled.like}>{SingleObject["category"]}</p>*/}
            {/*                </div>*/}
            {/*            </div>*/}
            {/*        </div>                ))*/}
            {/*}*/}

            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
            {/*<Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />*/}
        </div>
    );
}

export default PostContainer;