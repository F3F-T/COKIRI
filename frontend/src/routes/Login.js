import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/Login.module.css"

const Login = () => {
    return (
        <>
            <div>
                <section className={styles.header}>
                    <div className={styles.loginImg}>코끼리 이미지</div>
                    <h1 className={styles.startDescription1}>코끼리로 물물교환 시작하기</h1>
                    <h2 className={styles.startDescription2}>간편하게 가입하고 우리동네 물건들을 확인하세요</h2>
                </section>
                <section className={styles.contents}>
                    <div className={styles.loginContents}>
                        <div className={styles.loginFailMsg}>
                            <p>코끼리 ID 혹은 비밀번호를 잘못 입력하셨어요.</p>
                        </div>
                        <fieldset>
                            <div className={styles.id}>
                                <input type="text" className={styles.idInput} placeholder="코끼리 ID(이메일)을 입력해주세요."/>
                            </div>
                            <div className={styles.password}>
                                <input type="text" className={styles.passwordInput} placeholder="비밀번호를 입력해주세요."/>
                            </div>
                            <div className={styles.savedIdCheck}>
                                <label><input type="checkbox"/>로그인 상태 유지</label>
                            </div>
                            <div className={styles.btnLogin}>
                                <button type={"button"}>코끼리 로그인</button>
                            </div>
                        </fieldset>
                    </div>
                    <div className={styles.loginMenu}>
                        <span>회원가입</span>
                        <span>코끼리 ID 찾기</span>
                        <span>코끼리 비밀번호 찾기</span>
                    </div>
                </section>
                <section className={styles.footer}></section>
            </div>
        </>
    )
        ;
}

export default Login;