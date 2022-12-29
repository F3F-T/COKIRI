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
        userName: string;
        birthDate: number;
        nickname: string;
        phoneNumber: number;
        userLoginType: string;
    }

    /**
     * emailCheck :
     * 1) invalid : 유효하지 않은 이메일일때
     * 2) valid : 유효한 이메일일때
     * 3) gmail : @gmail.com일때
     * 4) duplicated : 이메일이 중복일때
     */
    type checkEmailTypes = 'invalid' | 'valid' | 'gmail' | 'duplicated'



    //각 항목들의 유효성 체크와
    //성공인지, 실패인지에 따라 UI(실패하면 빨간색, 성공하면 초록색)를 결정해주는 Boolean
    interface ValidationCheck {
        emailCheck: checkEmailTypes;
        emailCheckBoolean: boolean;
        passwordCheck: boolean;
        passwordCheckBoolean : boolean;
        nameCheck: string;
        birthCheck: string;
        nicknameCheck: string;
        phoneNumberCheck: string;

    }

    const [validationCheck, setValidationCheck] = useState<ValidationCheck>(
        {
            emailCheck: undefined,
            emailCheckBoolean: undefined,

            passwordCheck: undefined,
            passwordCheckBoolean : undefined,

            birthCheck: undefined,

            nameCheck: undefined,
            nicknameCheck: undefined,
            phoneNumberCheck: undefined,

        }
    );

    const [passwordReCheck, setpasswordReCheck] = useState<boolean>(undefined);
    const [test, setTest] = useState(undefined);

        // const [emailCheck, setEmailCheck] = useState<string>(undefined);
    const [userInfo, setuserInfo] = useState<UserInfo>(null);
    const [postResult, setPostResult] = useState(null);
    const navigate = useNavigate();

    //
    async function postSignUpData() {
        try {
            const res = await axios.post("http://localhost:8080/auth/signup", userInfo);

            const result = {
                status: res.status + "-" + res.statusText,
                headers: res.headers,
                data: res.data,
            };
            console.log(result);
            alert('로그인 성공');
            navigate('/signup/emailcheck')

        } catch (err) {
            console.log(err); ///
            alert('로그인 실패');

        }
    }

    //이메일 중복 체크 함수
    async function CheckEmailDuplicated(email: object) {
        console.log("CheckEmailDuplicated 접근")
        console.log(email)
        try {
            const res = await axios.post("http://localhost:8080/auth/check-email", email);

            const result = res.data;
            const duplicated = result.exists

            if(duplicated) //중복인 경우 -> true 반환
            {
                setValidationCheck((prevState) => {
                    return {...prevState, emailCheck: "duplicated", emailCheckBoolean: false}
                })
            }
            else //중복이 아닌 경우 -> false 반환
            {
                setValidationCheck((prevState) => {
                    return {...prevState, emailCheck: "valid", emailCheckBoolean: true}
                })
                setuserInfo((prevState) => {
                    return {
                        ...prevState, email : email.toString()
                        , userLoginType: "EMAIL"
                    }
                })
            }

        } catch (err) {
            console.log(err);
            alert('서버와 통신 실패');

        }
    }


    const signUpButtonClick = (e) => {
        postSignUpData();
    }

    //입력완료하면 값이 state에 저장된다.
    const onChangeEmail = (e) => {
        let inputEmail = e.target.value;

        //이메일 유효성 검사
        let emailValidationCheck = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i

        //이메일 유효성 검사를 통과했을때, (형식에 맞는 경우 true 리턴)
        if (emailValidationCheck.test(inputEmail)) {
            //@gmail.com일때
            if (inputEmail.includes("@gmail.com")) {
                setValidationCheck((prevState) => {
                    return {...prevState, emailCheck: "gmail", emailCheckBoolean: false}
                })
            } else { //일반 이메일일때
                //이메일 중복체크 백엔드 통신
                //string인 inputEmail을 json형태의 객체로 변환
                let jsonObj = {"email": inputEmail};

                //변환한 json 객체로 이메일 중복체크
                CheckEmailDuplicated(jsonObj);

            }
        } else //이메일 유효성 검사 실패했을때
        {
            setValidationCheck((prevState) => {
                return {...prevState, emailCheck: "invalid", emailCheckBoolean: false}
            })
        }


    }

    const onChangePassword = (e) => {

        let inputPassword = e.target.value;

        //숫자+영문자+특수문자 조합으로 8자리 이상 입력
        const passwordValidation = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/

        //유효성 검사 통과할때(안전할때)
        if (passwordValidation.test(inputPassword)) {
            console.log("안전해")
            setValidationCheck((prevState) => {
                return {...prevState, passwordCheck: true , passwordCheckBoolean: true}
            })
            setuserInfo((prevState) => {
                return {...prevState, password: e.target.value}
            })
        }else{
            setValidationCheck((prevState) => {
                return {...prevState, passwordCheck: false , passwordCheckBoolean: false}
            })
        }

    }
    const onChangeCheckPassword = (e) => {
        if (userInfo.password === e.target.value) {
            setpasswordReCheck(true);
            console.log("OK")
        } else {
            setpasswordReCheck(false);
            console.log("비밀번호 일치하지 않음")
        }
    }

    const onChangeName = (e) => {
        setuserInfo((prevState) => {
            return {...prevState, userName: e.target.value}
        })

    }

    const onChangeBirth = (e) => {
        setuserInfo((prevState) => {
            return {...prevState, birthDate: e.target.value}
        })
    }

    const onChangeNickname = (e) => {
        setuserInfo((prevState) => {
            return {...prevState, nickname: e.target.value}
        })
    }

    const onChangePhoneNumber = (e) => {
        setuserInfo((prevState) => {
            return {...prevState, phoneNumber: e.target.value}
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
                {(validationCheck.emailCheck === undefined &&
                        <Message validCheck={validationCheck.emailCheckBoolean} content={""}/>)
                    ||
                    (validationCheck.emailCheck === "valid" &&
                        <Message validCheck={validationCheck.emailCheckBoolean} content={"✔ 사용 가능한 이메일입니다."}/>)
                    ||
                    (validationCheck.emailCheck === "gmail" &&
                        <Message validCheck={validationCheck.emailCheckBoolean}
                                 content={"❌ gmail 계정은 Google 로그인을 이용해주세요."}/>)
                    ||
                    (validationCheck.emailCheck === "invalid" &&
                        <Message validCheck={validationCheck.emailCheckBoolean} content={"❌ 유효하지 않은 이메일입니다."}/>)
                    ||
                    (validationCheck.emailCheck === "duplicated" &&
                        <Message validCheck={validationCheck.emailCheckBoolean} content={"❌ 이미 가입된 이메일입니다."}/>)}


                <TextInput placeholder={"비밀번호"} onBlur={onChangePassword}/>
                {(validationCheck.passwordCheck === undefined && <Message validCheck={validationCheck.passwordCheckBoolean} content={""}/>)
                    ||
                    (validationCheck.passwordCheck ?
                        <Message validCheck={validationCheck.passwordCheckBoolean} content={"✔ 안전한 비밀번호입니다."}/>
                        :
                        <Message validCheck={validationCheck.passwordCheckBoolean} content={"❌ 숫자+영문자+특수문자 조합으로 8자리 이상 입력해주세요."}/>)}

                <TextInput placeholder={"비밀번호 확인"} onBlur={onChangeCheckPassword}/>
                {(passwordReCheck === undefined && <Message validCheck={passwordReCheck} content={""}/>)
                    ||
                    (passwordReCheck ?
                        <Message validCheck={passwordReCheck} content={"✔ 비밀번호가 일치합니다"}/>
                        :
                        <Message validCheck={passwordReCheck} content={"❌ 비밀번호가 일치하지 않습니다"}/>)}


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