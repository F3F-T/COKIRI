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
import {HiOutlineDotsVertical, HiPencil} from "react-icons/hi";
import {CgMore, CgMoreO, CgMoreVertical, CgMoreVerticalAlt} from "react-icons/cg"
import {resetCategory} from "../../store/categoryReducer";
import {changeRefreshState} from "../../store/refreshReducer";
import comments from "../../component/comments/Comments";
import timeConvert from "../../utils/timeConvert";
import {LocalDateTime} from "js-joda";
import CustomSwiper from "../../component/common/CustomSwiper";
import tradeEx from "../../img/tradeEx.jpeg";


const PostDetail = () => {

    // const detail = useSelector((state : Rootstate)=>{return state.postDetailReducer})
    // console.log("asdfasdfa",detail)
    const navigate = useNavigate();

    interface PostType {
        id?: number;
        title?: string;
        content?: string;
        price: string;
        tradeEachOther?: boolean;
        authorNickname?: string;
        wishCategory?: string;
        productCategory?: string;
        tradeStatus?: string;
        tagNames?: string[];
        scrapCount?: number;
        messageRoomCount?: number;
        createdTime?: string;
        userInfo?: UserInfo;
        userInfoWithAddress: {
            userDetail: UserInfo
            address?: [
                {
                    postalAddress?: string
                }
            ]
        }
        images: string[];
    }

    type CommentTypes = "primary" | "secondary";

    interface UserInfo {
        id: number;
        email: string;
        birthDate: string;
        description: string;
        imageUrl: string;
        loginType: string;
        phoneNumber: string;
        scrapId: number;
        userName: string;
        nickname: string;

    }

    interface CommentType {
        id: number;
        postId?: number;
        memberId: Number;
        memberNickname: String;
        imageUrl: String;
        content: String;
        depth: Number;
        parentCommentId: number | null;
        userInfo: UserInfo;
        //댓글인지 대댓글인지 확인
    }


    //글 작성
    interface WriteCommentType {
        authorId: Number;
        postId: number;
        depth: number;
        content: string;
        parendCommentId: number | null;
    }

    const params = useParams();
    // console.log(params)
    const postId = params.id;

    const [post, setPost] = useState<PostType>(null)
    const [commentList, setCommentList] = useState<CommentType[]>(null)
    const [writeComment, setWriteComment] = useState<WriteCommentType>(null)
    const [refreshFetch, setRefreshFetch] = useState({commentChange: false})
    // const [isAuthor, setIsAuthor] = useState<boolean>();
    let isAuthor: boolean = undefined;

    const dispatch = useDispatch();
    const store = useSelector((state: Rootstate) => state);
    //댓글 작성 후 input text 초기화를 위한 state
    //<input type={"text"} className={styles.writeCommentsInput} placeholder={"댓글을 작성하세요"} onChange={onChangeComment} value={commentText}/>
    //에서 value를 사용하기 위해선 onBlur가 아닌 onChange를 사용해야만 한다
    const [commentText, setCommentText] = useState("");
    const isAuthorTrue = ["수정", "|", "삭제"];
    const isAuthorFalse = ["신고"]

    async function getPost() {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try {
            console.log("getPost 요청")
            const res = await Api.get(`/post/${postId}`);

            setPost(prevState => {
                return {...prevState, ...res.data};
            })

        } catch (err) {
            console.log(err)
            alert("get 실패");
        }
    }

    async function getComments() {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try {
            const res = await Api.get(`/post/${postId}/comments`);

            console.log(res)
            setCommentList(prevState => {
                return [...res.data];
            })
        } catch (err) {
            console.log(err)
            alert("get 실패");
        }
    }

    //TODO:함민혁) 코끼리톡 구현할때 이걸 누르면 메시지룸이 생성되게 구현하고, navigate에서 매개변수를 전달해주면 될거야
    //예시 : navigate('/signup/emailcheck', {state : userInfo})
    const talkButton = () => {
        navigate('/kokiritalk')
    }

    useEffect(() => {
        getPost();
        getComments();
        console.log(post)
        console.log(commentList);

    }, [store.refreshReducer.commentChange])


    const [scrapSaved, setScrapSaved] = useState<boolean>(true);
    const onClickScrap = async () => {
        setScrapSaved(prevState => !prevState);

        const userId: Number = store.userInfoReducer.id;

        const jsonObj = {userId: userId, postId: post.id}
        console.log(jsonObj);
        if (scrapSaved) {
            await Api.post(`/user/scrap`, jsonObj);
        } else {
            await Api.delete(`/user/scrap`, {
                data: jsonObj
            })
        }
    }

    const onChangeComment = (e) => {
        const inputComment = e.target.value;
        setCommentText(inputComment);
        setWriteComment((prevState) => {
            return {
                ...prevState, authorId: store.userInfoReducer.id,
                postId: post.id,
                depth: 0,
                content: inputComment,
                parendCommentId: null,
            }
        })

    }

    const UploadComment = async () => {
        try {
            const res = await Api.post(`/post/${postId}/comments`, writeComment);
            console.log(writeComment);
            console.log(res);
            dispatch(changeRefreshState());
            setRefreshFetch((prevState) => {
                return {
                    ...prevState, commentChange: true
                }
            })
            setCommentText("");
            alert("댓글 작성 성공")
        } catch (err) {
            console.log(err)
            alert("댓글 작성 실패")
        }
    }

    const deletePost = async () => {
        try {
            const config = {
                data: {
                    id : post.id,
                    authorId : post.userInfoWithAddress.userDetail.id
                }
            }
            //삭제는 일반적인 axios 방식과 달리 message body를 config로 넘겨주어야한다.
            const res = await Api.delete(`/post/${postId}`, config);
            alert("게시글 삭제 성공")
        } catch (err) {
            console.log(err)
            alert("게시글 삭제 실패")
        }
    }

    const updatePost = async () => {
        navigate(`/post/${post.id}/edit`)
    }



    if (!post) {
        return null;
    }


    if (!commentList) {
        return null
    }

    console.log(post)

    // console.log(commentList);

    //게시글 작성자 판단

    if (post.userInfoWithAddress.userDetail.id === store.userInfoReducer.id) {
        console.log("게시글 작성자임")
        isAuthor = true;
    } else {
        console.log("게시글 작성자가 아님")
        isAuthor = false;
    }

    console.log(isAuthor);
    const primaryComment = commentList.filter((comment) => {
        return comment.depth === 0
    })

    const secondaryComment = commentList.filter((comment) => {
        return comment.depth === 1
    })

    //댓글, 대댓글을 순서대로 배열에 담는 로직, 댓글 대댓글을 연결
    let commentSort = primaryComment.reduce((prev, cur) => {
        prev.push(cur);
        secondaryComment.forEach(secondary => {
            if (secondary.parentCommentId === cur.id) {
                prev.push(secondary);
            }
        })
        return prev;
    }, []);

    console.log(commentSort);

    return (
        <div className={styles.postDetail}>
            <article className={styles.post}>
                <section className={styles.postTop}>
                    <div className={styles.postTopProfile}>
                        <img className={styles.postTopProfileImg} src={post.userInfoWithAddress.userDetail.imageUrl}
                             onClick={() => navigate('/mypage')}></img>
                        <div className={styles.postTopProfileInfo}>

                            <div className={styles.postTopNickname}>{post.userInfoWithAddress.userDetail.nickname}</div>
                            {
                                ((post.userInfoWithAddress.address.length < 1) ?
                                        null :
                                        <div
                                            className={styles.postTopAddress}>{post.userInfoWithAddress.address[0].postalAddress}</div>
                                )
                            }
                        </div>
                        {
                            commentSort.map((comment) => (
                                <div key={comment.id}>
                                    {comment.depth === 0 &&
                                        <Comments key={comment.id} postId={comment.postId} id={comment.id}
                                                  className={"primary"} userID={comment.memberNickname}
                                                  content={comment.content} time={timeConvert(comment.createdTime)}
                                                  imageUrl={comment.imageUrl}/>}
                                    {comment.depth === 1 &&
                                        <Comments key={comment.id + 1} id={comment.id} className={"secondary"}
                                                  userID={comment.memberNickname} content={comment.content}
                                                  time={timeConvert(comment.createdTime)} imageUrl={comment.imageUrl}/>}
                                </div>
                            ))
                        }
                        <ul className={styles.ProfileActionList}>
                            {
                                isAuthor ?
                                    (<>
                                        <li onClick={updatePost}>수정</li>
                                        <li>|</li>
                                        <li onClick={deletePost}>삭제</li>
                                    </>)
                                    :
                                    (<li>신고하기</li>)
                            }
                        </ul>
                    </div>
                </section>
                <section className={styles.postBody}>
                    <div className={styles.postImg}>
                        {
                            ((post.images.length < 1) ?
                                    <img className={styles.postBodyImg} src={coatImg}></img> :
                                    <CustomSwiper key={post.id} imageList={post.images}/>
                            )
                        }
                        {/*<CustomSwiper imageList = {post.images}/>*/}
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
                                <AiOutlineHeart className={styles.likeImg} onClick={onClickScrap}/>
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
                            <p className={styles.timeNum}>{timeConvert(post.createdTime)}</p>
                        </div>
                    </div>
                    <button className={styles.exchangeBtn} onClick={talkButton}>코끼리톡으로 교환하기</button>
                </section>
            </article>
            <section className={styles.comments}>
                {
                    commentSort.map((comment) => (
                        <div key={comment.id}>
                            {comment.depth === 0 &&
                                <Comments key={comment.id} postId={comment.postId} id={comment.id} className={"primary"}
                                          userID={comment.memberNickname} content={comment.content}
                                          time={timeConvert(comment.createdTime)} imageUrl={comment.imageUrl}/>}
                            {comment.depth === 1 &&
                                <Comments key={comment.id + 1} id={comment.id} className={"secondary"}
                                          userID={comment.memberNickname} content={comment.content}
                                          time={timeConvert(comment.createdTime)} imageUrl={comment.imageUrl}/>}
                        </div>
                    ))
                }
            </section>
            <div className={styles.writeComments}>
                <input type={"text"} className={styles.writeCommentsInput} placeholder={"댓글을 작성하세요"}
                       onChange={onChangeComment} value={commentText}/>
                <HiPencil className={styles.pencilIcon} onClick={UploadComment}/>
            </div>
        </div>


    );
}

export default PostDetail;