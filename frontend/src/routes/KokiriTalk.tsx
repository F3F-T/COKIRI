import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/talk/kokiriTalk.module.scss"
import profileImg from "../img/profileImg.png"
import spamImg from "../img/spam.png"
import Comments from "../component/comments/Comments";
import Card from "../component/tradeCard/Card";
import TalkList from "../component/talk/TalkList";
import {useNavigate} from "react-router-dom";
import Message from "../component/talk/Message";



interface props{

}
const OnClickTalkList = (props3:props) => {
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

const KokiriTalk = () => {
    const navigate = useNavigate();
    const [ click, setClick ] = useState(false)
    const [key,setKey] = useState<number>(1)


    const onClickTalkList = () => {
        setKey(1);
    }

    const onClickTalkList2 = () => {
        console.log("2번 클릭 이벤트");
        setKey(2);

    }

    const onClickTalkList3 = () => {
        console.log("3번 클릭 이벤트")
        setKey(3);
    }

//
    return (
        <div className={styles.kokiritalk}>
            <div className={styles.left}>
                <div className={styles.leftHeader}>코끼리톡</div>
                <div className={styles.talkContainer}>

                 <TalkList keys={1} partner={"함민혁"} lastContent={"주무시나요"} date={"몰라"} onClick = {onClickTalkList} />
                 <TalkList keys={2} partner={"홍의성"} lastContent={"주무시나요2"} date={"몰라"} onClick = {onClickTalkList2} />
                 <TalkList keys={3} partner={"함민혁"} lastContent={"주무시나요3"} date={"몰라"} onClick = {onClickTalkList3} />
                </div>
            </div>

            <div className={styles.right}>
                <div className={styles.right_header}>
                    <div className={styles.right_header1}>
                        <Card className={"forTalk"} postTitle={"코트다 이놈아"} category={"의류"}/>
                    </div>
                    <div className={styles.right_header2}>
                        <p className={styles.delete}>삭제</p>
                        <p className={styles.block}>차단</p>
                        <p className={styles.inform}>신고</p>
                    </div>
                </div>
                <div className={styles.talkContainer2}>
                    <Message keys={key}/>
                </div>
            </div>
        </div>
    );
}


export default KokiriTalk;