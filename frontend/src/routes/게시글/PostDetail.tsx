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
import {useSelector} from "react-redux";
import {Rootstate} from "../../index";
import Api from "../../utils/api";
import {useNavigate, useParams} from "react-router-dom";
import Card from "../../component/tradeCard/Card";



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

    const params = useParams();
    console.log(params)
    const postId = params.id;

    const [post,setPost] = useState<PostType>(null)

    async function getPost() {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try{
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

    //TODO:함민혁) 코끼리톡 구현할때 이걸 누르면 메시지룸이 생성되게 구현하고, navigate에서 매개변수를 전달해주면 될거야
    //예시 : navigate('/signup/emailcheck', {state : userInfo})
    const talkButton = () => {
        navigate('/kokiritalk' )
    }

    useEffect(()=>{
        getPost();
    },[])

    const store = useSelector((state:Rootstate) => state);

    // useEffect(()=>{
    //     console.log("jwt 토큰이 바뀜")
    //     console.log(store.jwtTokenReducer.accessToken);
    //
    // },[store.jwtTokenReducer.accessToken])



    if(!post)
    {
        return null;
    }



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
                            <img className={styles.likeImg} src={like} onClick={()=>{}}/>
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
                <Comments className={"primary"}  userID={"홍의성"} content={"댓글 내용입니다."} time={"12/21 12:00"}  />
                <Comments className={"secondary"}  userID={"함민혁"} content={"댓글 내용입니다."} time={"12/21 12:00"}  />
                <Comments className={"secondary"}  userID={"홍의성"} content={"댓글 내용입니다."} time={"12/21 12:00"}  />
                <Comments className={"secondary"}  userID={"홍의성"} content={"댓글 내용입니다."} time={"12/21 12:00"}  />
                <Comments className={"secondary"}  userID={"홍의성"} content={"댓글 내용입니다."} time={"12/21 12:00"}  />
                <Comments className={"primary"}  userID={"홍의성"} content={"댓글 내용입니다."} time={"12/21 12:00"}  />
                <Comments className={"primary"}  userID={"홍의성"} content={"댓글 내용입니다."} time={"12/21 12:00"}  />
            </section>
        </div>
    );
}

export default PostDetail;