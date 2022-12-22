import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/loginAndSignup/Login.module.css"
import loginImg from "../../img/cokkiriLogo.png"
import {useNavigate} from "react-router-dom";
import TextInput from "../../component/common/TextInput";
import Button from "../../component/common/Button";



const Login = () => {

    const [email,setEmail] = useState('');

    const navigate = useNavigate();

    const signInClick = () => {
        navigate(`/signup`)
    }

    const onChangeEmail = (e) => {
        console.log(e.target.value);
        setEmail(e.target.value);
    }

    const handleChange = (e: React.FormEvent<HTMLInputElement>) => {
        const newValue = e.currentTarget.value;
        console.log(newValue);
    }

    const handleClick= (e: React.MouseEvent<HTMLButtonElement,MouseEvent>) => {
        console.log(e.target);
        console.log(e.currentTarget);
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
                                <TextInput placeholder={"코끼리 ID(이메일)을 입력해주세요."} onChange={handleChange}/>
                            <div className={styles.password}>
                                <TextInput placeholder={"비밀번호를 입력해주세요."}/>
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