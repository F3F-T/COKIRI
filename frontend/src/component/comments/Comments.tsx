import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/comments/Comments.module.scss";
import profileImg from "../../img/profileImg.png";
import classNames from "classnames/bind";

/**
 * Props 부모 : PostDetail.tsx
 * PostDetail.tsx에서 받은 props를 통해 댓글을 생성하는 component
 *  CommentProps에 댓글과 대댓글의 UI를 선택해주는 CommentTypes를 지정해주었다.
**/

//classNames로 styles를 bind해서 styles에 쉽게 접근하고 css 조건문을 쉽게 달수있게 돕는 API
const cx = classNames.bind(styles)

//CommentTypes를 기본 댓글인지, 대댓글인지를 선언하여 UI와 구성되는 데이터를 달리한다.
type CommentTypes = "primary" | "secondary";

//props에서 받을 Comment data의 형식들을 미리 선언(typescript)
interface CommentProps {
    //className은 PrimaryComment, SecondaryComment에도 넘겨줄 것이기 때문에 className을 optional로 설정
    className?: CommentTypes;
    userID: string;

    //userProfileImg : string; (url, string형식?)
    content: string;
    time: string;
}

const PrimaryComment = (commentInfo: CommentProps) => {
   return(
       <>
        <div className={cx('Profile')}>
            <img className={cx('ProfileImg')} src={profileImg}></img>
            <div className={styles.ProfileInfo}>
                {commentInfo.userID}
            </div>
            <ul className={styles.ProfileActionList}>
                <li>대댓글</li>
                <li>공감</li>
                <li>쪽지</li>
                <li>신고</li>
            </ul>
        </div>
        <div className={styles.comments}>
            {commentInfo.content}
        </div>
        <div className={styles.time}>
            {commentInfo.time}
        </div>
       </>
);
}

const SecondaryComment = (commentInfo: CommentProps) => {
    return(
        <>
            <div className={cx('Profile')}>
                <img className={cx('ProfileImg')} src={profileImg}></img>
                <div className={styles.ProfileInfo}>
                    {commentInfo.userID}
                </div>
                <ul className={styles.ProfileActionList}>
                    <li>대댓글</li>
                    <li>공감</li>
                    <li>쪽지</li>
                    <li>신고</li>
                </ul>
            </div>
            <div className={styles.comments}>
                {commentInfo.content}
            </div>
            <div className={styles.time}>
                {commentInfo.time}
            </div>
        </>
    );
}


const Comments = (commentInfo: CommentProps) => { //받는 props가 CommentProps임을 알려준다.

    return (
        <>
            {commentInfo.className === "primary" &&
                <div className={cx(commentInfo.className)}>
                    <PrimaryComment userID={commentInfo.userID} content={commentInfo.content} time={commentInfo.time}/>
                </div>
            }

            {commentInfo.className === "secondary" &&
                <div className={cx(commentInfo.className)}>
                    <SecondaryComment userID={commentInfo.userID} content={commentInfo.content} time={commentInfo.time}/>
                </div>
            }
        </>

    );
}

export default Comments;