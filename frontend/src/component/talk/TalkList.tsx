import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/talk/talkList.module.scss"
import profileImg from "../img/profileImg.png"
import spamImg from "../img/spam.png"
import classNames from "classnames/bind";
import {storeCategory} from "../../store/categoryReducer";
import {useDispatch, useSelector} from "react-redux";



// type clickOrNot = true | false ;
const tl = classNames.bind(styles)
interface props{
    onClick?: (e : React.MouseEvent<HTMLButtonElement,MouseEvent>) => void;
    partner : string;
    lastContent : string;
    date : string;
    keys : number;
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

    // props2.click =click
    console.log(props2);
    return(
        <>
                <div className={styles.talkContent}>
                    <button onClick={props2.onClick}>클릭해보세용</button>
                    <p className={styles.talkPartner}>{props2.partner}</p>
                    <p className={styles.lastContent}>{props2.lastContent}</p>
                    <p className={styles.date}>{props2.date}</p>
                </div>
        </>
    )
}
const TalkListRight = ()=>{

    return(
        <>
            <div className={styles.send}>
                <div className={styles.sendTitle}>받은 쪽지</div>
                <input className={styles.sendContent} type={"text"} />
            </div>
            <div className={styles.receive}>
                <div className={styles.receiveTitle}>보낸 쪽지</div>
                <input className={styles.receiveContent} type={"text"} />
            </div>
            <div className={styles.receive}>
                <div className={styles.receiveTitle}>보낸 쪽지</div>
                <input className={styles.receiveContent} type={"text"} />
            </div>
        </>
    )
}


const TalkList = (props2: props) => {
    const [click, setClick] = useState<boolean>(false);


    return (
        <>

            {console.log(props2)}
            <div className={styles.talkContent}>
                <button onClick={props2.onClick}>클릭해보세용</button>
                <p className={styles.talkPartner}>{props2.partner}</p>
                <p className={styles.lastContent}>{props2.lastContent}</p>
                <p className={styles.date}>{props2.date}</p>
            </div>
            {
                props2.keys === 1
            }

            {/*{props2.click == undefined&&*/}
            {/*    <TalkListLeft partner={props2.partner} lastContent={props2.lastContent} date={props2.date}/>*/}
            {/*}*/}
            {/*{props2.click == true &&*/}
            {/*    <>  {    console.log("dsd",props2.click)*/}
            {/*    }*/}
            {/*        <TalkListLeft partner={props2.partner} lastContent={props2.lastContent} date={props2.date}/>*/}
            {/*        <TalkListRight/>*/}
            {/*    </>*/}
            {/*}*/}
        </>

    );
}


export default TalkList;