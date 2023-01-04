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
    function setDealTab(tab){
        setTab(tab)
        console.log("zzim 페이지")
        console.log(tab1)
        // return tab
    }
    const navigate = useNavigate();


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
                <div className={styles.menu}>
                    {/*{tab1 === 'curr' ? <button className={`${styles["post"+(tab1 ==="curr"? "" : "active")]}`}  onClick={() =>{ setDealTab('curr'); navigate('/mypage');}}>d게시글</button>*/}
                    {/*    :<button className={`${styles["post"+(tab1 ==="curr"? "" : "active")]}`}  onClick={() =>{ setDealTab('curr'); navigate('/mypage');}}>게시글</button>*/}
                    {/*}*/}
                    {/*{tab1 === 'next' ? <button className={`${styles["zzim"+(tab1 ==="next"? "" : "active")]}`} onClick={() =>{ setDealTab('next'); navigate('/mypage/zzim');}}>d관심 상품</button>*/}

                    {/*    : <button className={`${styles["zzim"+(tab1 ==="next"? "" : "active")]}`} onClick={() =>{ setDealTab('next'); {tab1==="next"? navigate('/mypage/zzim'):navigate('/mypage')}}}>관심 상품</button>*/}
                    {/*}*/}
                    <button className={styles.postactive}  onClick={() =>{ setDealTab('curr'); navigate('/mypage');}}>게시글</button>
                    {/*<button className={`${styles["zzim"+(tab1 ==="next"? "" : "active")]}`} onClick={() =>{ setDealTab('next'); navigate('/mypage/zzim');}}>관심 상품</button>*/}
                    <button className={styles.zzim} onClick={()=>{ setDealTab('next')}}>관심 상품</button>
                </div>
                <div className={styles.container}>
                    <Card className={"forMypage"} postTitle={"찜해놓은거임"} like={3} comment={5} category={"가구"} />
                    <Card className={"forMypage"} postTitle={"찜해놓은거임"} like={4} comment={8} category={"음식"} />
                    <Card className={"forMypage"} postTitle={"찜해놓은거임"}  like={7} comment={5} category={"가구"} />
                    <Card className={"forMypage"} postTitle={"찜해놓은거임"}  like={3} comment={5} category={"가구"} />
                    <Card className={"forMypage"} postTitle={"찜해놓은거임"} like={3} comment={5} category={"가구"} />
                    <Card className={"forMypage"} postTitle={"찜해놓은거임"}  like={3} comment={5} category={"가구"} />
                    <Card className={"forMypage"} postTitle={"찜해놓은거임"}  like={3} comment={5} category={"가구"} />
                    <Card className={"forMypage"} postTitle={"찜해놓은거임"} like={3} comment={5} category={"가구"} />

                </div>
            </div>
        </>
    );
}

export default MyPage;