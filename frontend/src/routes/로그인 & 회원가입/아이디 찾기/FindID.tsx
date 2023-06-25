import React, {useState, useEffect, useMemo, useCallback, forwardRef, useImperativeHandle} from 'react';
import styles from "../../../styles/loginAndSignup/Signup.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../../img/cokkiriLogo.png"
import PriceBox from "../../../component/trade/PriceBox";
import TextInput from "../../../component/common/TextInput";
import Button from "../../../component/common/Button";
import Message from "../../../component/로그인 & 회원가입/Message";
import {stringify} from "querystring";
import useGeoLocation from "../../../hooks/useGeolocation"

import axios from "axios";
import {forEach} from "list";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../../index";
import {setEmail, setPW} from "../../../store/userInfoReducer";
import Api from "../../../utils/api";

const FindID = () => {
    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();
    interface UserInfo {
        userName: string;
        phoneNumber: string;
    }

    /**
     * Type:
     * 1) invalid : 헤딩 필드가 조건에 부합하지 않아 유효하지 않을때
     * 2) valid : 헤딩 필드가 유효하여 통과할때
     * 3) duplicated : 헤딩 필드가 중복일때
     * 4) gmail : 구글 이메일일때
     */



    //각 항목들의 유효성 체크와
    //성공인지, 실패인지에 따라 UI(실패하면 빨간색, 성공하면 초록색)를 결정해주는 Boolean
    interface ValidationCheck {
        nameCheck: boolean;
        nameCheckBoolean: boolean;

        phoneNumberCheck: boolean;
        phoneNumberCheckBoolean: boolean;

    }
    const location = useGeoLocation();
    const [validationCheck, setValidationCheck] = useState<ValidationCheck>(
        {

            nameCheck: undefined,
            nameCheckBoolean: undefined,

            phoneNumberCheck: undefined,
            phoneNumberCheckBoolean: undefined,

        }
    );

    const [passwordReCheck, setpasswordReCheck] = useState<boolean>(undefined);

    // const [emailCheck, setEmailCheck] = useState<string>(undefined);
    const [userInfo, setuserInfo] = useState<UserInfo>(null);
    const navigate = useNavigate();

    async function findEmail() {
        try {
            const res = await Api.post("https://f3f-cokiri.site/auth/find/email", userInfo);
            console.log(res)
            if(res.status === 200)
            {
                navigate('/findid/response', {state : res.data})
            }


        } catch (err) {
            console.log(err);
            alert('해당 이름과 전화번호로 가입한 유저가 존재하지 않습니다');

        }
    }

    const signUpButtonClick = (e) => {

        //유효성 검증이 모두 성공했을 경우 (모두 true일 경우) 회원가입
        if (validationCheck.nameCheckBoolean &&
            validationCheck.phoneNumberCheckBoolean) {
            findEmail()

        } else { //유효성 검증 하나라도 실패한 경우 회원가입 실패
            alert("정보를 모두 올바르게 입력해주세요")
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
                return {...prevState, nameCheckBoolean: true}
            })

            }
        else{
            setValidationCheck((prevState) => {
                return {...prevState, nameCheckBoolean: false}
            })
        }
    }


    const onChangePhoneNumber = (e) => {
        let inputPhoneNumber = e.target.value;

        if(inputPhoneNumber.length>7)
        {
            setuserInfo((prevState) => {
                return {...prevState, phoneNumber: e.target.value,
                }
            })
            setValidationCheck((prevState) => {
                return {...prevState, phoneNumberCheckBoolean: true}
            })
        }
        else{
            setValidationCheck((prevState) => {
                return {...prevState, phoneNumberCheckBoolean: false}
            })
        }

    }



    return (
        <div className={styles.signup}>
            <div className={styles.signupHeader}>
                <div className={styles.signup_1}>비밀번호 찾기</div>
                <div className={styles.signup_2}><br/></div>
                <div className={styles.signup_3}>회원정보 입력</div>

            </div>
            <div className={styles.userInfo}>
                <TextInput type ={"text"} placeholder={"이름을 입력하세요"} onBlur={onChangeName}/>
                <TextInput type ={"text"} placeholder={"핸드폰 번호를 입력하세요"} onBlur={onChangePhoneNumber}/>


            </div>

            <div className={styles.btnPlace}>
                <Button content={"이메일 찾기"} className={"black"} onClick={signUpButtonClick}/>
            </div>

        </div>
    );
}


export default FindID;