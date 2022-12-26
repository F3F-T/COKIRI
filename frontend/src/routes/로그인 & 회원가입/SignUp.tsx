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

import {stringify} from "querystring";
import axios from "axios";

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
    const [postResult, setPostResult] = useState(null);
    const navigate = useNavigate();


    const formatResponse = (result) => {
        //두번째 replace parameter는 filter같은 역할을 한다.
        //세번째 parameter는 원하는 공백
        JSON.stringify(result, null, 2);

    }

    async function postSignUpData() {
        try {
            const res = await axios.post("/auth/signup", userInfo);

            const result = {
                status: res.status + "-" + res.statusText,
                headers: res.headers,
                data: res.data,
            };
            console.log(result);
            // setPostResult(formatResponse(result));
        } catch (err) {
            console.log(err); ///

        }
    }


    const signUpButtonClick = () => {
        console.log(userInfo);
        postSignUpData();
    }

    //입력완료하면 값이 state에 저장된다.
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

    const onChangeName = (e) => {
        setuserInfo((prevState) => {
            return {...prevState, name: e.target.value}
        })
    }

    const onChangeBirth = (e) => {
        setuserInfo((prevState) => {
            return {...prevState, birth: e.target.value}
        })
    }

    const onChangeNickname = (e) => {
        setuserInfo((prevState) => {
            return {...prevState, nickname: e.target.value}
        })
    }

    const onChangePhoneNumber = (e) => {
        setuserInfo((prevState) => {
            return {...prevState, phonenumber: e.target.value}
        })
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
                        <Message passwordCheck={passwordCheck} content={"✔ 비밀번호가 일치합니다"}/>
                        :
                        <Message passwordCheck={passwordCheck} content={"❌ 비밀번호가 일치하지 않습니다"}/>)}


                <section className={styles.userNameBirth}>
                    <TextInput placeholder={"이름"} onBlur={onChangeName}/>
                    <TextInput placeholder={"생일"} onBlur={onChangeBirth}/>
                </section>

                <TextInput placeholder={"닉네임"} onBlur={onChangeNickname}/>
                <TextInput placeholder={"전화번호"} onBlur={onChangePhoneNumber}/>
            </div>

            <div className={styles.btnPlace}>
                <Button content={"회원가입"} className={"black"} onClick={signUpButtonClick}/>
            </div>

        </div>
    );
}


export default SignUp;