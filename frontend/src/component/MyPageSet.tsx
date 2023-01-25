import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/MyPage.module.css"
import {Outlet, useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../img/photoSelect.png"
import PriceBox from "../component/trade/PriceBox";
import profile2 from "../img/profile.jpeg"
import PostContainer from "../component/trade/PostContainer";
import axios from "axios";
import Api from "../utils/api"
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../index";
import {Simulate} from "react-dom/test-utils";
import input = Simulate.input;
import {setUserInfo, setUserNick, setUserProfile, setOnelineIntro, deleteUserInfo,logoutUserInfo} from "../store/userInfoReducer";
import {userInfo} from "os";
import TextInput from "./common/TextInput";
import Message from "./로그인 & 회원가입/Message";
import Modal from "../routes/로그인 & 회원가입/NeighborModal";
import {deleteToken,logoutToken} from "../store/jwtTokenReducer";
import {resetaddress1,resetaddress2} from "../store/userAddressInfoReducer";
import {resetTalkCard} from "../store/talkCardReducer";

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
    //모달창
    const [isOpenModal, setOpenModal] = useState<boolean>(false);



    const onClickToggleModal = useCallback(() => {
        setOpenModal(!isOpenModal);
    }, [isOpenModal]);
    //프로필사진
    const[profile,setProfile] = useState("")
    const fileInput = useRef(null)
    //한줄소개
    const[intro,setIntro] = useState("")
    //로그아웃
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
            const userInfo1={
                userId: userInfo.userId,
                newNickname: userInfo.newNickname
            }

            const res = await Api.patch("/user/nickname", userInfo1);
            setNewNick(res.data.newNickname)
            dispatch(setUserNick(res.data.newNickname))
            alert('닉넴 변경 성공');
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
                    ...prevState, userId: res.data.userDetail.id
                }
            })
        }
        catch (err){
            console.log(err);
            alert("실패??")
        }
    }



    // console.log("value",value);

    async function getMyPostList() {
        //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            const res = await Api.get('/user/posts?');
            console.log("내 게시글rdd",Object.keys(res.data.content).length);
            // @ts-ignore
            setNum(Object.keys(res.data.content).length);
        }
        catch (err)
        {
            console.log(err)
            alert("게시글 수 불러오기 실패");
        }
    }
    const onChangeImg = async (e: React.ChangeEvent<HTMLInputElement>) => {
        e.preventDefault();
        if(e.target.files){
            const uploadFile = e.target.files[0]
            console.log('uploadFile',uploadFile);
            const formData = new FormData()
            formData.append('imageFiles',uploadFile)
            const res = await axios.post("http://localhost:8080/auth/image/profileImage", formData);
            console.log("리턴 데이터 ", res.data.imageUrls[0])
            dispatch(setUserProfile(res.data.imageUrls[0]))
            const mbody = {
                userId : info.id,
                newImageUrl : res.data.imageUrls[0],
            }
            const res2 = await Api.patch("/user/imageUrl",mbody);
        }
    }

    async function oneLineIntro(inputIntro:string) {
        try{
            const intro={
                userId: info.id,
                description: inputIntro
            }
            const res = await Api.patch('/user/description',intro);
            console.log("한줄소개 리덕스",res.data.newDescription)
            console.log("한줄소개 리덕스2",info.onelineIntro)

        }
        catch (err)
        {
            console.log(err)
            alert("한줄소개 실패");
        }
    }
    const inputIntro= (e) => {
        let inputIntro = e.target.value;
        if (inputIntro.length > 0) {
            setIntro(inputIntro);
            dispatch(setOnelineIntro(inputIntro));
            oneLineIntro(inputIntro)
            console.log("한줄소개입니다.",info.onelineIntro)

        }
        else{
        }
    }
    console.log("한줄소개입니다.",info.onelineIntro)

    async function logOut() {
        try{
            const res = await Api.get('/logout');
            console.log(res);
            alert("로그아웃");
            dispatch(logoutToken());
            dispatch(logoutUserInfo());
            dispatch(resetaddress1());
            dispatch(resetaddress2());
            dispatch(resetTalkCard());
            navigate(`/`)
        }
        catch (err)
        {
            console.log(err)
            alert("로그아웃 실패");
        }
    }

    // logOut()
    return (
            <>
            <div className={styles.profile}>
                {isOpenModal && (
                    <Modal onClickToggleModal={onClickToggleModal}>
                        <embed type="text/html"  width="800" height="608"/>
                    </Modal>
                )}
                <div className={styles.profileImage}>
                    <img className={styles.Image} src={info.imageUrl} onClick={()=>{fileInput.current.click()}}/>
                    <form>
                        <input type="file" style={{display:'none'}} accept="image/*" onChange={onChangeImg} ref={fileInput}/>
                    </form>
                </div>
                <div className={styles.userInfo}>
                    <div className={styles.nickName}>{newNick}</div>
                    <TextInput placeholder={info.nickname} onChange={onChangeNickname}/>
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
                    <input className={styles.intro} placeholder={info.onelineIntro} onChange={inputIntro} ></input>
                    <button className={styles.nickChangeBtn} onClick={logOut}>로그아웃</button>

                    <div className={styles.intro2}>
                        <div className={styles.i1}>
                            <p>게시글</p> <p className={styles.postNum}>{postNum}</p>
                        </div>
                        <div className={styles.i1}>
                            <p>상품 거래</p> <p className={styles.tradeNum}>8</p>
                        </div>
                    </div>
                    <button className={styles.gpsBox} onClick={() => onClickToggleModal()}>
                        동네 등록을 해주세요.
                    </button>
                </div>
            </div>
                <Outlet/>


            </>
    );
}

export default MyPage;