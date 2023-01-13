import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/loginAndSignup/Login.module.scss"
import loginImg from "../../img/cokkiriLogo.png"
import {useNavigate} from "react-router-dom";
import TextInput from "../../component/common/TextInput";
import Button from "../../component/common/Button";
import axios from "axios";
import Modal from "./NeighborModal"
import GoogleButton from "./GoogleButton.js"
import { useGoogleLogin } from '@react-oauth/google'
import {useDispatch, useSelector} from "react-redux";
// import {gapi} from 'gapi-script';
import {setToken, deleteToken} from "../../store/jwtTokenReducer";
import {Rootstate} from "../../index";
import Api from "../../utils/api"
import {setUserInfo} from "../../store/userInfoReducer";

const Login = () => {

    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();

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

            //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            const res = await Api.post('/auth/login',userInfo);
            console.log(res)
            const accessToken = res.data;

            //jwt 토큰 redux에 넣기
            const jwtToken = accessToken.tokenInfo;
            console.log(jwtToken)

            dispatch(setToken(jwtToken));
            dispatch(setUserInfo(res.data.userInfo))
            console.log(store)
            alert("로그인 성공")
            navigate(`/`)
            }
            catch (err)
            {
                console.log(err)
                alert("로그인에 실패하였습니다." + `\n` +
                    "아이디 혹은 비밀번호를 다시 확인해주세요")
            }


            // console.log(store.jwtTokenReducer);
            // console.log(store.jwtTokenReducer.accessToken);
            // console.log(store.jwtTokenReducer.authenticated);
            // console.log(store.jwtTokenReducer.accessTokenExpiresIn);

    }

    const handleClick= () => {
        console.log(userInfo);
        postLoginData();
    }

    const googleClick=()=>{
        googleLogin();
    }
    // googleLogin();
    const url = google;
    interface googleUserInfo {
        email: string;
        name: string;
    }
    const [userInfoG, setUserInfoG] = useState<googleUserInfo>(null);

    const login2 = useGoogleLogin({
        onSuccess: async response => {
            try {
                const res = await axios.get("https://www.googleapis.com/oauth2/v3/userinfo", {
                    headers: {
                        "Authorization": `Bearer ${response.access_token}`
                    }
                })
                const data = res.data
                console.log("d",data);

                setUserInfoG((prevState) => {
                    return {
                        ...prevState, email:data.email , name:data.name
                    }

                })
                console.log("유저정보",userInfoG)
                setUserInfoG((prevState) => {
                    return {
                        ...prevState, email:data.email , name:data.name
                    }

                })
                const res1 = await axios.post("http://localhost:8080/auth/google_login", userInfoG)
                console.log("res2...", res1)
                // if(userInfoG != null){
                //     console.log("유저정보",userInfoG)
                //     const res1 = await axios.post("http://localhost:8080/auth/google_login",userInfoG)
                //     console.log("res2...",res1)
                // }
                }
            catch (err) {
                console.log(err)
            }
        }
    });
    // async function Login3(data) {
    //     try {
    //         console.log("erㅇㅇㅇㅇr")
    //
    //         setUserInfoG((prevState) => {
    //             return {
    //                 ...prevState, email:data.email , name:data.name
    //             }
    //
    //         })
    //         const res1 = await axios.post("http://localhost:8080/auth/google_login", userInfoG)
    //         console.log("res2...", res1)
    //     } catch(err) {
    //         console.log("err",err)
    //     }
    //
    // }
    // const f3 =()=>{
    //     return new Promise((res,rej)=>{
    //         setTimeout(()=>{
    //             res("ㄴㅇㄹㅁㄴㅇㄹ");
    //         },5000)
    //     })
    // }


    return (
        <><div className={styles.box}>
            {/*<button onClick={()=>{window.open(url)}}>2324234</button>*/}
            {/*<GoogleButton/>*/}
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
                {/*<Button className={"white"} onClick={()=>{  onClickToggleModal(); }} content={"구글 로그인"}/>*/}
                {/*<GoogleButton/>*/}
                {/*@ts-ignore*/}
                <Button className={"white"} onClick={login2} content={"구글 로그인"}/>
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