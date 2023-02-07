import React, {useState, useEffect, useMemo, useCallback, forwardRef, useImperativeHandle} from 'react';
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
import useGeoLocation from "../../hooks/useGeolocation"

import axios from "axios";
import {forEach} from "list";

const SignUp = () => {

    interface UserInfo {
        email: string;
        password: string;
        userName: string;
        birthDate: number;
        nickname: string;
        phoneNumber: string;
        userLoginType: string;
    }

    /**
     * Type:
     * 1) invalid : 헤딩 필드가 조건에 부합하지 않아 유효하지 않을때
     * 2) valid : 헤딩 필드가 유효하여 통과할때
     * 3) duplicated : 헤딩 필드가 중복일때
     * 4) gmail : 구글 이메일일때
     */
    type checkEmailTypes = 'invalid' | 'valid' | 'gmail' | 'duplicated'
    type checkNicknameTypes = 'invalid' | 'valid' | 'duplicated'
    type checkPhoneNumberTypes = 'invalid' | 'valid' | 'duplicated'


    //각 항목들의 유효성 체크와
    //성공인지, 실패인지에 따라 UI(실패하면 빨간색, 성공하면 초록색)를 결정해주는 Boolean
    interface ValidationCheck {
        emailCheck: checkEmailTypes;
        emailCheckBoolean: boolean;
        passwordCheck: boolean;
        passwordCheckBoolean: boolean;

        nameAndBirthCheck: boolean;
        nameAndBirthCheckBoolean: boolean;

        nicknameCheck: checkNicknameTypes;
        nicknameCheckBoolean: boolean;

        phoneNumberCheck: checkPhoneNumberTypes;
        phoneNumberCheckBoolean: boolean;

    }
    const location = useGeoLocation();
    const [validationCheck, setValidationCheck] = useState<ValidationCheck>(
        {
            emailCheck: undefined,
            emailCheckBoolean: undefined,

            passwordCheck: undefined,
            passwordCheckBoolean: undefined,

            nameAndBirthCheck: undefined,
            nameAndBirthCheckBoolean: undefined,

            nicknameCheck: undefined,
            nicknameCheckBoolean: undefined,

            phoneNumberCheck: undefined,
            phoneNumberCheckBoolean: undefined,

        }
    );

    const [passwordReCheck, setpasswordReCheck] = useState<boolean>(undefined);

    // const [emailCheck, setEmailCheck] = useState<string>(undefined);
    const [userInfo, setuserInfo] = useState<UserInfo>(null);
    const navigate = useNavigate();

    //이메일 중복 체크 함수
    async function CheckEmailDuplicated(email: object) {
        try {
            const res = await axios.post("http://localhost:8080/auth/check-email", email);
            console.log(res);
            const result = res.data;
            const duplicated = result.exists

            if (duplicated) //중복인 경우 -> true 반환
            {
                setValidationCheck((prevState) => {
                    console.log("프리베이트 슽이트",prevState)

                    return {...prevState, emailCheck: "duplicated", emailCheckBoolean: false}
                })
            } else //중복이 아닌 경우 -> false 반환
            {

                setValidationCheck((prevState) => {
                    return {...prevState, emailCheck: "valid", emailCheckBoolean: true}
                })
                setuserInfo((prevState) => {
                    return {
                        ...prevState, email: email["email"]
                        , userLoginType: "EMAIL"
                    }
                })
            }

        } catch (err) {
            console.log(err);
            alert('서버와 통신 실패');

        }
    }

    async function CheckNickNameDuplicated(nickname: object) {

        try {
            const res = await axios.post("http://localhost:8080/auth/check-nickname", nickname);
            const result = res.data;
            const duplicated = result.exists

            if (duplicated) //중복인 경우 -> true 반환
            {
                setValidationCheck((prevState) => {
                    return {...prevState, nicknameCheck: "duplicated", nicknameCheckBoolean: false}
                })
                console.log(validationCheck);
            } else //중복이 아닌 경우 -> false 반환
            {
                setValidationCheck((prevState) => {
                    return {...prevState, nicknameCheck: "valid", nicknameCheckBoolean: true}
                })
                setuserInfo((prevState) => {
                    return {
                        ...prevState, nickname: nickname["nickname"]
                    }
                })
            }

        } catch (err) {
            console.log(err);
            alert('서버와 통신 실패');

        }
    }

    async function CheckPhoneNumberDuplicated(phoneNumber: object) {

        try {
            const res = await axios.post("http://localhost:8080/auth/check-phone", phoneNumber);
            const result = res.data;
            const duplicated = result.exists

            if (duplicated) //중복인 경우 -> true 반환
            {
                setValidationCheck((prevState) => {
                    return {...prevState, phoneNumberCheck: "duplicated", phoneNumberCheckBoolean: false}
                })
            } else //중복이 아닌 경우 -> false 반환
            {
                setValidationCheck((prevState) => {
                    return {...prevState, phoneNumberCheck: "valid", phoneNumberCheckBoolean: true}
                })
                setuserInfo((prevState) => {
                    return {
                        ...prevState,
                        // address : {
                        //     addressName: "wd",
                        //     postalAddress:"99",
                        //     latitude: JSON.stringify(location.coordinates.lat) ,
                        //     longitude: JSON.stringify(location.coordinates.lng),
                        // }
                        phoneNumber: phoneNumber["phoneNumber"],
                    }
                })

            }
        } catch (err) {
            console.log(err);
            alert('서버와 통신 실패');
        }
    }


    const signUpButtonClick = (e) => {

        //유효성 검증이 모두 성공했을 경우 (모두 true일 경우) 회원가입
        if (validationCheck.emailCheckBoolean &&
            validationCheck.passwordCheckBoolean &&
            validationCheck.nameAndBirthCheckBoolean &&
            validationCheck.nicknameCheckBoolean &&
            validationCheck.phoneNumberCheckBoolean) {
            MailConfirm({"email" : userInfo.email})
            navigate('/signup/emailcheck', {state : userInfo})

        } else { //유효성 검증 하나라도 실패한 경우 회원가입 실패
            alert("회원가입 정보를 모두 만족시켜주세요")
        }
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
            setValidationCheck((prevState) => {
                return {...prevState, passwordCheck: true, passwordCheckBoolean: true}
            })
            setuserInfo((prevState) => {
                return {...prevState, password: e.target.value}
            })
        } else {
            setValidationCheck((prevState) => {
                return {...prevState, passwordCheck: false, passwordCheckBoolean: false}
            })
        }
    }

    const onChangeCheckPassword = (e) => {
        if (userInfo.password === e.target.value) {
            setpasswordReCheck(true);
        } else {
            setpasswordReCheck(false);
        }
    }

    const onChangeName = (e) => {
        let inputName = e.target.value;
        //한글자 이상 작성했을때
        if (inputName.length > 0) {
            setuserInfo((prevState) => {
                return {...prevState, userName: e.target.value,
                   }
            })

            setValidationCheck((prevState) => {
                return {...prevState, nameAndBirthCheck: true, nameAndBirthCheckBoolean: true}
            })

        }
        //한글자도 작성하지 않았을때 에러
        else {
            setValidationCheck((prevState) => {
                return {...prevState, nameAndBirthCheck: false, nameAndBirthCheckBoolean: false}
            })
        }

    }

    const onChangeBirth = (e) => {
        let inputBirth = e.target.value;
        //생일 6자리 입력했을때 올바른 값
        if (inputBirth.length === 6) {
            setuserInfo((prevState) => {
                return {...prevState, birthDate: e.target.value}
            })

            setValidationCheck((prevState) => {
                return {...prevState, nameAndBirthCheck: true, nameAndBirthCheckBoolean: true}
            })

        } //생일 6자리가 아닐때
        else {
            setValidationCheck((prevState) => {
                return {...prevState, nameAndBirthCheck: false, nameAndBirthCheckBoolean: false}
            })
        }

    }

    const onChangeNickname = (e) => {
        let inputNickname = e.target.value;

        //이메일 유효성 검사를 통과했을때, (형식에 맞는 경우 true 리턴)
        if (inputNickname.length > 0) {
            //닉네임 중복체크 백엔드 통신
            //string인 inputNickname을 json형태의 객체로 변환
            let jsonObj = {"nickname": inputNickname};
            //변환한 json 객체로 이메일 중복체크
            CheckNickNameDuplicated(jsonObj);

        } else //닉네임 유효성 검사 실패했을때
        {
            setValidationCheck((prevState) => {
                return {...prevState, nicknameCheck: "invalid", nicknameCheckBoolean: false}
            })
        }
    }

    const onChangePhoneNumber = (e) => {
        let inputPhoneNumber = e.target.value;

        let phoneNumberValidation = /^(01[016789]{1})[0-9]{3,4}[0-9]{4}$/;
        //핸드폰번호 유효성 검사를 통과했을때, (형식에 맞는 경우 true 리턴)
        if (phoneNumberValidation.test(inputPhoneNumber)) {
            //중복체크 백엔드 통신
            //string type인 inputPhonenumber을 json형태의 객체로 변환
            let jsonObj =
                {"phoneNumber": inputPhoneNumber};

            //변환한 json 객체로 이메일 중복체크
            CheckPhoneNumberDuplicated(jsonObj);


        } else //유효성 검사 실패했을때
        {
            setValidationCheck((prevState) => {
                return {...prevState, phoneNumberCheck: "invalid", phoneNumberCheckBoolean: false}
            })
        }

    }

    async function MailConfirm(jsonEmail:object) {
        try {
            const res = await axios.post("http://localhost:8080/auth/mailConfirm", jsonEmail);

            const result = {
                status: res.status + "-" + res.statusText,
                headers: res.headers,
                data: res.data,
            };

            if(result.data.success)
            {
                console.log("이메일 전송")
            }
            else{
                console.log("이메일 전송 실패2")
            }

        } catch (err) {
            console.log(err);
            alert('이메일 전송 실패.');

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
                <TextInput type ={"text"} placeholder={"이메일"} onBlur={onChangeEmail}/>
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


                <TextInput type ={"password"} placeholder={"비밀번호"} onBlur={onChangePassword}/>
                {(validationCheck.passwordCheck === undefined &&
                        <Message validCheck={validationCheck.passwordCheckBoolean} content={""}/>)
                    ||
                    (validationCheck.passwordCheck ?
                        <Message validCheck={validationCheck.passwordCheckBoolean} content={"✔ 안전한 비밀번호입니다."}/>
                        :
                        <Message validCheck={validationCheck.passwordCheckBoolean}
                                 content={"❌ 숫자+영문자+특수문자 조합으로 8자리 이상 입력해주세요."}/>)}

                <TextInput type ={"password"} placeholder={"비밀번호 확인"} onBlur={onChangeCheckPassword}/>
                {(passwordReCheck === undefined && <Message validCheck={passwordReCheck} content={""}/>)
                    ||
                    (passwordReCheck ?
                        <Message validCheck={passwordReCheck} content={"✔ 비밀번호가 일치합니다"}/>
                        :
                        <Message validCheck={passwordReCheck} content={"❌ 비밀번호가 일치하지 않습니다"}/>)}


                <section className={styles.userNameBirth}>
                    <TextInput type ={"text"} placeholder={"이름"} onBlur={onChangeName}/>
                    <TextInput type ={"text"} placeholder={"생일 6자리"} onBlur={onChangeBirth}/>
                </section>
                {(validationCheck.nameAndBirthCheck === undefined &&
                        <Message validCheck={validationCheck.nameAndBirthCheckBoolean} content={""}/>)
                    ||
                    (validationCheck.nameAndBirthCheck ?
                        <Message validCheck={undefined} content={""}/>
                        :
                        <Message validCheck={validationCheck.nameAndBirthCheckBoolean}
                                 content={"❌ 이름과 생일을 올바르게 입력해주세요"}/>)}

                <TextInput type ={"text"} placeholder={"닉네임"} onBlur={onChangeNickname}/>
                {(validationCheck.nicknameCheck === undefined &&
                        <Message validCheck={validationCheck.nicknameCheckBoolean} content={""}/>)
                    ||
                    (validationCheck.nicknameCheck === "valid" &&
                        <Message validCheck={validationCheck.nicknameCheckBoolean} content={"✔ 사용 가능한 닉네임입니다."}/>)
                    ||
                    (validationCheck.nicknameCheck === "invalid" &&
                        <Message validCheck={validationCheck.nicknameCheckBoolean} content={"❌ 닉네임은 한글자 이상이어야합니다."}/>)
                    ||
                    (validationCheck.nicknameCheck === "duplicated" &&
                        <Message validCheck={validationCheck.nicknameCheckBoolean} content={"❌ 이미 가입된 닉네임입니다."}/>)}

                <TextInput type ={"text"} placeholder={"전화번호"} onBlur={onChangePhoneNumber}/>
                {(validationCheck.phoneNumberCheck === undefined &&
                        <Message validCheck={validationCheck.phoneNumberCheckBoolean} content={""}/>)
                    ||
                    (validationCheck.phoneNumberCheck === "valid" &&
                        <Message validCheck={validationCheck.phoneNumberCheckBoolean} content={"✔ 사용 가능한 전화번호 입니다."}/>)
                    ||
                    (validationCheck.phoneNumberCheck === "invalid" &&
                        <Message validCheck={validationCheck.phoneNumberCheckBoolean}
                                 content={"❌ 유효하지 않은 핸드폰 번호입니다. 예시) 01012345678"}/>)
                    ||
                    (validationCheck.phoneNumberCheck === "duplicated" &&
                        <Message validCheck={validationCheck.phoneNumberCheckBoolean}
                                 content={"❌ 이미 가입된 핸드폰 번호입니다."}/>)}
            </div>

            <div className={styles.btnPlace}>
                <Button content={"회원가입"} className={"black"} onClick={signUpButtonClick}/>
            </div>

        </div>
    );
}


export default SignUp;