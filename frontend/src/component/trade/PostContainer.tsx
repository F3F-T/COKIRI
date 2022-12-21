import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/PostContainer.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../img/cokkiriLogo.png"
import Card from "../tradeCard/Card"
import Comments from "../comments/Comments";


// ㅂㅅ
const PostContainer = () => {

        let navigate = useNavigate();


        return (
        <div className={styles.postContainer}>
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
            <Card className={"forTrade"} postTitle={"스팸씨발아"} postContent={"스패애애ㅐㅇㅁ"} like={3} comment={5} category={"가구"} />
        </div>
    );
}

export default PostContainer;