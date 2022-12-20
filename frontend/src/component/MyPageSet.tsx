import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/MyPage.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../img/photoSelect.png"
import PriceBox from "../component/trade/PriceBox";
import profile from "../img/profile.jpeg"
import PostContainer from "../component/trade/PostContainer";

// interface TextInputProps {
//     init: string;
// }

const MyPage = () =>  {
    const [tab1, setTab] = useState<string>('curr');

    const navigate = useNavigate();

    function setDealTab(tab){
        setTab(tab)
        console.log(tab1)
        // return tab
    }
    // const ref = useRef(null);
    // const [text, setText] = useState(init);
    // const [editable, setEditable] = useState(false);
    // const editOn = () => {
    //     setEditable(true);
    // };
    // const handleChange = (e) => {
    //     setText(e.target.value);
    // };
    // const handleKeyDown = (e) => {
    //     if (e.key === "Enter") {
    //         setEditable(!editable);
    //     }
    // };
    // const handleClickOutside = (e) => {
    //     if (editable == true && !ref.current.contains(e.target)) setEditable(false);
    // };
    // useEffect(() => {
    //     window.addEventListener("click", handleClickOutside, true);
    // });

    return (
            <>
            <div className={styles.profile}>
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
            <div className={styles.menu}>
                <button className={`${styles["post"+(tab1 ==="curr"? "" : "active")]}`}  onClick={() =>{ setDealTab('curr'); navigate('/mypage');}}>게시글</button>
                <button className={`${styles["zzim"+(tab1 ==="next"? "" : "active")]}`} onClick={() =>{ setDealTab('next'); navigate('/mypage/zzim');}}>관심 상품</button>
            </div>
            </>
    );
}

export default MyPage;