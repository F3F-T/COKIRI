import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/talk/kokiriTalk.module.scss"
import profileImg from "../img/profileImg.png"
import spamImg from "../img/spam.png"
import Comments from "../component/comments/Comments";
import Card from "../component/tradeCard/Card";
import TalkList from "../component/talk/TalkList";





const PostDetail = () => {
    const [ click, setClick ] = useState(false)


    return (
        <div className={styles.kokiritalk}>
            <div className={styles.left}>
                <div className={styles.leftHeader}>코끼리톡</div>
                <div className={styles.talkContainer}>
                 <TalkList partner={"함민혁"} lastContent={"주무시나요"} date={"몰라"}/>
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

                </div>
            </div>
        </div>
    );
}


export default PostDetail;