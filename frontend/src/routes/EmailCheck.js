import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/EmailCheck.module.css";
import loginImg from "../img/cokkiriLogo.png";
import {useNavigate} from "react-router-dom";

const EmailCheck = () => {

    const navigate = useNavigate();

    const emailCheckClick = () => {
        navigate(`/signup/emailcheck/ok`)
    }


    return (
        <>
            <div className={styles.EmailCheckAllContent}>
                <section className={styles.header}>
                    <h1>인증 번호를 입력해주세요</h1>
                    <h2>해당 이메일로 인증 번호를 전송했어요.</h2>
                </section>
                <section className={styles.contents}>
                    <input type={"text"} className={styles.passwordInput} />
                    <button onClick={emailCheckClick}className={styles.btnLogin} type={"button"}>이메일 인증하기</button>
                </section>
            </div>
        </>
    );
}

export default EmailCheck;