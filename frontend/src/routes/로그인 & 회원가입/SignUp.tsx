import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/loginAndSignup/Signup.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../img/cokkiriLogo.png"
import PriceBox from "../../component/trade/PriceBox";
import TextInput from "../../component/common/TextInput";
import Button from "../../component/common/Button";
import Message from "../../component/로그인 & 회원가입/Message";


const SignUp = () => {

    interface UserInfo {
        email: string;
        password: string;
        name: string;
        birth: number;
        nickname: string;
        phonenumber: number;
    }

    const [passwordCheck, setpasswordCheck] = useState<boolean>(undefined);

    const [userInfo, setuserInfo] = useState<UserInfo>(null);

    const navigate = useNavigate();

    const signUpButtonClick = () => {
        navigate(`/signup/emailcheck`)
    }

    const onChangeEmail = (e) => {
        setuserInfo((prevState) => {
            return {...prevState, email: e.target.value}
        })
    }

    const onChangePassword = (e) => {
        setuserInfo((prevState) => {
            return {...prevState, password: e.target.value}
        })
    }

    const onChangeCheckPassword = (e) => {
        if (userInfo.password === e.target.value) {
            setpasswordCheck(true);
            console.log("OK")
        } else {
            setpasswordCheck(false);
            console.log("비밀번호 일치하지 않음")
        }
    }

    return (
        <div className={styles.signup}>
            <div className={styles.signupHeader}>
                <div className={styles.signup_1}>코끼리 ID 회원가입</div>
                <div className={styles.signup_2}><br/></div>
                <div className={styles.signup_3}>회원정보 입력</div>

            </div>
            <div className={styles.userInfo}>

                <TextInput placeholder={"이메일"} onBlur={onChangeEmail}/>
                <TextInput placeholder={"비밀번호"} onBlur={onChangePassword}/>
                <TextInput placeholder={"비밀번호 확인"} onBlur={onChangeCheckPassword}/>
                {(passwordCheck === undefined && <Message passwordCheck={passwordCheck} content={""}/>)
                    ||
                    (passwordCheck ?
                        <Message passwordCheck={passwordCheck} content={"비밀번호가 일치합니다"}/>
                        :
                        <Message passwordCheck={passwordCheck} content={"비밀번호가 일치하지 않습니다"}/>)}


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