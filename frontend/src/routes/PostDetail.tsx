import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/post/PostDetail.module.css"
import profileImg from "../img/profileImg.png"
import spamImg from "../img/spam.png"

const PostDetail = () => {
    return (
        <>
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
                <div className={styles.postDetail}>
                    <h2 className={styles.postDetailTitle}>스팸 클래식 중간크기</h2>
                    <div className={styles.postDetailCategory}>식품 </div>
                    <div className={styles.postDetailPrice}>4500원</div>
                    <div className={styles.postDetailContent}>글 본문</div>
                    <div className={styles.postDetailTag}>해시태그</div>
                    <div className={styles.postDetailTag}>해시태그</div>

                </div>
            </section>
        </article>
        </>
    );
}

export default PostDetail;