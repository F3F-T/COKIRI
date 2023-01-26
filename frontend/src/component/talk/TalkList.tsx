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
import {setMessageRoomId, setOpponetNick, setPostId} from "../../store/talkCardReducer";
import {Rootstate} from "../../index";



// type clickOrNot = true | false ;
const tl = classNames.bind(styles);

interface props{
    onClick?: (e : React.MouseEvent<HTMLButtonElement,MouseEvent>) => any;
    partner : string;
    lastContent : string;
    date : string;
    keys? : number;
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
    // props2.click =click
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

    return (
        <>
            <TalkListLeft keys={props2.keys} onClick={props2.onClick} partner={props2.partner} lastContent={props2.lastContent} date={props2.date}/>
        </>

    );
}


export default TalkList;