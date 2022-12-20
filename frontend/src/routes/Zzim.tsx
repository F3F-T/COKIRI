import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/MyPage.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../img/photoSelect.png"
import PriceBox from "../component/trade/PriceBox";
import profile from "../img/profile.jpeg"
import PostContainer from "../component/trade/PostContainer";
import MyPageSet from "../component/MyPageSet";

// interface TextInputProps {
//     init: string;
// }

const MyPage = () =>  {
    const [tab1, setTab] = useState('curr');

    const navigate = useNavigate();

    function setDealTab(tab){
        setTab(tab)
        console.log("zzim 페이지")
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
            <div className={styles.MyPage}>
                <MyPageSet/>
                <div className={styles.ZZ}>여기는 찜 자리야 </div>
            </div>
        </>
    );
}

export default MyPage;