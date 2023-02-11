import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/MyPage.module.css"
import {Outlet, useLocation, useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../img/photoSelect.png"
import setting from "../img/setting.png"
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
import SettingModal from "../routes/로그인 & 회원가입/SettingModal";


const MyPage = () =>  {
    interface UserInfo {
        userId:number;
        newNickname: string;
    }
    type checkNicknameTypes = 'invalid' | 'valid' | 'duplicated'
    const [tab1, setTab] = useState<string>('next');

    function setDealTab(tab) {
        setTab(tab)
        console.log(tab1)
        // return tab
    }
    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const [userInfo, setuserInfo] = useState<UserInfo>(null);

    const {state} = useLocation(); //다른 유저꺼 받을 때


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
    const [isChild, setIsChild] = useState<number>(1);
    const onClickToggleModal = useCallback(() => {
        setOpenModal(!isOpenModal);
        setIsChild(1)
    }, [isOpenModal]);

    const onClickToggleModal2 = useCallback(() => {
        setOpenModal(!isOpenModal);
        setIsChild(3)

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
    if (! getMyPostList) {
        return null
    }


///////////
//     다른유저로 들어왔을때 서버에서 받아야되는 정보 : 유저아이디, postalAddress, 닉네임, 프사




///////////
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
                {isOpenModal && isChild==1 &&(
                    <SettingModal onClickToggleModal={onClickToggleModal} >
                        <embed type="text/html"  width="800" height="608"/>
                    </SettingModal>
                )}
                <div className={styles.profileImage}>
                    <img className={styles.Image} src={info.imageUrl} onClick={()=>{fileInput.current.click()}}/>
                    <form>
                        <input type="file" style={{display:'none'}} accept="image/*" onChange={onChangeImg} ref={fileInput}/>
                    </form>
                </div>
                <div className={styles.userInfo}>
                    <div className={styles.wheelBox}>
                        <div className={styles.nickName}>{newNick}</div>
                        {/*다른 유저 마이페이지 들어왔을때는 이게 나오면 안돼*/}
                        <button className={styles.wheelBox2} onClick={() => onClickToggleModal()}>
                            <img className={styles.wheel} src={setting}/>
                            <div className={styles.setting}>프로필 편집</div>
                        </button>

                    </div>
                    {/*<TextInput placeholder={info.nickname} onChange={onChangeNickname}/>*/}
                    {/*{(validationCheck.nicknameCheck === undefined &&*/}
                    {/*        <Message validCheck={validationCheck.nicknameCheckBoolean} content={""}/>)*/}
                    {/*    ||*/}
                    {/*    (validationCheck.nicknameCheck === "valid" &&*/}
                    {/*        <Message validCheck={validationCheck.nicknameCheckBoolean} content={"✔ 사용 가능한 닉네임입니다."}/>)*/}
                    {/*    ||*/}
                    {/*    (validationCheck.nicknameCheck === "invalid" &&*/}
                    {/*        <Message validCheck={validationCheck.nicknameCheckBoolean} content={"❌ 닉네임은 한글자 이상이어야합니다."}/>)*/}
                    {/*    ||*/}
                    {/*    (validationCheck.nicknameCheck === "duplicated" &&*/}
                    {/*        <Message validCheck={validationCheck.nicknameCheckBoolean} content={"❌ 이미 가입된 닉네임입니다."}/>)}*/}
                    {/*<button className={styles.nickChangeBtn} onClick={nicknameChange}>변경</button>*/}
                    {/*<input className={styles.intro} placeholder={info.onelineIntro} onChange={inputIntro}></input>*/}
                    <button className={styles.nickChangeBtn} onClick={logOut}>로그아웃</button>

                    <div className={styles.intro2}>
                        <div className={styles.i1}>
                            {/*다른유저일때 if문 걸어서 체크해야지*/}
                            <p>게시글</p> <p className={styles.postNum}>{postNum}</p>
                        </div>
                        <div className={styles.i1}>
                            <p>상품 거래</p> <p className={styles.tradeNum}>8</p>
                        </div>
                    </div>
                    {/*다른 유저면 다른 if문 하나 더 걸어서 분리*/
                        store.userAddressInfoReducer.oneWordAddress1 ==undefined?
                            <p className={styles.i2_2} onClick={() => onClickToggleModal()}> 동네 인증을 해주세요!</p>:
                            <p className={styles.i2}> {store.userAddressInfoReducer.oneWordAddress1} 주민이에요.</p>

                    }

                </div>

            </div>
                <div className={styles.menu}>
                    {/*<button className={`${styles["post"+(tab1 ==="curr"? "" : "active")]}`}  onClick={() =>{ setDealTab('curr'); navigate('/mypage');}}>게시글</button>*/}
                    {/*<button className={styles.zzimactive} onClick={()=>{navigate('/mypage/zzim')}}>관심 상품</button>*/}
                    {/*여기는 철웅이꺼 불러온거 띄워야지*/}

                    {tab1 === 'next' ? <button className={`${styles["post" + (tab1 === "next" ? "" : "active")]}`}
                                               onClick={() => {
                                                   setDealTab('next');navigate(`/mypage/${info.id}`)
                                               }}>게시글</button>
                        : <button className={`${styles["post" + (tab1 === "next" ? "" : "active")]}`}
                                  onClick={() => {
                                      setDealTab('next')
                                      ;navigate(`/mypage/${info.id}`)
                                  }}>게시글</button>
                    }
                    {/*다른 유저일 때는 다른 걸 띄워줘야돼 zzim말고 이모티콘과 함께 000님의 zzim목록은 보실 수 없어요*/}
                    {tab1 === 'curr' ? <button className={`${styles["zzim" + (tab1 === "curr" ? "" : "active")]}`}
                                               onClick={() => {
                                                   setDealTab('curr')
                                                   ;navigate(`/mypage/zzim/${info.id}`)
                                               }}>관심 상품</button>
                        : <button className={`${styles["zzim" + (tab1 === "curr" ? "" : "active")]}`}
                                  onClick={()=>{setDealTab('curr');navigate(`/mypage/zzim/${info.id}`)}}>관심 상품</button>
                    }
                </div>

                <Outlet/>


            </>
    );
}

export default MyPage;