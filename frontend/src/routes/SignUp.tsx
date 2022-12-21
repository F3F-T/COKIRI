import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/trade/Signup.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../img/cokkiriLogo.png"
import PriceBox from "../component/trade/PriceBox";


const SignUp = () => {
    const navigate = useNavigate();

    const signUpButtonClick = () => {
        navigate(`/signup/emailcheck`)
    }

    return (
        <div className={styles.signup}>
            <div className={styles.signupHeader}>
                <div className={styles.signup_1}>코끼리 ID 회원가입</div>
                <div className={styles.signup_2}><br/></div>
                <div className={styles.signup_3}>회원정보 입력</div>

            </div>
            <div className={styles.userInfo}>
                <div className={styles.userID}>
                    <input type="text" className={styles.userID_2} placeholder="이메일"/>
                </div>
                <div className={styles.userPW}>
                    <input type="text" className={styles.userID_2} placeholder="비밀번호"/>
                </div>
                <div className={styles.userPWC}>
                    <input type="text" className={styles.userID_2} placeholder="비밀번호 확인"/>
                </div>
                <div className={styles.userNameBirth}>
                    <div className={styles.userName}>
                        <input type="text" className={styles.userID_3} placeholder="이름"/>
                    </div>
                    <div className={styles.userBirth}>
                        <input type="text" className={styles.userID_3} placeholder="생일"/>
                    </div>
                </div>
                <div className={styles.userNick}>
                    <input type="text" className={styles.userID_2} placeholder="닉네임"/>
                </div>
                <div className={styles.userPN}>
                    <input type="text" className={styles.userID_2} placeholder="전화번호"/>
                </div>
            </div>
            <div className={styles.btnPlace}>
                <button className={styles.btn} onClick={signUpButtonClick}>회원가입</button>
            </div>

        </div>


    );
}

export default SignUp;