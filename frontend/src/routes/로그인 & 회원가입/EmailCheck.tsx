import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/loginAndSignup/EmailCheck.module.css";
import loginImg from "../../img/cokkiriLogo.png";
import {useNavigate, useLocation} from "react-router-dom";
import Button from "../../component/common/Button";
import axios from "axios";
import TextInput from "../../component/common/TextInput";
import Message from "../../component/로그인 & 회원가입/Message";
import classNames from "classnames/bind";
import {setEmail} from "../../store/userInfoReducer";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";

const EmailCheck = () => {

    const cx = classNames.bind(styles)

    const navigate = useNavigate();
    const {state} = useLocation();
    const jsonEmail:object = {"email" : state.email}
    const [code,setCode] = useState();
    const userInfo = state;
    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();

    async function SignUpData() {
        try {
            const res = await axios.post("http://localhost:8080/auth/signup", userInfo);

            const result = {
                status: res.status + "-" + res.statusText,
                headers: res.headers,
                data: res.data,
            };
            console.log(result);
            console.log(userInfo);
            alert('회원가입에 성공했습니다.');


        } catch (err) {
            console.log(err);
            alert('회원가입에 실패했습니다.');

        }
    }

    async function CodeConfirm(code:object){
        console.log(code)
        try {
            const res = await axios.post("http://localhost:8080/auth/codeConfirm", code);

            const result = {
                status: res.status + "-" + res.statusText,
                headers: res.headers,
                 data: res.data,
            };
            console.log(res)
            console.log(result);
            if(result.data.matches)
            {
                console.log("코드가 일치합니다")
                SignUpData();
                navigate(`/signup/emailcheck/ok`)
            }
            else{
                alert("코드가 일치하지않습니다")
            }

        } catch (err) {
            console.log(err);
            alert('코드가 일치하지않습니다');

        }
    }

    const onChangeCode = (e) => {
        const inputCode = e.target.value;
        setCode(inputCode);
        console.log(inputCode);
    }
    const emailCheckClick = async () => {

        const jsonCode: object = {"email": state.email, "code": code}
        dispatch(setEmail(state.email))
        console.log(jsonCode)
        CodeConfirm(jsonCode)

        // navigate(`/signup/emailcheck/ok`)
    }

    //코드 확인


    return (
        <>
            <div className={styles.EmailCheckAllContent}>
                <section className={styles.header}>
                    <h1>인증 번호를 입력해주세요</h1>
                    <h2>{state.email} 으로 인증 번호를 전송했어요.</h2>
                </section>
                <section className={styles.contents}>
                    <input type={"text"} className={cx('passwordInput')} onBlur={onChangeCode}/>
                    <Button className={"black"} content={"이메일 인증하기"} onClick={emailCheckClick} />
                </section>

            </div>
        </>
    );
}

export default EmailCheck;