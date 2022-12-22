import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/Signup.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../img/cokkiriLogo.png"
import PriceBox from "../../component/trade/PriceBox";
import TextInput from "../../component/common/TextInput";
import Button from "../../component/common/Button";


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
                <TextInput placeholder={"이메일"}/>
                <TextInput placeholder={"비밀번호"}/>
                <TextInput placeholder={"비밀번호 확인"}/>

                <div className={styles.userNameBirth}>
                    <TextInput placeholder={"이름"}/>
                    <TextInput placeholder={"생일"}/>
                </div>

                <TextInput placeholder={"닉네임"}/>
                <TextInput placeholder={"전화번호"}/>
            </div>

            <div className={styles.btnPlace}>
                <Button content={"회원가입"} className={"black"} onClick={signUpButtonClick}/>
            </div>

        </div>


    );
}

export default SignUp;