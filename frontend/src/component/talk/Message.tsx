import styles from "../../styles/talk/talkList.module.scss";
import React from "react";
import MyPage from "../MyPageSet";


interface props{
    send : string;
    receive : string;
    key : number;
}
const TalkListRight = (props3 : props)=>{

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
        </>
    )
}
export default TalkListRight;