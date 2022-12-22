import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/loginAndSignup/EmailCheck.module.css";
import loginImg from "../../img/cokkiriLogo.png";
import {useNavigate} from "react-router-dom";
import Button from "../../component/common/Button";

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
                    <Button className={"black"} content={"이메일 인증하기"} onClick={emailCheckClick} />
                </section>
            </div>
        </>
    );
}

export default EmailCheck;