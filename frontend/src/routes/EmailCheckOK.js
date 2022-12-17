import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/EmailCheckOK.module.css";
import loginImg from "../img/cokkiriLogo.png";
import {useNavigate} from "react-router-dom";

const EmailCheckOK = () => {

    const navigate = useNavigate();

    const startCokiri = () => {
        navigate(`/`)
    }
    return (
        <>
            <div className={styles.EmailCheckAllContent}>
                <section className={styles.header}>
                    <img src={loginImg} className={styles.loginImg}></img>
                    <h1>코끼리에 오신걸 환영해요.</h1>
                    <h2>동네 주민들과 물물교환을 시작해보세요!</h2>
                </section>
                <section className={styles.contents}>
                    <button onClick={startCokiri} className={styles.btn} type={"button"}>코끼리 시작하기</button>
                </section>
            </div>
        </>
    );
}

export default EmailCheckOK;