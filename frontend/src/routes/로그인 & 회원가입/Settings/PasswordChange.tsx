import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../../../styles/loginAndSignup/SettingRight.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../img/photoSelect.png"
import TextInput from "../../../component/common/TextInput";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../../index";
import Api from "../../../utils/api";
import {setUserNick} from "../../../store/userInfoReducer";
import axios from "axios";
import Message from "../../../component/로그인 & 회원가입/Message";
import {resetaddress1, resetaddress2} from "../../../store/userAddressInfoReducer";


const PwChange = () =>  {
    interface UserInfo {
        password: string;
    }

    const dispatch = useDispatch();
    const store = useSelector((state:Rootstate) => state);
    interface ValidationCheck {
        passwordCheck: boolean;
        passwordCheckBoolean: boolean;

    }
    const [validationCheck, setValidationCheck] = useState<ValidationCheck>(
        {
            passwordCheck: undefined,
            passwordCheckBoolean: undefined,
        }
    );
    const [passwordReCheck, setpasswordReCheck] = useState<boolean>(undefined);
    const [existPW, setExistPW] = useState<boolean>(undefined);
    const [userInfo, setuserInfo] = useState<UserInfo>(null);

    const onChangeCheckPassword2 = (e) => {
        if (userInfo.password === e.target.value) {
            setpasswordReCheck(true);
        } else {
            setpasswordReCheck(false);
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
        if (store.userInfoReducer.password === e.target.value) {
            setExistPW(true);
        } else {
            setExistPW(false);
        }
    }

    async function pwChanges() {

        if (validationCheck.passwordCheckBoolean){
            try{
                const pwChange= {
                    oldPassword: store.userInfoReducer.password,
                    newPassword: userInfo.password
                }
                const res = await Api.patch("user/password", pwChange);
                alert("변경 성공")
            }
            catch (err)
            {
                console.log(err)
                alert("변경 실패")
            }
        }
        else{
            alert("돌아가")

        }

    }
    return (
        <div className={styles.box1}>
            <div className={styles.ad1}>비밀번호 변경하기</div>
            <div className={styles.set}>
                <div className={styles.set1}>
                <p className={styles.p1}>이전 비밀번호</p>
                <input type={"password"} className={styles.inputPW} onBlur={onChangeCheckPassword}/>
                </div>
                {(existPW === undefined && <Message validCheck={existPW} content={""}/>)
                    ||
                    (existPW ?
                        <div className={styles.valCheck2}>
                            <Message validCheck={existPW} content={"✔ 비밀번호가 일치합니다"}/></div>
                        :
                        <div className={styles.valCheck2}>
                        <Message validCheck={existPW} content={"❌ 비밀번호가 일치하지 않습니다"}/></div>)}
            </div>
            <div className={styles.set}>
                <div className={styles.set2}>
                <p className={styles.p1}>새 비밀번호</p>
                <input type={"password"} className={styles.inputPW} onBlur={onChangePassword}/>
                </div>
                {(validationCheck.passwordCheck === undefined &&

                        <Message validCheck={validationCheck.passwordCheckBoolean} content={""}/>)
                    ||
                    (validationCheck.passwordCheck ?
                        <div className={styles.valCheck2}>
                            <Message validCheck={validationCheck.passwordCheckBoolean} content={"✔ 안전한 비밀번호입니다."}/></div>
                        :
                        <div className={styles.valCheck2}>
                        <Message validCheck={validationCheck.passwordCheckBoolean}
                                 content={"❌ 숫자+영문자+특수문자 조합으로 8자리 이상 입력해주세요."}/></div>)}
            </div>
            <div className={styles.set}>
                <div className={styles.set2}>
                <p className={styles.p1}>새 비밀번호 확인</p>
                <input type={"password"} className={styles.inputPW} onBlur={onChangeCheckPassword2}/>
                </div>
                {(passwordReCheck === undefined && <Message validCheck={passwordReCheck} content={""}/>)
                    ||
                    (passwordReCheck ?
                        <div className={styles.valCheck2}>
                            <Message validCheck={passwordReCheck} content={"✔ 비밀번호가 일치합니다"}/></div>
                        :
                        <div className={styles.valCheck2}>
                            <Message validCheck={passwordReCheck} content={"❌ 비밀번호가 일치하지 않습니다"}/></div>)}
            </div>
            <button className={styles.pwBtn} onClick={pwChanges}>비밀번호 변경</button>
            <button className={styles.lostPW}>비밀번호를 잊으셨나요?</button>

        </div>
    );
}

export default PwChange;