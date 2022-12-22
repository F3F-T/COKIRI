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
    // Clickable(): clickOrNot;
    click? : boolean | undefined;
    partner : string;
    lastContent : string;
    date : string;
}

// const [ click, setClick ] = useState(false)


const TalkListLeft = (props2:props)=>{
    // props2.click =click
    console.log(props2.click)
    return(
        <>
                <div className={styles.talkContent} onClick={()=>{props2.click = true}}>
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

    console.log("tlwkr")
    console.log(props2.click)

    return (

        <>
            {props2.click == undefined&&
                <TalkListLeft partner={props2.partner} lastContent={props2.lastContent} date={props2.date}/>
            }
            {props2.click == true &&
                <>  {    console.log("dsd",props2.click)
                }
                    <TalkListLeft partner={props2.partner} lastContent={props2.lastContent} date={props2.date}/>
                    <TalkListRight/>
                </>
            }
        </>

    );
}


export default TalkList;