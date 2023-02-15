import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../../styles/loginAndSignup/EmailCheckOK.module.css";
import loginImg from "../../../img/cokkiriLogo.png"
import {useLocation, useNavigate} from "react-router-dom";

import {useDispatch, useSelector} from "react-redux";

import Button from "../../../component/common/Button";
import {Rootstate} from "../../../index";
import axios from "axios/index";
import Api from "../../../utils/api";

const FindIDResponse = () => {

    const navigate = useNavigate();
    const store = useSelector((state: Rootstate) => state);
    const dispatch = useDispatch();
    const {state} = useLocation();
    console.log(state)


    const loginClick = () => {
        navigate('/login')
    }


    return (
        <>
            <div className={styles.EmailCheckAllContent}>
                <section className={styles.header}>
                    <img src={loginImg} className={styles.loginImg}></img>
                    <h1>가입한 이메일입니다</h1>
                    <h2 className={styles.findEmailResponse}>{state.email}</h2>
                </section>
                <section className={styles.contents}>
                    <Button content={"로그인 하기"} className={"black"} onClick={loginClick}/>
                </section>
            </div>
        </>
    );
}

export default FindIDResponse;