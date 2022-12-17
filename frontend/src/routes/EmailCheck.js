import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/EmailCheck.module.css";
import loginImg from "../img/cokkiriLogo.png";

const EmailCheck = () => {
    return (
        <>
            <div className={styles.EmailCheckAllContent}>
                <section className={styles.header}>
                    <h1>인증 번호를 입력해주세요</h1>
                    <h2>해당 이메일로 인증 번호를 전송했어요.</h2>
                    {/*<div className={styles.loginFailMsg}>*/}
                    {/*    <p>코끼리 ID 혹은 비밀번호를 잘못 입력하셨어요.</p>*/}
                    {/*</div>*/}
                </section>
                <section className={styles.contents}>
                    <input type={"text"} className={styles.passwordInput} />
                    <button className={styles.btnLogin} type={"button"}>이메일 인증하기</button>

                </section>
            </div>
        </>
    );
}

export default EmailCheck;