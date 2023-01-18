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
import {setToken, deleteToken, logoutToken} from "../../store/jwtTokenReducer";
import {Rootstate} from "../../index";
import Api from "../../utils/api"
import {
    setUserInfo,
    setUserProfile,
    deleteUserInfo,
    setUserNick,
    setUserName,
    setOnelineIntro, logoutUserInfo,
} from "../../store/userInfoReducer";
import {
    parcelAddress1, parcelAddress2,
    setAddress1,
    setAddress2,
    setAddressName1, setAddressName2, setLat1, setLat2, setLng1, setLng2,
    setUserAddressInfo1, setUserAddressInfo2
} from "../../store/userAddressInfoReducer";

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

    // async function googleLogin(){
    //     try{
    //         const res = await axios.get("http://localhost:8080/auth/social_login/google")
    //         console.log("링크",res.data.url)
    //         setGoogle(res.data.url)
    //         alert("전송")
    //     }
    //     catch (err){
    //         console.log(err);
    //         alert("실패")
    //     }
    // }
    async function postLoginData() {
            //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            const res = await Api.post('/login',userInfo);
            console.log(res)
            const accessToken = res.data;
            //jwt 토큰 redux에 넣기
            const jwtToken = accessToken.tokenInfo;
            console.log(jwtToken)
            console.log("바뀐address",res.data.userInfo.address[1])
            dispatch(setToken(jwtToken));
            dispatch(setUserInfo(res.data.userInfo.userDetail))
            dispatch(setOnelineIntro(res.data.userInfo.userDetail.description))

            if(res.data.userInfo.address[0]!=null){
                dispatch(setUserAddressInfo1(res.data.userInfo.address[0].id))
                dispatch(setAddressName1(res.data.userInfo.address[0].addressName))
                dispatch(parcelAddress1(res.data.userInfo.address[0].postalAddress))
                dispatch(setLat1(res.data.userInfo.address[0].latitude))
                dispatch(setLng1(res.data.userInfo.address[0].longitude))
            }
            if(res.data.userInfo.address[1]!=null){
                dispatch(setUserAddressInfo2(res.data.userInfo.address[1].id))
                dispatch(setAddressName2(res.data.userInfo.address[1].addressName))
                dispatch(parcelAddress2(res.data.userInfo.address[1].postalAddress))
                dispatch(setLat2(res.data.userInfo.address[1].latitude))
                dispatch(setLng2(res.data.userInfo.address[1].longitude))
            }

            // dispatch(setAddress1(res.data.userInfo.address[0]))
            // dispatch(setAddress2(res.data.userInfo.address[1]))
            console.log("store",store)
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
    const googleLogin4 = useGoogleLogin({
        onSuccess: async (codeResponse) => {
            console.log(codeResponse)
            const googleCode = codeResponse.code;
            console.log("code", codeResponse.code)
            const res = await axios.get(`http://localhost:8080/auth/social_login/google?code=${googleCode}`)
            console.log("res", res)
        },
        flow: 'auth-code',
    })
    const googleLogin3 = useGoogleLogin({
        onSuccess: (codeResponse) => console.log(codeResponse),
        flow: 'auth-code'
    })
    const googleLogin = useGoogleLogin({
        onSuccess: async response => {
            try {
                const res = await axios.get("https://www.googleapis.com/oauth2/v3/userinfo", {

                    headers: {
                        "Authorization": `Bearer ${response.access_token}`
                    }
                })
                console.log("이건가??",response.access_token);
                const data = res.data
                console.log("d",data);
                console.log("code확인",res);
                console.log("code확인",);


                const googleUserInfo1 ={
                    token :response.access_token
                }
                console.log("유저정보",googleUserInfo1)
                const res1 = await axios.post("http://localhost:8080/auth/social_login/google", googleUserInfo1)
                console.log("res2...", res1)
                const jwtToken = res1.data.tokenInfo;
                console.log("토큰",jwtToken)
                console.log("구글유저정보", res1.data.userInfo)
                dispatch(logoutToken());
                dispatch(setToken(jwtToken));
                dispatch(logoutUserInfo())
                // dispatch(setUserInfo(res.data.userInfo))
                dispatch(setUserInfo(res1.data.userInfo))
                // dispatch(setUserNick(res1.data.userInfo.nickname))//얘는 뱉는거로
                // dispatch(setUserName(res1.data.userInfo.userName))
                // dispatch(setUserProfile(res1.data.userInfo.imageUrl))
                // dispatch(setOnelineIntro(res1.data.userInfo.description))
                navigate(`/`)
                }
            catch (err) {
                console.log(err)
            }
        }
    });


    return (
        <><div className={styles.box}>
            {/*{isOpenModal && (*/}
            {/*    <Modal onClickToggleModal={onClickToggleModal}>*/}
            {/*        <embed type="text/html" src={url} width="800" height="608"/>*/}
            {/*    </Modal>*/}
            {/*)}*/}
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
                                <TextInput type={"text"} placeholder={"코끼리 ID(이메일)을 입력해주세요."} onBlur={onChangeEmail}/>
                            <div className={styles.password}>
                                <TextInput type={"password"} placeholder={"비밀번호를 입력해주세요."} onBlur={onChangePassword}/>

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
                {/*@ts-ignore*/}
                <Button className={"white"} onClick={googleLogin} content={"구글 로그인"}/>
            </div>
        </div>
        </>
    );
}
//

export default Login;