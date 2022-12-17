import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/Login.module.css"
import loginImg from "../img/cokkiriLogo.png"
import {useNavigate} from "react-router-dom";

const Login = () => {

    const navigate = useNavigate();


    const signInClick = () => {
        navigate(`/signup`)
    }

    return (
        <>
            <div className={styles.loginAllContent}>
                <section className={styles.header}>
                    <img src={loginImg} className={styles.loginImg}></img>
                    <h1>코끼리로 물물교환 시작하기</h1>
                    <h2>간편하게 가입하고 우리동네 물건들을 확인하세요</h2>
                    {/*<div className={styles.loginFailMsg}>*/}
                    {/*    <p>코끼리 ID 혹은 비밀번호를 잘못 입력하셨어요.</p>*/}
                    {/*</div>*/}
                </section>
                <section className={styles.contents}>
                    <div className={styles.loginContents}>
                        <fieldset>
                            <div className={styles.idAndPassword}>
                            <div className={styles.id}>
                                <input type="text" className={styles.idInput} placeholder="코끼리 ID(이메일)을 입력해주세요."/>
                            </div>
                            <div className={styles.password}>
                                <input type="text" className={styles.passwordInput} placeholder="비밀번호를 입력해주세요."/>
                            </div>
                            </div>
                            <div className={styles.savedIdCheck}>
                                <label><input type="checkbox"/>  로그인 상태 유지</label>
                            </div>
                                <button className={styles.btnLogin} type={"button"}>코끼리 로그인</button>
                        </fieldset>
                    </div>
                    <div className={styles.loginMenu}>
                        <span onClick={signInClick}>회원가입</span>
                        <span>ID 찾기</span>
                        <span>비밀번호 찾기</span>
                    </div>
                </section>
                <section className={styles.footer}></section>

                <button className={styles.btnGoogle} type={"button"}>구글 로그인</button>
            </div>
        </>
    );
}
//
export default Login;