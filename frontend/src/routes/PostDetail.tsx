import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/post/PostDetail.module.css"
import profileImg from "../img/profileImg.png"
import spamImg from "../img/spam.png"
import Comments from "../component/comments/Comments";

const PostDetail = () => {
    return (
        <div className={styles.postDetail}>
            <article className={styles.post}>
                <section className={styles.postTop}>
                    <div className={styles.postTopProfile}>
                        <img className={styles.postTopProfileImg} src={profileImg}></img>
                        <div className={styles.postTopProfileInfo}>
                            <div className={styles.postTopNickname}>상도동파티피플</div>
                            <div className={styles.postTopAddress}>서울시 동작구 상도동</div>
                        </div>
                    </div>
                    <span className={styles.postTopReport}>신고</span>
                </section>
                <section className={styles.postBody}>
                    <div className={styles.postImg}>
                        <img className={styles.postBodyImg} src={spamImg}></img>
                        {/*<button></button>*/}
                        {/*<button>오른쪽으로 가기</button>*/}
                    </div>
                    <div className={styles.postDetailInfo}>
                        <h2 className={styles.postDetailTitle}>스팸 클래식 중간크기</h2>
                        <div className={styles.postDetailCategory}>식품</div>
                        <div className={styles.postDetailPrice}>4500원</div>
                        <div className={styles.postDetailContent}>3개월 전에 산 스팸 클래식 중간크기에요.<br/>
                            다른 통조림 류랑 교환하실분 !! 꼭 식품이 아니더라도 관심있으신 분 연락주세요 ㅎㅎ
                        </div>
                        <div className={styles.postDetailTag}>#스팸 #통조림 #고기 #스팸클래식</div>
                        <div className={styles.postDetailSwapCategory}> ⇌식품</div>
                    </div>

                </section>
                <section className={styles.postBottom}>
                    <span>좋아요 13</span>
                    <span>     27분전</span>
                    <span>   쪽지 수 3</span>
                    <button>코끼리톡으로 교환하기</button>
                </section>
            </article>
            <section className={styles.comments}>
                <h6>댓글</h6>
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