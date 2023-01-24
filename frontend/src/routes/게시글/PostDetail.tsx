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
import timeConvert from "../../utils/timeConvert";
import {LocalDateTime} from "js-joda";
import {userInfo} from "os";
import internal from "stream";
import useGeoLocation from "../../hooks/useGeolocation";
import {
    setProductImg,
    setOpponetNick,
    setTradeStatus,
    setTitle,
    setWishCategory,
    setTradeCategory,
    resetTalkCard,
    setSellerId, setPostId
} from "../../store/talkListReducer";
import {FALSE} from "sass";



const PostDetail = () => {
    let existOrNot : boolean
    // const detail = useSelector((state : Rootstate)=>{return state.postDetailReducer})
    // console.log("asdfasdfa",detail)
    const navigate = useNavigate();
    interface PostType {
        id? : number;
        title? : string;
        content? : string;
        price?: string;
        tradeEachOther? : boolean;
        authorNickname? : string;
        wishCategory? : string;
        productCategory? : string;
        tradeStatus? : string;
        tagNames? : string[];
        scrapCount? : number;
        messageRoomCount? : number;
        createdTime? : string;
        userInfo? : UserInfo;
        userInfoWithAddress : {
            userDetail : UserInfo
        }
        images?:[
            {id:number, imgPath: string}
        ]
    }

    type CommentTypes = "primary" | "secondary";
    interface UserInfo {
        id : number;
        email : string;
        birthDate : string;
        description : string;
        imageUrl : string;
        loginType : string;
        phoneNumber : string;
        scrapId : number;
        userName : string;
        nickname :string;

    }
    interface CommentType {
        id : number;
        postId? : number;
        memberId : Number;
        memberNickname : String;
        imageUrl : String;
        content : String;
        depth : Number;
        parentCommentId : number | null;
        userInfo : UserInfo;
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

    //
    interface ExistType{
        existOrNot : boolean
    }
    //

    const params = useParams();
    // console.log(params)
    const postId = params.id;

    const [post,setPost] = useState<PostType>(null)
    const [commentList,setCommentList] = useState<CommentType[]>(null)
    const [writeComment,setWriteComment] = useState<WriteCommentType>(null)
    const [refreshFetch,setRefreshFetch] = useState({commentChange : false})

    const dispatch = useDispatch();
    const talkCard = useSelector((state : Rootstate)=>{return state.talkListReducer})
    const store = useSelector((state:Rootstate) => state);
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})

    //댓글 작성 후 input text 초기화를 위한 state
    //<input type={"text"} className={styles.writeCommentsInput} placeholder={"댓글을 작성하세요"} onChange={onChangeComment} value={commentText}/>
    //에서 value를 사용하기 위해선 onBlur가 아닌 onChange를 사용해야만 한다
    const [commentText, setCommentText] = useState("");
    const [exist,setExist] = useState<ExistType>({existOrNot:false});
    // dispatch(resetTalkCard())

    async function getPost() {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            console.log("getPost 요청")
            const res = await Api.get(`/post/${postId}`);

            console.log(res)
            setPost(prevState => {
                return {...prevState, ...res.data};
            })


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
    const talkButton = async () => {
        ////해당 포스트를 들어올때마다 talklistreducer에 값을 다 넣어버리자
        dispatch(setProductImg(post.images[0].imgPath))
        dispatch(setTitle(post.title))
        dispatch(setWishCategory(post.wishCategory))
        dispatch(setTradeCategory(post.productCategory))
        dispatch(setTradeStatus(post.tradeStatus))
        dispatch(setSellerId(post.userInfoWithAddress.userDetail.id))
        dispatch(setPostId(post.id))
        // await dispatch(setSellerId(post.userInfoWithAddress.userDetail.id))
        await getMessageRoom()
        console.log("existexist",existOrNot)
        navigate(`/kokiriTalk/${info.id}`, {state: existOrNot})
    }
    async function getMessageRoom() {
        try{
            const res = await Api.get('/user/messageRooms');
            alert("메세지룸 조회 성공1")
            console.log("d",res.data)
            // res.data.content.map((a:String)=>(
            //     a['buyerNickname'] === info.nickname ?
            //         setExist((prevState) => {
            //             return {...prevState,existOrNot : false
            //             }
            //         })
            //         :
            //         setExist((prevState) => {
            //             return {...prevState,existOrNot : true
            //             }
            //         })
            // ))
            console.log("콘탠츠길이",res.data.content.length)
            for(let i =0 ; i<res.data.content.length;i++){
                if(res.data.content[i].buyerNickname === info.nickname)
                {
                    if(res.data.content[i].sellerNickname === post.userInfoWithAddress.userDetail.nickname){
                        console.log("이미방있어요요요요")
                        existOrNot = true
                    }
                    else {
                        existOrNot = false
                    }

                }

            }
        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 조회 실패1")
        }
    }
    // const onClickPost = (post) => {
    //     console.log(post)
    //     console.log(post.id)
    //     navigate(`/post/${post.id}`)
    // }

    useEffect(()=>{
        getPost();
        getComments();
        console.log(post)
        console.log(commentList);
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
        setCommentText(inputComment);
        setWriteComment((prevState) => {
            return {...prevState, authorId: store.userInfoReducer.id,
                postId : post.id,
                depth : 0,
                content : inputComment,
                parendCommentId : null,
            }
        })
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
            setCommentText("");
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

    // console.log(post)
    // console.log(commentList);

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

    console.log("유저 아이디", info.id)

    // async function createMessageRoom() {
    //     console.log("포스트 아이디", postId)
    //     try{
    //         const post_buyerId1 = {
    //             postId: postId,
    //             buyerId: info.id
    //         }
    //         const res = await Api.post(`/post/${postId}/messageRooms`,post_buyerId1);
    //         dispatch(setOpponetNick(res.data.sellerNickName))
    //         console.log("메세지룸 추가", res.data)
    //         alert("메세지룸 추가 성공")
    //
    //     }
    //     catch (err)
    //     {
    //         console.log(err)
    //         alert("메세지룸 추가 실패")
    //     }
    // }
    const hihihihi = () => {
        console.log("존재유무",exist)
    }
    return (
        <div className={styles.postDetail}>
            <article className={styles.post}>
                <section className={styles.postTop}>
                    <div className={styles.postTopProfile}>
                        <img className={styles.postTopProfileImg} src={post.userInfoWithAddress.userDetail.imageUrl} onClick={()=>navigate('/mypage')}></img>
                        <div className={styles.postTopProfileInfo}>
                            <div className={styles.postTopNickname}>{post.userInfoWithAddress.userDetail.nickname}</div>
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
                            <p className={styles.timeNum}>{timeConvert(post.createdTime)}</p>
                        </div>
                    </div>
                    <button className={styles.exchangeBtn} onClick={()=>{talkButton();}}>코끼리톡으로 교환하기</button>
                </section>
            </article>
            <section className={styles.comments}>
                {
                    result.map((comment)=>(
                        <div key={comment.id}>
                            {comment.depth ===0 && <Comments postId = {comment.postId} id = {comment.id} className={"primary"}  userID={comment.memberNickname} content={comment.content} time={timeConvert(comment.createdTime)} imageUrl={comment.imageUrl} />}
                            {comment.depth ===1 && <Comments id = {comment.id} className={"secondary"}  userID={comment.memberNickname} content={comment.content} time={timeConvert(comment.createdTime)} imageUrl={comment.imageUrl}   />}
                        </div>
                    ))
                }
            </section>
            <div className = {styles.writeComments}>
                <input type={"text"} className={styles.writeCommentsInput} placeholder={"댓글을 작성하세요"} onChange={onChangeComment} value={commentText}/>
                 <HiPencil className={styles.pencilIcon} onClick={UploadComment}/>
            </div>
            <button onClick={hihihihi}>버튼버튼</button>
        </div>
    );
}

export default PostDetail;