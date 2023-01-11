import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/MyPage.module.css"
import {Outlet, useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../img/photoSelect.png"
import PriceBox from "../component/trade/PriceBox";
import profile from "../img/profile.jpeg"
import PostContainer from "../component/trade/PostContainer";
import axios from "axios";
import Api from "../utils/api"
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../index";
import {Simulate} from "react-dom/test-utils";
import input = Simulate.input;
import {setUserInfo, setUserNick} from "../store/userInfoReducer";
import {userInfo} from "os";
import TextInput from "./common/TextInput";
import Message from "./로그인 & 회원가입/Message";

// interface TextInputProps {
//     init: string;
// }

const MyPage = () =>  {
    interface UserInfo {
        userId:number;
        newNickname: string;

    }
    type checkNicknameTypes = 'invalid' | 'valid' | 'duplicated'
    interface ValidationCheck {
        nicknameCheck: checkNicknameTypes;
        nicknameCheckBoolean: boolean;
    }
    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const [userInfo, setuserInfo] = useState<UserInfo>(null);

    const [validationCheck, setValidationCheck] = useState<ValidationCheck>(
        {
            nicknameCheck: undefined,
            nicknameCheckBoolean: undefined,
        }
    );
    useEffect(()=>{
        getMyPostList();
    },[])
    useEffect(()=>{
        readNickName()
    },[])
    const [newNick,setNewNick]=useState(info.nickname)

    const [postNum,setNum]=useState('');

    // useEffect(()=>{
    //     if (userInfo) {
    //         dispatch(setUserNick(userInfo.newNickname))
    //         console.log("리덕스222.", info.nickname)
    //     }
    // },[userInfo])
    console.log("리덕스.", info.nickname)
    console.log("useState.", newNick)
    if (! readNickName) {
        return null
    }
    if (! nicknameChange) {
        return null
    }
    if (! getMyPostList) {
        return null
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
    async function CheckNickNameDuplicated(nickname: object) {

        try {
            const res = await axios.post("http://localhost:8080/auth/check-nickname", nickname);
            const result = res.data;
            console.log("체크닉듀플리케잇 들어옴",result);
            const duplicated = result.exists

            if (duplicated) //중복인 경우 -> true 반환
            {
                setValidationCheck((prevState) => {
                    return {...prevState, nicknameCheck: "duplicated", nicknameCheckBoolean: false}
                })
            } else //중복이 아닌 경우 -> false 반환
            {
                setValidationCheck((prevState) => {
                    return {...prevState, nicknameCheck: "valid", nicknameCheckBoolean: true}
                })
                setuserInfo((prevState) => {
                    return {
                        ...prevState, newNickname: nickname["nickname"]
                    }
                })
                // dispatch(setUserNick(nickname["nickname"]))

            }

        } catch (err) {
            console.log(err);
            alert('서버와 통신 실패');

        }
    }

    async function nicknameChange() {
        try {
            console.log("닉넴체인지들어옴");

            const res = await Api.patch("/user/nickname", userInfo);

            const result = {
                status: res.status + "-" + res.statusText,
                headers: res.headers,
                data: res.data,
            };
            console.log(result);
            console.log("바뀐 유저정보", userInfo.newNickname);
            console.log("바뀐 닉넴정보", res.data.newNickname);
            setNewNick(res.data.newNickname)
            // dispatch(setUserNick(newNick))
            dispatch(setUserNick(res.data.newNickname))
            console.log("리덕스에 들어갔나?.", info)


            alert('닉넴 변경 성공');
            // if(info.nickname==undefined){
            //     console.log("?????.")
            //     dispatch(setUserNick(res.data.newNickname));
            //     dispatch(setUserNick( userInfo.newNickname));
            // }


        } catch (err) {
            console.log(err);
            alert('닉넴 변경 실패');

        }
    }


    async function readNickName(){
        try{
            const res = await  Api.get("/user")
            console.log("유저정보",res.data.id)
            setuserInfo((prevState) => {
                return {
                    ...prevState, userId: res.data.id
                }
            })
        }
        catch (err){
            console.log(err);
            alert("실패")
        }
    }



    // console.log("value",value);

    async function getMyPostList() {
        //interceptor를 사용한 방식 (header에 token값 전달)
        try{

            const res = await Api.get('/user/posts');
            console.log("내 게시글rdd",Object.keys(res.data.userPosts).length);
            // @ts-ignore
            setNum(Object.keys(res.data.userPosts).length);
        }
        catch (err)
        {
            console.log(err)
            alert("get 실패2");
        }
    }





    return (
            <>
            <div className={styles.profile}>
                <div className={styles.profileImage}>
                    <img className={styles.Image} src={profile}/>
                </div>
                <div className={styles.userInfo}>
                    <div className={styles.nickName}>{newNick}</div>
                    <TextInput placeholder={info.nickname} onBlur={onChangeNickname}/>
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
                    <button className={styles.nickChangeBtn} onClick={nicknameChange}>변경</button>
                    <input className={styles.intro} placeholder={"한 줄 소개를 입력하세요."}></input>
                    <div className={styles.intro2}>
                        <div className={styles.i1}>
                            <p>게시글</p> <p className={styles.postNum}>{postNum}</p>
                        </div>
                        <div className={styles.i1}>
                            <p>상품 거래</p> <p className={styles.tradeNum}>8</p>
                        </div>
                    </div>
                    <button className={styles.gpsBox} onClick={() => navigate('/neighborauth')}>
                        동네 등록을 해주세요.
                    </button>
                </div>
            </div>
                <Outlet/>


            </>
    );
}

export default MyPage;