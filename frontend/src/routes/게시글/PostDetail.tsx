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
import {changeCommentRefreshState} from "../../store/refreshReducer";
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
    setSellerId, setPostId, setBuyerId, setMessageRoomId
} from "../../store/talkCardReducer";
import {FALSE} from "sass";

import ImageSwiper from "../../component/common/ImageSwiper";
import tradeEx from "../../img/tradeEx.jpeg";
import {prepend} from "list";
import Select from "react-select";
import {ClipLoader} from "react-spinners";


const PostDetail = () => {
    let existOrNot : boolean = false
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
        scrap : boolean;
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
    // const [isAuthor, setIsAuthor] = useState<boolean>();
    let isAuthor;
    const [isAuthorProps, setIsAuthorProps] = useState();

    const dispatch = useDispatch();
    const talkCard = useSelector((state : Rootstate)=>{return state.talkCardReducer})
    const store = useSelector((state:Rootstate) => state);
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})

    //댓글 작성 후 input text 초기화를 위한 state
    //<input type={"text"} className={styles.writeCommentsInput} placeholder={"댓글을 작성하세요"} onChange={onChangeComment} value={commentText}/>
    //에서 value를 사용하기 위해선 onBlur가 아닌 onChange를 사용해야만 한다
    const [commentText, setCommentText] = useState("");
    const [exist,setExist] = useState<boolean>(false);
    // dispatch(resetTalkCard())
    const isAuthorTrue = ["수정", "|", "삭제"];
    const isAuthorFalse = ["신고"]
    const [scrapCountInReact, setScrapCountInReact] = useState<number>();
    const [scrapSaved, setScrapSaved] = useState<boolean>();

    //select
    interface TradeStatus {
        name: string;
    }

    const tradeStatus: TradeStatus[] =
        [
            {name: '교환가능'},
            {name: '예약중'},
            {name: '교환완료'},
        ]


    interface ArrayObjectSelectState {
        selectedTradeStatus: TradeStatus | null;
    }

    const [tradeState, setTradeState] = React.useState<ArrayObjectSelectState>({
        selectedTradeStatus: null,
    });


    async function getPost() {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try {
            console.log("getPost 요청")
            const res = await Api.get(`/post/${postId}`);

            setPost(prevState => {
                return {...prevState, ...res.data};
            })
            setScrapSaved(prevState => res.data.scrap)
            setScrapCountInReact(prevState => res.data.scrapCount);

            if(res.data.tradeStatus === "TRADABLE")
            {
                console.log("교환가능");
                setTradeState({selectedTradeStatus: {name : "거래가능"}});
            }
            else if(res.data.tradeStatus === "TRADING")
            {
                setTradeState({selectedTradeStatus: {name : "예약중"}});
            }
            else if(res.data.tradeStatus === "TRADED")
            {
                setTradeState({selectedTradeStatus: {name : "교환완료"}});
            }

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

    async function changeTradeStatus(option) {
        let tradeStatusToBackend
        if(option.name === "교환가능")
        {
            tradeStatusToBackend = "TRADABLE"
        }
        else if(option.name === "예약중")
        {
            tradeStatusToBackend = "TRADING"
        }
        else if(option.name ==="교환완료")
        {
            tradeStatusToBackend = "TRADED"
        }
        try {
            const jsonObj = {userId : store.userInfoReducer.id, postId : post.id, tradeStatus : tradeStatusToBackend}
            const res = await Api.patch(`/trade`,jsonObj);

            console.log(res)
            // alert("교환상태 변경")

        } catch (err) {
            console.log(err)
            alert("교환상태 변경 실패");
        }
    }

    //TODO:함민혁) 코끼리톡 구현할때 이걸 누르면 메시지룸이 생성되게 구현하고, navigate에서 매개변수를 전달해주면 될거야
    //예시 : navigate('/signup/emailcheck', {state : userInfo})
    //여기서부터 함민혁이 추가한 코드
    const talkButton = async () => {
        ////해당 포스트를 들어올때마다 talklistreducer에 값을 다 넣어버리자
        dispatch(setProductImg(post.images[0]))
        dispatch(setTitle(post.title))
        dispatch(setWishCategory(post.wishCategory))
        dispatch(setTradeCategory(post.productCategory))
        dispatch(setTradeStatus(post.tradeStatus))
        dispatch(setSellerId(post.userInfoWithAddress.userDetail.id))
        dispatch(setPostId(post.id))
        if(info.id != post.userInfoWithAddress.userDetail.id){
            dispatch(setOpponetNick(post.userInfoWithAddress.userDetail.nickname))
        }
        // await dispatch(setSellerId(post.userInfoWithAddress.userDetail.id))
        await getMessageRoom2()
        navigate(`/kokiriTalk/${info.id}`, {state: {existOrNot}})
    }
    async function getMessageRoom2() {
        try{
            const res = await Api.get('/user/messageRooms');
            // console.log("김동준전체조회",res.data)
            const res2 = await Api.get(`/user/${info.id}/totalMessageRooms`);
            console.log("김윤정전체조회",res2.data.length)
            if(res2.data.length == 0){
                console.log("여기로 들어온거야?")
                console.log("postId",post.id)
                console.log("info.id",info.id)
                const post_buyerId2= {
                    postId: post.id,
                    buyerId: info.id
                }
                const res4 = await Api.post(`/post/${post.id}/messageRooms`,post_buyerId2);
                console.log("룸생성정보 in postdetail",res4)
                dispatch(setOpponetNick(res4.data.sellerNickName))
                await dispatch(setMessageRoomId(res4.data.id))
                await dispatch(setSellerId(res4.data.sellerId))
                dispatch(setPostId(res4.data.postId))
            }
            else {
                console.log("아니면 여기로 들어온거야")
                for (let i = 0; i < res2.data.length; i++) {
                    // if(res2.data[i].sellerDelStatus == false && res2.data[i].buyerDelStatus == false  ) {
                        console.log("postDetailSeller",res2.data[i].sellerDelStatus)
                        console.log("postDetailbuyer",res2.data[i].buyerDelStatus)
                    if (res2.data[i].buyerId === info.id) {
                            console.log("들어옴?1")
                            if (res2.data[i].postId === post.id) {
                                console.log("들어옴?2")
                                console.log("이미 방이 존재합니다.")
                                dispatch(setMessageRoomId(res2.data[i].id))
                                existOrNot = true
                                break;
                            } else {
                                console.log("들어옴?3")
                                existOrNot = false
                        }
                    } else {
                        try {
                            console.log("들어옴?4")
                            const post_buyerId1 = {
                                postId: post.id,
                                buyerId: info.id
                            }

                            const res4 = await Api.post(`/post/${post.id}/messageRooms`, post_buyerId1);
                            dispatch(setOpponetNick(res4.data.sellerNickName))
                            await dispatch(setMessageRoomId(res4.data.id))
                            await dispatch(setSellerId(res4.data.sellerId))
                            dispatch(setPostId(res4.data.postId))
                            break;
                        } catch (err) {
                            console.log("들어옴?5")
                            console.log(err)
                            alert("메세지룸 추가 실패 in postdetail")
                        }
                    }

                }
            }
        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 조회 실패 in postdetail")
        }
    }
    //여기까지 함민혁코드


    // const onClickPost = (post) => {
    //     console.log(post)
    //     console.log(post.id)
    //     navigate(`/post/${post.id}`)
    // }

    useEffect(() => {
        getPost();
    }, [])


    //만약 여기에 post를 dep에 넣고 getPost를 부른다면 comment가 변화할때마다 비효율적인 함수 호출이 일어난다.
    useEffect(() => {
        getComments();
    }, [store.refreshReducer.commentChange])


    const onClickScrap = async () => {

        const userId: Number = store.userInfoReducer.id;

        const jsonObj = {userId: userId, postId: post.id}
        console.log(jsonObj);
        if (!scrapSaved) {
            await Api.post(`/user/scrap`, jsonObj);
            setScrapCountInReact(prevState => prevState+1);
        } else {
            await Api.delete(`/user/scrap`, {
                data: jsonObj
            })
            setScrapCountInReact(prevState => prevState-1);
        }
        setScrapSaved(prevState => !prevState);
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
            dispatch(changeCommentRefreshState());
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
            if(window.confirm("정말 게시글을 삭제하시겠어요?"))
            {
                alert("게시글 삭제 성공")
            }
            navigate(`/mulmultrade`);
        } catch (err) {
            console.log(err)
            alert("게시글 삭제 실패")
        }
    }

    const updatePost = async () => {
        navigate(`/post/${post.id}/edit`)
    }

    const onClickTag = (tagname) => {
        navigate(`/tagsearch?tags=${tagname}`);
    }



    if (!post) {
        return null;
    }

    if (!post.images) {
        return null;
    }

    if (!commentList) {
        return null
    }
    // console.log(post)
    // console.log(post.images);
    console.log("이거이거..")
    console.log(store.refreshReducer.commentChange);


    //게시글 작성자 판단

    if (post.userInfoWithAddress.userDetail.id === store.userInfoReducer.id) {
        // console.log("게시글 작성자임")
        isAuthor = true;
    } else {
        // console.log("게시글 작성자가 아님")
        isAuthor = false;
    }

    // console.log(isAuthor);
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

    // console.log(commentSort);
    // console.log(scrapSaved);
    const onClickUserPage = () => {
        console.log("클릭")
        navigate(`/mypage/${post.userInfoWithAddress.userDetail.id}`,{state:post.userInfoWithAddress.userDetail.id})
    }

    return (
        <div className={styles.postDetail}>
            <article className={styles.post}>
                <section className={styles.postTop}>
                    <div className={styles.postTopProfile}>
                        <img className={styles.postTopProfileImg} onClick={onClickUserPage} src={post.userInfoWithAddress.userDetail.imageUrl}/>
                        <div className={styles.postTopProfileInfo}>

                            <div className={styles.postTopNickname} onClick={onClickUserPage}>{post.userInfoWithAddress.userDetail.nickname}</div>
                            {
                                ((post.userInfoWithAddress.address.length < 1) ?
                                        null :
                                        <div
                                            className={styles.postTopAddress}>{post.userInfoWithAddress.address[0].postalAddress}</div>
                                )
                            }
                        </div>
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
                                    <ImageSwiper key={post.id} imageList={post.images}/>
                            )
                        }
                        {/*<ImageSwiper imageList = {post.images}/>*/}
                    </div>
                    <div className={styles.postDetailInfo}>
                        <h2 className={styles.postDetailTitle}>{post.title}</h2>
                        <div className={styles.postDetailCategory}>{post.productCategory}</div>
                        <div className={styles.postDetailPrice}>{post.price}원</div>
                        <div className={styles.contentAndTag}>
                        <div className={styles.postDetailContent}>{post.content}</div>
                        <div className={styles.postDetailTag}>
                            {

                                    post.tagNames.map((tagname) => (
                                        <span onClick={() => {onClickTag(tagname)}} className = {styles.tagC}>
                                            #{tagname}
                                        </span>
                                    ))

                            }
                        </div>
                    </div>
                    </div>
                    <div className={styles.categoryandStatus}>
                    <div className={styles.postDetailSwapCategoryBox}>
                        <img className={styles.transfer} src={transfer}/>
                        <div className={styles.postDetailSwapCategory}> {post.wishCategory}</div>
                    </div>
                    <div className = {styles.tradeStatusDiv}>
                        {
                            isAuthor ?
                                (<>
                                    <Select
                                        className={styles.tradeStatus}
                                        styles={{ // zIndex
                                            menu: provided => ({...provided, zIndex: 999})
                                        }}
                                        // If you don't need a state you can remove the two following lines value & onChange
                                        value={tradeState.selectedTradeStatus}
                                        onChange={(option: TradeStatus | null) => {

                                            setTradeState({selectedTradeStatus: option});
                                            changeTradeStatus(option);

                                        }}
                                        getOptionLabel={(category: TradeStatus) => category.name}
                                        getOptionValue={(category: TradeStatus) => category.name}
                                        options={tradeStatus}
                                        isSearchable={false}
                                        isClearable={false}
                                        backspaceRemovesValue={false}
                                        placeholder={"교환가능"}
                                    />
                                </>)
                                :
                                <span className = {styles.tradeStatusString}>{tradeState.selectedTradeStatus.name}</span>
                        }
                    </div>
                    </div>


                </section>
                <section className={styles.postBottom}>
                    <div className={styles.metaBox}>
                        <div className={styles.imgBox}>
                            {(scrapSaved ?
                                <AiTwotoneHeart color={"red"} className={styles.likeImg} onClick={onClickScrap}/>
                                :
                                <AiOutlineHeart className={styles.likeImg} onClick={onClickScrap}/>)
                            }
                            <p className={styles.likeNum}>{scrapCountInReact}</p>
                        </div>
                        <div className={styles.commentBox}>
                            <img className={styles.commentImg} src={talk}/>
                            <p className={styles.commmentNum}>{post.messageRoomCount}</p>
                        </div>
                        <div className={styles.timeBox}>
                            <img className={styles.timeImg} src={clock}/>
                            <p className={styles.timeNum}>{timeConvert(post.createdTime)}</p>
                        </div>
                        {/*<button className={styles.tradeStatus} onClick={talkButton}>거래상태</button>*/}
                    </div>
                    <div className={styles.tradeAndTalk}>
                    <button className={styles.exchangeBtn} onClick={talkButton}>코끼리톡으로 교환하기</button>
                    </div>
                </section>
            </article>
            <section className={styles.comments}>
                {
                    commentSort.map((comment) => (
                        <div key={comment.id}>
                            {comment.depth === 0 &&
                                <Comments key={comment.id} postId={comment.postId} id={comment.id} className={"primary"}
                                          userID={comment.memberNickname} content={comment.content}
                                          time={timeConvert(comment.createdTime)} imageUrl={comment.imageUrl} isAuthor={isAuthor} memberId={comment.memberId}/>}
                            {comment.depth === 1 &&
                                <Comments key={comment.id + 1} postId={comment.postId} id={comment.id} className={"secondary"}
                                          userID={comment.memberNickname} content={comment.content}
                                          time={timeConvert(comment.createdTime)} imageUrl={comment.imageUrl} isAuthor={isAuthor} memberId={comment.memberId}/>}
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