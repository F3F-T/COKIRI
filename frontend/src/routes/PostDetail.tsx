import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/post/PostDetail.module.css"
import profileImg from "../img/profileImg.png"
import spamImg from "../img/spam.png"
import coatImg from "../img/coat.png"
import transfer from "../img/transfer.png"
import clock from "../img/clock.png"
import like from "../img/heart.png"
import talk from "../img/send.png"

import Comments from "../component/comments/Comments";
import {useSelector} from "react-redux";
import {Rootstate} from "../index";

const PostDetail = () => {

    let detail = useSelector((state : Rootstate)=>{return state.postDetailReducer})
    console.log(detail)



    return (
        <div className={styles.postDetail}>
            <article className={styles.post}>
                <section className={styles.postTop}>
                    <div className={styles.postTopProfile}>
                        <img className={styles.postTopProfileImg} src={profileImg}></img>
                        <div className={styles.postTopProfileInfo}>
                            <div className={styles.postTopNickname}>상도동팔이피플</div>
                            <div className={styles.postTopAddress}>상도 1동 33길</div>
                        </div>
                    </div>
                </section>
                <section className={styles.postBody}>
                    <div className={styles.postImg}>
                        <img className={styles.postBodyImg} src={coatImg}></img>
                        {/*<button></button>*/}
                        {/*<button>오른쪽으로 가기</button>*/}
                    </div>
                    <div className={styles.postDetailInfo}>
                        <h2 className={styles.postDetailTitle}>21fw 쿠어 MTR 발마칸 코트M (멜란지토프)</h2>
                        <div className={styles.postDetailCategory}>남성의류</div>
                        <div className={styles.postDetailPrice}>120,000원</div>
                        <div className={styles.postDetailContent}>3개월 전에 산 스팸 클래식 중간크기에요.<br/>
                            다른 통조림 류랑 교환하실분 !! 꼭 식품이 아니더라도 관심있으신 분 연락주세요 ㅎㅎ
                        </div>
                        <div className={styles.postDetailTag}>#스팸 #통조림 #고기 #스팸클래식</div>
                        <div className={styles.postDetailSwapCategoryBox}>
                            <img className={styles.transfer} src={transfer}/>
                            <div className={styles.postDetailSwapCategory}> 식품</div>
                        </div>
                    </div>

                </section>
                <section className={styles.postBottom}>
                    <div className={styles.metaBox}>
                        <div className={styles.imgBox}>
                            <img className={styles.likeImg} src={like} onClick={()=>{}}/>
                            <p className={styles.likeNum}>4</p>
                        </div>
                        <div className={styles.commentBox}>
                            <img className={styles.commentImg} src={talk}/>
                            <p className={styles.commmentNum}>4</p>
                        </div>
                        <div className={styles.timeBox}>
                            <img className={styles.timeImg} src={clock}/>
                            <p className={styles.timeNum}>4분전</p>
                        </div>
                    </div>
                    <button className={styles.exchangeBtn}>코끼리톡으로 교환하기</button>
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