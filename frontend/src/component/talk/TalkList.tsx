import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/talk/talkList.module.scss"
import buttonstyled from "../../styles/common/Button.module.scss"

import profileImg from "../img/profileImg.png"
import spamImg from "../img/spam.png"
import classNames from "classnames/bind";
import {storeCategory} from "../../store/categoryReducer";
import {useDispatch, useSelector} from "react-redux";
import Message from "./Message";
import Api from "../../utils/api";
import {
    setBuyerId,
    setMessageRoomId,
    setOpponetNick,
    setPostId, setProductImg, setSellerId, setTitle,
    setTradeCategory,
    setTradeStatus, setWishCategory
} from "../../store/talkCardReducer";
import {Rootstate} from "../../index";
import {changeRefreshState} from "../../store/refreshReducer";
import timeConvert from "../../utils/timeConvert";



// type clickOrNot = true | false ;
const tl = classNames.bind(styles);

interface props{
    onClick?: (e : React.MouseEvent<HTMLButtonElement,MouseEvent>) => any;
    partner : string;
    lastContent : string;
    date : string;
    keys? : number;
    counts? : number;
}
interface talkInfoType{
    partner : string;
    lastContent : string;
    date : string;
    keys? : number;
    counts? : number;
}

// const object ={
//     a: 1,
//     b: 2,
//     c: 3
// } as const
//
// type objectShape = typeof object
// type keys = keyof objectShape

const TalkListLeft = (props2:props)=>{
    const dispatch = useDispatch();
    const talkCard = useSelector((state : Rootstate)=>{return state.talkCardReducer})
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})


    // console.log("TalkList props",props2);
    return(
        <>
                <div className={styles.talkContent}>
                    <button className={styles.talkBtn} onClick={props2.onClick}>
                        <div className={styles.box1}>
                            <div className={styles.box1_1}>
                                <p className={styles.talkPartner}>{props2.partner}</p>
                            </div>
                            <div className={styles.box1_2}>
                                <p className={styles.date}>{props2.date}</p>
                            </div>
                        </div>
                        <div className={styles.box2}>
                            <p className={styles.lastContent}>{props2.lastContent}</p>
                        </div>
                    </button>
                </div>
        </>
    )
}


const TalkList = (props2: props) => {
    const realCount = props2.counts
    // props2.click =click

    console.log("talklist들어옴",props2.keys)
    //last message 한번더 호출
    useEffect(() => {
        getMessageRoom()
    }, [realCount])
    async function getMessageRoom() {
        try{
            const res = await Api.get('/user/messageRooms');
        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 조회 실패 in Talklist")
        }
    }
    return (
        <>
            {/*<TalkListLeft keys={key} onClick={props2.onClick} partner={partner} lastContent={msg} date={timeConvert(date)} counts={realCount}/>*/}
            <TalkListLeft keys={props2.keys} onClick={props2.onClick} partner={props2.partner} lastContent={props2.lastContent} date={props2.date} counts={realCount}/>

        </>

    );
}


export default TalkList;