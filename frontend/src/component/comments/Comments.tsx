import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/comments/Comments.module.scss";
import profileImg from "../../img/profileImg.png";
import classNames from "classnames/bind";
import {HiPencil} from "react-icons/hi";
import Api from "../../utils/api";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";
import {changeRefreshState} from "../../store/refreshReducer";

/**
 * Props 부모 : PostDetail.tsx
 * PostDetail.tsx에서 받은 props를 통해 댓글을 생성하는 component
 *  CommentProps에 댓글과 대댓글의 UI를 선택해주는 CommentTypes를 지정해주었다.
 *
 *
 *  classNames 공부하며 익히기
 *  typescript 공부
 *  sass 적용해보기
 *
**/

//classNames로 styles를 bind해서 styles에 쉽게 접근하고 css 조건문을 쉽게 달수있게 돕는 API
const cx = classNames.bind(styles)

//CommentTypes를 기본 댓글인지, 대댓글인지를 선언하여 UI와 구성되는 데이터를 달리한다.
type CommentTypes = "primary" | "secondary";

//props에서 받을 Comment data의 형식들을 미리 선언(typescript)
interface CommentProps {
    //className은 PrimaryComment, SecondaryComment에도 넘겨줄 것이기 때문에 className을 optional로 설정
    className?: CommentTypes;
    userID: String;
    postId? : number;

    //userProfileImg : string; (url, string형식?)
    content: String;
    time: String;
    id?: number;
}

//글 작성
interface WriteCommentType {
    authorId : Number;
    postId? : number;
    depth : number;
    content : string;
    parendCommentId : number | null;
}



const PrimaryComment = (commentInfo: CommentProps) => {

    const [enableReComment,setEnableReComment] = useState<boolean>(false);
    const [writeComment,setWriteComment] = useState<WriteCommentType>(null)
    const [refreshFetch,setRefreshFetch] = useState({commentChange : false})

    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();
    const onClickReComment = (comment) => {
        setEnableReComment(prevState => !prevState);
    }

    const UploadComment = async () => {
        try{
            const res = await Api.post(`/post/${commentInfo.postId}/comments`, writeComment);
            dispatch(changeRefreshState());
            console.log(writeComment);
            alert("대댓글 작성 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("대댓글 작성 실패")
        }
    }

    const onChangeComment = (e) => {
        const inputComment = e.target.value;

        setWriteComment((prevState) => {
            return {...prevState,
                authorId: store.userInfoReducer.id,
                postId : commentInfo.postId,
                depth : 1,
                content : inputComment,
                parentCommentId : commentInfo.id,
            }
        })
    }



    return(
       <>
        <div className={cx('Profile')}>
            <img className={cx('ProfileImg')} src={profileImg}></img>
            <div className={styles.ProfileInfo}>
                {commentInfo.userID}
            </div>
            <ul className={styles.ProfileActionList}>
                <li onClick={()=> onClickReComment(commentInfo)}>대댓글</li>
                <li>신고</li>
            </ul>
        </div>
        <div className={styles.comments}>
            {commentInfo.content}
        </div>
        <div className={styles.time}>
            {commentInfo.time}
        </div>
           {
               enableReComment ?
                   (
               <div className = {styles.writeComments}>
               <input type={"text"} className={styles.writeCommentsInput} placeholder={"대댓글을 입력하세요"} onBlurCapture={onChangeComment}/>
               <HiPencil className={styles.pencilIcon} onClick={UploadComment}/>
               </div>
                   )
           : ""}
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
                    <PrimaryComment postId = {commentInfo.postId} id= {commentInfo.id} userID={commentInfo.userID} content={commentInfo.content} time={commentInfo.time}/>
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