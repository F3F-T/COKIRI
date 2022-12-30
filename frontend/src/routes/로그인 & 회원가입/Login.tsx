import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/loginAndSignup/Login.module.scss"
import loginImg from "../../img/cokkiriLogo.png"
import {useNavigate} from "react-router-dom";
import TextInput from "../../component/common/TextInput";
import Button from "../../component/common/Button";
import axios from "axios";
import Modal from "../../routes/로그인 & 회원가입/GoogleLoginModal"
// import GoogleLogin from 'react-google-login'
// import {gapi} from 'gapi-script';

const Login = () => {

    const [isOpenModal, setOpenModal] = useState<boolean>(false);

    const onClickToggleModal = useCallback(() => {
        setOpenModal(!isOpenModal);
    }, [isOpenModal]);



    const [email,setEmail] = useState('');
    const [google,setGoogle] = useState('');
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

    async function googleLogin(){
        try{
            const res = await axios.get("http://localhost:8080/auth/social_login/google")
            console.log("링크",res.data.url)
            setGoogle(res.data.url)
            alert("전송")
        }
        catch (err){
            console.log(err);
            alert("실패")
        }
    }
    async function postLoginData() {
        try {

            const res = await axios.post("http://localhost:8080/auth/login", userInfo);
            console.log("res",res);
            const accessToken = res.data;

            axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
            console.log("토큰값")
            console.log(accessToken);

            const result = {
                status: res.status + "-" + res.statusText,
                headers: res.headers,
                data: res.data,
            };

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

    const googleClick=()=>{
        googleLogin();
    }
    // googleLogin();
    const url = google


    return (
        <><div className={styles.box}>
            <button onClick={()=>{window.open(url)}}>2324234</button>

            {isOpenModal && (
                <Modal onClickToggleModal={onClickToggleModal}>
                    <embed type="text/html" src={url} width="800" height="608"/>
                </Modal>
            )}
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
                <Button className={"white"} onClick={()=>{ googleClick(); onClickToggleModal(); }} content={"구글 로그인"}/>
                {/*<GoogleLogin clientId={url} buttonText={"구글아이디로 로그인하기"}/>*/}
                    {/*onClickToggleModal*/}
            </div>
        </div>
        </>
    );
}
//

function Modal2(){
    return(
        <div className={styles.modal}>
            모달창입니다.
        </div>
    )
}
export default Login;