import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/MyPage.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../img/photoSelect.png"
import PriceBox from "../component/trade/PriceBox";
import profile from "../img/profile.jpeg"
import PostContainer from "../component/trade/PostContainer";
import Api from "../utils/api";

// interface TextInputProps {
//     init: string;
// }

const MyPage = () =>  {
    const [tab1, setTab] = useState<string>('curr');

    const navigate = useNavigate();

    function setDealTab(tab) {
        setTab(tab)
        console.log(tab1)
        // return tab
    }

    async function readNickName(){
        try{
            const res = await  Api.get("/user")
            console.log("유저정보",res)
            alert("전송")
        }
        catch (err){
            console.log(err);
            alert("실패")
        }
    }

    return (
            <>
            <div className={styles.profile}>
                <button onClick={readNickName}/>
                <div className={styles.profileImage}>
                    <img className={styles.Image} src={profile}/>
                </div>
                <div className={styles.userInfo}>
                    <input className={styles.nickName} placeholder={"닉네임"}></input>
                    <input className={styles.intro} placeholder={"한 줄 소개를 입력하세요."}></input>
                    <div className={styles.intro2}>
                        <div className={styles.i1}>
                            <p>게시글</p> <p className={styles.postNum}>5</p>
                        </div>
                        <div className={styles.i1}>
                            <p>상품 거래</p> <p className={styles.tradeNum}>8</p>
                        </div>
                    </div>

                </div>
            </div>

            </>
    );
}

export default MyPage;