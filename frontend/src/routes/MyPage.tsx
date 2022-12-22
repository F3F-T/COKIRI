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
import Card from "../component/tradeCard/Card";

// interface TextInputProps {
//     init: string;
// }

const MyPage = () =>  {
    const [tab1, setTab] = useState('curr');

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
            <div className={styles.MyPage}>
            <MyPageSet/>
              <div className={styles.container}>
                  <Card className={"forMypage"} postTitle={"스팸아"} postContent={"아직 미완성 디자인"} like={3} comment={5} category={"가구"} />
                  <Card className={"forMypage"} postTitle={"고추참치"} postContent={"스패애애ㅐㅇㅁ"} like={4} comment={8} category={"음식"} />
                  <Card className={"forMypage"} postTitle={"홍"} postContent={"스패애애ㅐㅇㅁ"} like={7} comment={5} category={"가구"} />
                  <Card className={"forMypage"} postTitle={"홍라면"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
                  <Card className={"forMypage"} postTitle={"김치찌개"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
                  <Card className={"forMypage"} postTitle={"그만"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
                  <Card className={"forMypage"} postTitle={"그만"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
                  <Card className={"forMypage"} postTitle={"아직"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />

              </div>

            </div>
        </>

    );
}

export default MyPage;