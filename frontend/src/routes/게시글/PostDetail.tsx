import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/post/PostDetail.module.css"
import profileImg from "../../img/profileImg.png"
import spamImg from "../../img/spam.png"
import coatImg from "../../img/coat.png"
import transfer from "../../img/transfer.png"
import clock from "../../img/clock.png"
import like from "../../img/heart.png"
import talk from "../../img/send.png"

import Comments from "../../component/comments/Comments";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";
import Api from "../../utils/api";
import {useNavigate, useParams} from "react-router-dom";
import {AiOutlineHeart, AiTwotoneHeart} from "react-icons/ai";
import Card from "../../component/tradeCard/Card";
import Message from "../../component/로그인 & 회원가입/Message";
import {HiPencil} from "react-icons/hi";
import {resetCategory} from "../../store/categoryReducer";
import {changeRefreshState} from "../../store/refreshReducer";
import comments from "../../component/comments/Comments";



const PostDetail = () => {

    // const detail = useSelector((state : Rootstate)=>{return state.postDetailReducer})
    // console.log("asdfasdfa",detail)
    const navigate = useNavigate();

    interface PostType {
        id? : number;
        title? : string;
        content? : string;
        price: string;
        tradeEachOther? : boolean;
        authorNickname? : string;
        wishCategory? : string;
        productCategory? : string;
        tradeStatus? : string;
        tagNames? : string[];
        scrapCount? : number;
        messageRoomCount? : number;
        createdTime? : string;
    }

    type CommentTypes = "primary" | "secondary";
    interface CommentType {
        id : number;
        postId? : number;
        memberId : Number;
        memberNickname : String;
        imageUrl : String;
        content : String;
        depth : Number;
        parentCommentId : number | null;

        //댓글인지 대댓글인지 확인
    }


    //글 작성
    interface WriteCommentType {
        authorId : Number;
        postId : number;
        depth : number;
        content : string;
        parendCommentId : number | null;
    }

    const params = useParams();
    console.log(params)
    const postId = params.id;

    const [post,setPost] = useState<PostType>(null)
    const [commentList,setCommentList] = useState<CommentType[]>(null)
    const [writeComment,setWriteComment] = useState<WriteCommentType>(null)
    const [refreshFetch,setRefreshFetch] = useState({commentChange : false})
    const dispatch = useDispatch();
    const store = useSelector((state:Rootstate) => state);

    async function getPost() {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            console.log("getPost 요청")
            const res = await Api.get(`/post/${postId}`);

            console.log(res)
            setPost(prevState => {
                return {...prevState, ...res.data};
            })

            console.log(post)

        }
        catch (err)
        {
            console.log(err)
            alert("get 실패");
        }
    }

    async function getComments() {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            const res = await Api.get(`/post/${postId}/comments`);

            console.log(res)
            setCommentList(prevState => {
                return [...res.data];
            })
        }
        catch (err)
        {
            console.log(err)
            alert("get 실패");
        }
    }

    //TODO:함민혁) 코끼리톡 구현할때 이걸 누르면 메시지룸이 생성되게 구현하고, navigate에서 매개변수를 전달해주면 될거야
    //예시 : navigate('/signup/emailcheck', {state : userInfo})
    const talkButton = () => {
        navigate('/kokiritalk' )
    }

    useEffect(()=>{
        getPost();
        getComments();
    },[store.refreshReducer.commentChange])



    const [scrapSaved,setScrapSaved] = useState<boolean>(true);
    const onClickScrap = async () => {
        setScrapSaved(prevState => !prevState);

        const userId:Number = store.userInfoReducer.id;

        const jsonObj = {userId : userId, postId : post.id}
        console.log(jsonObj);
        if (scrapSaved) {
            await Api.post(`/user/scrap`, jsonObj);
        }else
        {
            await Api.delete(`/user/scrap`, {
                data: jsonObj
            })
        }
    }

    const onChangeComment = (e) => {
        const inputComment = e.target.value;
        setWriteComment((prevState) => {
            return {...prevState, authorId: store.userInfoReducer.id,
                postId : post.id,
                depth : 0,
                content : inputComment,
                parendCommentId : null,
            }
        })

        console.log(writeComment);
    }

    const UploadComment = async () => {
        try{
            const res = await Api.post(`/post/${postId}/comments`, writeComment);
            console.log(writeComment);
            console.log(res);
            dispatch(changeRefreshState());
            setRefreshFetch((prevState) => {
                return {...prevState,commentChange : true
                }
            })
            alert("댓글 작성 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("댓글 작성 실패")
        }
    }




    if(!post)
    {
        return null;
    }

    if (!commentList) {
        return null
    }

    const primaryComment = commentList.filter((comment) => {
        return comment.depth === 0
    })

    const secondaryComment = commentList.filter((comment) => {
        return comment.depth === 1
    })

    //댓글, 대댓글을 순서대로 배열에 담는 로직, 댓글 대댓글을 연결
    let result = primaryComment.reduce((prev,cur)=>{
        prev.push(cur);
        secondaryComment.forEach(secondary => {
            if(secondary.parentCommentId === cur.id)
            {
                prev.push(secondary);
            }
        })
        return prev;
    },[]);

    return (
        <div className={styles.postDetail}>
            <article className={styles.post}>
                <section className={styles.postTop}>
                    <div className={styles.postTopProfile}>
                        <img className={styles.postTopProfileImg} src={profileImg}></img>
                        <div className={styles.postTopProfileInfo}>
                            <div className={styles.postTopNickname}>{post.authorNickname}</div>
                            <div className={styles.postTopAddress}>상도 1동 33길</div>
                        </div>
                    </div>
                </section>
                <section className={styles.postBody}>
                    <div className={styles.postImg}>
                        <img className={styles.postBodyImg} src={coatImg}></img>
                    </div>
                    <div className={styles.postDetailInfo}>
                        <h2 className={styles.postDetailTitle}>{post.title}</h2>
                        <div className={styles.postDetailCategory}>{post.productCategory}</div>
                        <div className={styles.postDetailPrice}></div>
                        <div className={styles.postDetailContent}>{post.content}</div>
                        <div className={styles.postDetailTag}>#{post.tagNames}</div>
                        <div className={styles.postDetailSwapCategoryBox}>
                            <img className={styles.transfer} src={transfer}/>
                            <div className={styles.postDetailSwapCategory}> {post.wishCategory}</div>
                        </div>
                    </div>


                </section>
                <section className={styles.postBottom}>
                    <div className={styles.metaBox}>
                        <div className={styles.imgBox}>
                            {(scrapSaved ?
                            <AiOutlineHeart className={styles.likeImg}  onClick={onClickScrap}/>
                            :
                            <AiTwotoneHeart color={"red"} className={styles.likeImg} onClick={onClickScrap}/>)}

                            {/*<AiOutlineHeart className={styles.likeImg}  onClick={onClickScrap}/>*/}
                            {/*<AiTwotoneHeart color={"red"} className={styles.likeImg} onClick={()=>{}}/>*/}
                            <p className={styles.likeNum}>{post.scrapCount}</p>
                        </div>
                        <div className={styles.commentBox}>
                            <img className={styles.commentImg} src={talk}/>
                            <p className={styles.commmentNum}>{post.messageRoomCount}</p>
                        </div>
                        <div className={styles.timeBox}>
                            <img className={styles.timeImg} src={clock}/>
                            <p className={styles.timeNum}>4분전</p>
                        </div>
                    </div>
                    <button className={styles.exchangeBtn} onClick={talkButton}>코끼리톡으로 교환하기</button>
                </section>
            </article>
            <section className={styles.comments}>
                {
                    result.map((comment)=>(
                        <>
                            {comment.depth ===0 && <Comments postId = {comment.postId} id = {comment.id} className={"primary"}  userID={comment.memberNickname} content={comment.content} time={"12/21 12:00"}  />}
                            {comment.depth ===1 && <Comments id = {comment.id} className={"secondary"}  userID={comment.memberNickname} content={comment.content} time={"12/21 12:00"}  />}
                        </>
                    ))
                }
            </section>
            <div className = {styles.writeComments}>
                <input type={"text"} className={styles.writeCommentsInput} placeholder={"댓글을 작성하세요"} onBlurCapture={onChangeComment}/>
                 <HiPencil className={styles.pencilIcon} onClick={UploadComment}/>
            </div>
        </div>
    );
}

export default PostDetail;