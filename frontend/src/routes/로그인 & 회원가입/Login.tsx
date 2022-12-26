import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/loginAndSignup/Login.module.css"
import loginImg from "../../img/cokkiriLogo.png"
import {useNavigate} from "react-router-dom";
import TextInput from "../../component/common/TextInput";
import Button from "../../component/common/Button";
import axios from "axios";



const Login = () => {

    const [email,setEmail] = useState('');

    interface UserInfo {
        email: string;
        password: string;

    }

    const [passwordCheck, setpasswordCheck] = useState<boolean>(undefined);

    const [userInfo, setuserInfo] = useState<UserInfo>(null);
    const [postResult, setPostResult] = useState(null);
    const navigate = useNavigate();

    const signInClick = () => {
        navigate(`/signup`)
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

    async function postLoginData() {
        try {
            const res = await axios.post("/auth/login", userInfo);

            const accessToken = res.data;
            axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
            console.log(accessToken);

            const result = {
                status: res.status + "-" + res.statusText,
                headers: res.headers,
                data: res.data,
            };
            console.log(result);
            alert('로그인 성공');
            // setPostResult(formatResponse(result));
        } catch (err) {
            console.log(err); ///
            alert('로그인 실패');

        }
    }

    const handleClick= () => {
        console.log(userInfo);
        postLoginData();
    }


    return (
        <>
            <div className={styles.loginAllContent}>
                <section className={styles.header}>
                    <img src={loginImg} className={styles.loginImg}></img>
                    <h1>코끼리로 물물교환 시작하기</h1>
                    <h2>간편하게 가입하고 우리동네 물건들을 확인하세요</h2>
                </section>
                <section className={styles.contents}>
                    <div className={styles.loginContents}>
                        <fieldset>
                            <div className={styles.idAndPassword}>
                                <TextInput placeholder={"코끼리 ID(이메일)을 입력해주세요."} onBlur={onChangeEmail}/>
                            <div className={styles.password}>
                                <TextInput placeholder={"비밀번호를 입력해주세요."} onBlur={onChangePassword}/>
                            </div>

                            </div>
                            <div className={styles.savedIdCheck}>
                                <label><input type="checkbox"/>  로그인 상태 유지</label>
                            </div>
                                <Button className={"black"} onClick={handleClick} content={"코끼리 로그인"}/>
                        </fieldset>
                    </div>
                    <div className={styles.loginMenu}>
                        <span onClick={signInClick}>회원가입</span>
                        <span>ID 찾기</span>
                        <span>비밀번호 찾기</span>
                    </div>
                </section>
                <Button className={"white"} onClick={handleClick} content={"구글 로그인"}/>
            </div>
        </>
    );
}
//
export default Login;