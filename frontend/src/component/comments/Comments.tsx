import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/profileInfo/Comments.module.scss";
import profileImg from "../../img/profileImg.png";


const Comments = () => {



    return (
        <div>
            <div className={styles.Profile}>
                <img className={styles.ProfileImg} src={profileImg}></img>
                <div className={styles.ProfileInfo}>
                    상도동파티피플
                </div>
                <ul className={styles.ProfileActionList}>
                    <li>대댓글</li>
                    <li>공감</li>
                    <li>쪽지</li>
                    <li>신고</li>
                </ul>
            </div>
            <div className={styles.comments}>
                혹시 칼로리 몇인지 알 수 있을까요?
            </div>
            <div className={styles.time}>
                12/20 17: 05
            </div>
        </div>

    );
}

export default Comments;